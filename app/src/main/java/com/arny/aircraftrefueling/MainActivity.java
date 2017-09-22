package com.arny.aircraftrefueling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.arny.aircraftrefueling.adapters.TabPagerAdapter;
import com.arny.aircraftrefueling.fragments.KilogrammFragment;
import com.arny.aircraftrefueling.fragments.LitreFragment;
import com.arny.arnylib.utils.ToastMaker;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
	private static final int MENU_KILO = 0;
	private static final int MENU_LITRE = 1;
	private static final String DRAWER_SELECTION = "drawer_selection";
	private Context context = MainActivity.this;
	private Drawer drawer = null;
	private Toolbar toolbar;
	private static final int TIME_DELAY = 2000;
	private static long back_pressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setLogo(R.mipmap.ic_launcher);
			getSupportActionBar().setDisplayUseLogoEnabled(true);
		}
		toolbar.setTitle(getString(R.string.app_name));
		initDrawer();
		restoreDraverPosition(savedInstanceState);
	}

	private void restoreDraverPosition(Bundle savedInstanceState) {
		Log.i(MainActivity.class.getSimpleName(), "restoreDraverPosition: savedInstanceState = " + savedInstanceState);
		if (savedInstanceState == null) {
			drawer.setSelection(MENU_KILO);
		} else {
			try {
				drawer.setSelection(Long.parseLong(savedInstanceState.getString(DRAWER_SELECTION)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState = drawer.saveInstanceState(outState);
		outState.putString(DRAWER_SELECTION, String.valueOf(drawer.getCurrentSelection()));
		super.onSaveInstanceState(outState);
	}

	private void initDrawer() {
		drawer = new DrawerBuilder()
				.withActivity(this)
				.withRootView(R.id.drawer_container)
				.withToolbar(toolbar)
				.withActionBarDrawerToggle(true)
				.withActionBarDrawerToggleAnimated(true)
				.addDrawerItems(
						new PrimaryDrawerItem()
								.withIdentifier(MENU_KILO)
								.withName(R.string.menu_kilogramms)
								.withIcon(GoogleMaterial.Icon.gmd_format_color_fill),
						new PrimaryDrawerItem()
								.withIdentifier(MENU_LITRE)
								.withName(R.string.menu_litres)
								.withIcon(GoogleMaterial.Icon.gmd_opacity)
				)
				.withOnDrawerItemClickListener((view, position, drawerItem) -> {
					selectItem((int) drawerItem.getIdentifier());
					return true;
				})
				.build();
	}

	private void selectItem(int position) {
		Fragment fragment = getFragment(position);
		commitFragment(fragment);
	}

	private void commitFragment(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
			drawer.closeDrawer();
		}
	}

	@Nullable
	private Fragment getFragment(int position) {
		Fragment fragment = null;
		switch (position) {
			case MENU_KILO:
				fragment = new KilogrammFragment();
				toolbar.setTitle(getString(R.string.menu_kilogramms));
				break;
			case MENU_LITRE:
				fragment = new LitreFragment();
				toolbar.setTitle(getString(R.string.menu_litres));
				break;
		}
		return fragment;
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen()) {
			drawer.closeDrawer();
		} else {
			if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {super.onBackPressed();
			} else {
				Toast.makeText(getBaseContext(), R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();
			}
			back_pressed = System.currentTimeMillis();
		}
	}

}
