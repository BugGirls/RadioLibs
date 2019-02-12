<%@ page import="hndt.radiolibs.bean.ManagerBean" %>
<%@ page import="hndt.radiolibs.biz.EnumValue" %>
<%@ page import="hndt.radiolibs.servlet.TipAnnotation" %>
<%@ page import="hndt.radiolibs.ctrl.CommonController" %>
<%

    ManagerBean manager = (ManagerBean) session.getAttribute(EnumValue.LITERAL_MANAGER);
    if (manager == null) {
        response.sendRedirect("login.xhtml");
        return;
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>RadioLibs</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="/resources/js/jquery-1.9.1.min.js"></script>
    <script src="/resources/amazeui/js/amazeui.min.js"></script>
    <script src="/resources/amazeui/js/app.js"></script>
    <script type="text/javascript" src="/resources/layer/layer.js"></script>
    <link rel="stylesheet" href="/resources/amazeui/css/amazeui.min.css"/>
    <link rel="stylesheet" href="/resources/amazeui/css/admin.css">
</head>
<script type="text/javascript">
    if (top.location != self.location) {
        top.location = "http://admin.hndt.com/login.xhtml";
    }

    function tip(data) {
        console.log(data);
        if (data.code > -2) {
            if (data.code == -1) $("#tip_alert").addClass("am-alert-warning");
            if (data.code == 0) $("#tip_alert").addClass("am-alert-secondary");
            if (data.code == 1) $("#tip_alert").addClass("am-alert-success");
            $("#tip_text").html(data.text);
            $("#tip_wrap").fadeIn(400, function () {
                setTimeout("closeTips()", 2000);
            });
        }
    }

    function getUrlParam(href, name) {
        var vars = {};
        var parts = href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
            vars[key] = value;
        });
        return vars[name];
    }

    function closeTips() {
        $("#tip_wrap").fadeOut(400, function () {
            $("#tip_alert").removeClass("am-alert-warning").removeClass("am-alert-secondary").removeClass("am-alert-success").removeClass("am-alert-danger");
        });
    }

    //简单弹窗 指定url title shadeClose(不传为false)参数
    function openWindowBase(url, title, shade) {
        if (url.indexOf("?") < 0) {
            url += "?1=1";
        }
        if (arguments.length > 1) {
            for (var i = 1; i < arguments.length; i++) {
                url += "&" + arguments[i];
            }
        }
        //iframe层
        layer.open({
            type: 2, title: title, closeBtn: 1, shadeClose: shade, shade: 0.5, area: ['60%', '70%'], content: url
        });
        console.log(url);
    }

    var Tip = {};
    Tip.socket = null;
    Tip.connect = (function (host) {
        if ('WebSocket' in window) {
            Tip.socket = new WebSocket(host);
        } else if ('MozWebSocket' in window) {
            Tip.socket = new MozWebSocket(host);
        } else {
            console.log('请使用高版本浏览器');
            return;
        }

        Tip.socket.onopen = function () {
            setInterval(function () {
                Tip.socket.send('t')
            }, 10000);
        };
        Tip.socket.onclose = function () {
        };
        Tip.socket.onerror = function () {
            console.log("错误");
        };

        Tip.socket.onmessage = function (event) {
            if (event.data.length === 1) {
                return;
            }
            var obj;
            eval("obj=" + event.data);
            tip(obj);
        };
    });

    Tip.initialize = function () {
        if (window.location.protocol == 'http:') {
            Tip.connect('ws://' + window.location.host + '/api/ws/tip?manager_id=<%=manager.getId()%>');
        } else {
            Tip.connect('wss://' + window.location.host + '/api/ws/tip?manager_id=<%=manager.getId()%>');
        }
    };
    Tip.initialize();

    $(function () {
        $('#logout').click(function() {
            window.location.href = '/api/manager/logout';
        })
    });
</script>
<body>
<header class="am-topbar am-topbar-inverse admin-header">
    <div class="am-topbar-brand">
        <strong><span>河南人民广播电台</span> 网络广播管理平台</strong>
        <small>V1.0</small>
    </div>
    <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only"
            data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span
            class="am-icon-bars"></span></button>
    <div class="am-collapse am-topbar-collapse" id="topbar-collapse">
        <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
            <%--<li><a href="javascript:;"><span class="am-icon-envelope-o"></span> 收件箱 <span class="am-badge am-badge-warning">5</span></a></li>--%>
            <li class="am-dropdown" data-am-dropdown>
                <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;" title="<%=new CommonController().asString(manager.getRoleList(), "name")%>">
                    <span class="am-icon-users"></span> <%=manager.getName()%> <span class="am-icon-caret-down"></span>
                </a>
                <ul class="am-dropdown-content">
                    <li><a id="logout" class="" onclick="return confirm('确实要退出吗？')"><span
                            class="am-icon-power-off"></span> 退出</a></li>
                </ul>
            </li>
            <li class="am-hide-sm-only"><a href="javascript:;" id="admin-fullscreen"><span
                    class="am-icon-arrows-alt"></span> <span class="admin-fullText">开启全屏</span></a></li>
        </ul>
    </div>
</header>
<%--header结束--%>
<div class="am-cf admin-main">
    <%--sidebar start--%>
    <div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
        <div class="am-offcanvas-bar admin-offcanvas-bar">
            <ul class="am-list admin-sidebar-list setbg">

                <li style="background: #f9f9f9"><a href="home.xhtml" target="mainIframe"><span class="am-icon-home"></span> 首页</a></li>

                <li><a href="channel_list.xhtml" target="mainIframe"><span class="am-icon-bookmark"></span> 我的网络广播频率</a></li>
                <li><a href="typed_list.xhtml" target="mainIframe"><span class="am-icon-gears"></span> 规则管理</a></li>
                <li><a href="clock_list.xhtml" target="mainIframe"><span class="am-icon-clock-o"></span> 钟型管理</a></li>
                <li><a href="program_list.xhtml" target="mainIframe"><span class="am-icon-list-ul"></span> 节目单管理</a></li>
                <li class="admin-parent">
                    <a class="am-cf" data-am-collapse="{target: '#collapse-nav1'}"><span class="am-icon-database"></span> 系统资源库 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
                    <ul class="am-list admin-sidebar-sub am-collapse" id="collapse-nav1">
                        <% for (EnumValue.Category category : EnumValue.Category.values()) { %>
                        <li><a href="res_list.xhtml?category=<%=category.getCode()%>" target="mainIframe"><span class="am-icon-file-audio-o"></span> <%=category.getName()%>
                        </a></li>
                        <% }%>
                    </ul>
                </li>
                <li class="admin-parent">
                    <a  class="am-cf" data-am-collapse="{target: '#collapse-nav2'}"><span class="am-icon-database am-text-success"></span> 我的资源库 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
                    <ul class="am-list admin-sidebar-sub am-collapse" id="collapse-nav2">
                        <% for (EnumValue.Category category : EnumValue.Category.values()) { %>
                        <li><a href="res_by_member_list.xhtml?category=<%=category.getCode()%>" target="mainIframe"><span class="am-icon-file-audio-o"></span> <%=category.getName()%>
                        </a></li>
                        <% }%>
                    </ul>
                </li>
                <li><a href="res_list_search.xhtml" target="mainIframe"><span class="am-icon-search"></span> 可用资源检索</a></li>
                <li><a href="tag_group_list.xhtml" target="mainIframe"><span class="am-icon-tags"></span> 标签组管理</a></li>
                <li><a href="log_list.xhtml" target="mainIframe"><span class="am-icon-calendar"></span> 操作日志</a></li>

            </ul>
        </div>
    </div>
    <%--sidebar end--%>
    <%--content start--%>
    <div class="admin-content">
        <iframe id="mainIframe" name="mainIframe" src="home.xhtml" scrolling="auto" frameborder="0" style="width: 100%;height: 100%;"></iframe>
    </div>
</div>

<div id="tip_wrap" class="am-panel am-panel-default am-padding-0" style="position: fixed; top: 2px; left: 32%; right: 32%;display: none;z-index: 1998;">
    <div id="tip_alert" class="am-alert am-text-center am-margin-0" style="background: #fff; color: #5c5c5c;">
        <i class="am-close am-icon-times" onclick="closeTips()" style="color: indianred;"></i>
        <div id="tip_text"></div>
    </div>
</div>
</body>

<style>
    li.z-crt{
        background-color:#f9f9f9;
    }
</style>
<script>
    $(function() {
        $('ul.setbg li').click(function() {
            var _this = $(this);

            $('li').each(function() {
                $(this).removeAttr('style');
                $(this).removeClass('z-crt');
            });
            $(this).attr('style', 'background:#f9f9f9');
        });
        $('ul.setbg li').find('ul').children().click(function(e) {
            e.stopPropagation();
            $(this).addClass('z-crt').siblings().removeClass('z-crt');
        });
    });
</script>
</html>
