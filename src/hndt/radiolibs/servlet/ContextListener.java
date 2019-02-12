package hndt.radiolibs.servlet;


import hndt.radiolibs.biz.AppBusiness;
import hndt.radiolibs.biz.ExtractBusiness;
import hndt.radiolibs.util.DataSourcePool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebListener
public class ContextListener implements ServletContextListener {
    // 定时任务
    private ScheduledExecutorService extractExecutor;

    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        AppBusiness.getInstance().initWith(event.getServletContext());
        // 只有一个线程，用来调度执行将来的任务
        extractExecutor = Executors.newSingleThreadScheduledExecutor();
        extractExecutor.scheduleAtFixedRate(new ExtractBusiness(event.getServletContext()), 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器
     * @param event
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            DataSourcePool.close();
            extractExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
