@file:JvmName("DatabaseModuleKt")

package com.dream.keymediation.repository.remote.api

import android.content.Context
import com.dream.keymediation.usecases.information.video.YoutubeInteractor
import dagger.Component

fun network() = NetworkComponent.instance

@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    fun context() : Context
    fun keyMeditationApi() : KeyMeditationApi
    fun youTubeApi() : YoutubeApi
    fun youTubeInteractor(): YoutubeInteractor

    companion object {
        lateinit var instance: NetworkComponent
    }
}