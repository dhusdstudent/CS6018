package com.example.undergraduateapp_phase2

import android.os.Build
import androidx.annotation.RequiresApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


class DegreeRepo(
    private val client: HttpClient
) {
    companion object {
        private const val URL = "https://msd2026.github.io/degreePlans/"
    }


    suspend fun fetchDegreePlans(): DegreePlanList {
        return client.get("${URL}degreePlans.json").body()
    }

    suspend fun fetchDegree(path: String): Degree {
        val DTO: DegreeDTO = client.get("$URL$path").body()
        return DTO.toDomain()
    }
}

