$(function () {

    var url = (window.location.href);
    console.log(url);

    if (url.indexOf('_edit_') >= 0) {
        $(":input").change(function () {
            window.parent.changed = true;
        });
        $(":submit").bind("click", function () {
            window.parent.changed = false;
        });
    }

    if (url.indexOf('/res_edit_create.xhtml') >= 0 || url.indexOf('/res_edit_right.xhtml') >= 0) {

        $(".am-dropdown-toggle").bind("click", function (e) {
            $('#dropdown').dropdown();
        });
        $(".am-dropdown-content li").bind("click", function (e) {
            $("#dropdown").dropdown("toggle");
            $(".dropdown-text").val($(this).text());
        });
    }

    else if (url.indexOf('/channel_edit_field.xhtml?channel_id') >= 0) {

    }

    else if (url.indexOf('/typed_list.xhtml') >= 0) {

    }

    else if (url.indexOf('/article_edit_base.xhtml') >= 0) {

    }

    else if (url.indexOf('/vlive/clock_select.xhtml') >= 0) {
        $(".CheckboxRow").tableSelectable();
    }

    else if (url.indexOf('/vlive/typed_select.xhtml') >= 0) {
        $(".CheckboxRow").tableSelectable();
    }

    else if (url.indexOf('/vlive/res_list_select.xhtml') >= 0) {
        $(".CheckboxRow").tableSelectable();
    }

});

function getUrlParam(name) {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars[name];
}

function filenameByUrl(url) {
    a = url.lastIndexOf('/');
    b = url.lastIndexOf('?');
    if (b < 0) {
        b = url.length;
    }
    name = url.substring(a + 1, b);
    return name;
}

function parseDom(arg) {
    var objE = document.createElement("div");
    objE.innerHTML = arg;
    return objE.childNodes;
}

function getImageWidth(url, callback) {
    var img = new Image();
    img.src = url;
    // 如果图片被缓存，则直接返回缓存数据
    if (img.complete) {
        callback(img.width, img.height);
    } else {
        // 完全加载完毕的事件
        img.onload = function () {
            callback(img.width, img.height);
        }
    }
}

function selectLeftTreeLeaf(id) {
    zTree = (top.frames['navTreeIframe'].zTree);
    var node = zTree.getNodeByParam("id", id, null);
    zTree.selectNode(node);
}

function appendUrlParam(url, param) {
    if (url.indexOf("?") < 0) {
        url = url + "?" + param;
    } else {
        url = url + "&" + param;
    }
    return url;
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

function jumpto(href) {
    window.parent.jumpto(href);
}

function reloadThisWindow() {
    location.reload();
}

$.fn.tableULStyle = function () {
    var h = "";
    var $table = $(this);
    $table.find("td").each(function () {
        h = h + "<li>" + $(this).html() + "</li>";
    });
    $table.find("tbody").remove();
    $table.html("<ul>" + h + "</ul>");
};


$.fn.tableSelectable = function () {
    //全选反选行变色
    var $table = $(this);
    $table.on("click", "tbody>tr", function () {
        $("td:checkbox", $table).prop("checked", false).change();
        var b = $(":checkbox", this).prop("checked");
        $(":checkbox", this).prop("checked", !b).change();
    });


    $table.on("change", "td :checkbox", function () {
        $(this).parents('tr').toggleClass("am-active", $(this).prop("checked"));
    });

    $table.on("click", "td :checkbox", function (e) {
        e.stopPropagation();
    });
    $table.on("click", "td a", function (e) {
        e.stopPropagation();
    });

    $table.on("click", "th :checkbox", function (e) {
        $("td :checkbox", $table).each(function () {
            var b = ($(this).prop("checked"));
            $(this).prop("checked", !b).change();
        });
    });
};


