<%@ page import="hndt.radiolibs.bean.ManagerBean" %>
<%@ page import="hndt.radiolibs.biz.EnumValue" %>
<%@ page import="hndt.radiolibs.servlet.TipAnnotation" %>
<%@ page import="hndt.radiolibs.biz.RuntimeBusiness" %>
<%@ page import="hndt.radiolibs.util.DBTool" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="hndt.radiolibs.util.SQL" %>
<%@ page import="java.util.List" %>
<%@ page import="hndt.radiolibs.bean.ResBean" %>
<%@ page import="hndt.radiolibs.biz.ResBusiness" %>
<%@ page import="hndt.radiolibs.util.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
//    SQL sql = SQL.of("select * from res_tag_custom");
//    List<ResBean> list = DBTool.list(ResBean.class, sql.sql());
//    LocalDateTime basetime = LocalDateTime.now().minusDays(5);
//    for (ResBean row : list) {
//        LocalDateTime time = basetime.plusMinutes(row.getId());
//        DBTool.update("update res_tag_custom set updatetime=? where id=?", time.format(Utils.DATEFORMAT1), row.getId());
//    }
%>