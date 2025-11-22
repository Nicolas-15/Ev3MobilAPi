package com.example.proyecto2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente para JSONPlaceholder (tu código original)
object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// NUEVO: Cliente para tu backend local
object MyApiRetrofitClient {
    // URL base para el emulador de Android. Apunta al localhost de tu PC.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
