package com.dream.keymediation.repository.remote.api

import android.content.Context
import com.dream.keymediation.MyApplication
import com.dream.keymediation.repository.model.VideoJsonAdapter
import com.dream.keymediation.usecases.information.video.YoutubeInteractor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named

@Module
class NetworkModule(private val app: MyApplication) {

    companion object {
        val BASE_URL = "https://api.key-meditation.com/webservices/"
        val YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/"
    }

    @Provides
    fun provideContext() : Context = app

    @Provides
    fun provideOkHttpClient() : OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient.addInterceptor(logging)

        return okHttpClient.build()
    }

    @Provides
    fun provideYoutubeMoshi() : Moshi = Moshi.Builder()
            .add(VideoJsonAdapter())
            .build()

    @Provides
    @Named("youtube")
    fun provideYoutubeRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit = Retrofit.Builder()
            .baseUrl(YOUTUBE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Inject fun providesKeyMeditationApi(@Named("keymeditation") retrofit: Retrofit) : KeyMeditationApi = retrofit.create(KeyMeditationApi::class.java)

    @Provides
    @Inject fun providesYoutubeApi(@Named("youtube") retrofit: Retrofit) : YoutubeApi = retrofit.create(YoutubeApi::class.java)

    @Provides
    fun providesYoutubeInteractor() : YoutubeInteractor = YoutubeInteractor()
}
