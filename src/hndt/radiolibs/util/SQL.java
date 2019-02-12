package hndt.radiolibs.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SQL {

    StringBuilder sql;
    List<Object> values;

    private SQL(String prefix) {
        sql = new StringBuilder();
        values = new ArrayList<>();
        prefix = prefix.replace("select ", "SELECT ");
        prefix = prefix.replace(" from ", " FROM ");
        prefix = prefix.replace(" where ", " WHERE ");
        sql.append(prefix);
    }

    public static SQL of(String prefix) {
        return new SQL(prefix);
    }

    // select id, name, desc from channel
    public SQL and(String field, Object value) {
        if (value != null && value.toString().length() > 0 && !"-1".equals(value.toString()) && !"[]".equals(value.toString())) {
            if (sql.indexOf(" WHERE") < 0) {// where
                sql.append(" WHERE");
            }

            if (!sqlEndwith(" WHERE")) {// 如果没有where则添加and关键字
                sql.append(" AND");
            }
            if (field.indexOf(" like") > 0) {
                field = field.replace(" like", " LIKE");
            }
            if (field.indexOf(" in") > 0) {
                field = field.replace(" in", " IN");
            }
            sql.append(" ");
            sql.append(field);// where manager_id IN
            if (field.endsWith(" IN") || field.endsWith(" AGAINST")) {// where manager_id IN (1,2,101)
                if (value instanceof Collection) {
                    if (value != null && value instanceof List && !((Collection) value).isEmpty()) {
                        sql.append(" (");
                        Collection inparams = (Collection) value;
                        for (Object inp : inparams) {
                            if (inp instanceof Number) {
                                sql.append(inp).append(",");
                            } else {
                                sql.append("'").append(inp).append("'").append(",");
                            }
                        }
                        sql.delete(sql.length() - 1, sql.length());
                        sql.append(")");
                    } else {
                        sql.delete(sql.lastIndexOf(field), sql.length());
                    }
                } else {
                    sql.append(" (").append(value).append(")");
                }
            } else if (field.endsWith(" LIKE")) {// where manager_id LIKE ?
                sql.append(" ?");
                values.add("%" + value + "%");
            } else {// where manager_id=?
                sql.append("?");
                values.add(value);
            }
        }
        return this;
    }

    public SQL or(Object... expressions) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < expressions.length; i = i + 2) {
            String field = (String) expressions[i];
            Object value = expressions[i + 1];
            if (value != null && value.toString().length() > 0 && !"-1".equals(value.toString())) {
                map.put(field, value);
            }
        }
        if (!map.isEmpty()) {
            if (sql.indexOf(" WHERE") < 0) {
                sql.append(" WHERE");
            }

            if (!sqlEndwith(" WHERE")) {
                sql.append(" AND");
            }

            if (map.size() > 1) {
                sql.append(" (");
            }

            Set<String> fields = map.keySet();
            for (String field : fields) {
                Object value = map.get(field);

                if (field.indexOf(" like") > 0) {
                    field = field.replace(" like", " LIKE");
                }
                if (field.indexOf(" in") > 0) {
                    field = field.replace(" in", " IN");
                }
                sql.append(" ");
                sql.append(field);

                if (field.endsWith(" IN") || field.endsWith(" AGAINST")) {
                    sql.append(" (");
                    if (value instanceof List) {

                        if (value != null && value instanceof List && !((List) value).isEmpty()) {
                            List inparams = (List) value;
                            for (Object inp : inparams) {
                                if (inp instanceof Number) {
                                    sql.append(inp).append(",");
                                }
                                if (inp instanceof String) {
                                    sql.append("'").append(inp).append("'").append(",");
                                }
                            }
                            sql.delete(sql.length() - 1, sql.length());
                        } else {
                            sql.delete(sql.lastIndexOf(field), sql.length());
                        }

                    } else {
                        sql.append(value);
                    }
                    sql.append(")");
                } else if (field.endsWith(" LIKE")) {
                    sql.append(" ?");
                    values.add("%" + value + "%");
                } else {
                    sql.append(" ?");
                    values.add(value);
                }
                sql.append(" OR");
            }
            sqlDeleteEnd(" OR");
            if (map.size() > 1) {
                sql.append(") ");
            }
        }

        return this;
    }

    public SQL limit(long start, long offset) {
        sql.append(" LIMIT ?,?");
        values.add(start);
        values.add(offset);
        return this;
    }

    public SQL append(String additional) {
        if (additional != null && additional.indexOf("order by") >= 0) {
            additional = additional.replace("order by", "ORDER BY");
            additional = additional.replace(" desc", " DESC");
            additional = additional.replace(" asc", " ASC");
            additional = additional.replace(" null ", " id ");
        }
        if (additional != null && !sqlEndwith(" ") && !additional.startsWith(" ")) {
            sql.append(" ");
        }
        if (additional != null) {
            sql.append(additional);
        }
        return this;
    }

    public SQL appendParams(Object... params) {
        for (Object param : params) {
            if (param != null) {
                values.add(param);
            }
        }
        return this;
    }

    public String countSql() {
        StringBuilder sb = new StringBuilder(sql);
        if (sb.indexOf("LIMIT ?,?") >= 0) {
            sb.delete(sb.indexOf("LIMIT ?,?"), sb.length());
        }
        if (sb.indexOf("ORDER BY") > 0) {
            sb.delete(sb.indexOf("ORDER BY"), sb.length());
        }
        sb.replace(sb.indexOf("SELECT"), sb.indexOf("FROM"), "SELECT COUNT(id) ");
        return sb.toString();
    }

    public Object[] countParams() {
        Object[] params = new Object[]{};
        String count_sql = countSql();
        int len = StringUtils.countMatches(count_sql, "?");
        if (len > 0) {
            params = ArrayUtils.subarray(values.toArray(), 0, len);
        }
        return params;
    }

    public String sql() {
        return sql.toString();
    }

    public Object[] params() {
        return values.toArray();
    }

    private boolean sqlEndwith(String word) {
        String suffix = sql.substring(sql.length() - word.length(), sql.length());
        return suffix.equals(word);
    }

    private void sqlDeleteEnd(String word) {
        sql.delete(sql.length() - word.length(), sql.length());
    }

    public static String toInString(List list) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(list)) {
            for (Object inp : list) {
                if (inp instanceof Number) {
                    sb.append(inp).append(",");
                }
                if (inp instanceof String) {
                    sb.append("'").append(inp).append("'").append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.sql());
        sb.append('\n');
        sb.append(StringUtils.join(this.params(), ','));
        return sb.toString();
    }
}
