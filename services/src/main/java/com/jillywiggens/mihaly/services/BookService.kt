package com.jillywiggens.mihaly.services

import io.reactivex.Observable
import retrofit2.http.GET

interface BookService {
    @GET("books") fun getBooks() : Observable<Any>
}