package com.altechhdd.view.scan

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.altechhdd.BaseActivity
import com.altechhdd.databinding.ActivityScanBinding
import com.altechhdd.model.GetCoilDetail.GetCoilDetailResponse
import com.altechhdd.model.GetCoilDetail.SetCoilDetailData
import com.altechhdd.network.CallbackObserver
import com.altechhdd.network.Networking
import com.altechhdd.utils.Uttils
import com.altechhdd.view.Detail.ActivityMaterialIssuance
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.pickfords.surveyorapp.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ActivityScan: BaseActivity(){

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityScanBinding

    private var listener: OkScanQRListener? = null
    private  var strFrom : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        getBundleData()
        setContentView(view)

        if (ContextCompat.checkSelfPermission(this@ActivityScan, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            askForCameraPermission()
        } else {
            setupControls()
        }

    }

    private fun getBundleData() {
         strFrom = intent.getStringExtra("From").toString()
         Log.e("From",strFrom)
    }


    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue


                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        cameraSource.stop()
                        if (strFrom == AppConstants.putAway){
                            listener?.onScanResultData(scannedValue)
                            AppConstants.scanCode = scannedValue
                            finish()
                        }else{
                            getCoilDetail(this@ActivityScan,scannedValue)
                        }
                    }
                }else
                {

                }
            }
        })
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@ActivityScan,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }


    fun setListener(listener: OkScanQRListener?): ActivityScan {
        this.listener = listener
        return this
    }



    interface OkScanQRListener{
        fun onScanResultData(scan: String?)
    }

    //  For Get Survey Detail List
    fun getCoilDetail(context: Context, coilNumber: String) {
        if (Uttils.isNetworkConnected(context)) {
            var setCoilData: SetCoilDetailData = SetCoilDetailData()
            setCoilData.CoilNo = coilNumber
            CoroutineScope(Dispatchers.Default).launch {
                Networking(context)
                    .getServices()
                    .getCoilDetails(setCoilData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CallbackObserver<GetCoilDetailResponse>() {
                        override fun onSuccess(response: GetCoilDetailResponse) {

                            if (response.IsSuccess == true){
                                if (response.CoilNoDetails !=null && response.CoilNoDetails.size >0 ){
                                   if(getscannedcoil.getScannedCoilList1(response.CoilNoDetails[0].coilNo!!)
                                           .isEmpty()
                                   ){
                                       getscannedcoil.insert(response.CoilNoDetails[0])
                                   }

                                    finish()
                                }
                                else{
                                    Uttils.showToast(context,"No Data Found!")
                                    finish()
                                }
                            }
                            else{
                                Uttils.showToast(context,response.ResponseMessage.toString())
                                finish()
                            }
                        }

                        override fun onFailed(code: Int, message: String) {
                            Uttils.showToast(context,message)
                            finish()
                        }
                    })
            }

        } else {
            Log.e("getDetails", "From API")

        }
    }

}