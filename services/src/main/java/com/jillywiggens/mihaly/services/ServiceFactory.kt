package com.jillywiggens.mihaly.services

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class ServiceFactory {

    companion object {
        inline fun <reified T> generate(endpoint: String): T = Retrofit.Builder()
                .baseUrl("https://127.0.0.1/$endpoint/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(T::class.java)

        val bookService get() = generate<BookService>("books")
    }
}