package com.altechhdd.model

import com.google.gson.annotations.SerializedName

class SetLoginResponse {

    @SerializedName("user_name" ) var userName : String? = null
    @SerializedName("email"     ) var email    : String? = null
    @SerializedName("name"      ) var name     : String? = null

}