package com.altechhdd.model.GetLocation

import com.google.gson.annotations.SerializedName

data class GetLocationResponse  (

    @SerializedName("LocationList"    ) var LocationList    : ArrayList<GetLocationList> = arrayListOf(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?                 = null,
    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?                = null

)