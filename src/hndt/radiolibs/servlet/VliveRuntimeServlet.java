package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.biz.RuntimeBusiness;
import hndt.radiolibs.biz.WowzaRuntimeBusiness;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.GSON;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * 填充Runtime表中的：playdate（播放日期）
 * <p>
 * 输入: /api/vlive/runtime?channel_id=50&type=0
 * 输出：null
 *
 * @author Hystar
 * @date 2017/9/14
 */
@WebServlet("/api/vlive/runtime")
public class VliveRuntimeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(contentTypeJson);
        resp.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);
        int j = RandomUtils.nextInt(120883,121049);
        ResBean res = ResBusiness.getInstance().load(j);
        ResVo target = WowzaRuntimeBusiness.getInstance().convertVo(res);
        String text = GSON.toJson(target);
        PrintWriter out = resp.getWriter();
        out.println(text);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
