package com.altechhdd.model.GetLocation

import com.google.gson.annotations.SerializedName

data class GetLocationList(

    @SerializedName("Id"   ) var Id   : Int?    = null,
    @SerializedName("Name" ) var Name : String? = null

)