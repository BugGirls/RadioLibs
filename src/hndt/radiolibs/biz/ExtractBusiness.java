package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.LogBean;
import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.servlet.VlivePlayItemServlet;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;

import javax.servlet.ServletContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
                    Logger.info("生成3日内节目单，频率ID：" + cbean.getId() + "，频率名称：" + cbean.getName());
                    // 生成3日内节目单，如有已有就不再生成
                    if (RuntimeBusiness.getInstance().none(cbean.getId(), now.plusDays(1).toLocalDate())) {
                        RuntimeBusiness.getInstance().generate(cbean.getId(), Timestamp.valueOf(now.plusDays(1)));
                    }
                    if (RuntimeBusiness.getInstance().none(cbean.getId(), now.plusDays(2).toLocalDate())) {
                        RuntimeBusiness.getInstance().generate(cbean.getId(), Timestamp.valueOf(now.plusDays(2)));
                    }
                    if (RuntimeBusiness.getInstance().none(cbean.getId(), now.plusDays(3).toLocalDate())) {
                        RuntimeBusiness.getInstance().generate(cbean.getId(), Timestamp.valueOf(now.plusDays(3)));
                    }
                }
                //清除播放API的缓存
                VlivePlayItemServlet.clear();
            } catch (Exception e) {
                Logger.error(e);
            }
        } else if (now.getHour() == 23 && now.getMinute() == 57) {
            ResBusiness.deleteOnLifetime();
        }
    }

}
