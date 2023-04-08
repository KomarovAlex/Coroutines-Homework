package otus.homework.coroutines.api

import retrofit2.http.GET

interface CatsImageService {
    @GET("floof")
    suspend fun getCatImageUrl(): CatImage
}