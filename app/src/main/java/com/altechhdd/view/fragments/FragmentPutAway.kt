package com.altechhdd.view.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.altechhdd.R
import com.altechhdd.databinding.FragmentputawayBinding
import com.altechhdd.model.SynCountModel
import com.altechhdd.network.CallbackObserver
import com.altechhdd.network.Networking
import com.altechhdd.utils.Uttils
import com.altechhdd.view.BaseFragment
import com.altechhdd.view.Detail.ActivityMaterialIssuance
import com.altechhdd.view.Detail.ActivityPutAwayDetail
import com.pickfords.surveyorapp.interfaces.FragmentLifecycleInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentPutAway : BaseFragment(), FragmentLifecycleInterface,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var fragmentaryBinding: FragmentputawayBinding


    override fun onPauseFragment() {
    }

    override fun onResumeFragment(s: String?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentaryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragmentputaway, container, false)
        setData()
        setAction()
        callApiOfGetCount()
        return fragmentaryBinding.root
    }


    private fun setData() {
        if (session != null) {
            if (session.user != null) {
                fragmentaryBinding.txtPutwayCount.text = session.user!!.PutawayCount.toString()
                fragmentaryBinding.txtIssueCount.text =
                    session.user!!.IssuedMaterialCount.toString()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        callApiOfGetCount()
    }

    private fun setAction() {
        fragmentaryBinding.llPutAwayDetail.setOnClickListener {
            val mIntent = Intent(activity, ActivityPutAwayDetail::class.java)
            mActivity.startActivity(mIntent)
        }

        fragmentaryBinding.llMaterialIssue.setOnClickListener {
            val mIntent = Intent(activity, ActivityMaterialIssuance::class.java)
            mActivity.startActivity(mIntent)
        }
        fragmentaryBinding.pullToRefresh.setOnRefreshListener(this)

    }

    private fun callApiOfGetCount() {
        if (Uttils.isNetworkConnected(activity)) {
            Log.d("Api", "CAll api")

            CoroutineScope(Dispatchers.Default).launch {
                Networking(activity)
                    .getServices()
                    .getCount()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CallbackObserver<SynCountModel>() {
                        override fun onSuccess(response: SynCountModel) {

                            if (response.IsSuccess == true) {

                                fragmentaryBinding.txtPutwayCount.text =
                                    response.putawayCount.toString()
                                fragmentaryBinding.txtIssueCount.text =
                                    response.issuedMaterialCount.toString()
                            } else {
                                Uttils.showToast(
                                    requireContext(),
                                    response.responseMessage.toString()
                                )
                            }
                        }

                        override fun onFailed(code: Int, message: String) {

                            Uttils.showToast(requireContext(), message)
                        }

                    })
            }

        } else {
            Uttils.showToast(requireContext(), "No Internet")
        }
    }

    override fun onRefresh() {
        fragmentaryBinding.pullToRefresh.isRefreshing = false
        callApiOfGetCount()
    }

}