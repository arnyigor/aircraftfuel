package com.arny.aircraftrefueling

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arny.aircraftrefueling.fragments.LitreFragment
import com.arny.aircraftrefueling.presenter.fragment.FragmentRefueling
import com.arny.aircraftrefueling.utils.getFragmentByTag
import com.arny.aircraftrefueling.utils.replaceFragment
import com.arny.aircraftrefueling.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MENU_FUEL = 1
        private const val MENU_DEICE = 2
        private const val TIME_DELAY = 2000
    }

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
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
                    dlMain.closeDrawers()
                    true
                }
                R.id.menu_deicing -> {
                    selectItem(MENU_DEICE)
                    dlMain.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

/*    private fun respDialog() {
        val responsibility: Boolean = Config.getBoolean("responsibility", false, this)
        if (!responsibility) {
            ResponsibilityDialog(this).show()
        }
    }*/

    private fun selectItem(position: Int) {
        val fragmentItem = navigateFragments(position)
        val fragmentTag = fragmentItem?.javaClass?.simpleName
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
            MENU_FUEL -> FragmentRefueling.getInstance()
            MENU_DEICE -> LitreFragment.getInstance()
            else -> null
        }
    }

    override fun onBackPressed() {
        val drawerLayout = dlMain
        if (drawerLayout.isOpen) {
            drawerLayout.closeDrawer(navViewMain)
        } else {
            val fragments = supportFragmentManager.fragments
            var isMain = false
            for (curFrag in fragments) {
                if (curFrag != null && curFrag.isVisible && curFrag is FragmentRefueling) {
                    isMain = true
                }
            }
            if (!isMain) {
                selectItem(MENU_FUEL)
            } else {
                if (backPressedTime + TIME_DELAY > System.currentTimeMillis()) {
                    super.onBackPressed()
                } else {
                    clRoot.showSnackBar(getString(R.string.press_back_again_to_exit))
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
    }
}
