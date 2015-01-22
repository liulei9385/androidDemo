package com.example.libpng;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libpng.utils.ViewUtils;

public class MainActivity extends Activity {

	private ImageView imageView;
	private Button getVersinBtn;
	private Context context;

	private static Handler handler = new Handler();

	// 引入编译的jni库
	static {
		System.loadLibrary("png");
		System.loadLibrary("test");
	}

	private void findView() {
		context = MainActivity.this;
		imageView = (ImageView) findViewById(R.id.imgView1);
		getVersinBtn = (Button) findViewById(R.id.button1);
		setOnClickListener(getVersinBtn);
	}

	private void setOnClickListener(View view) {
		if (view != null)
			view.setOnClickListener(clickListener);
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
		final String imgPath = pngFilePath;
		handler.postDelayed(new Runnable() {

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

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			final int viewId = v.getId();
			switch (viewId) {
			case R.id.button1:
				// create a textView
				final String versionText = getVersion();
				if (!ViewUtils.isViewExists(MainActivity.this, R.id.textViewId)) {
					TextView textView = new TextView(context);
					textView.setId(R.id.textViewId);
					textView.setText(versionText);
					textView.setTextColor(ViewUtils.getResColor(context,
							R.color.Blue));

					int wrap_content = FrameLayout.LayoutParams.WRAP_CONTENT;
					FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
							wrap_content, wrap_content);
					layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
					addContentView(textView, layoutParams);
				}
				break;

			default:
				break;
			}

		}
	};

	// ########### nativate method ###########
	public native void generateJniPng(String fileName, int type);

	public native String getVersion();
	// ########### nativate method ###########
}
