package otus.homework.coroutines

import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.api.CatsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy { retrofit.create(CatsService::class.java) }

    private val catImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomfox.ca/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catsImageService by lazy { catImageRetrofit.create(CatsImageService::class.java) }
}