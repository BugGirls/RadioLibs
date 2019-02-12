package hndt.radiolibs.biz;


import com.google.gson.reflect.TypeToken;
import hndt.radiolibs.bean.*;
import hndt.radiolibs.servlet.VliveChannelServlet;
import hndt.radiolibs.servlet.vo.*;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.HttpClient;
import hndt.radiolibs.util.Logger;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class WowzaRuntimeBusiness {

    private static final String PLACEHOLDER = "PLACEHOLDER";
    private static final String BLANK = "blank.mp3";
    private static final String UPLOADS = "/uploads/";
    private static final String MEDIATYPE = "mp3:";

    public ChannelVo convertVo(ChannelBean cbean) {
        ChannelVo channelVo = new ChannelVo();
        channelVo.setId(cbean.getId());
        channelVo.setUuid(cbean.getUuid());
        channelVo.setName(cbean.getName());
        channelVo.setDescription(cbean.getDescription());
        channelVo.setLogo(cbean.getLogo());
        channelVo.setShim(convertVo(cbean.getShim()));
        LocalDateTime now = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 0, 0, 0);
        ProgramBean programBean = ProgramBusiness.getInstance().load(cbean.getManager_id(), cbean.getId(), now.getDayOfWeek().getValue());

        if (programBean != null && programBean.getStarttime() != null) {
            channelVo.setStarttime(programBean.getStarttime().toLocalDateTime().toLocalTime());
        }
        if (programBean != null && programBean.getEndtime() != null) {
            channelVo.setEndtime(programBean.getEndtime().toLocalDateTime().toLocalTime());
        }
        return channelVo;
    }


    public ResVo convertVo(ResBean res) {
        ResVo vo = new ResVo();
        if (res == null) {
            return vo;
        }
        try {
            vo.setName(res.getTitle_proper());
            vo.setDuration(Long.valueOf(res.getFormat_duration()).intValue());
            vo.setPlayDuration(vo.getDuration());
            vo.setPath(res.getPath());
            vo.setSize(res.getSize());
            if (res.getFormat_starting_point() != null) {
                vo.setStarting(Long.valueOf(res.getFormat_starting_point()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }


    public ResVo convertVo(RuntimeBean runtime, ResBean rbean) {
        ResVo vo = new ResVo();
        vo.setId(runtime.getRes_id());
        vo.setClockId(runtime.getClock_id());
        vo.setTypeId(runtime.getTyped_id());
        vo.setRuntimeId(runtime.getId());
        vo.setPlaceholder(runtime.getPlaceholder());
        vo.setUnitary(runtime.getUnitary());
        if (rbean != null) {
            if (vo.getId() == null || vo.getId().longValue() == 0) {
                vo.setId(rbean.getId());
            }
            vo.setUuid(rbean.getUuid());
            vo.setName(rbean.getTitle_proper());
            vo.setDuration(Long.valueOf(rbean.getFormat_duration()).intValue());
            vo.setPlayDuration(vo.getDuration());
            vo.setSize(rbean.getSize());
            vo.setPath(rbean.getPath());
            vo.setStarting(0L);
            vo.setSinging(null);
            vo.setLyrics(null);
            vo.setMusic(null);
        } else {
            vo.setName(PLACEHOLDER);
            vo.setPath(BLANK);
            vo.setDuration(Long.valueOf(runtime.getDuration()).intValue());
        }
        return vo;
    }

    public PlaylistItemVo convertVo(ResVo res) {
        PlaylistItemVo vo = new PlaylistItemVo();
        if (res == null) {
            return vo;
        }
        try {
            PropertyUtils.copyProperties(vo, res);
            vo.setTitle(res.getName());
            vo.setLength(res.getPlayDuration());
            vo.setName(buildVodPath(res.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    public ResVo blankVo() {
        ResBean res = ResBusiness.getInstance().load(1);
        ResVo vo = convertVo(res);
        return vo;
    }


    private String buildVodPath(String path) {
        if (path.indexOf(UPLOADS) == 0) {
            path = path.substring(UPLOADS.length());
        }
        if (!path.startsWith(MEDIATYPE)) {
            path = MEDIATYPE + path;
        }
        return path;
    }

    private static WowzaRuntimeBusiness biz = null;
    private List<ChannelVo> channels = new ArrayList<>();

    public WowzaRuntimeBusiness() {
    }

    public static synchronized WowzaRuntimeBusiness getInstance() {
        if (biz == null) {
            biz = new WowzaRuntimeBusiness();
        }
        return biz;
    }

    public ClockVo convertVo(ClockBean clockBean) {
        ClockVo vo = new ClockVo();
        vo.setId(clockBean.getId());
        vo.setChannel_id(clockBean.getChannel_id());
        vo.setName(clockBean.getName());
        vo.setDuration(clockBean.getDuration());
        return vo;
    }
}
