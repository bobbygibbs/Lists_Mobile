package com.jillywiggens.mihaly.services

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("MapClick.php?FcstType=json") fun getWeatherData(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
    ) : Observable<WeatherResponse>
}