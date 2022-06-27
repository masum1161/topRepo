package net.top.repo.api


import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import net.top.repo.BuildConfig
import net.top.repo.api.response.home_page.GitResponse
import net.top.repo.utilities.BasicAuthInterceptor
import net.top.repo.utilities.Constants
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface TopRepoApiService {
    @GET("search/repositories")
    suspend fun getRepoList(
        @Query("q") search: String = "Android"
    ): Response<GitResponse>


    companion object {
        fun create(): TopRepoApiService {
            Constants.main()
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(BasicAuthInterceptor(Constants.USERNAME, Constants.PASSWORD))
            builder.addInterceptor(HostSelectionInterceptor())
            builder.connectTimeout(2, TimeUnit.MINUTES);
            builder.readTimeout(1, TimeUnit.MINUTES);
            builder.writeTimeout(1, TimeUnit.MINUTES);
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(OkHttpProfilerInterceptor())
            }
            val client = builder.build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(TopRepoApiService::class.java)
        }
    }
}