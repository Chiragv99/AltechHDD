package com.altechhdd.viewModel


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.databinding.ActivityMaterialIssuanceBinding
import com.altechhdd.model.GetCoilDetail.GetCoilResponseDetail
import com.altechhdd.model.GetLocation.GetLocationList
import com.altechhdd.model.GetLocation.GetLocationResponse
import com.altechhdd.network.CallbackObserver
import com.altechhdd.network.Networking
import com.altechhdd.utils.Session
import com.altechhdd.utils.Uttils
import com.altechhdd.view.adapter.MaterialIssuanceAdapter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pickfords.surveyorapp.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MaterialIssuanceViewModel(val context: Context) : BaseViewModel() {

    var destination: ObservableField<String> = ObservableField()
    var plan: ObservableField<String> = ObservableField()
    var planNumber: ObservableField<String> = ObservableField()
    var coilNumber: ObservableField<String> = ObservableField()
    var location: ObservableField<String> = ObservableField()
    private var locationLiveList: MutableLiveData<List<GetLocationList>?> =
        MutableLiveData()
    val locationList: MutableList<String?> = mutableListOf()

    private var locationSpinnerAdapter: ArrayAdapter<String?>? = null
    fun getLocationSpinnerAdapter(): ArrayAdapter<String?>? = locationSpinnerAdapter

    var selectedLocationId: Int? = 0

    init {
        locationLiveList.observeForever {
            if (it != null) {

                locationList.clear()
                locationList.add("Select Location")
                for (data in it) {
                    locationList.add(data.Name)
                }
                locationSpinnerAdapter?.notifyDataSetChanged()
            }
        }


        locationSpinnerAdapter =
            ArrayAdapter<String?>(context, android.R.layout.simple_spinner_item, locationList)
        locationSpinnerAdapter?.setDropDownViewResource(R.layout.custom_spinner_item)

    }

    //  For Get Survey Detail List
    fun getLocation(context: Context) {
        if (Uttils.isNetworkConnected(context)) {
            isLoading.postValue(true)
            CoroutineScope(Dispatchers.Default).launch {
                Networking(context)
                    .getServices()
                    .getLocation()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CallbackObserver<GetLocationResponse>() {
                        override fun onSuccess(response: GetLocationResponse) {
                            isLoading.postValue(false)
                            if (response.IsSuccess == true) {
                                locationLiveList.postValue(response.LocationList)
                            } else {
                                Uttils.showToast(context, response.ResponseMessage.toString())
                                locationLiveList.postValue(null)
                            }
                        }

                        override fun onFailed(code: Int, message: String) {
                            isLoading.postValue(false)
                            Uttils.showToast(context, message)
                            locationLiveList.postValue(null)
                        }

                    })
            }

        } else {
            Log.e("getDetails", "From API")

        }
    }

    //  Click Listener Location List
    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // Log.e("Select", locationLiveList.value!![position - 1].Name.toString())
            if (position != 0) {
                selectedLocationId = locationLiveList.value!![position - 1].Id
            }
        }
    }

    fun getFilterDashboardAdapter(): MaterialIssuanceAdapter? = materialIssuranceAdapter

    var materialIssuanceLiveData: MutableLiveData<List<GetCoilResponseDetail>> =
        MutableLiveData()


    private var materialIssuranceAdapter: MaterialIssuanceAdapter? = null

    var materialInsuranceList: MutableList<List<GetCoilResponseDetail>> = mutableListOf()
    fun clear() {
        materialInsuranceList = ArrayList()
        materialIssuranceAdapter = MaterialIssuanceAdapter(materialInsuranceList, this);
        materialIssuranceAdapter?.notifyDataSetChanged()
        materialIssuanceLiveData?.value = ArrayList()

    }

    init {

        // Add Insurance List From Live Data
        materialInsuranceList.clear()

        materialIssuanceLiveData.observeForever {
            if (it != null) {
                materialInsuranceList.clear()
                for (data in it) {
                    materialInsuranceList.add(it)
                }

                materialIssuranceAdapter?.notifyDataSetChanged()
            }
        }


        // Get Data from Database
        if ((context as BaseActivity).getscannedcoil.getScannedCoilList() != null && (context as BaseActivity).getscannedcoil.getScannedCoilList().size > 0) {
            materialIssuanceLiveData.postValue(context.getscannedcoil.getScannedCoilList())
        }

        // For Adapter
        materialIssuranceAdapter = MaterialIssuanceAdapter(materialInsuranceList, this);


    }

    fun getScannedCoil() {
        // Get Data from Database
        if ((context as BaseActivity).getscannedcoil.getScannedCoilList() != null && (context as BaseActivity).getscannedcoil.getScannedCoilList().size > 0) {
            materialIssuanceLiveData.postValue(context.getscannedcoil.getScannedCoilList())
            materialIssuranceAdapter!!.notifyDataSetChanged()
        }

    }

    //  For Get Survey Detail List
    fun callIssueApi(
        context: Context,
        scanCode: String,
        planNumber: String,
        binding: ActivityMaterialIssuanceBinding
    ) {
        if (Uttils.isNetworkConnected(context)) {
            val session = Session(context)

            val jsonArray = JsonArray()

            if (materialInsuranceList != null && materialInsuranceList.size!! > 0) {
                for (i in 0 until materialInsuranceList.size!!) {
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("CoilNo", materialInsuranceList[0][i].coilNo)
                    jsonArray.add(jsonObject)
                }

            }
            val objIssue = JsonObject()
            objIssue.addProperty("IssuedBy", session.user!!.UId)
            objIssue.addProperty("ProdLocId", selectedLocationId)
            if (planNumber.isEmpty()) {
                objIssue.addProperty("IsPlan", false)
            } else {
                objIssue.addProperty("IsPlan", true)
            }

            objIssue.addProperty("PlanNo", planNumber)

            objIssue.add("IssuanceDetail", jsonArray)

            Log.e("Json", objIssue.toString())
            isLoading.postValue(true)
            CoroutineScope(Dispatchers.Default).launch {
                Networking(context)
                    .getServices()
                    .setMaterialIssuance(objIssue)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CallbackObserver<JsonObject>() {
                        override fun onSuccess(response: JsonObject) {
                            isLoading.postValue(false)

                            selectedLocationId = 0
                            binding.edtLocation.setSelection(0)
                            binding.edtEnterPlanNumber.text.clear()
                            binding.switchPlan.isChecked = false

                            clear()
                            Log.e("getDetails", response.toString())
                            (context as BaseActivity).getscannedcoil.delete()
                            if (response.get("IsSuccess").toString() == "true") {
                                showDialog(
                                    "Coil successfully issued Document No. is: ${
                                        response.get(
                                            "GeneratedDocNo"
                                        )
                                    }"
                                )
                            } else {
                                showDialog(
                                    "${
                                        response.get(
                                            "ResponseMessage"
                                        )
                                    }"
                                )
                            }
                        }

                        override fun onFailed(code: Int, message: String) {
                            isLoading.postValue(false)
                            //Uttils.showToast(context, message)
                            showDialog(message)

                        }
                    })
            }

        } else {
            Log.e("getDetails", "From API")
        }
    }

    fun showDialog(msg: String?) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.item_dialog)
        val text = dialog?.findViewById(com.altechhdd.R.id.txMsg) as TextView
        val ivClose = dialog?.findViewById(com.altechhdd.R.id.ivClose) as ImageView
        text.text = msg

        ivClose.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }
}