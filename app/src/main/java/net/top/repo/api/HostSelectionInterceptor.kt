package net.top.repo.api

import net.top.repo.TopRepoApplication
import net.top.repo.utilities.Constants
import net.top.repo.utilities.SessionHelper
import okhttp3.Interceptor
import okhttp3.Response

class HostSelectionInterceptor() :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var sessionHelper = SessionHelper(TopRepoApplication.getContext().applicationContext)
        var host = sessionHelper.getHost()?: Constants.devServerLink.replace("https://", "").replace("/", "")
        var request = chain.request()

        val newUrl = request.url.newBuilder()
            .scheme("https")
            .host(host)
            .build()

        request = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}