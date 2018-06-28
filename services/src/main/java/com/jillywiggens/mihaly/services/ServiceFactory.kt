package com.jillywiggens.mihaly.services

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class ServiceFactory {

    companion object {
        inline fun <reified T> generate(): T = Retrofit.Builder()
                .baseUrl("https://127.0.0.1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(T::class.java)

        val bookService get() = generate<BookService>()
    }
}