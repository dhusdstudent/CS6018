package com.example.undergraduateapp_phase2

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface DegreeRepository {
    suspend fun fetchDegreePlans(): DegreePlanList
    suspend fun fetchDegree(path: String): Degree
}

class DegreeRepo(
    private val client: HttpClient
) : DegreeRepository {
    companion object {
        private const val URL = "https://msd2026.github.io/degreePlans/"
    }


    override suspend fun fetchDegreePlans(): DegreePlanList {
        return client.get("${URL}degreePlans.json").body()
    }

    override suspend fun fetchDegree(path: String): Degree {
        val DTO: DegreeDTO = client.get("$URL$path").body()
        return DTO.toDomain()
    }
}

