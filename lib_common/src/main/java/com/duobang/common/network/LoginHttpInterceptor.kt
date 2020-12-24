package com.duobang.common.network

import com.blankj.utilcode.util.LogUtils
import com.duobang.base.base.appContext
import com.duobang.base.ext.util.logd
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.routerJump
import com.duobang.common.util.CacheUtil
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
        val token = CacheUtil.getToken()
        token.logd("token")
        var request = chain.request()
        request = request.newBuilder().build()
        var response = chain.proceed(request)
        response = response.newBuilder().build()
        if ("" == token) {
            val headers = response.headers().toString()
            if (headers.contains("Set-Cookie") || headers.contains("set-cookie")) {
                val sessionId = getHttpCookieId(response.headers()["set-cookie"])
                LogUtils.d("LoginIntercept ::: cookie = $sessionId")
                CacheUtil.setToken(sessionId!!)
            }
        } else {
            if (401 == response.code() || 403 == response.code()) {
                //重新登录
                CacheUtil.setToken("")
                appContext.routerJump(RouterConstant.ACT.LOGIN)
            } else {
                response.header("cookie", "sessionId=$token")
            }
        }
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