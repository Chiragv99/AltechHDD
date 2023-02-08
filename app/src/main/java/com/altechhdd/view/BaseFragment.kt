package com.altechhdd.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.altechhdd.utils.Session



open class BaseFragment : Fragment() {
    lateinit var mContext: Context
    lateinit var mActivity: Activity
    private var progressDialog: ProgressDialog? = null
    lateinit var session: Session


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = context as Activity
        session = Session(mActivity)

    }



    fun hideProgressbar() {
        if (isAdded&&progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

}
