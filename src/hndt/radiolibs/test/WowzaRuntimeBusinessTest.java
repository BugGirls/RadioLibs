package hndt.radiolibs.test;

import com.google.gson.reflect.TypeToken;
import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.ExtractBusiness;
import hndt.radiolibs.biz.RuntimeBusiness;
import hndt.radiolibs.biz.WowzaRuntimeBusiness;
import hndt.radiolibs.servlet.vo.ChannelVo;
import hndt.radiolibs.servlet.vo.ClockVo;
import hndt.radiolibs.servlet.vo.PlaylistItemVo;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.HttpClient;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WowzaRuntimeBusinessTest {
    static boolean between(LocalTime start, LocalTime end, LocalTime time) {
        return ((time.isAfter(start) || time.equals(start)) && time.isBefore(end));
    }

    public static void main2(String[] args) throws Exception{
        File file = new File("/Users/pysh/project/虚拟直播/工程/RadioLibs/web/test/json");
        String text = FileUtils.readFileToString(file);

        LocalTime now = LocalTime.now();
        Type type = new TypeToken<List<ClockVo>>() {}.getType();
        List<ClockVo> list = GSON.toList(text,ClockVo.class);

        for (ClockVo clockVo : list) {
            for (ResVo resVo : clockVo.getPlaylist()) {
                if (between(resVo.getStarttime(),resVo.getEndtime(),now)){
                    System.out.println(resVo);
                }
            }
        }
    }
    public static void main(String[] args) {

        final String[] result = {""};
        //测试开始
        List<PlaylistItemVo> list = new ArrayList<>();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(() -> {
            try {
                String url = "http://localhost:8080/api/vlive/playitem/next?channel_id=80";
                String response = HttpClient.getInstance().urlGet(url);
                System.out.println(LocalTime.now() + "-" + response);
                PlaylistItemVo vo = GSON.toObject(response, PlaylistItemVo.class);
                if (vo.getRuntimeId()!=null) {
                    RuntimeBusiness.getInstance().updatePlayDate(String.valueOf(vo.getRuntimeId()));
                }
                TimeUnit.SECONDS.sleep(vo.getPlayDuration());
            } catch (Exception e) {
                Logger.error(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

    }
}
