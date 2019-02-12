<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="hndt.radiolibs.bean.ChannelBean" %>
<%@ page import="hndt.radiolibs.biz.ChannelBusiness" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="hndt.radiolibs.biz.PropertyBusiness" %>
<%
    ChannelBean bean = ChannelBusiness.getInstance().load(NumberUtils.toLong(request.getParameter("channel_id")));
    String streamHost = PropertyBusiness.getInstance().find("stream.host");
    String streamApp = PropertyBusiness.getInstance().find("stream.app");
    String managerUrl = PropertyBusiness.getInstance().find("manager.url");
    String publish_mpeg = "http://"+streamHost+"/"+streamApp+"/"+bean.getUuid()+"/manifest.mpd";
    String publish_rtmp = "rtmp://"+streamHost+"/"+streamApp+"/"+bean.getUuid()+"";
    String publish_ms = "http://"+streamHost+"/"+streamApp+"/"+bean.getUuid()+"/Manifest";
    String publish_ios = "http://"+streamHost+"/"+streamApp+"/"+bean.getUuid()+"/playlist.m3u8";
    String publish_rtsp = "rtsp://"+streamHost+"/"+streamApp+"/"+bean.getUuid()+"";
%>
<html>
<head>
    <title><%=bean.getName()%>试听</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <script src="/resources/js/hls.min.js"></script>
    <link rel="stylesheet" href="/resources/amazeui/css/amazeui.min.css"/>
    <link rel="stylesheet" href="/resources/css/system.css"/>
    <link rel="stylesheet" href="/resources/css/width.css"/>
</head>
<body class="am-margin">


<h1 class=""><%=bean.getName()%></h1>

<div class="am-panel am-panel-default" style="height: 150px; width: 600px;">
    <ul>
        <li><%=publish_mpeg%></li>
        <li><%=publish_rtmp%></li>
        <li><%=publish_ms%></li>
        <li><%=publish_ios%></li>
        <li><%=publish_rtsp%></li>
    </ul>
</div>


<% if (bean != null) { %>

<audio src="" id="audio" controls autoplay></audio>

<script>
    var audio = document.getElementById('audio')
    if (Hls.isSupported()) {
        var hls = new Hls()
        hls.loadSource(
            '<%=publish_ios%>'
        )
        hls.attachMedia(audio)
        hls.on(Hls.Events.MANIFEST_PARSED, function(){
            audio.play()
        })
    }
</script>

<%}%>

</body>
</html>


