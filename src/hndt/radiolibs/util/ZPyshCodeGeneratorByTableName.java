package hndt.radiolibs.util;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ZPyshCodeGeneratorByTableName {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.print("输入表名：");
        String table = input.next().toLowerCase();
        ZPyshCodeGeneratorByTableName gen = new ZPyshCodeGeneratorByTableName(table);
        gen.beanFields();
        gen.sqlInsert();
        System.out.println("");
        gen.sqlUpdate();
        System.out.println("");
        gen.editXhtml();
        System.out.println("");
    }

    String table;

    public ZPyshCodeGeneratorByTableName(String table) {
        this.table = table;
    }

    public void beanFields() {
        Map<String, ColumnBean> columns = columns(table);
        Set<String> set = columns.keySet();
        StringBuilder sb = new StringBuilder();
        for (String key : set) {
            sb.append("    ");
            ColumnBean cb = columns.get(key);
            if ("bigint".equals(cb.getType())) {
                sb.append("Long");
            } else if ("char".equals(cb.getType())) {
                sb.append("String");
            } else if ("varchar".equals(cb.getType())) {
                sb.append("String");
            } else if ("text".equals(cb.getType())) {
                sb.append("String");
            } else if ("mediumtext".equals(cb.getType())) {
                sb.append("String");
            } else if ("int".equals(cb.getType())) {
                sb.append("int");
            } else if ("tinyint".equals(cb.getType())) {
                sb.append("int");
            } else if ("timestamp".equals(cb.getType()) || "datetime".equals(cb.getType())) {
                sb.append("Timestamp");
            } else if ("double".equals(cb.getType())) {
                sb.append("double");
            }
            sb.append(" ");
            sb.append(key);
            sb.append(";");
            sb.append(" //");
            sb.append(cb.getComment());
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public void sqlUpdate() {
        //INSERT
        Map<String, ColumnBean> columns = columns(table);
        Set<String> set = columns.keySet();
        set.remove("id");
        set.remove("createtime");

        StringBuilder sb = new StringBuilder("\"");
        sb.append("UPDATE ");
        sb.append(table);
        sb.append(" SET ");
        for (String key : set) {
            sb.append(key);
            sb.append("=?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE id=?\"");
        sb.append(",");

        for (String key : set) {
            sb.append("bean.get");
            sb.append(StringUtils.capitalize(key));
            sb.append("(),");
        }
        sb.append(" bean.getId()");
        System.out.println(sb.toString());
    }

    public void sqlInsert() {
        //INSERT
        Map<String, ColumnBean> columns = columns(table);
        Set<String> set = columns.keySet();
        set.remove("id");
        set.remove("createtime");

        StringBuilder sb = new StringBuilder("\"");
        sb.append("INSERT INTO ");
        sb.append(table);
        sb.append("(");
        for (String key : set) {
            sb.append(key);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES (");
        for (String key : set) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        sb.append("\"");
        sb.append(",");

        for (String key : set) {
            sb.append("bean.get");
            sb.append(StringUtils.capitalize(key));
            sb.append("(),");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }

    public Map<String, ColumnBean> columns(String table) {
        Map<String, ColumnBean> map = new LinkedHashMap<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = DataSourcePool.getInstance().getConnection();
            statement = conn.prepareStatement("SHOW FULL FIELDS FROM " + table);
            rs = statement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");
                int idx = type.indexOf('(');
                if (idx > 0) {
                    type = type.substring(0, idx);
                }
                map.put(name, new ColumnBean(name, type, comment));
            }
            rs.close();
        } catch (SQLException e) {
            Logger.error(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(conn);
        }
        return map;
    }

    public void editXhtml() {
        StringBuilder sb = new StringBuilder();
        Map<String, ColumnBean> columns = columns(table);
        Set<String> set = columns.keySet();
        for (String key : set) {
            ColumnBean cb = columns.get(key);
            if ("id".equals(key)) {
                continue;
            }
            sb.append("<h:outputText value=\"" + cb.getComment() + "\"/>");
            sb.append("\n");
            sb.append("<h:inputText value=\"#{" + table + "Controller.bean." + key + "}\" />");
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

}
