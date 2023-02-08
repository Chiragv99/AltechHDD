package com.altechhdd.view.Detail

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.databinding.ActivityPutawayDetailsBinding
import com.altechhdd.model.PutAwayModel
import com.altechhdd.utils.Uttils
import com.altechhdd.view.scan.ActivityScan
import com.altechhdd.viewModel.PutAwayViewModel
import com.pickfords.surveyorapp.utils.AppConstants

class ActivityPutAwayDetail : BaseActivity(){
    private lateinit var binding: ActivityPutawayDetailsBinding
    private val putAwayModel by lazy { PutAwayViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_putaway_details)

        binding.viewModel = putAwayModel
        binding.lifecycleOwner = this

        findById()
        setAction()
        setObserver()
        getLocation()

    }

    private fun getLocation() {
        binding.viewModel!!.getLocation(this)
    }

    private fun findById() {
       binding.llHeader.txtHeader.text = getText(R.string.put_away)
       AppConstants.scanCode = ""
    }
    private fun setAction() {
        binding.llHeader.ivBackIcon.setOnClickListener {
            finish()
        }

        binding.llHeader.ivScanIcon.setOnClickListener {
            val mIntent = Intent(this, ActivityScan::class.java)
            mIntent.putExtra("From",AppConstants.putAway)
            startActivity(mIntent)
        }
    }

    private fun setObserver() {
        // For Show And Hide Progress
        putAwayModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showProgressbar()
            else if (!isLoading) hideProgressbar()
        }


        putAwayModel.getPutAwayInModel().observe(this) { user ->
            if (validate(user)){

            }
        }

        putAwayModel.getBlankDataAction().observe(this) { it ->
            if (it !=null){
                AppConstants.scanCode = ""
                binding.edtCoil.setText("")
                binding.edtLocation.setSelection(0)
            }
        }
    }

    private fun validate(user: PutAwayModel): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(user.coilNumber)) {
            isValid = false
            binding.edtCoil.error = getString(R.string.enter_coil_number)
            binding.edtCoil.requestFocus()
        }
        else if(putAwayModel.selectedLocationId == 0){
            isValid = false
            Uttils.showToast(this,getString(R.string.select_location))
        }
        else{
            putAwayModel.callApiForPutWay(this,AppConstants.scanCode)
        }

        return isValid
    }

    override fun onResume() {
        super.onResume()
        binding.edtCoil.setText(AppConstants.scanCode)
    }
}