package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.servlet.VlivePlayItemServlet;
import hndt.radiolibs.util.Logger;

import javax.servlet.ServletContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成节目单信息
 */
public class ExtractBusiness implements Runnable {
    private ServletContext servletContext;

    public ExtractBusiness(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() == 23 && now.getMinute() == 55) {
            RuntimeBusiness.getInstance().clear(now);
        }
        // 每天深夜23点56分更新明天的节目单
        if (now.getHour() == 23 && now.getMinute() == 56) {
            try {
                List<ChannelBean> list = ChannelBusiness.getInstance().list();
                for (ChannelBean cbean : list) {
                    // 有声文摘特定需求：生成一个月的节目单
                    if (cbean.getId() == 96) {
                        Logger.info("有声文摘生成一个月的节目单");
                        for (int i = 1; i <= now.getDayOfMonth(); i++) {
                            if (RuntimeBusiness.getInstance().none(cbean.getId(), now.plusDays(i).toLocalDate())) {
                                RuntimeBusiness.getInstance().generate(cbean.getId(), Timestamp.valueOf(now.plusDays(i)));
                            }
                        }
                    } else {
                        Logger.info("生成3日内节目单，频率ID：" + cbean.getId() + "，频率名称：" + cbean.getName());
                        for (int i = 1; i <= 3; i++) {
                            // 生成3日内节目单，如果已有就不再生成
                            if (RuntimeBusiness.getInstance().none(cbean.getId(), now.plusDays(i).toLocalDate())) {
                                RuntimeBusiness.getInstance().generate(cbean.getId(), Timestamp.valueOf(now.plusDays(i)));
                            }
                        }
                    }
                }
                //清除播放API的缓存
                VlivePlayItemServlet.clear();
            } catch (Exception e) {
                Logger.error(e);
            }
        } else if (now.getHour() == 23 && now.getMinute() == 57) {
            // 删除过期的资源文件
            ResBusiness.deleteOnLifetime();
        }
    }

}
