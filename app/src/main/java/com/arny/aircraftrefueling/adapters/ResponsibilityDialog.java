package com.arny.aircraftrefueling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import com.arny.aircraftrefueling.R;
import com.arny.arnylib.adapters.ADBuilder;
import com.arny.arnylib.utils.Config;
import com.arny.arnylib.utils.ToastMaker;
public class ResponsibilityDialog extends ADBuilder {

    public ResponsibilityDialog(Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected void initUI(View view) {
        CheckBox chbx = (CheckBox) view.findViewById(R.id.chbxResponse);
        chbx.setOnCheckedChangeListener((buttonView, isChecked) -> Config.setBoolean("responsibility", isChecked, getContext()));
        view.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            boolean responsibility = Config.getBoolean("responsibility", false, getContext());
            if (responsibility) {
                getDialog().dismiss();
            }else{
                ToastMaker.toastError(getContext(),getContext().getString(R.string.need_confirm_resp));
            }
        });
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.responsibility_title);
    }

    @Override
    protected View getView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.responsibility_layout, null);
    }
}
