package hndt.radiolibs.util;

import com.google.gson.reflect.TypeToken;
import hndt.radiolibs.biz.EnumValue;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static long DAY = 86400000;
    public static long HOUR = 3600000;
    public static long SECOND = 1000; //秒

    public static long SECRET = 0;

    /**
     * 登录信息验证
     */
    public static final String host = "http://uc.hndt.com";
    /**
     * 网络广播登录信息
     */
    public static final String APPID_RESP = "f6e1954a6e535b377b377f9ebf6071ec";
    public static final String APPSECRET_RESP = "e5dc9c083327af987ccae87b8be9e70decdcffd3";
    /**
     * 音频库登录信息
     */
    public static final String APPID_VLIVE = "2130d4f5d06daa7af3bada6e226b24f4";
    public static final String APPSECRET_VLIVE = "809365c0497cb427de41f7c8905c6cfe38888a98";

    /**
     * 垫片标签
     */
    public static final String GASKET_TAG = "1001200";


    /**
     * public
     */
    public static final DateTimeFormatter DATEFORMAT1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATEFORMAT2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATEFORMAT3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATEFORMAT4 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATEFORMAT5 = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter DATEFORMAT6 = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATEFORMAT7 = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATEFORMAT_ZH1 = DateTimeFormatter.ofPattern("yyyy年MM月dd HH:mm");

    public static final DateTimeFormatter DATEFORMAT_ZH2 = DateTimeFormatter.ofPattern("yyyy年MM月dd");
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
    public static final String REGEX_EMAIL = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-zd]+[-.])+[A-Za-z\\d]{2,5}$";
    public static final String REGEX_EMAIL2 = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    public static final Type typeInteger = new TypeToken<List<Integer>>() {
    }.getType();
    public static final Type typeLong = new TypeToken<List<Long>>() {
    }.getType();
    public static final Type typeString = new TypeToken<List<String>>() {
    }.getType();

    /**
     * private
     */
    private static DateFormat DF_FORM_ID = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    public static void copyBean(Object dest, Object src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.error(ex);
        }
    }

    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL2, email);
    }

    public static String nowTimeString() {
        return LocalDateTime.now().format(DATEFORMAT1);
    }

    public static String nowDateString() {
        return LocalDateTime.now().format(DATEFORMAT3);
    }

    public static Timestamp nowTimestamp() {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        stamp.setNanos(0);
        return stamp;
    }

    public static String format(LocalDateTime date, DateTimeFormatter formatter) {
        return formatter.format(date);
    }

    public static LocalDateTime parse(String date, DateTimeFormatter formatter) {
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDate parseDate(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }

    /**
     * 判断传入的字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 把不带[]的字符串转为集合，限于小写逗号
     * @param text
     * @return
     */
    public static List<String> asList(String text) {
        if (Utils.isNotEmpty(text)) {
            String[] words = StringUtils.split(text, ',');
            List<String> list = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                list.add(words[i]);
            }
            return list;
        }
        return null;
    }

    /**
     * 把不带[]的字符串转为集合，限于小写逗号
     * @param text
     * @return
     */
    public static List<Long> asListLong(String text) {
        if (Utils.isNotEmpty(text)) {
            String[] words = StringUtils.split(text, ',');
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                list.add(Long.parseLong(words[i]));
            }
            return list;
        }
        return null;
    }

    public static List<Integer> asListInteger(String text) {
        if (Utils.isNotEmpty(text)) {
            String[] words = StringUtils.split(text, ',');
            Integer[] ids = new Integer[words.length];
            for (int i = 0; i < words.length; i++) {
                ids[i] = Integer.parseInt(words[i]);
            }
            List<Integer> list = Arrays.asList(ids);
            return list;
        }
        return null;
    }

    /**
     * 传入一个以','分割的字符串，返回List类型的数组
     *
     * @param str：一种是不带[]的字符串集合，一种是带[]的json类型的字符串
     * @return
     */
    public static List<Long> stringToList(String str) {
        List<Long> idList = new ArrayList<>();

        if (StringUtils.isNotEmpty(str)) {
            try {
                idList = GSON.toList(str, Long.class);
            } catch (Exception e) {
                String[] ids = str.split(",");

                for (String s : ids) {
                    idList.add(Long.parseLong(s));
                }
            }
        }
        return idList;
    }

    /**
     * 用正则分割
     * @param line
     * @return
     */
    public static List<String> split(String line) {
        String[] words = line.split("[\\r\\s+|；|,|，|;|]");
        List<String> words_list = new ArrayList<>(Arrays.asList(words));
        List<String> list = new ArrayList<>(words_list.size());
        for (String s : words_list) {
            String ss = StringUtils.trimToNull(s);
            if (isNotEmpty(ss)) {
                list.add(ss);
            }
        }
        return list;
    }

    public static String asString(List list) {
        return StringUtils.join(list, ',');
    }

    public static double round(Double val, int precision) {
        BigDecimal bd = new BigDecimal(val).setScale(precision, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static double round(Double val) {
        BigDecimal bd = new BigDecimal(val).setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static String md5(String str) {
        String result = str;
        try {
            if (isNotEmpty(str)) {
                result = DigestUtils.md5Hex(str.getBytes(EnumValue.ENCODING_UTF8));
            }
        } catch (Exception ce) {
            ce.printStackTrace();
        }
        return result;
    }

    private static void removeNull(Collection list) {
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                it.remove();
            }
        }
    }

    private static boolean isNotEmpty(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof String) {
            String temp = o.toString().trim();
            return temp.length() > 0;
        } else if (o instanceof Integer) {
            return ((Integer) o) != 0L;
        } else if (o instanceof Long) {
            return ((Long) o) != 0L;
        } else if (o instanceof Double) {
            return ((Double) o) != 0D;
        } else if (o instanceof Float) {
            return ((Float) o) != 0;
        } else if (o instanceof Collection) {
            Collection temp = (Collection) o;
            removeNull(temp);
            return !(temp.isEmpty());
        } else {
            return o != null;
        }
    }

    private static boolean isEmpty(Object o) {
        return !isNotEmpty(o);
    }

    /**
     * 参数全部不为 [null、0、''] 时,返回真。
     */
    public static boolean isNotEmpty(Object... o) {
        boolean notEmpty = true;
        try {
            for (Object object : o) {
                if (isEmpty(object)) {
                    notEmpty = false;
                    break;
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return notEmpty;
    }

    /**
     * 参数任意一个为 [null、0、''] 时,返回真。
     */
    public static boolean isEmpty(Object... o) {
        return !isNotEmpty(o);
    }

    public static HashMap asHashMap(Object... params) {
        if (params.length == 0 || params.length % 2 != 0) {
            throw new CException();
        }
        HashMap map = new HashMap(params.length / 2);
        for (int i = 0; i < params.length; i = i + 2) {
            map.put(params[i], params[i + 1]);
        }
        return map;
    }

    public static String asJsonMap(Object... params) {
        Map map = asHashMap(params);
        return GSON.getInstance().create().toJson(map);
    }

    public static String asJsonArray(Object... params) {
        return GSON.getInstance().create().toJson(params);
    }

    public static List<String> find(String text, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 用于打印输出
     *
     * @param request
     * @return
     */
    public static void printParameters(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            sb.append(key).append("=").append(request.getParameter(key));
            sb.append("; ");
        }
        System.out.println(sb.toString());
    }

    public static void println(Object... objs) {
        System.out.println(Utils.asJsonArray(objs));
    }

    public static String sha1(String src) {
        String obj = src;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(src.getBytes());
            Formatter formatter = new Formatter();
            for (byte b : digest) {
                formatter.format("%02x", b);
            }
            obj = formatter.toString();
        } catch (Exception e) {
            Logger.error(e);
        }
        return obj;
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String urlEncode(String url) {
        String url_en = url;
        try {
            url_en = URLEncoder.encode(url, EnumValue.ENCODING_UTF8);
        } catch (Exception e) {
        }
        return url_en;
    }

    public static String requestIp(HttpServletRequest request) {
        //depends on 部署环境
        String ip = null;
        if (request != null) {
            ip = request.getHeader("x-real-ip");
            if (ip == null) {
                ip = request.getHeader("CLIENT-IP");
            }
            if (ip == null) {
                ip = request.getHeader("X-FORWARDED-FOR");
            }
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    public static String urlDecode(String url) {
        String url_de = url;
        try {
            url_de = URLDecoder.decode(url, EnumValue.ENCODING_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url_de;
    }

    public static boolean isNumber(String text) {
        Pattern pattern = NUMBER_PATTERN;
        Matcher match = pattern.matcher(text);
        return match.matches();
    }

    public static boolean isMobile(String tel) {
        return Utils.isNotEmpty(tel) && (tel.startsWith("1")) && isNumber(tel) && tel.length() == 11;
    }

    public static String base64Encode(String text) {
        String t_text = null;
        try {
            if (Utils.isNotEmpty(text)) {
                t_text = new String(Base64.getEncoder().encode(text.getBytes("utf-8")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t_text;
    }

    public static String base64Decode(String text) {
        String t_text = null;
        try {
            if (Utils.isNotEmpty(text)) {
                t_text = new String(Base64.getDecoder().decode(text.getBytes("utf-8")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t_text;
    }

    public static String base64Decode2(String text) {
        String result = null;
        byte[] b= new byte[0];
        try {
            b = org.apache.commons.codec.binary.Base64.decodeBase64(text.getBytes(CharEncoding.UTF_8));
            result=new String(b, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 以key为密钥,把src加密,返回密文的base64表现形式
     * <p>
     * String key = "JinTianHaoYun7@LaoLangQingChiJi";
     * String data = "9";
     * System.out.println("要加密的字符串:" + data);
     * String result = (util.desEncrypt(data, key));
     * System.out.println("加密后:" + result);
     * String result2 = util.desDecrypt(result, key);
     * System.out.println("解密后:" + result2);
     */
    public static String desEncrypt(String src, String key) {
        try {
            final String method = "DES";
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(method);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(method);
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(src.getBytes());
            return new String(Base64.getEncoder().encode(result));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * src是base64表示的密文
     * 以key为密钥,把src解密,返回明文字符串
     */
    public static String desDecrypt(String src, String key) {
        try {
            final String method = "DES";
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(method);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(method);
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            return new String(cipher.doFinal(Base64.getDecoder().decode(src)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value) {
        String value2 = base64Encode(value);
        Cookie cookie = new Cookie(name, value2);
        cookie.setPath("/");
        cookie.setDomain(".hnr.cn");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    cookie = c;
                    break;
                }
            }
        }
        return cookie == null ? null : base64Decode(cookie.getValue());
    }

    static final String[] mobileDevices = new String[]{"Android", "iPhone", "iPod", "iPad", "UC", "Windows Phone", "webOS", "BlackBerry"};

    public static boolean ifMobileReq(HttpServletRequest request) {
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

    public static String urlWithComponent(Object... words) {
        StringBuilder sb = new StringBuilder();
        for (Object str : words) {
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                if (sb.charAt(sb.length() - 1) != '/') {
                    sb.append('/');
                }
                String str_t = String.valueOf(str);
                if (str_t.indexOf('/') == 0) {
                    str_t = str_t.substring(1, str_t.length());
                }
                sb.append(String.valueOf(str_t));
            }
        }
        return sb.toString();
    }

    public static String convertDotString(String content) {
        String result = "";
        if (content.contains("，")) {
            result = content.replaceAll("，", ",");
        }
        if (content.contains(";")) {
            result = content.replaceAll(";", ",");
        }
        if (Utils.isEmpty(result)) {
            result = content.replace(" ", ",");
        }
        return result;
    }

    public static String suffixRandom(String url) {
        String patameter_url = "";
        if (Utils.isEmpty(url)) {
            return patameter_url;
        }
        String random = String.valueOf(System.currentTimeMillis() / 1000);
        char c1 = '?', c2 = '&';
        StringBuilder sb = new StringBuilder(url);
        int idx = url.lastIndexOf(c1);
        int idx2 = url.lastIndexOf(c2);
        if (idx < 0 && idx2 < 0) {
            sb.append(c1).append(random);
        } else if (idx == sb.length() - 1 || idx2 == sb.length() - 1) {
            sb.append(random);
        } else if (idx > 0) {
            sb.append(c2).append(random);
        }
        patameter_url = sb.toString();

        return patameter_url;
    }

    public static boolean urlIsUsage(String url) {
        boolean isUsage = true;
        URL test = null;
        if (Utils.isEmpty(url) || !url.contains("http")) {
            isUsage = false;
        }
        try {
            test = new URL(url);
        } catch (MalformedURLException e) {
            isUsage = false;
        }
        if (isUsage) {
            HttpURLConnection con = null;
            int state = 0;
            try {
                con = (HttpURLConnection) test.openConnection();
                if (con != null) {
                    state = con.getResponseCode();
                    isUsage = state == 200;
                } else {
                    isUsage = false;
                }
            } catch (IOException e) {
                isUsage = false;
            }
        }
        return isUsage;
    }

    public static boolean testDbConfig(String url, String user, String password) {
        if (Utils.isEmpty(url, user, password)) {
            return false;
        } else {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException sqle) {
                Logger.error(sqle);
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isInstalled(String basePath) {
        Path lock = Paths.get(basePath, "install", "install.lock");
        return Files.exists(lock);
    }

    /**
     * 双重校验锁获取一个Random单例
     * @return
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 获取一个[0, max)之间的随机整数
     *
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        return getRandom().nextInt(max);
    }

    /**
     * 删除ArrayList中重复元素
     *
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

}
