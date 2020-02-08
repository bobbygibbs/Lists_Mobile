package com.jillywiggens.mihaly.services.books

import com.fasterxml.jackson.databind.JsonNode
import com.jillywiggens.mihaly.models.books.Book
import com.jillywiggens.mihaly.services.ServiceFactory
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

object BookServiceFactory {

    private const val PERSONAL_SERVER_LOCAL_IP_ADDRESS = "http://jillywiggens.ddns.net/"

    fun generate() = ServiceFactory.generateServiceFromUrl<BookService>(PERSONAL_SERVER_LOCAL_IP_ADDRESS)

    interface BookService {
        @GET("books")
        fun getBooks(): Single<JsonNode>

        @POST("books")
        fun addBook(@Body book: Book): Single<Response<Void>>

        @POST("books/{id}/finish")
        fun finishBook(@Path("id") id: Int): Single<Response<Void>>

        @DELETE("books/{id}")
        fun deleteBook(@Path("id") id: Int): Single<Response<Void>>
    }
}