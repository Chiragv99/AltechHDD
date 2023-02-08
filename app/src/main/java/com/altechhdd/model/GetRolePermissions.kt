package com.altechhdd.model

import com.google.gson.annotations.SerializedName

data class GetRolePermissions(@SerializedName("menu"     ) var menu     : String?  = null,
                              @SerializedName("IsActive" ) var IsActive : Boolean? = null
)
