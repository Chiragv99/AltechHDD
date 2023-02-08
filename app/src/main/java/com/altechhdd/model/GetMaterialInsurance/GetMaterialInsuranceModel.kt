package com.altechhdd.model.GetMaterialInsurance

import com.google.gson.annotations.SerializedName

data class GetMaterialInsuranceModel(
    @SerializedName("CoilNo"  ) var CoilNo  : String?    = null,
    @SerializedName("Width"   ) var Width   : Int?    = null,
    @SerializedName("WidthKG" ) var WidthKG : Int?    = null,
    @SerializedName("Source"  ) var Source  : String? = null) {
}