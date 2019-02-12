<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="hndt.radiolibs.util.Logger" %>

<%
    org.slf4j.Logger uo = LoggerFactory.getLogger("special");
    uo.info("和田大神3");

    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.info("denglu ,登陆");

    Logger.info("Tomcat 日志记录");
%>