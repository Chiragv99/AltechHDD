package com.altechhdd.model

import com.google.gson.annotations.SerializedName

data class GetLoginResponse(@SerializedName("UId"             ) var UId             : Int?                       = null,
                            @SerializedName("UserName"        ) var UserName        : String?                    = null,
                            @SerializedName("PutawayCount"        ) var PutawayCount        : Int?                       = null,
                            @SerializedName("IssuedMaterialCount" ) var IssuedMaterialCount : Int?                       = null,
                            @SerializedName("RolePermissions" ) var RolePermissions : ArrayList<GetRolePermissions> = arrayListOf(),
                            @SerializedName("ResponseMessage" ) var ResponseMessage : String?                    = null,
                            @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?                   = null)
