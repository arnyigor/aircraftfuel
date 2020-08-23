package com.arny.aircraftrefueling.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.arny.aircraftrefueling.R;

import java.io.File;

import static com.arny.aircraftrefueling.constants.Consts.DIR_SD;
import static com.arny.aircraftrefueling.constants.Consts.FILENAME_SD;

public class Local {
    public static <E> String getFString(String format, E unit) {
        try {
            return String.format(format, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteFileSD(Context context, String dir, String filename) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(context, (context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState()), Toast.LENGTH_LONG).show();
            return false;
        }
        return FileUtils.deleteFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir + "/" + filename));
    }

    public static Boolean writeFileSD(Context context, String fileText) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            ToastMaker.toastError(context,
                    context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState());
            return false;
        }
        return FileUtils.writeToFile(fileText,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIR_SD,
                FILENAME_SD,
                true);
    }

    public static boolean isFileExist(Context context, String dir, String filename) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
			ToastMaker.toastError(context,
					context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState());
            return false;
        } else {
            String parent = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir;
            File file = new File(parent, filename);
            return !(!file.exists() || !file.isFile());
        }
    }
}
