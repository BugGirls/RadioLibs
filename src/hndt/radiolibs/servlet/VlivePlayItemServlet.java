package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.servlet.vo.ClockVo;
import hndt.radiolibs.servlet.vo.PlaylistItemVo;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import net.sf.cglib.core.Local;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static hndt.radiolibs.util.Utils.DATEFORMAT4;

/**
 * 钟型之外或没有节目或没有钟型，播放blank
 * 钟型之内没有资源，播放垫片，如果频率没有定义垫片，找系统垫片
 * <p>
 * wowza：
 * 如果网络频率节目开始(到了节目开始时间)，就开始第一个请求，之后，每次播放完毕调用此api，得到PlaylistItemVo
 * <p>
 * 服务端：
 * 1 根据channel_id得到钟型列表
 * 2 计算每个钟型和每个资源的开始和结束时间
 * 3 根据时间，定位到钟型和资源
 */

@WebServlet("/api/vlive/playitem/next")
public class VlivePlayItemServlet extends BaseServlet {

    //用于是不是重复请求。key=钟型，value=playlist
    static Map<Long, List<ResVo>> map = new ConcurrentHashMap<>();
    //用于是不是重复请求。key=channel_id，value=LocalTime
    static Map<Long, LocalTime> channelReqeustMap = new ConcurrentHashMap<>();
    //用于是不是第一次到达这个Clock。key=钟型，value=计数
    static Map<Long, Integer> clockMap = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String func = req.getParameter("func");
        if (func != null && func.length() > 0 && "clear".equals(func)) {
            map.clear();
            clockMap.clear();
            return;
        }

        Long channel_id = NumberUtils.toLong(req.getParameter("channel_id"), -1L);

        resp.setContentType(contentTypeJson);
        resp.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);

        String dateString = req.getParameter("date");
        LocalTime now = LocalTime.now().withNano(0);
        LocalDate date = LocalDate.now();
        if (Utils.isNotEmpty(dateString)) {
            date = LocalDateTime.parse(dateString, DATEFORMAT4).toLocalDate();
        }

        String prev = req.getParameter("prev");
        if (prev != null && prev.length() > 0 && prev.indexOf('/') >= 0) {
            prev = prev.substring(prev.lastIndexOf('/'));
        }

        LocalDateTime dateTime = date.atTime(0, 0, 0);
        ProgramBean program = null;
        ClockVo targetClockVo = null;
        ResVo target = null;
        PrintWriter out = resp.getWriter();
        if (channel_id > 0) {
            List<ClockVo> clockList = new ArrayList<>(10);

            ChannelBean channelBean = ChannelBusiness.getInstance().load(channel_id);
            ChannelBusiness.getInstance().attachShim(channelBean);
            ResVo shimVo = WowzaRuntimeBusiness.getInstance().convertVo(channelBean.getShim());
            ResVo blankVo = WowzaRuntimeBusiness.getInstance().blankVo();

            List<RuntimeBean> runtimes = RuntimeBusiness.getInstance().list(channel_id, date);

            if (runtimes != null && !runtimes.isEmpty()) {
                program = ProgramBusiness.getInstance().load(runtimes.get(0).getProgram_id());
                ProgramBusiness.getInstance().attachOrderClockIdsList(program);
                List<ClockBean> clockTempList = ClockBusiness.getInstance().listByOrder(program.getClockIds(), channel_id, Timestamp.valueOf(dateTime));
                clockTempList.removeIf(x -> x == null);

                List<Long> ids = runtimes.stream().map(x -> x.getRes_id()).collect(Collectors.toList());
                Map<Long, ResBean> mapRes = ResBusiness.getInstance().mapByIds(ids);

                for (ClockBean clockBean : clockTempList) {
                    List<RuntimeBean> runtimesInClock = runtimes.stream().filter(x -> x.getClock_id() == clockBean.getId().longValue()).collect(Collectors.toList());
                    List<ResVo> resVoInClock = new ArrayList<>();
                    for (RuntimeBean runtime : runtimesInClock) {
                        ResBean rbean = mapRes.get(runtime.getRes_id());
                        ResVo vo = WowzaRuntimeBusiness.getInstance().convertVo(runtime, rbean);
                        resVoInClock.add(vo);
                    }
                    ClockVo vo = WowzaRuntimeBusiness.getInstance().convertVo(clockBean);
                    vo.setPlaylist(resVoInClock);
                    clockList.add(vo);
                }

                if (prev == null || Objects.equals("", prev)) {
                    clockList.forEach(x -> {
                        if (clockMap.get(x.getId()) != null) {
                            clockMap.remove(x.getId());
                            map.remove(x.getId());
                        }
                    });
                    channelReqeustMap.clear();
                }
                calClockPlaytime(program, clockList);

                //根据当前时间定位到钟型
                targetClockVo = clockList.stream().filter(c -> between(c.getStart(), c.getEnd(), now)).findFirst().orElse(null);

                if (targetClockVo != null) {
                    if (isRepeat(targetClockVo.getId(), prev)) {
                        target = null;
                    } else {
                        List<ResVo> playlist = targetClockVo.getPlaylist();
                        if (target == null && playlist.size() > 0) {
                            //根据前一个播放资源定位到下一个资源。
                            if (prev == null || prev.length() == 0 || isBeginClock(targetClockVo.getId())) {
                                target = playlist.get(0);
                            } else if (isLast(playlist, prev) || (isShim(shimVo, prev) && lastPlayingIsShim(shimVo, targetClockVo))) {
                                target = GSON.toObject(GSON.toJson(shimVo), ResVo.class);
                            } else {
                                target = nextItem(playlist, runtimes, prev);
                            }
                        }
                    }
                }

                if (target != null) {
                    if (target.getPlaceholder() == 1) {
                        target = placeholder(target);
                    }
                    if (target.getRuntimeId() != null) {
                        RuntimeBusiness.getInstance().updatePlayDate(String.valueOf(target.getRuntimeId()));
                    }
                    if (targetClockVo != null) target.setClockId(targetClockVo.getId());
                }

                if (target != null) {
                    long diff = now.until(targetClockVo.getEnd(), ChronoUnit.SECONDS);
                    if (diff < target.getDuration()) {
                        target.setPlayDuration((int) diff);
                    }
                }

                if (target != null) {
                    if (map.get(targetClockVo.getId()) == null) {
                        map.put(targetClockVo.getId(), new ArrayList<ResVo>());
                    }

                    target.setStarttime(LocalTime.now());
                    map.get(targetClockVo.getId()).add(target);

                    if (map.get(targetClockVo.getId()).size() > 999) {
                        map.get(targetClockVo.getId()).remove(0);
                    }

                    if (clockMap.get(targetClockVo.getId()) == null) {
                        clockMap.put(targetClockVo.getId(), 1);
                    }
                }

                if (target != null && target.getName() != null) {
                    System.out.println(LocalTime.now() + ": " + GSON.toJson(target));
                }
                //找不到钟型
                if (targetClockVo == null && !isRepeatRequest(channel_id)) {
                    target = blankVo;
                    channelReqeustMap.put(channel_id, LocalTime.now());
                }
            }
        }

        PlaylistItemVo vo = WowzaRuntimeBusiness.getInstance().convertVo(target);
        String text = GSON.toJson(vo);
        out.println(text);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    boolean isBeginClock(Long clockId) {
        return clockMap.get(clockId) == null || clockMap.get(clockId) == 0L;
    }

    boolean isShim(ResVo shim, String prev) {
        return prev != null && prev.length() > 0 && shim.getPath().contains(prev);
    }

    boolean lastPlayingIsShim(ResVo shim, ClockVo clock) {
        List<ResVo> playlist = map.get(clock.getId());
        return Objects.equals(shim.getPath(), playlist.get(playlist.size() - 1).getPath());
    }

    boolean isLast(List<ResVo> playlist, String prev) {
        return playlist.get(playlist.size() - 1).getPath().contains(prev);
    }

    //根据prev得到下一个播放项，看时间是否到
    boolean isRepeat(Long clockId, String prev) {
        List<ResVo> playlist = map.get(clockId);
        boolean repeat = playlist != null && playlist.size() > 1 && playlist.get(playlist.size() - 2).getPath().contains(prev);
        if (repeat) {
            ResVo target = null;
            for (int i = 0; i < playlist.size(); i++) {
                if (playlist.get(i).getPath().contains(prev) && playlist.size() >= i + 2) {
                    target = playlist.get(i + 1);
                }
            }
            if (target != null) {
                long diff = ChronoUnit.SECONDS.between(target.getStarttime(), LocalTime.now());
                if (diff >= target.getPlayDuration()) {
                    repeat = false;
                }
            }
        }
        return repeat;
    }

    boolean isRepeatRequest(Long programId) {
        if (channelReqeustMap.get(programId) != null) {
            channelReqeustMap.get(programId).plusSeconds(1).isAfter(LocalTime.now());
            return true;
        } else {
            return false;
        }
    }

    ResVo nextItem(List<ResVo> playlist, List<RuntimeBean> runtimes, String prev) {
        if (isLast(playlist, prev)) {
            return playlist.get(playlist.size() - 1);
        }
        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getPath().contains(prev)) {
                ResVo rvo = playlist.get(i + 1);
                Optional<RuntimeBean> any = runtimes.stream().filter(x -> Objects.equals(x.getId(), rvo.getRuntimeId())).findAny();
                if (any.isPresent() && any.get().getPlaydate() == null) {
                    return playlist.get(i + 1);
                }
            }
        }
        return null;
    }

    //设置钟型的播放截止时间
    void calClockPlaytime(ProgramBean program, List<ClockVo> clockList) {
        for (int i = 0; i < clockList.size(); i++) {
            ClockVo vo = clockList.get(i);
            if (i == 0) {
                vo.setStart(program.getLocalStartTime());
            } else {
                vo.setStart(clockList.get(i - 1).getEnd());
            }
            vo.setEnd(vo.getStart().plusMinutes(vo.getDuration()));
        }
        clockList.removeIf(x -> x.getStart().isAfter(program.getLocalEndTime()));
        clockList.forEach(x -> {
            if (x.getEnd().isAfter(program.getLocalEndTime())) {
                x.setEnd(program.getLocalEndTime());
            }
        });
    }

    boolean between(LocalTime start, LocalTime end, LocalTime time) {
        return ((time.isAfter(start) || time.equals(start)) && time.isBefore(end));
    }

    ResVo placeholder(ResVo target) {
        if (target.getPlaceholder() != 1) {
            return target;
        }
        ResVo vo = null;
        RuntimeBean runtime = RuntimeBusiness.getInstance().load(target.getRuntimeId());
        TypedBean typed = null;
        if (runtime != null) {
            typed = TypedBusiness.getInstance().load(runtime.getTyped_id());
        } else if (Utils.isNotEmpty(target.getTypeId())) {
            typed = TypedBusiness.getInstance().load(target.getTypeId());
        }
        if (runtime != null) {
            List<ResBean> list = RuntimeBusiness.getInstance().resByTyped(typed.getManager_id(), runtime.getChannel_id(), typed, LocalDateTime.now());
            if (list.size() > 0) {
                ResBean row = list.get(0);
                vo = WowzaRuntimeBusiness.getInstance().convertVo(runtime, row);
                RuntimeBusiness.getInstance().updatePlaceholder(runtime.getId(), vo.getDuration(), vo.getId());
            }
        }
        return vo == null ? target : vo;
    }

    public static void clear() {
        map.clear();
        clockMap.clear();
        channelReqeustMap.clear();
    }


}
