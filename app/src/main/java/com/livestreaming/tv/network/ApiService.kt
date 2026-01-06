package com.livestreaming.tv.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(
    private val client: HttpClient
) {
    
    /**
     * Example GET request
     * Replace with your actual API endpoints
     */
    suspend fun getMovies(): Result<List<Any>> {
        return try {
            val response = client.get("movies")
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Example POST request
     */
    suspend fun postData(data: Map<String, Any>): Result<Any> {
        return try {
            val response = client.post("data") {
                contentType(ContentType.Application.Json)
                setBody(data)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Example GET request with query parameters
     */
    suspend fun searchMovies(query: String, page: Int = 1): Result<Any> {
        return try {
            val response = client.get("search/movies") {
                parameter("query", query)
                parameter("page", page)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
