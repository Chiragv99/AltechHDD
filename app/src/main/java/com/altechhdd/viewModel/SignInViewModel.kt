package com.altechhdd.viewModel

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.altechhdd.model.GetLoginResponse
import com.altechhdd.model.SignInModel
import com.altechhdd.network.CallbackObserver
import com.altechhdd.network.Networking
import com.altechhdd.utils.Session
import com.altechhdd.utils.Uttils
import com.altechhdd.view.activity.SignInActivity
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInViewModel(val context: Context)  {
    var email: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    val progressDialog = ProgressDialog(context)
    private var signInMutableLiveData: MutableLiveData<SignInModel> = MutableLiveData()
    val session  = Session(context)

    fun getSignInModel(): MutableLiveData<SignInModel> {
        return signInMutableLiveData
    }

    fun onSignInClicked() {
        val model = SignInModel()
        model.email = email.get()
        model.password = password.get()
        signInMutableLiveData.value = model
        Log.e("Login","LoginClicked")
    }

    fun getLoginResponse(){

        if(Uttils.isNetworkConnected(context)){
            val obj_Login = JsonObject()
            obj_Login.addProperty("LoginId",email.get().toString())
            obj_Login.addProperty("LoginPassword",password.get().toString())
            obj_Login.addProperty("PlantID",1)

            Log.e("Json",obj_Login.toString())
            showProgress()
            Networking.with(context)
                .getServices()
                .getLoginResponse(obj_Login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CallbackObserver<GetLoginResponse>() {
                    override fun onSuccess(response: GetLoginResponse) {
                        hideProgress()
                        if (response.IsSuccess == true){
                            session.storeIsFirstTimeKey(false)
                            session.user = response
                            (context as SignInActivity).goToHomePage()
                        }
                        else{
                            Uttils.showToast(context,response.ResponseMessage.toString())
                        }
                    }

                    override fun onFailed(code: Int, message: String) {
                        hideProgress()
                        Uttils.showToast(context,message)
                    }
                })
        }
        else{
            Uttils.showToast(context,"Please check your Internet!")
        }
    }

    private fun showProgress(){
        progressDialog.setTitle("Please Wait..")
        progressDialog.show()
    }

    fun hideProgress(){
        progressDialog.dismiss()
    }

}