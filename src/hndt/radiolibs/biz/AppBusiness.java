package hndt.radiolibs.biz;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContext;

@ApplicationScoped
public class AppBusiness {

    ServletContext servletContext;
    String realRootPath;

    public void initWith(ServletContext context) {
        servletContext = (context);
        realRootPath = context.getRealPath("/");
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getRealRootPath() {
        return realRootPath;
    }

    private static AppBusiness biz = null;

    private AppBusiness() {
    }

    public synchronized static AppBusiness getInstance() {
        if (biz == null) {
            biz = new AppBusiness();
        }
        return biz;
    }
}
