package com.altechhdd.model.GetCoilDetail

import com.google.gson.annotations.SerializedName

data class GetCoilDetailResponse(@SerializedName("CoilNoDetails"   ) var CoilNoDetails   : ArrayList<GetCoilResponseDetail> = arrayListOf(),
                                 @SerializedName("ResponseMessage" ) var ResponseMessage : String?                  = null,
                                 @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?                 = null) {
}