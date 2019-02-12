package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ProgramBean;
import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.ChannelBusiness;
import hndt.radiolibs.biz.ProgramBusiness;
import hndt.radiolibs.biz.WowzaRuntimeBusiness;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import hndt.radiolibs.servlet.vo.ChannelVo;
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

/**
 * 获取指定id的频率信息
 * <p>
 * 输入: /api/vlive/channel
 * 输出：ChannelBean JSON序列化信息
 */

@WebServlet("/api/vlive/channel")
public class VliveChannelServlet extends BaseServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);
        String uri = req.getRequestURI();
        String query_uri = uri + "?" + req.getQueryString();
        PrintWriter out = response.getWriter();
        String text = "";

        // 获取参数channel_id
        String channel_id = req.getParameter("channel_id");

        if (allow(req, response, query_uri)) {
            ChannelBean channelBean = ChannelBusiness.getInstance().load(Integer.parseInt(channel_id));
            ChannelBusiness.getInstance().attachShim(channelBean);
            ChannelVo channelVo = WowzaRuntimeBusiness.getInstance().convertVo(channelBean);
            channelVo.setShim(WowzaRuntimeBusiness.getInstance().convertVo(channelBean.getShim()));
            text = GSON.toJson(channelVo);
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