package com.altechhdd.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.view.activity.ActivityDashboard
import com.altechhdd.view.activity.SignInActivity
import com.pickfords.surveyorapp.extentions.goToActivityAndClearTask

class SplashActivity :  BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setDelay()
    }

    // Set Delay for Splash
    private fun setDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (session.isLoggedIn){
                goToActivityAndClearTask<ActivityDashboard>()
            }
            else{
                goToActivityAndClearTask<SignInActivity>()
            }

        }, 2500)
    }
}
