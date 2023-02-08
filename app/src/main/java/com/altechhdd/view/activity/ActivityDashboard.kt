package com.altechhdd.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.altechhdd.BaseActivity
import com.altechhdd.R
import com.altechhdd.databinding.ActivityDashboardBinding
import com.altechhdd.view.Detail.ActivityMaterialIssuance
import com.altechhdd.view.Detail.ActivityPutAwayDetail
import com.altechhdd.view.dialogs.MessageDialog
import com.pickfords.surveyorapp.extentions.goToActivityAndClearTask

class ActivityDashboard : BaseActivity() ,MessageDialog.OkButtonListener{

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        // Set up ActionBar
        setSupportActionBar(binding.toolbarDashboard)
        val actionBar = supportActionBar
        if (actionBar != null) {
            //    supportActionBar!!.setHomeAsUpIndicator(R.drawable.v_ic_menu)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.title = getString(R.string.menu)
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu_icon)


        navController = findNavController(R.id.navHostFragmentPickford)
        @Suppress("DEPRECATION")
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.dashboardFragment,

        ).setDrawerLayout(binding.drawer).build()


        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
        setupNavControl() //To configure navController with drawer and bottom navigation


        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            if ( binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            }
            when (menuItem.itemId) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.dashboardFragment)
                    binding.toolbarDashboard.setTitle(R.string.menu)

                }
                R.id.putway -> {
                    binding.toolbarDashboard.setTitle(R.string.menu)
                    val mIntent = Intent(this, ActivityPutAwayDetail::class.java)
                    startActivity(mIntent)

                }
                R.id.materialissue -> {
                    binding.toolbarDashboard.setTitle(R.string.menu)
                    val mIntent = Intent(this, ActivityMaterialIssuance::class.java)
                   startActivity(mIntent)
                }
                R.id.actionSignOut -> {
                    MessageDialog(
                        this,
                        getString(R.string.are_you_sure_you_want_to_logout)
                    ).setListener(this).setCancelButton(true).show()

                }
            }

            true
        }
        setupToolbarWithMenu(getString(R.string.menu))

    }

    fun setupToolbarWithMenu(title: String? = null) {
        setSupportActionBar(binding.toolbarDashboard)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon)
            if (title != null) binding.tvTitle.text = title
        }

    }


    private fun setupNavControl() {
        binding.navigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
    }

    override fun onSupportNavigateUp(): Boolean { //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        when {
            binding.drawer.isDrawerOpen(GravityCompat.START) -> {
                binding.drawer.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawer.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onOkPressed(dialog: MessageDialog) {
        db.clearAllTables()
        session.clearSession()
        dialog.dismiss()
        goToActivityAndClearTask<SignInActivity>()
    }

}