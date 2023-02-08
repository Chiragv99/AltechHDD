package com.altechhdd.room

import android.app.appsearch.GetSchemaResponse
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.altechhdd.model.GetCoilDetail.GetCoilDetailResponse
import com.altechhdd.model.GetCoilDetail.GetCoilResponseDetail

@Dao
interface GetScannedCoilDao {
    @Query("Select * from scannedcoils")
    fun getScannedCoilList(): List<GetCoilResponseDetail>


    @Query("Select * from scannedcoils where coilNo = :no")
    fun getScannedCoilList1(no: String): List<GetCoilResponseDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dashboardList: GetCoilResponseDetail?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(addressList: List<GetCoilResponseDetail?>?)

    @Query("DELETE FROM scannedcoils")
    fun delete()

}