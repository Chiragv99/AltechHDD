package com.altechhdd.model

import com.google.gson.annotations.SerializedName

data class SynCountModel(
    @SerializedName("PutawayCount") var putawayCount: Int = 0,
    @SerializedName("IssuedMaterialCount") var issuedMaterialCount: Int? = 0,
    @SerializedName("ResponseMessage") var responseMessage: String? = "",
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = false
)