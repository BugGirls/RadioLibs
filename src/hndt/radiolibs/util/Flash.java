package hndt.radiolibs.util;

import java.util.HashMap;
import java.util.Map;

public class Flash {

    int code;
    String message;

    private static final Map<String, String> map;
    //
    private static final String CONTROLLER = "Controller";
    private static final String BUSINESS = "Business";
    //

    static {
        map = new HashMap<>();
        map.put("upload", "上传");
        map.put("help", "帮助文档");
        map.put("component", "组件");
        map.put("resfile", "资源文件");
        map.put("restitle", "资源题名");
        map.put("restag", "资源标签");
        map.put("rescreate", "资源创建者");
        map.put("resformat", "资源格式");
        map.put("respublish", "资源出版者");
        map.put("resright", "资源权利");
        map.put("channel", "频率");
        map.put("role", "角色");
        map.put("manager", "人员");
        map.put("permission", "权限");
        map.put("program", "节目");
        map.put("clock", "钟型");
        map.put("typed", "规则");
        map.put("typedtag", "规则类型");

        //
        map.put("save", "保存");
        map.put("update", "更新");
        map.put("delete", "删除");
        map.put("send", "发送");
        map.put("status", "修改状态");
        map.put("commit", "提交");
        map.put("upload", "上传");
        map.put("copy", "拷贝");
        map.put("clean", "清空");
        map.put("savetypetags", "保存");

        //
        map.put("-1", "发生错误");
        map.put("0", "没能执行");
        map.put("1", "成功");
        map.put("", "");
    }

    public Flash(int code) {
        this.code = code;

        String entry = null;
        String operate = null;
        String result = null;

        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        for (StackTraceElement ste : stack) {
            if (ste.getClassName().endsWith(BUSINESS)) {
                String ctrl_name = ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1, ste.getClassName().indexOf(BUSINESS));
                String ctrl_desc = map.get(ctrl_name.toLowerCase());
                if (ctrl_desc != null) {
                    entry = ctrl_desc;
                }
            }
        }

        if (entry == null) {
            for (StackTraceElement ste : stack) {
                if (ste.getClassName().endsWith(CONTROLLER)) {
                    String ctrl_name = ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1, ste.getClassName().indexOf(CONTROLLER));
                    String ctrl_desc = map.get(ctrl_name.toLowerCase());
                    if (ctrl_desc != null) {
                        entry = ctrl_desc;
                        break;
                    }
                }
            }
        }

        for (StackTraceElement ste : stack) {
            operate = map.get(ste.getMethodName().toLowerCase());
            if (operate != null) {
                break;
            }
        }

        result = map.get(String.valueOf(code));
        if (code > 1) {
            result = map.get(String.valueOf(1));
        }

        if (entry != null && operate != null && result != null) {
            this.message = entry + operate + result;
        } else if (operate != null && result != null) {
            this.message = operate + result;
        } else if (result != null) {
            this.message = "操作" + result;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
