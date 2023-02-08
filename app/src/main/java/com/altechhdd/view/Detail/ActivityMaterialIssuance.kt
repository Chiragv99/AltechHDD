package com.altechhdd.view.Detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.databinding.ActivityMaterialIssuanceBinding
import com.altechhdd.utils.Uttils
import com.altechhdd.view.scan.ActivityScan
import com.altechhdd.viewModel.MaterialIssuanceViewModel
import com.pickfords.surveyorapp.utils.AppConstants

class ActivityMaterialIssuance : BaseActivity() {

    private lateinit var binding: ActivityMaterialIssuanceBinding
    private val materialIssueModel by lazy { MaterialIssuanceViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_material_issuance)

        binding.viewModel = materialIssueModel
        binding.lifecycleOwner = this

        findById()
        setAction()
        setObserver()
        getLocation()
    }

    private fun getLocation() {
        binding.viewModel!!.getLocation(this)
    }

    private fun setObserver() {
        // For Show And Hide Progress
        materialIssueModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showProgressbar()
            else if (!isLoading) hideProgressbar()
        }
        // Hide and Show view for No Data Found
        materialIssueModel.materialIssuanceLiveData.observeForever {
            if (it != null) {
                binding.rvScannedCoil.adapter = materialIssueModel.getFilterDashboardAdapter()
                binding.txtScannedCoils.text =
                    "Scanned Coils" + " ( " + materialIssueModel.materialInsuranceList.size.toString() + " )"
            }
        }


    }

    private fun findById() {
        binding.llHeader.txtHeader.text = getText(R.string.material_issue)
    }

    private fun setAction() {
        binding.llHeader.ivBackIcon.setOnClickListener {
            finish()
        }

        binding.llHeader.ivScanIcon.setOnClickListener {
            val mIntent = Intent(this, ActivityScan::class.java)
            mIntent.putExtra("From", AppConstants.materialAssurance)
            startActivity(mIntent)
        }

        binding.switchPlan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.llEnterPlan.visibility = View.VISIBLE
            } else {
                binding.llEnterPlan.visibility = View.GONE

            }
        }


        binding.btnIssue.setOnClickListener {
            validate()
        }

        binding.btnCancel.setOnClickListener {
            materialIssueModel.selectedLocationId = 0
            binding.edtEnterPlanNumber.text.clear()
            binding.switchPlan.isChecked = false
            getscannedcoil.delete()
            binding.viewModel?.clear()
            binding.edtLocation.setSelection(0)
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (materialIssueModel.selectedLocationId == 0) {
            isValid = false
            Uttils.showToast(this, "Please Select Location")
        } else if (binding.switchPlan.isChecked && binding.edtEnterPlanNumber.text.toString()
                .isEmpty()
        ) {
            Uttils.showToast(this, "Please enter plan number")
        } else {
            materialIssueModel.callIssueApi(
                this,
                AppConstants.scanCode,
                binding.edtEnterPlanNumber.text.toString(),
                binding
            )
        }

        return isValid
    }

    override fun onResume() {
        super.onResume()
        Log.e("OnResume", "OnResume")
        binding.viewModel = materialIssueModel


        materialIssueModel.getScannedCoil()

        // Hide and Show view for No Data Found
        materialIssueModel.materialIssuanceLiveData.observeForever {
            if (it != null) {
                binding.rvScannedCoil.adapter = materialIssueModel.getFilterDashboardAdapter()
                binding.txtScannedCoils.text =
                    "Scanned Coils" + " ( " + materialIssueModel.materialInsuranceList.size.toString() + " )"
            } else {
                binding.txtScannedCoils.text = "Scanned Coils (0)"
            }
        }

    }

}