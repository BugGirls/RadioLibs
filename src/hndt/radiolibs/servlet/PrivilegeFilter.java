package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.biz.PermissionBusiness;
import hndt.radiolibs.biz.RoleBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "privilege", urlPatterns = {"*.xhtml"})
public class PrivilegeFilter implements Filter {

    private static final String login_url = "login.xhtml";
    private static final String error_url = "error.xhtml";
    private static final String jsf_post_back = "javax.faces.ViewState";
    private static final String jsf_resource = "/javax.faces.resource/";
    private static final String test_uri_start = "/test/";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding(EnumValue.ENCODING_UTF8);
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        HttpSession session = req.getSession();

        if (!uri.endsWith("/") && !uri.contains(jsf_resource) && !uri.startsWith(test_uri_start) && !uri.contains(error_url)) {
            //权限判断
            uri = uri.substring(ctx.length());
            ManagerBean manager = (ManagerBean) session.getAttribute(EnumValue.LITERAL_MANAGER);
            if (manager != null) {
                if (ManagerBusiness.getInstance().allowVisit(manager, uri) || uri.contains(login_url)) {
                    filterChain.doFilter(req, res);
                } else {
                    res.sendRedirect(ctx + error_url);
                }
            } else if (uri.length() != 1 && uri.contains(login_url)) {
                filterChain.doFilter(req, res);
            } else {
                res.sendRedirect(ctx + login_url);
            }
        } else {
            filterChain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
