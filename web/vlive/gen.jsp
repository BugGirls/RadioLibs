<%@ page import="hndt.radiolibs.bean.ManagerBean" %>
<%@ page import="hndt.radiolibs.biz.EnumValue" %>
<%@ page import="hndt.radiolibs.servlet.TipAnnotation" %>
<%@ page import="hndt.radiolibs.biz.RuntimeBusiness" %>
<%@ page import="hndt.radiolibs.util.DBTool" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    long channel_id = NumberUtils.toLong(request.getParameter("channel_id"));
    RuntimeBusiness.getInstance().clear(channel_id,LocalDateTime.now());
    RuntimeBusiness.getInstance().generate(channel_id, Timestamp.valueOf(LocalDateTime.now().plusDays(-1)));
%>