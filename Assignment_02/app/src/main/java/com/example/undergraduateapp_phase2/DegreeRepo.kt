package com.example.undergraduateapp_phase2

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient

class DegreeRepo(
    private val client: HttpClient
)
{

    private val URL =
        "https://msd2026.github.io/degreePlans/cs.json"

    suspend fun fetchDegreePlans(): DegreePlanList {
        return client.get("${URL}degreePlans.json").body()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchDegree(path: String): Degree {
        val DTO: DegreeDTO = client.get("$URL$path").body()
        return DTO.toDomain()
    }
}

