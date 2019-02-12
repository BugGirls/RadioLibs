package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.RuntimeBusiness;
import hndt.radiolibs.biz.TypedBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取指定id的频率信息
 * <p>
 * 输入: /api/vlive/channel
 * 输出：ChannelBean JSON序列化信息
 * /api/vlive/placeholder?type_id=53&clock_id=52
 */

@WebServlet("/api/vlive/placeholder")
public class VlivePlaceholderServlet extends BaseServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);
        String uri = req.getRequestURI();
        String query_uri = uri + "?" + req.getQueryString();
        PrintWriter out = response.getWriter();
        String text = "";

        ResBean resBean = null;
        // 获取参数runtime_id
        String runtime_id = req.getParameter("runtime_id");
        String typed_id = req.getParameter("typed_id");
        if (allow(req, response, query_uri)) {
            RuntimeBean rbean = RuntimeBusiness.getInstance().load(NumberUtils.toLong(runtime_id));
            TypedBean typed = null;
            if (rbean != null) {
                typed = TypedBusiness.getInstance().load(rbean.getTyped_id());
            } else if (Utils.isNotEmpty(typed_id)) {
                typed = TypedBusiness.getInstance().load(NumberUtils.toLong(typed_id));
            }
            if (rbean != null) {
                List<ResBean> list = RuntimeBusiness.getInstance().resByTyped(typed.getManager_id(), rbean.getChannel_id(), typed, LocalDateTime.now());
                if (list.size() > 0) {
                    ResBean row = list.get(0);
                    resBean = new ResBean();
                    resBean.setId(row.getId());
                    resBean.setName(row.getTitle_proper());
                    resBean.setPath(row.getPath());
                    resBean.setFormat_duration(row.getFormat_duration());
                    resBean.setDuration(row.getFormat_duration());
                    if (rbean != null) {
                        RuntimeBusiness.getInstance().updatePlaceholder(rbean.getId(), resBean.getFormat_duration().intValue(), resBean.getId());
                    }
                }
            }
            text = resBean == null ? "" : GSON.toJson(resBean);
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