package hndt.radiolibs.util;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    public String urlGet(String url) {
        String text = null;
        try {
            final CacheControl cacheControl = new CacheControl.Builder().noCache().noStore().build();
            Request request = new Request.Builder().url(url).cacheControl(cacheControl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                text = new String(response.body().bytes(), "utf-8");
            } else {
                throw new IOException("网络请求失败：" + url);
            }
        } catch (Exception e) {
            Logger.error("读取URL内容失败：" + url);
        }
        return text;
    }

    private static HttpClient biz = null;
    private OkHttpClient client;

    private HttpClient() {
    }

    public synchronized static HttpClient getInstance() {
        if (biz == null) {
            biz = new HttpClient();
            biz.client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).connectTimeout(10, TimeUnit.SECONDS).build();
        }
        return biz;
    }
}
