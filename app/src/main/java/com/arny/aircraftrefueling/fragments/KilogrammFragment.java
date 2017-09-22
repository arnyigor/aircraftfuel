package com.arny.aircraftrefueling.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arny.aircraftrefueling.R;
import com.arny.aircraftrefueling.Utils;
import com.arny.arnylib.utils.DateTimeUtils;
import com.arny.arnylib.utils.DroidUtils;
import com.arny.arnylib.utils.Utility;

import java.util.Locale;

public class KilogrammFragment extends Fragment {
    private static final double LITRE_AM_GALLON = 0.2642;
    private static final String DIR_SD = "AirRefuelFiles";
    private static final String FILENAME_SD = "AirRefuelKilo.txt";
    private final double WING_TANK_MAX_LITRE = 4876.0;
    private final double NO_USE_LITRE = 50;
    private EditText edtDensityFuel, edtRequiredfuel, edtTotalFuel;
    private String left, right, centre, FileText, FileTextNull;
    private double mMassOstat, mMassRequired, mReqMassTotal, mRo;
    private TextView tvCT, tvLT, tvRT, tvTotalKilo, tvTotalLitre, tvReqHeader;
    private static final int V_UNIT_LITRE = 0;
    private static final int V_UNIT_AM_GAL = 1;
    private String v_unit;
    private int vUnit;
    private Context context;
    private Button btnKiloFileSave,btnKiloFileDel;

    public KilogrammFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kilogramm, container, false);
        context = container.getContext();
        edtTotalFuel = (EditText) rootView.findViewById(R.id.editTotalMass);
        edtDensityFuel = (EditText) rootView.findViewById(R.id.editDensityFuel);
        edtRequiredfuel = (EditText) rootView.findViewById(R.id.editRequiredMass);
        tvTotalLitre = (TextView) rootView.findViewById(R.id.tvTotalLitre);
        tvTotalKilo = (TextView) rootView.findViewById(R.id.tvTotalKilo);
        tvLT = (TextView) rootView.findViewById(R.id.tvLT);
        tvRT = (TextView) rootView.findViewById(R.id.tvRT);
        tvCT = (TextView) rootView.findViewById(R.id.tvCT);
        tvReqHeader = (TextView) rootView.findViewById(R.id.tvReqHeader);
        final Spinner vUnits = (Spinner) rootView.findViewById(R.id.volumeUnit);
        final Button buttonKiloCnt = (Button) rootView.findViewById(R.id.buttonKiloCnt);
        btnKiloFileSave = (Button) rootView.findViewById(R.id.btnKiloSave);
        btnKiloFileDel = (Button) rootView.findViewById(R.id.btnKiloDel);
        if (Utils.isFileExist(context, DIR_SD, FILENAME_SD)) {
            btnKiloFileDel.setVisibility(View.VISIBLE);
        }
        btnKiloFileSave.setOnClickListener(btnSavefileListener);
        btnKiloFileDel.setOnClickListener(btnFileDelListener);
        buttonKiloCnt.setOnClickListener(btnClick);
        ArrayAdapter<String> vUnitsAdapter = new ArrayAdapter<>(context, R.layout.v_unit_item, getResources().getStringArray(R.array.volumeUnits));
        vUnits.setAdapter(vUnitsAdapter);
        vUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                v_unit = adapterView.getItemAtPosition(i).toString();
                vUnit = i;
                tvReqHeader.setText(v_unit);
                tvTotalLitre.setText(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                v_unit = adapterView.getItemAtPosition(V_UNIT_LITRE).toString();
                vUnit = V_UNIT_LITRE;
                tvReqHeader.setText(v_unit);
                tvTotalLitre.setText(null);
            }
        });
        return rootView;
    }
    View.OnClickListener btnFileDelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
	        DroidUtils.alertConfirmDialog(context, getString(R.string.file_del), () -> {
		        if (Utils.isFileExist(context,DIR_SD,FILENAME_SD)){
			        if (Utils.deleteFileSD(context,DIR_SD,FILENAME_SD)){
				        btnKiloFileDel.setVisibility(View.GONE);
			        }
		        }
	        });
        }
    };
    View.OnClickListener btnSavefileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
	        DroidUtils.alertConfirmDialog(context, getString(R.string.file_save_data), () -> {
		        getfileText();
		        if (tvTotalLitre.getText().toString().trim().equals("")) {
			        Toast.makeText(context, getString(R.string.error_calulate_requied), Toast.LENGTH_LONG).show();
		        } else {
			        if (!Utils.writeFileSD(context,DIR_SD,FILENAME_SD,FileText)){
				        Toast.makeText(context, getString(R.string.error_file_not_write), Toast.LENGTH_SHORT).show();
				        return;
			        }
		        }
		        btnKiloFileDel.setVisibility(View.VISIBLE);
	        });
        }
    };
    View.OnClickListener btnClick = view -> calculateFuelCapacity();

    private void getfileText() {
        FileText = DateTimeUtils.getDateTime("dd MMM yyyy HH:mm:ss") + "\n" +
                getString(R.string.file_fuel_remain) +
		        Utils.getFString("%.0f", mMassOstat) +
                getString(R.string.sh_unit_kilo) + ";" +
                getString(R.string.file_fueled) +
                Utils.getFString("%.0f", mReqMassTotal) +
                getString(R.string.sh_unit_litre) + ";" +
                getString(R.string.unit_density) + ": " +
                Utils.getFString("%.3f", mRo) +
                getString(R.string.sh_unit_kilo_on_litre) +
                getString(R.string.file_total_litre) + "\n";
    }

    /**
     * Функция заправки
     *
     * @param massreq сколько необходимо килограмм
     * @param mro массовая плотность
     */
    public void tankFueling(double massreq, double mro) {
        double maxCen = 2 * (WING_TANK_MAX_LITRE * mro - NO_USE_LITRE);
        double l, r, c;
        if (massreq <= maxCen) {
            l = massreq / 2;
            r = massreq / 2;
            left = String.format("%.0f",l) ;
            right = String.format("%.0f",r) ;
            centre = String.format("%d",0) ;
        }else{
            c = massreq - maxCen;
            l = massreq - c;
            r = l / 2;
            left = String.format("%.0f",r) ;
            right = String.format("%.0f",r) ;
            centre = String.format("%.0f",c) ;
        }
    }

    private void calculateFuelCapacity() {
        if (Utility.empty(edtTotalFuel.getText().toString())) {
            Toast.makeText(context, R.string.error_val_on_board, Toast.LENGTH_LONG).show();
            return;
        }
        if (Utility.empty(edtRequiredfuel.getText().toString()) || edtRequiredfuel.getText().toString().trim().equals("0")) {
            Toast.makeText(context, R.string.error_val_req, Toast.LENGTH_LONG).show();
            return;
        }
        if (Utility.empty(edtDensityFuel.getText().toString()) || edtDensityFuel.getText().toString().trim().equals("0")) {
            Toast.makeText(context, R.string.error_val_density, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            mRo = Float.parseFloat(edtDensityFuel.getText().toString());
            mMassOstat = Float.parseFloat(edtTotalFuel.getText().toString());
            mMassRequired = Float.parseFloat(edtRequiredfuel.getText().toString());
            String totStr = LitreCnt(mMassOstat, mRo, mMassRequired);
            double totLit = (!totStr.equals("0") || !totStr.equals("")) ? Double.parseDouble(totStr) : 0;
            if (totLit < 0) {
                Toast.makeText(context, getString(R.string.error_wrong_data), Toast.LENGTH_SHORT).show();
                return;
            }
            totLit = getTotLit(totLit);
            tvTotalLitre.setText(String.format(Locale.getDefault(), "%.0f", totLit));
            tvTotalKilo.setText(String.format(Locale.getDefault(), "%.0f", mReqMassTotal));
            tankFueling(mMassRequired, mRo);
            tvLT.setText(String.format("%s:\n%s", getString(R.string.left_fuel_tank), left));
            tvRT.setText(String.format("%s:\n%s", getString(R.string.right_fuel_tank), right));
            tvCT.setText(String.format("%s:\n%s", getString(R.string.center_fuel_tank), centre));
            btnKiloFileSave.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getTotLit(double totLit) {
        switch (vUnit) {
            case V_UNIT_AM_GAL:
                totLit = totLit * LITRE_AM_GALLON;
                break;
            default:
                break;
        }
        return totLit;
    }

    private String LitreCnt(double ostat, double mro, double mreq) {
        mReqMassTotal = mreq - ostat;
        return String.format("%.0f", (mReqMassTotal / mro));
    }

}