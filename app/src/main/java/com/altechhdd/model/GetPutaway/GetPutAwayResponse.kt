package com.altechhdd.model.GetPutaway

import com.google.gson.annotations.SerializedName

data class GetPutAwayResponse(@SerializedName("ResponseMessage" ) var ResponseMessage : String?  = null,
                              @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean? = null) {
}