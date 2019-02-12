package hndt.radiolibs.servlet;

import hndt.radiolibs.biz.EnumValue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户退出登录
 *
 * @author Hystar
 * @date 2017/10/24
 */
@WebServlet("/api/manager/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);

        // 清除session
        req.getSession().removeAttribute(EnumValue.LITERAL_MANAGER);

        if (req.getRequestURL().toString().contains("localhost")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("http://uc.hndt.com");
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
