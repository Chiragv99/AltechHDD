package com.altechhdd.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.altechhdd.R
import com.altechhdd.model.GetLocation.GetLocationList
import com.altechhdd.model.GetLocation.GetLocationResponse
import com.altechhdd.model.GetPutaway.GetPutAwayResponse
import com.altechhdd.model.GetPutaway.SetPutAway
import com.altechhdd.model.PutAwayModel
import com.altechhdd.network.CallbackObserver
import com.altechhdd.network.Networking
import com.altechhdd.utils.Session
import com.altechhdd.utils.Uttils
import com.pickfords.surveyorapp.base.BaseViewModel
import com.pickfords.surveyorapp.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutAwayViewModel(val context: Context) : BaseViewModel() {

    var coilNumber: ObservableField<String> = ObservableField()
    var location: ObservableField<String> = ObservableField()
    private var putAwayInMutableLiveData: MutableLiveData<PutAwayModel> = MutableLiveData()


    private var locationLiveList: MutableLiveData<List<GetLocationList>?> =
        MutableLiveData()

    private var setBlankData: MutableLiveData<String> =
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


    //  Click Listener Location List
    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
          // Log.e("Select", locationLiveList.value!![position - 1].Name.toString())
            if (position != 0){
                selectedLocationId = locationLiveList.value!![position - 1].Id
            }
        }
    }

    fun getPutAwayInModel(): MutableLiveData<PutAwayModel> {
        return putAwayInMutableLiveData
    }

    fun getBlankDataAction(): MutableLiveData<String> {
        return setBlankData
    }



    fun onPutAwayClick() {
        val model = PutAwayModel()
        model.coilNumber = coilNumber.get()
        model.location = location.get()
        putAwayInMutableLiveData.value = model
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
                            if (response.IsSuccess == true){
                                locationLiveList.postValue(response.LocationList)
                            }
                            else{
                                Uttils.showToast(context,response.ResponseMessage.toString())
                                locationLiveList.postValue(null)
                            }
                        }

                        override fun onFailed(code: Int, message: String) {
                            isLoading.postValue(false)
                            Uttils.showToast(context,message)
                            locationLiveList.postValue(null)
                        }

                    })
            }

        } else {
            Log.e("getDetails", "From API")

        }
    }

    //  For Get Survey Detail List
    fun callApiForPutWay(context: Context, scanCode: String) {
        if (Uttils.isNetworkConnected(context)) {
            val session = Session(context)

            var setPutAway = SetPutAway()
            setPutAway.UId = session.user!!.UId
            setPutAway.SRFCoilNo = scanCode
            setPutAway.WHLID = selectedLocationId

            isLoading.postValue(true)
            CoroutineScope(Dispatchers.Default).launch {
                Networking(context)
                    .getServices()
                    .getPutAwayResponse(setPutAway)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CallbackObserver<GetPutAwayResponse>() {
                        override fun onSuccess(response: GetPutAwayResponse) {
                            isLoading.postValue(false)
                            if (response.IsSuccess == true){
                                Uttils.showToast(context,response.ResponseMessage.toString())
                                setBlankData.postValue("Blank")
                            }
                            else{
                                Uttils.showToast(context,response.ResponseMessage.toString())
                            }
                        }

                        override fun onFailed(code: Int, message: String) {
                            isLoading.postValue(false)
                            Uttils.showToast(context,message)
                        }

                    })
            }

        } else {
            Log.e("getDetails", "From API")
        }
    }

}