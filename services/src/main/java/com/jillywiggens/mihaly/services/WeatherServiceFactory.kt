package com.jillywiggens.mihaly.services

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherServiceFactory {

    private const val NATIONAL_WEATHER_SERVICE_URL = "https://forecast.weather.gov/"

    fun generate() = ServiceFactory.generateServiceFromUrl<WeatherService>(NATIONAL_WEATHER_SERVICE_URL)

    interface WeatherService {
        @GET("MapClick.php?FcstType=json")
        fun getWeatherData(
                @Query("lat") latitude: Double,
                @Query("lon") longitude: Double
        ): Observable<JsonNode>
    }
}