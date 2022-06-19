package nr.king.carserv.di

import android.content.Context
import okhttp3.Interceptor

class AuthInterceptor(context: Context) : Interceptor {

    private var mContext = context
    var TAG="AuthInterceptor"

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val requestBuilder = chain.request().newBuilder()
        request.newBuilder().header("X-Auth-Token", "9cabe3a2-10ba-46b1-afe7-bc63d1f66009")
            .header("Content-Type","application/x-www-form-urlencoded").build()

        return chain.proceed(request)
    }
}