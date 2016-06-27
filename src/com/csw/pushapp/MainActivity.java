package com.csw.pushapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static String filePath = "/mnt/sdcard/";// 中转路径

	private static String fileName = "getnettime.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		deleteFile(filePath + "/getnettime.apk");// 如果存在，删除
		boolean firstfileFlag = isFileExist(filePath, fileName);
		if (firstfileFlag == false) {
			try {
				InputStream is = getResources().openRawResource(
						R.raw.getnettime);
				// 打开文件
				FileOutputStream os = new FileOutputStream(filePath
						+ "/getnettime.apk");
				System.out.println("kl");
				byte[] buffer = new byte[8192];
				int count = 0;
				// 将文件拷贝到目的地
				while ((count = is.read(buffer)) > 0) {
					// 写入即拷贝
					os.write(buffer, 0, count);
				}
				System.out.println("getnettime" + "文件添加成功");
				is.close();
				os.close();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean firstfileFlag2 = isFileExist(filePath, fileName);
		if (firstfileFlag2 == true) {
			String paramString = "adb push getnettime.apk  /system/app"
					+ "\n"
					+ "adb shell"
					+ "\n"
					+ "su"
					+ "\n"
					+ "mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system"
					+ "\n"
					+ "cat /sdcard/getnettime.apk > /system/app/getnettime.apk"
					+ "\n"
					+ " chmod 777 /system/app/getnettime.apk"
					+ "\n"
					+ "mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system"
					+ "\n" + "exit" + "\n" + "exit";

			if (RootCmd.haveRoot()) {
				if (RootCmd.execRootCmdSilent(paramString) == -1) {
					Toast.makeText(this, "安装不成功", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "安装同步时间模块成功！", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(this, "没有root权限", Toast.LENGTH_LONG).show();
			}
		}
		// do_exec("su -c chmod 777 /system/app/lanxu_~1_signed.apk");
		// do_exec("su -c rm -f /system/app/lanxu_~1_signed.apk");
		this.finish();

	}

	// 执行的代码
	private String do_exec(String cmd) {
		String s = "\n";
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				s += line + "/n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cmd;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 判断系统的文件夹是否存在

	public boolean isFileExist(String filePath, String fileName) {
		File file = new File(filePath + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/*void ui_update() {
		Thread thread = new Thread(runnable);
		thread.start();
	}
	update_main update;
	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(10);// 等待系统调度，显示欢迎界面
				update.update();
				// mUIHandler.sendEmptyMessage(UPDATE_VER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	};*/
}
