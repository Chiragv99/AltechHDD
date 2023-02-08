package com.altechhdd.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.altechhdd.model.GetCoilDetail.GetCoilResponseDetail


@Database(entities = [GetCoilResponseDetail::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun coilDao(): GetScannedCoilDao


}