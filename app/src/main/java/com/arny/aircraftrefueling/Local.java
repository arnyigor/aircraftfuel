package com.arny.aircraftrefueling;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import com.arny.arnylib.files.FileUtils;
import com.arny.arnylib.utils.ToastMaker;

import java.io.File;

public class Local {
	public static final String DIR_SD = "AirRefuelFiles";
	public static  final String FILENAME_SD = "AirRefuelLitre.txt";
	public static <E> String getFString(String format, E unit) {
		try {
			return String.format(format, unit);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Boolean deleteFileSD(Context context, String dir, String filename) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			Toast.makeText(context, (context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState()), Toast.LENGTH_LONG).show();
			return false;
		}
		return FileUtils.deleteFile(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/" + dir + "/" + filename);
	}

	public static void toast(Context context, String message, boolean success) {
		if (success) {
			ToastMaker.toastSuccess(context, message);
		} else {
			ToastMaker.toastError(context, message);
		}
	}

	public static Boolean writeFileSD(Context context,String fileText) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			ToastMaker.toastError(context, context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState());
			return false;
		}
		return FileUtils.writeToFile(fileText, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIR_SD, FILENAME_SD, true);
	}

	public static boolean isFileExist(Context context, String dir, String filename) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			ToastMaker.toastError(context, context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState());
			return false;
		} else {
			File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/" + dir, filename);
			return !(!file.exists() || !file.isFile());
		}
	}
}
