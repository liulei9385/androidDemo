package com.example.libpng;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView imageView;

	// 引入编译的jni库
	static {
		System.loadLibrary("png");
		System.loadLibrary("test");
	}

	private void findView() {
		imageView = (ImageView) findViewById(R.id.imgView1);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// init view
		findView();

		// fielPath
		File externalSdPath = this.getExternalCacheDir();
		String pngFilePath = null;
		if (externalSdPath != null)
			pngFilePath = externalSdPath.getAbsolutePath() + "/example.png";
		if (pngFilePath != null)
			generateJniPng(pngFilePath, 0);
		System.out.println("开始停留-----");
		final String imgPath = pngFilePath;
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				System.out.println("开始执行-----");
				if (new File(imgPath).exists()) {
					Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
					imageView.setImageBitmap(bitmap);
				}
			}
		}, 2 * 1000);
	}

	public native void generateJniPng(String fileName, int type);
}
