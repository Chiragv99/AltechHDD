package com.altechhdd.view.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.databinding.ActivitySigninBinding
import com.altechhdd.model.SignInModel
import com.altechhdd.viewModel.SignInViewModel
import com.pickfords.surveyorapp.extentions.goToActivityAndClearTask


class SignInActivity :  BaseActivity(){
    private val signInViewModel by lazy { SignInViewModel(this) }
    lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)

        binding.viewModel = signInViewModel
        binding.lifecycleOwner = this

        setObserver()

    }

    private fun setObserver() {
        signInViewModel.getSignInModel().observe(this) { user ->
            if (validate(user))
                signInViewModel.getLoginResponse()
        }

    }

    public fun goToHomePage(){
        goToActivityAndClearTask<ActivityDashboard>()
    }

    private fun validate(user: SignInModel): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(user.email)) {
            isValid = false
            binding.edtEmail.error = "Please Enter User Id"
            binding.edtEmail.requestFocus()
        }
       else if (TextUtils.isEmpty(user.password)) {
            isValid = false
            binding.edtPassword.error ="Please Enter Password"
            binding.edtPassword.requestFocus()
        }
        else if (!user.isPasswordValid(2)){
            isValid = false
            binding.edtPassword.error = "Please Enter Proper Password"
            binding.edtPassword.requestFocus()
        }
        return isValid
    }

}
