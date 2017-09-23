package com.arny.aircraftrefueling;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import com.arny.arnylib.utils.ToastMaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
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
		File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/" + dir);
		file.mkdirs();
		if (new File(file, filename).delete()) {
			ToastMaker.toastSuccess(context, context.getResources().getString(R.string.file_deleted));
			return true;
		} else {
			ToastMaker.toastError(context, context.getResources().getString(R.string.error_file_not_deleted));
			return false;
		}
	}

	public static void toast(Context context, String message, boolean success) {
		if (success) {
			ToastMaker.toastSuccess(context, message);
		} else {
			ToastMaker.toastError(context, message);
		}
	}

	public static Boolean writeFileSD(Context context, String dir, String filename, String fileText) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			ToastMaker.toastError(context, context.getResources().getString(R.string.error_sd_card_not_avalable) + Environment.getExternalStorageState());
			return false;
		}
		File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/" + dir);
		file.mkdirs();
		File writeFile = new File(file, filename);
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeFile, true));
			bufferedWriter.write(fileText);
			bufferedWriter.close();
			ToastMaker.toastSuccess(context, context.getResources().getString(R.string.file_write_success) + writeFile.getAbsolutePath());
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
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
