package dev.senk0n.dogbreeds.data.http_client.shared

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import dev.senk0n.dogbreeds.shared.core.ConnectionException
import dev.senk0n.dogbreeds.shared.core.ParseResponseException
import dev.senk0n.dogbreeds.shared.core.ServerException
import retrofit2.HttpException
import java.io.IOException

open class HttpCallWrapper(
    moshi: Moshi
) {
    private val adapter = moshi.adapter(ErrorResponse::class.java)

    class ErrorResponse(val error: String)

    suspend fun <T> wrapCallExceptions(block: suspend () -> T): T =
        try {
            block()
        } catch (ex: JsonDataException) {
            throw ParseResponseException(ex)
        } catch (ex: JsonEncodingException) {
            throw ParseResponseException(ex)
        } catch (ex: HttpException) {
            throw try {
                val errorBody: ErrorResponse = adapter.fromJson(
                    ex.response()!!.errorBody()!!.string()
                )!!
                ServerException(ex.code().toShort(), errorBody.error)
            } catch (ex: Exception) {
                ParseResponseException(ex)
            }
        } catch (ex: IOException) {
            throw ConnectionException(ex)
        }
}