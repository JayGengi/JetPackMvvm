package com.duobang.jetpackmvvm.pms.network;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.duobang.jetpackmvvm.pms.util.CacheUtil;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录专用拦截器
 * 主要用来拦截Cookie，并做缓存
 */
public class LoginHttpInterceptor implements Interceptor {

    private static final String TAG = "LoginHttpInterceptor";

    public LoginHttpInterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().build();
        Response response = chain.proceed(request);
        response = response.newBuilder().build();
        String headers = response.headers().toString();
        if (headers.contains("Set-Cookie") || headers.contains("set-cookie")) {
            String sessionId = getHttpCookieId(response.headers().get("set-cookie"));
            LogUtils.d("LoginIntercept ::: cookie = " + sessionId);
            assert sessionId != null;
            CacheUtil.INSTANCE.setToken(sessionId);
        }
        LogUtils.d(TAG, "LoginIntercept ::: response: " + response.request().method());
        LogUtils.d(TAG, "LoginIntercept ::: response: " + response.request().url());
        LogUtils.d(TAG, "LoginIntercept ::: headers = " + headers);
        return response;
    }

    /* sessionId=ebdf25ee70e6cd74e21784447101ac13320d8199ef520764; path=/; max-age=864000; HttpOnly=true */
    private String getHttpCookieId(String headers) {
        List<HttpCookie> httpCookies = HttpCookie.parse(headers);
        if (httpCookies == null || httpCookies.size() == 0) {
            return null;
        }
        //后台定义只有一个cookie
        HttpCookie httpCookie = httpCookies.get(0);
        return httpCookie.getValue();
    }

}