package com.example.undergraduateapp_phase2

import android.net.http.HttpResponseCache.install
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient

fun createHttpClient(): HttpClient {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json( Json{ ignoreUnknownKeys = true})
        }
    }
}