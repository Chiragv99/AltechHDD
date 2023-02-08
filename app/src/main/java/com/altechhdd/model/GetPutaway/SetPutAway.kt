package com.altechhdd.model.GetPutaway

import com.google.gson.annotations.SerializedName

data class SetPutAway(@SerializedName("WHLID"     ) var WHLID     : Int?    = null,
                      @SerializedName("SRFCoilNo" ) var SRFCoilNo : String? = null,
                      @SerializedName("UId"       ) var UId       : Int?    = null) {
}