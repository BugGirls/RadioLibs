/**
 * author: pysh@163.com
 */
package hndt.radiolibs.util;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DBTool {

    public static long insert(String sql, Object... params) {
        long id = -1;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            id = insert(conn, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return id;
    }

    public static long insert(Connection conn, String sql, Object... params) {
        long id = -1;
        try {
            QueryRunner runner = new QueryRunner();
            id = runner.update(conn, sql, params);
            if (id > 0) {
                id = lastId(conn);
            }
        } catch (Exception ex) {
            close(conn);
            Logger.error(ex, "数据插入异常");
        }
        return id;
    }

    public static int update(Connection conn, String sql, Object... params) {
        int r = -1;
        try {
            QueryRunner runner = new QueryRunner();
            r = runner.update(conn, sql, params);
        } catch (Exception ex) {
            close(conn);
            Logger.error(ex);
        }
        return r;
    }

    public static int update(String sql, Object... params) {
        int r = -1;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            r = update(conn, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return r;
    }

    public static long lastId(Connection conn) {
        Long lastid = 0L;
        try {
            QueryRunner runner = new QueryRunner();
            lastid = runner.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<Number>()).longValue();
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        return lastid;
    }

    public synchronized static int transaction(TransactionExecutor executor) {
        int r = -1;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
            long row = executor.run();
            DbUtils.commitAndCloseQuietly(conn);
        } catch (Exception ex) {
            executor.rollback();
            DbUtils.rollbackAndCloseQuietly(conn);
            Logger.error(ex);
        } finally {

        }
        return r;
    }

    public static Connection begin() {
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
        } catch (Exception ex) {
            DbUtils.closeQuietly(conn);
            Logger.error(ex);
        }
        return conn;
    }

    public static void commit(Connection conn) {
        DbUtils.commitAndCloseQuietly(conn);
    }

    public static void rollback(Connection conn) {
        DbUtils.rollbackAndCloseQuietly(conn);
    }

    public static void close(Connection conn) {
        try {
            if (!conn.isClosed()) {
                if (conn.getAutoCommit()) {
                    DbUtils.closeQuietly(conn);
                } else {
                    DbUtils.rollbackAndCloseQuietly(conn);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static int batchUpdate(Statement... statments) {
        int r = 0;
        Connection conn = null;
        QueryRunner runner = new QueryRunner();
        try {
            conn = DataSourcePool.getInstance().getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
            for (Statement statement : statments) {
                runner.update(conn, statement.getSql(), statement.getParams());
                r = r + 1;
            }
            DbUtils.commitAndCloseQuietly(conn);
        } catch (SQLException ex) {
            DbUtils.rollbackAndCloseQuietly(conn);
            Logger.error(ex);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return r;
    }

    public static <T> List<T> list(Class<T> clazz, String sql, Object... params) {
        List<T> list = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            list = list(conn, clazz, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return list;
    }

    // 要求 T 必须有构造函数，table的字段名称和T的字段一致。没有数据数时返回空(null)
    public static <T> List<T> list(Connection conn, Class<T> clazz, String sql, Object... params) {
        List<T> result = null;
        try {
            QueryRunner runner = new QueryRunner();
            result = runner.query(conn, sql, new BeanListHandler<>(clazz), params);
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        if (result == null) {
            result = Collections.EMPTY_LIST;
        }
        return result;
    }


    public static Map<String, Object> map(Connection conn, String sql, Object... params) {
        Map<String, Object> map = null;
        try {
            QueryRunner runner = new QueryRunner();
            map = runner.query(conn, sql, new MapHandler(), params);
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        return map;
    }

    public static List<Map<String, Object>> listMap(Connection conn, String sql, Object... params) {
        List<Map<String, Object>> listmap = null;
        try {
            QueryRunner runner = new QueryRunner();
            listmap = runner.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        return listmap;
    }

    public static <T> T find(Connection conn, Class<T> clazz, String sql, Object... params) {
        List<T> result = list(conn, clazz, sql, params);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static <T> T field(Class<T> clazz, String sql, Object... params) {
        T t = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            t = field(conn, clazz, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return t;
    }

    public static <T> T field(Connection conn, Class<T> clazz, String sql, Object... params) {
        T result = null;
        try {
            QueryRunner runner = new QueryRunner();
            result = runner.query(conn, sql, new ScalarHandler<T>(), params);
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        return result;
    }

    public static <T> List<T> column(Connection conn, Class<T> clazz, String sql, Object... params) {
        List<T> result = null;
        try {
            QueryRunner runner = new QueryRunner();
            result = runner.query(conn, sql, new ColumnListHandler<T>(1), params);
        } catch (SQLException ex) {
            close(conn);
            Logger.error(ex);
        }
        return result;
    }

    public static Map<String, Object> map(String sql, Object... params) {
        Map<String, Object> map = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            map = map(conn, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return map;
    }

    public static List<Map<String, Object>> listMap(String sql, Object... params) {
        List<Map<String, Object>> map = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            map = listMap(conn, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return map;
    }

    public static <T> T find(Class<T> clazz, String sql, Object... params) {
        T t = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            t = find(conn, clazz, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return t;
    }




    public static <T> List<T> column(Class<T> clazz, String sql, Object... params) {
        List<T> result = null;
        Connection conn = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            result = column(conn, clazz, sql, params);
        } catch (SQLException ex) {
            Logger.error(ex);
        } finally {
            close(conn);
        }
        return result;
    }

    public static class Statement {

        String sql;
        Object[] params;

        public Statement(String sql, Object... params) {
            this.sql = sql;
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }

    }

    public abstract static class TransactionExecutor {
        public abstract long run();

        public void rollback() {
        }
    }

}
