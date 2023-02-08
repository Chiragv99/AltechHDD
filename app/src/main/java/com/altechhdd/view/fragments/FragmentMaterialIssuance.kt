package com.altechhdd.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.altechhdd.R
import com.altechhdd.databinding.FragmentputawayBinding
import com.altechhdd.view.BaseFragment
import com.altechhdd.view.Detail.ActivityPutAwayDetail
import com.pickfords.surveyorapp.interfaces.FragmentLifecycleInterface

class FragmentMaterialIssuance: BaseFragment(), FragmentLifecycleInterface {

    lateinit var fragmentaryBinding: FragmentputawayBinding


    override fun onPauseFragment() {
    }

    override fun onResumeFragment(s: String?) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragmentputaway, container, false)
        setAction()
        return fragmentaryBinding.root
    }

    private fun setAction() {
        fragmentaryBinding.llPutAwayDetail.setOnClickListener {
            val mIntent = Intent(activity, ActivityPutAwayDetail::class.java)
            mActivity.startActivity(mIntent)
        }
    }

}