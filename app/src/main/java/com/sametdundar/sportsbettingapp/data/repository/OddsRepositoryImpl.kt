package com.sametdundar.sportsbettingapp.data.repository

import com.sametdundar.sportsbettingapp.data.mapper.toDomain
import com.sametdundar.sportsbettingapp.data.remote.OddsApiService
import com.sametdundar.sportsbettingapp.domain.model.Odds
import com.sametdundar.sportsbettingapp.domain.model.Sport
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository
import retrofit2.HttpException
import java.io.IOException
import android.content.SharedPreferences

sealed class OddsApiException(message: String) : Exception(message) {
    class Unauthorized : OddsApiException("API anahtarı eksik, hatalı veya limiti doldu (401/403)")
    class NotFound : OddsApiException("İstenen veri bulunamadı veya süresi doldu (404)")
    class InvalidParams : OddsApiException("Geçersiz parametre gönderildi (422)")
    class RateLimit : OddsApiException("Çok sık istek gönderildi, limit aşıldı (429)")
    class ServerError : OddsApiException("Sunucu hatası (500)")
    class NetworkError : OddsApiException("Ağ bağlantı hatası")
    class Unknown(message: String) : OddsApiException(message)
}

class OddsRepositoryImpl(
    private val api: OddsApiService,
    private val sharedPreferences: SharedPreferences
) : OddsRepository {
    companion object {
        private const val KEY_API_KEY = "api_key"
        private const val DEFAULT_API_KEY = "YOUR_API_KEY"
    }

    override fun getApiKey(): String {
        return sharedPreferences.getString(KEY_API_KEY, DEFAULT_API_KEY) ?: DEFAULT_API_KEY
    }

    override fun setApiKey(newKey: String) {
        sharedPreferences.edit().putString(KEY_API_KEY, newKey).apply()
    }

    override suspend fun getSports(): List<Sport> {
        return safeApiCall { api.getSports(getApiKey()).map { it.toDomain() } }
    }

    override suspend fun getOdds(sportKey: String, regions: String, markets: String): List<Odds> {
        return safeApiCall { api.getOdds(sportKey, getApiKey(), regions, markets).map { it.toDomain() } }
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: HttpException) {
            when (e.code()) {
                401, 403 -> throw OddsApiException.Unauthorized()
                404 -> throw OddsApiException.NotFound()
                422 -> throw OddsApiException.InvalidParams()
                429 -> throw OddsApiException.RateLimit()
                500 -> throw OddsApiException.ServerError()
                else -> throw OddsApiException.Unknown(e.localizedMessage ?: "Bilinmeyen hata")
            }
        } catch (e: IOException) {
            throw OddsApiException.NetworkError()
        } catch (e: Exception) {
            throw OddsApiException.Unknown(e.localizedMessage ?: "Bilinmeyen hata")
        }
    }
} 