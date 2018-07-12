package com.jillywiggens.mihaly.services

import io.reactivex.Observable
import retrofit2.http.GET

object BookServiceFactory {

    private const val PERSONAL_SERVER_LOCAL_IP_ADDRESS = "http://192.168.0.20/"

    fun generate() = ServiceFactory.generateServiceFromUrl<BookService>(PERSONAL_SERVER_LOCAL_IP_ADDRESS)

    interface BookService {
        @GET("books")
        fun getBooks(): Observable<Any>
    }
}