package com.example.kmmfirstdemo

import io.ktor.client.HttpClient

actual class HttpClientFactory {
    actual fun getHttpClient(): HttpClient {
        return HttpClient {  }
    }
}