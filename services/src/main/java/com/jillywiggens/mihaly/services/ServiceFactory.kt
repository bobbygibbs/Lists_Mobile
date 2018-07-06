package com.jillywiggens.mihaly.services

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

object ServiceFactory {
        inline fun <reified T> generate(): T = Retrofit.Builder()
                .baseUrl("https://forecast.weather.gov/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(T::class.java)
}