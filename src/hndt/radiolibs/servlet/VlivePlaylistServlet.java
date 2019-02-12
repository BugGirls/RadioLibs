package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import hndt.radiolibs.servlet.vo.*;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import static hndt.radiolibs.util.Utils.DATEFORMAT4;

/**
 * 获取每个频率的播放信息列表
 * <p>
 * 输入: /api/vlive/playlist?channel_id=68&date=20171024
 * 输出：JSON格式，如example.json
 *
 * @author Hystar
 * @date 2017/10/24
 */

@WebServlet("/api/vlive/playlist")
public class VlivePlaylistServlet extends BaseServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);

        PrintWriter out = response.getWriter();
        String uri = req.getRequestURI();
        String queryUri = uri + "?" + req.getQueryString();
        String result = "";

        if (allow(req, response, queryUri)) {
            List<ClockVo> clockResult = new ArrayList<>();

            // 获取URL中的参数
            Long channelId = NumberUtils.toLong(req.getParameter("channel_id"), 0L);
            String dateString = req.getParameter("date");

            // 获取的date参数类型为20171024，需要进行格式转换
            LocalDateTime date;
            if (Utils.isNotEmpty(dateString)) {
                date = LocalDate.parse(dateString, DATEFORMAT4).atTime(0, 0, 0);
            } else {
                date = LocalDate.now().atTime(0, 0, 0);
            }

            if (channelId > 0) {
                // 通过channel_id和week来确定唯一的节目单信息
                int week = date.getDayOfWeek().getValue();
                ProgramBean program = ProgramBusiness.getInstance().load(null, channelId, week);
                if (program != null) {
                    // 填充该频率的ClockIds
                    ProgramBusiness.getInstance().attachOrderClockIdsList(program);

                    // 获取该频率下的钟型信息列表
                    List<ClockBean> clockBeanList = ClockBusiness.getInstance().listByOrder(program.getClockIds(), channelId, Timestamp.valueOf(date));
                    clockBeanList.removeIf(x -> x == null);
                    // 记录当前钟型的开始时间
                    Timestamp startTime;
                    // 记录当前钟型的结束时间
                    Timestamp endTime;
                    // 记录上个钟型的结束时间
                    Timestamp time = null;
                    for (ClockBean clockBean : clockBeanList) {
                        RuntimeBean runtimeBean = new RuntimeBean();
                        runtimeBean.setClock_id(clockBean.getId());
                        runtimeBean.setChannel_id(channelId);
                        runtimeBean.setCreatetime(Timestamp.valueOf(date));

                        List<RuntimeBean> runtimeBeanList = RuntimeBusiness.getInstance().list(null, channelId, null, clockBean.getId(), Timestamp.valueOf(date));

                        List<Long> ids = new ArrayList<>();
                        for (RuntimeBean runtimeBeanItem : runtimeBeanList) {
                            ids.add(runtimeBeanItem.getRes_id());
                        }
                        Map<Long, ResBean> mapRes = ResBusiness.getInstance().mapByIds(ids);
                        List<ResVo> resResult = new ArrayList<>();
                        for (RuntimeBean runtime : runtimeBeanList) {
                            ResBean rbean = mapRes.get(runtime.getRes_id());
                            ResVo vo = WowzaRuntimeBusiness.getInstance().convertVo(runtime, rbean);
                            resResult.add(vo);
                        }

                        ClockVo clock = new ClockVo();
                        clock.setId(clockBean.getId());
                        clock.setChannel_id(clockBean.getChannel_id());
                        clock.setName(clockBean.getName());
                        clock.setDuration(clockBean.getDuration());

                        // 设置每个钟型的开始播放和结束播放时间
                        if (clockBeanList.indexOf(clockBean) == 0) {
                            startTime = program.getStarttime();
                        } else {
                            startTime = time;
                        }
                        endTime = new Timestamp(startTime.getTime() + clockBean.getDuration() * 60 * 1000);
                        time = endTime;
                        clock.setStart(startTime.toLocalDateTime().toLocalTime());
                        clock.setEnd(endTime.toLocalDateTime().toLocalTime());

                        clock.setPlaylist(resResult);
                        clockResult.add(clock);
                    }
                }
            }
            result = GSON.toJson(clockResult);
        } else {
            result = GSON.toJson(Utils.asJsonMap("error", "非法请求"));
        }
        out.print(result);
        out.close();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
