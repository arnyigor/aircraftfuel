package com.arny.aircraftrefueling.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.databinding.ActivityMainBinding
import com.arny.aircraftrefueling.presentation.deicing.DeicingFragment
import com.arny.aircraftrefueling.presentation.refuel.RefuelFragment
import com.arny.aircraftrefueling.presentation.settings.SettingsFragment
import com.arny.aircraftrefueling.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private companion object {
        const val MENU_FUEL = 1
        const val MENU_DEICE = 2
        const val MENU_SETTINGS = 3
        const val TIME_DELAY = 2000
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                saveFile()
            }
        }

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.root);
        setSupportActionBar(binding.toolbar)
        setDrawer()
        selectItem(MENU_FUEL)
        respDialog()
    }

    private fun requestSDPermission() {
        requestPermission(
            requestPermissionLauncher,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ::saveFile
        )
    }

    private fun saveFile() {
    }

    private fun setDrawer() = with(binding) {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            dlMain,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )
        dlMain.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navViewMain.setNavigationItemSelectedListener { item ->
            dlMain.closeDrawer(navViewMain)
            when (item.itemId) {
                R.id.menu_refuel -> {
                    selectItem(MENU_FUEL)
                    true
                }
                R.id.menu_deicing -> {
                    selectItem(MENU_DEICE)
                    true
                }
                R.id.menu_settings -> {
                    selectItem(MENU_SETTINGS)
                    true
                }
                else -> false
            }
        }
    }

    private fun respDialog() {
        val responsibility = Prefs.getInstance(this).get<Boolean>(Consts.PREF_RESP) ?: false
        if (responsibility) {
            alertDialog(
                this,
                getString(R.string.responsibility_title),
                getString(R.string.response_text),
                getString(R.string.confirm),
                getString(android.R.string.cancel),
                onConfirm = {
                    Prefs.getInstance(this).put(Consts.PREF_RESP, true)
                    requestSDPermission()
                },
                onCancel = {
                    requestSDPermission()
                }
            )
        }
    }

    private fun selectItem(position: Int) {
        KeyboardHelper.hideKeyboard(this)
        val fragmentItem = navigateFragments(position)
        val fragmentTag = fragmentItem?.javaClass?.canonicalName
        var fragment = getFragmentByTag(fragmentTag)
        if (fragment == null) {
            fragment = fragmentItem
        }
        if (fragment != null) {
            replaceFragment(fragment, R.id.flContent, false)
        }
    }

    private fun navigateFragments(position: Int): Fragment? {
        return when (position) {
            MENU_FUEL -> RefuelFragment.getInstance()
            MENU_DEICE -> DeicingFragment.getInstance()
            MENU_SETTINGS -> SettingsFragment.getInstance()
            else -> null
        }
    }

    override fun onBackPressed() {
        val drawerLayout = binding.dlMain
        if (drawerLayout.isOpen) {
            drawerLayout.closeDrawer(binding.navViewMain)
        } else {
            val fragments = supportFragmentManager.fragments
            var isMain = false
            for (curFrag in fragments) {
                if (curFrag != null && curFrag.isVisible && curFrag is RefuelFragment) {
                    isMain = true
                }
            }
            if (!isMain) {
                selectItem(MENU_FUEL)
            } else {
                if (backPressedTime + TIME_DELAY > System.currentTimeMillis()) {
                    super.onBackPressed()
                } else {
                    binding.clRoot.showSnackBar(getString(R.string.press_back_again_to_exit))
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
    }
}
