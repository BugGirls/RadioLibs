package hndt.radiolibs.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import hndt.radiolibs.biz.PropertyBusiness;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSourcePool {
    private static DataSourcePool dspool;
    private static DruidDataSource source;
    static Map<String, String> config;

    private DataSourcePool() {
        synchronized (this) {
            if (source == null) {
                try {
                    config = new HashMap<>();
                    Properties pps = new Properties();
                    pps.load(getClass().getResource("/config.properties").openStream());
                    Enumeration enume = pps.propertyNames();//得到配置文件的名字
                    while (enume.hasMoreElements()) {
                        String key = (String) enume.nextElement();
                        String value = pps.getProperty(key);
                        config.put(key, value);
                    }

                    String os = System.getProperties().getProperty("os.name").toUpperCase();

                    source = new DruidDataSource();
                    source.setDriverClassName("com.mysql.jdbc.Driver");

                    source.setUrl(config.get("service.db.url"));
                    source.setUsername(config.get("service.db.user"));
                    source.setPassword(config.get("service.db.password"));


                    source.setMaxWait(6000);
                    source.setValidationQuery("SELECT 1 FROM DUAL");
                    source.setMinEvictableIdleTimeMillis(30000);
                    source.setInitialSize(1);
                    source.setMinIdle(1);
                    source.setMaxActive(999);

                    source.setFilters("stat");

                } catch (Exception e) {
                    Logger.error(e, "数据库连接异常");
                }
            }
        }

    }

    public synchronized Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static void close() {
        try {

            DataSources.destroy(source);
            if (source != null) {
                source.close();
            }

            Enumeration<Driver> drivers = DriverManager.getDrivers();
            Driver driver = null;
            // clear drivers
            while (drivers.hasMoreElements()) {
                try {
                    driver = drivers.nextElement();
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    // deregistration failed, might want to do something, log at the very least
                }
            }
            try {
                AbandonedConnectionCleanupThread.uncheckedShutdown();
            } catch (Exception e) {
                e.printStackTrace();
                // again failure, not much you can do
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DataSourcePool getInstance() {
        if (dspool == null) {
            dspool = new DataSourcePool();
        }
        return dspool;
    }
}
