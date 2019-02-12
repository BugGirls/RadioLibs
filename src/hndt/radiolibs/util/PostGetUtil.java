package hndt.radiolibs.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 @author pysh
 */
public class PostGetUtil {

    public static String params(String... args) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < args.length; i = i + 2) {
            String key = args[i];
            String val = args[i + 1];
            map.put(key, val);
        }
        return params(map);
    }

    public static String params(Map<String, String> args) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entrySet : args.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if (Utils.isEmpty(value)){
                continue;
            }
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(key);
            sb.append('=');
            sb.append(value);
        }
        return (sb.toString());
    }

    public static int post(String urlString, String params) {
        BufferedReader in = null;
        PrintWriter out = null;
        HttpURLConnection conn = null;
        int r = -1;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("hnrsim", "simmsg"); //发送短信的服务会校验这个header
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();

            String result = "";
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            r = 1;
        } catch (Exception ex) {
            Logger.error(ex);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
        return r;
    }

    public static String get(String urlString) {
        String body = null;
        InputStream in = null;
        try {
            in = new URL(urlString).openStream();
            body = IOUtils.toString(in, "utf-8");
            Logger.info(body);
        } catch (Exception ex) {
            Logger.error(ex);
            IOUtils.closeQuietly(in);
        }
        return body;
    }
}
