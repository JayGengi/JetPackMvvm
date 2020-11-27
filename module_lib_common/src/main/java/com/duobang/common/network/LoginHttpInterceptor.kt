package com.duobang.common.network

import com.blankj.utilcode.util.LogUtils
import com.duobang.common.util.CacheUtil.setToken
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpCookie

/**
 * 登录专用拦截器
 * 主要用来拦截Cookie，并做缓存
 */
class LoginHttpInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().build()
        var response = chain.proceed(request)
        response = response.newBuilder().build()
        val headers = response.headers().toString()
        if (headers.contains("Set-Cookie") || headers.contains("set-cookie")) {
            val sessionId = getHttpCookieId(response.headers()["set-cookie"])
            LogUtils.d("LoginIntercept ::: cookie = $sessionId")
            setToken(sessionId!!)
        }
        LogUtils.d(
            TAG,
            "LoginIntercept ::: response: " + response.request().method()
        )
        LogUtils.d(
            TAG,
            "LoginIntercept ::: response: " + response.request().url()
        )
        LogUtils.d(
            TAG,
            "LoginIntercept ::: headers = $headers"
        )
        return response
    }

    /* sessionId=ebdf25ee70e6cd74e21784447101ac13320d8199ef520764; path=/; max-age=864000; HttpOnly=true */
    private fun getHttpCookieId(headers: String?): String? {
        val httpCookies =
            HttpCookie.parse(headers)
        if (httpCookies == null || httpCookies.size == 0) {
            return null
        }
        //后台定义只有一个cookie
        val httpCookie = httpCookies[0]
        return httpCookie.value
    }

    companion object {
        private const val TAG = "LoginHttpInterceptor"
    }
}