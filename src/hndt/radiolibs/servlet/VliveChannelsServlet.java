package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ProgramBean;
import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.ChannelBusiness;
import hndt.radiolibs.biz.ProgramBusiness;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.beanutils.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取所有有效的频率列表信息
 * <p>
 * 输入: /api/vlive/channels
 * 输出：ChannelBean JSON序列化信息
 */

@WebServlet("/api/vlive/channels")
public class VliveChannelsServlet extends BaseServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);
        String uri = req.getRequestURI();
        String query_uri = uri + "?" + req.getQueryString();
        PrintWriter out = response.getWriter();
        String text = "";
        LocalDateTime now = LocalDate.now().atTime(0, 0, 0);
        if (allow(req, response, query_uri)) {
            List<ChannelBean> channelBeanList = ChannelBusiness.getInstance().list();
            for (ChannelBean channelBean : channelBeanList) {
                // 填充channelBean的startTime和endTime
                ProgramBean programBean = ProgramBusiness.getInstance().load(channelBean.getManager_id(), channelBean.getId(), now.getDayOfWeek().getValue());
                if (programBean != null) {
                    channelBean.setStarttime(programBean.getStarttime().toLocalDateTime().toLocalTime());
                    channelBean.setEndtime(programBean.getEndtime().toLocalDateTime().toLocalTime());
                }
            }
            text = GSON.toJson(channelBeanList);
        } else {
            text = GSON.toJson(Utils.asJsonMap("error", "非法请求"));
        }
        out.print(text);
        out.close();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }


}