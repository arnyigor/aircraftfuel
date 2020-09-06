package com.arny.aircraftrefueling.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arny.aircraftrefueling.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LitreFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LOG_TAG_LITRE";
    private Context context;
    private String FileText;
    private CheckBox chkPVK;
    private EditText edtDensityPVK, edtMassFuel, edtPercentPVK, edtTotalOnboard;
    private double mPercPVK, mRo, mVolTotal;

    public static LitreFragment getInstance() {
        return new LitreFragment();
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
        edtTotalOnboard = rootView.findViewById(R.id.editTotalOnboard);
        edtDensityPVK = rootView.findViewById(R.id.editDensityPVK);
        edtPercentPVK = rootView.findViewById(R.id.editPercentPVK);
        edtMassFuel = rootView.findViewById(R.id.editTotalMassFuel);
        chkPVK = rootView.findViewById(R.id.checkPVK);
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
				/*DroidUtils.alertConfirmDialog(context, getString(R.string.file_save_data), () -> {
					if (Utility.empty(edtMassFuel.getText().toString())) {
						Toast.makeText(context, getString(R.string.error_calulate_requied), Toast.LENGTH_LONG).show();
					} else {
						getLitreFileText();
						if (Local.writeFileSD(context, FileText)) {
							getActivity().invalidateOptionsMenu();
						}
					}
				});*/
            }
            break;
            case R.id.action_delete_file:
				/*DroidUtils.alertConfirmDialog(context, getString(R.string.file_del), () -> {
					if (Local.isFileExist(context, DIR_SD, FILENAME_SD)) {
						if (Local.deleteFileSD(context, DIR_SD, FILENAME_SD)) {
							getActivity().invalidateOptionsMenu();
						}
					}
				});*/
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
		/*MenuItem delItem = menu.findItem(R.id.action_delete_file);
		delItem.setVisible(false);
		if (Local.isFileExist(context, DIR_SD, FILENAME_SD)) {
			delItem.setVisible(true);
		}*/
    }

    private void getLitreFileText() {
        String s = edtMassFuel.getText().toString();
        FileText = getPvkStringResult(s);
    }

    @NotNull
    private String getPvkStringResult(String totalMass) {
        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date()) + "\n"
                + getString(R.string.litre_qty) +
                String.format(Locale.getDefault(), ":%.0f", mVolTotal) + "(" + getString(R.string.unit_volume) + ");"
                + getString(R.string.file_percent_pvk) +
                String.format(Locale.getDefault(), ":%.0f", mPercPVK) + "(%);"
                + getString(R.string.file_density_pvk) +
                String.format(Locale.getDefault(), ":%.3f", mRo) + "(" + getString(R.string.unit_density) + ");"
                + getString(R.string.file_total_mass) + ":" +
                totalMass + "(" + getString(R.string.unit_mass_kg) + ")\n";
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
        return String.format(Locale.getDefault(), "%.0f", (mvtot - d4 + d4 * mro));
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
