package hndt.radiolibs.test;

import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.servlet.vo.PlaylistItemVo;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.HttpClient;
import hndt.radiolibs.util.Utils;

import java.io.File;
import java.time.LocalTime;

/**
 * 测试
 *
 * @author Hystar
 * @date 2017/10/19
 */
public class PlayItemServletTest {

    public static void main(String[] args) throws Exception {

//      String text = HttpClient.getInstance().urlGet("http://a.admin.hndt.com/api/vlive/playitem/next?channel_id=83");
        String text = HttpClient.getInstance().urlGet("http://localhost:8080/api/vlive/playitem/next?channel_id=83");
        System.out.println(text);
        PlaylistItemVo vo = GSON.toObject(text, PlaylistItemVo.class);
        System.out.println(LocalTime.now() + "," + vo.getName() + "," + vo.getTitle() + ",duration=" + vo.getDuration() + ",playDuration=" + vo.getPlayDuration());

    }
}
