/*
 *  @author pysh@163.com
 */
package hndt.radiolibs.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author pysh
 */
public class BrowerRequest {
    static final String[] mobileDevices = new String[]{"Android", "iPhone", "iPod", "iPad", "UC", "Windows Phone", "webOS", "BlackBerry"};

    public static boolean ifMobileReq(HttpServletRequest request){
        boolean yesno = false;
        String agent = request.getHeader("User-Agent");
        for (String device : mobileDevices) {
            if (agent.contains(device)) {
                request.getSession().setAttribute("mobile", "m");
                yesno = true;
                break;
            }
        }
        return yesno;
    }

    public static String getBrowserInfo(String ua) {

        String browser = null;
        boolean isMSIE = (ua != null && (ua.indexOf("MSIE") != -1 || ua.indexOf("Trident") != -1));
        boolean isMobile = (ua != null && ua.indexOf("Mobile") != -1);
        boolean isAndroid = (ua != null && ua.indexOf("Android") != -1);
        boolean isiphone = (ua != null && ua.indexOf("iPhone") != -1);
        boolean isipad = (ua != null && ua.indexOf("iPad") != -1);
        boolean isMSIE10 = (ua != null && ua.indexOf("MSIE 10") != -1);
        boolean isMSIE9 = (ua != null && ua.indexOf("MSIE 9") != -1);
        boolean isMSIE8 = (ua != null && ua.indexOf("MSIE 8") != -1);
        boolean isChrome = (!isipad && !isAndroid && !isiphone && ua != null && ua.indexOf("Chrome/") != -1);
        boolean isSafari = (!isipad && !isAndroid && !isiphone && !isChrome && (ua != null && ua.indexOf("Safari/") != -1));
        boolean isSafari5 = (!isipad && !isAndroid && !isiphone && !isChrome && (ua != null && ua.indexOf("Safari/") != -1 && ua.indexOf("Version/5.") != -1));
        boolean isFirefox = (!isipad && !isAndroid && !isiphone && ua != null && ua.indexOf("Firefox/") != -1);
        String version = "1";

        if (isFirefox) {
// Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:13.0) Gecko/20100101 Firefox/13.0
            version = ua.replaceAll("^.*?Firefox/", "").replaceAll("\\.\\d+", "");
            browser = "Firefox " + version;
        } else if (isChrome) {
// Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5
            version = ua.replaceAll("^.*?Chrome/(\\d+)\\..*$", "$1");
            browser = "Chrome " + version;
        } else if (isiphone) {
// Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53
            //version = ua.replaceAll("^.*?Version/(\\d+)\\.(\\d+).*$", "$1$2");
            browser = "iPhone ";
        } else if (isAndroid) {
// Mozilla/5.0 (Linux;U; Android 4.2.1; zh-cn;M351 Build/JOP40D) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2

            browser = "Android ";

        } else if (isSafari) {
// Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2
            version = ua.replaceAll("^.*?Version/(\\d+)\\.(\\d+).*$", "$1$2");
            browser = "Safari " + version;
        } else if (isMSIE) {
// Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)
// IE 11 Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko 
            version = ua.indexOf("MSIE") != -1 ? ua.replaceAll("^.*?MSIE\\s+(\\d+).*$", "$1") : ua.replaceAll("^.*?rv:(\\d+).*$", "$1");
            browser = "IE " + version;
        } else if (isipad) {
// Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)
// IE 11 Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko 
            //version = ua.indexOf("MSIE") != -1 ? ua.replaceAll("^.*?MSIE\\s+(\\d+).*$", "$1") : ua.replaceAll("^.*?rv:(\\d+).*$", "$1");  
            browser = "iPad";
        }

        if (browser!=null && browser.length() > 30) {
            browser = null;
        }
        return browser;
    }
}
