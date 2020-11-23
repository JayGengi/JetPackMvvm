package com.duobang.jetpackmvvm.pms.network

import com.duobang.jetpackmvvm.ext.util.logd
import com.duobang.jetpackmvvm.pms.util.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = CacheUtil.getToken()
        token.logd("MyHeadInterceptor")
        val builder = chain.request().newBuilder()
            .header("cookie", "sessionId=$token")
        return chain.proceed(builder.build())
    }

}