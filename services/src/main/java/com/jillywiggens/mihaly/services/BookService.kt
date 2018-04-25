package com.jillywiggens.mihaly.services

import retrofit2.Call
import retrofit2.http.GET

interface BookService {
    @GET("books") fun getBooks() : Call<Any>
}