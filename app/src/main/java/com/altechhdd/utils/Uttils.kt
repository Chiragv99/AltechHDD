package com.altechhdd.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.altechhdd.BaseActivity
import com.altechhdd.view.scan.ActivityScan
import com.pickfords.surveyorapp.utils.AppConstants

class Uttils(context: Context) {
    private val progressDialog = ProgressDialog(context)
    companion object {

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
        /**
         * Check Internet is connected or not
         */
        fun isNetworkConnected(context: Context?): Boolean {
            val connectivityManager: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT < 23) {
                val ni = connectivityManager.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI
                            || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val network: Network? = connectivityManager.activeNetwork
                if (network != null) {
                    val networkCapabilities: NetworkCapabilities? =
                        connectivityManager.getNetworkCapabilities(network)

                    return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
            return false
        }

    }

     fun showProgress(){
        progressDialog.setTitle("Please Wait..")
        progressDialog.show()
    }

    fun hideProgress(){
        progressDialog.dismiss()
    }

}