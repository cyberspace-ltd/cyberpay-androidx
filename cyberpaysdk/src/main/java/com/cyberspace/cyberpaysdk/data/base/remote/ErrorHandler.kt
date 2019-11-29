package com.cyberspace.cyberpaysdk.data.base.remote

import com.cyberspace.cyberpaysdk.utils.Constant
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.HttpException

internal object ErrorHandler {

    fun getError(throwable: Throwable?): Throwable? {
        val body = getBody(throwable)
        if (body != null)
        {
            val message = getError(body)
            if (message != null) return Throwable(message)
        }
        return Throwable(Constant.errorNetwork)
    }

    @Throws(Exception::class)
    private fun getError(json: JsonObject): String? {
        return if (json.has("message")) {
            json["message"].asString
        } else null
    }

    @Throws(java.lang.Exception::class)
    private fun getBody(throwable: Throwable?): JsonObject? {
        if (throwable is HttpException) {
            val body = throwable.response()!!.errorBody()
            val parser = JsonParser()
            return parser.parse(body!!.string()).asJsonObject
        }
        return null
    }

}