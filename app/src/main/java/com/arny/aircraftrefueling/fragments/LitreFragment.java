package com.arny.aircraftrefueling.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.arny.aircraftrefueling.R;
import com.arny.aircraftrefueling.Local;
import com.arny.arnylib.utils.DroidUtils;
import com.arny.arnylib.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.arny.aircraftrefueling.Local.*;

public class LitreFragment extends Fragment implements View.OnClickListener {
	private static final String TAG = "LOG_TAG_LITRE";
	private Context context;
	private String FileText;
	private CheckBox chkPVK;
	private EditText edtDensityPVK, edtMassFuel, edtPercentPVK, edtTotalOnboard;
	private double mPercPVK, mRo, mVolTotal;

	public LitreFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_litre, container, false);
		this.context = container.getContext();
		edtTotalOnboard = (EditText) rootView.findViewById(R.id.editTotalOnboard);
		edtDensityPVK = (EditText) rootView.findViewById(R.id.editDensityPVK);
		edtPercentPVK = (EditText) rootView.findViewById(R.id.editPercentPVK);
		edtMassFuel = (EditText) rootView.findViewById(R.id.editTotalMassFuel);
		chkPVK = (CheckBox) rootView.findViewById(R.id.checkPVK);
		chkPVK.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				edtPercentPVK.setEnabled(true);
				edtPercentPVK.setText(String.valueOf(50));
				mPercPVK = Float.parseFloat(edtPercentPVK.getText().toString());
			} else {
				mPercPVK = 100.0;
				edtPercentPVK.setText(String.valueOf(mPercPVK));
				edtPercentPVK.setEnabled(false);
			}
		});
		rootView.findViewById(R.id.buttonLitreCnt).setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.action_save_to_file: {
				DroidUtils.alertConfirmDialog(context, getString(R.string.file_save_data), () -> {
					if (Utility.empty(edtMassFuel.getText().toString())) {
						Toast.makeText(context, getString(R.string.error_calulate_requied), Toast.LENGTH_LONG).show();
					} else {
						getLitreFileText();
						if (Local.writeFileSD(context, FileText)) {
							getActivity().invalidateOptionsMenu();
						}
					}
				});
			}
			break;
			case R.id.action_delete_file:
				DroidUtils.alertConfirmDialog(context, getString(R.string.file_del), () -> {
					if (Local.isFileExist(context, DIR_SD, FILENAME_SD)) {
						if (Local.deleteFileSD(context, DIR_SD, FILENAME_SD)) {
							getActivity().invalidateOptionsMenu();
						}
					}
				});
				break;
		}
		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem delItem = menu.findItem(R.id.action_delete_file);
		delItem.setVisible(false);
		if (Local.isFileExist(context, DIR_SD, FILENAME_SD)) {
			delItem.setVisible(true);
		}
	}

	private void getLitreFileText() {
		FileText = String.valueOf(new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date())) + "\n"
				+ getString(R.string.file_total_litre) +
				Local.getFString(":%.0f", mVolTotal) + getString(R.string.sh_unit_litre) + ";"
				+ getString(R.string.file_percent_pvk) +
				Local.getFString(":%.0f", mPercPVK) +
				getString(R.string.sh_unit_percent) + ";"
				+ getString(R.string.file_density_pvk) +
				Local.getFString(":%.3f", mRo) + getString(R.string.sh_unit_kilo_on_litre) + ";"
				+ getString(R.string.file_total_mass) +":"+
				edtMassFuel.getText().toString() + getString(R.string.sh_unit_kilo) + "\n";
	}

	private void calculateLitre() {
		if (edtTotalOnboard.getText().toString().trim().equals("") || edtTotalOnboard.getText().toString().equals("0")) {
			Toast.makeText(context, R.string.error_val_density_pvk, Toast.LENGTH_SHORT).show();
			return;
		}
		if (edtDensityPVK.getText().toString().trim().equals("") || edtDensityPVK.getText().toString().equals("0")) {
			Toast.makeText(context, R.string.error_val_density_pvk, Toast.LENGTH_SHORT).show();
			return;
		}
		if (edtPercentPVK.getText().toString().trim().equals("")) {
			Toast.makeText(context, R.string.error_val_proc_pvk, Toast.LENGTH_SHORT).show();
			return;
		}
		mPercPVK = Float.parseFloat(edtPercentPVK.getText().toString());
		if (mPercPVK < 0.0 || mPercPVK > 100.0) {
			Toast.makeText(context, R.string.error_val_proc_pvk, Toast.LENGTH_SHORT).show();
			return;
		}
		mVolTotal = Float.parseFloat(edtTotalOnboard.getText().toString());
		mRo = Float.parseFloat(edtDensityPVK.getText().toString());
		edtMassFuel.setText(MassCnt(mVolTotal, mRo, mPercPVK));
	}

	private String MassCnt(double mvtot, double mro, double percpvk) {
		double d4 = mvtot * (percpvk / 100.0);
		return Local.getFString("%.0f", (mvtot - d4 + d4 * mro));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonLitreCnt:
				calculateLitre();
				break;
		}
	}
}
