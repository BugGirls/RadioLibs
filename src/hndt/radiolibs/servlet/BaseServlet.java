package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerBusiness;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * API的基础Servlet
 */

public abstract class BaseServlet extends HttpServlet {
    static final String utf8 = "utf-8";
    static final String contentTypeHtml = "text/html; charset=UTF-8";
    static final String contentTypeJson = "application/json";

    String parameter(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }

    String parameter(HttpServletRequest request, String name, String defaultValue) {
        String v = request.getParameter(name);
        if (v == null) {
            v = defaultValue;
        }
        return v;
    }

    boolean allow(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException, ServletException {
        boolean allow = true;


        return allow;
    }

}
