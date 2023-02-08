package com.altechhdd.network


import com.altechhdd.model.GetCoilDetail.GetCoilDetailResponse
import com.altechhdd.model.GetCoilDetail.SetCoilDetailData
import com.altechhdd.model.GetLocation.GetLocationResponse
import com.altechhdd.model.GetLoginResponse
import com.altechhdd.model.GetPutaway.GetPutAwayResponse
import com.altechhdd.model.GetPutaway.SetPutAway
import com.altechhdd.model.SetLoginResponse
import com.altechhdd.model.SynCountModel
import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.*

interface APIInterface {

    @POST("api/MaterialIssuance/Login")
    fun getLoginResponse(@Body body: JsonObject): Observable<GetLoginResponse>

    // For Get Location in PutAway
    @GET("api/MaterialIssuance/GetLocations")
    fun getLocation(): Observable<GetLocationResponse>

  // For Get count which is display on dashboard
    @GET("api/MaterialIssuance/SyncPutawayCount")
    fun getCount(): Observable<SynCountModel>

    // For Get Coil Detail
    @POST("api/MaterialIssuance/GetCoilDetails")
    fun getCoilDetails(@Body body: SetCoilDetailData): Observable<GetCoilDetailResponse>

    // For Get PutWay Response
    @POST("api/MaterialIssuance/Putaway")
    fun getPutAwayResponse(@Body body: SetPutAway): Observable<GetPutAwayResponse>

    // For Get MaterialIssuance
    @POST("api/MaterialIssuance/MaterialIssuance")
    fun setMaterialIssuance(@Body body: JsonObject): Observable<JsonObject>

}
