package com.altechhdd.model.GetCoilDetail

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "scannedcoils")
class GetCoilResponseDetail() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int = 0

    @SerializedName("CoilNo")
    var coilNo: String? = null

    @ColumnInfo(name = "Width")
    @SerializedName("Width")
    var width: String? = null

    @ColumnInfo(name = "WeightKG")
    @SerializedName("WeightKG")
    var weightKG: String? = null


    @ColumnInfo(name = "SourceLoc")
    @SerializedName("SourceLoc")
    var sourceLoc: String? = null

}
