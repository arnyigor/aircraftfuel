package com.arny.aircraftrefueling.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.databinding.ActivityMainBinding
import com.arny.aircraftrefueling.utils.showSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        initOnBackPress(navController)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun initOnBackPress(navController: NavController) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val isLastFragment = navController.currentDestination?.id == R.id.nav_home
                when {
                    else -> {
                        onBack(isLastFragment)
                    }
                }
            }

            private fun onBack(isLastFragment: Boolean) {
                if (isLastFragment) {
                    if (backPressedTime + TIME_DELAY > System.currentTimeMillis()) {
                        finishAffinity()
                    } else {
                        binding.root.showSnackBar(getString(R.string.press_back_again_to_exit))
                    }
                    backPressedTime = System.currentTimeMillis()
                } else {
                    navController.navigateUp()
                }
            }
        })
    }

    private companion object {
        const val TIME_DELAY = 2000
    }
}
