package com.example.kmmfirstdemo

import io.ktor.client.HttpClient

expect class HttpClientFactory {
    fun getHttpClient(): HttpClient
}