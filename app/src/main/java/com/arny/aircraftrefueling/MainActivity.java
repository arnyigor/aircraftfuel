package com.arny.aircraftrefueling;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.arny.aircraftrefueling.fragments.KilogrammFragment;
import com.arny.aircraftrefueling.fragments.LitreFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class MainActivity extends AppCompatActivity {
	private static final int MENU_FUEL = 0;
	private static final int MENU_DEICE = 1;
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
		initDrawer();
		selectItem(MENU_FUEL);
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
								.withIdentifier(MENU_FUEL)
								.withName(R.string.menu_fueling)
								.withIcon(getResources().getDrawable(R.drawable.ic_fuel)),
						new PrimaryDrawerItem()
								.withIdentifier(MENU_DEICE)
								.withName(R.string.menu_deicing)
								.withIcon(getResources().getDrawable(R.drawable.ic_snowflake))
				)
				.withOnDrawerItemClickListener((view, position, drawerItem) -> {
					selectItem((int) drawerItem.getIdentifier());
					return true;
				})
				.build();
	}

	private void selectItem(int position) {
		commitFragment(getFragment(position));
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
			case MENU_FUEL:
				fragment = new KilogrammFragment();
				toolbar.setTitle(getString(R.string.menu_fueling));
				break;
			case MENU_DEICE:
				fragment = new LitreFragment();
				toolbar.setTitle(getString(R.string.menu_deicing));
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
