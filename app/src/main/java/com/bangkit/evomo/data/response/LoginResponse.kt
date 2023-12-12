package com.bangkit.evomo.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("isAgreeTnc")
	val isAgreeTnc: Boolean? = null,

	@field:SerializedName("scope")
	val scope: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("expired_at")
	val expiredAt: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("client_id")
	val clientId: String? = null
) : Parcelable
