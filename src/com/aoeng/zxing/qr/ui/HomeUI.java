/**
 * 
 */
package com.aoeng.zxing.qr.ui;

import com.aoeng.zxing.qr.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author [Aoeng Zhang] @datatime Aug 31, 2013:4:37:40 PM
 * @Email [zhangshch2008@gmail.com] Aug 31, 2013
 */
public class HomeUI extends Activity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_home);

		this.findViewById(R.id.btnCreateQR).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 生成二维码
				Intent intent = new Intent(HomeUI.this, CreateQRUI.class);
				startActivity(intent);
			}
		});

		this.findViewById(R.id.btnScanQR).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeUI.this, ScanQRUI.class);
				startActivity(intent);
			}
		});

		this.findViewById(R.id.btnScanQR2).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeUI.this, ScanQR2UI.class);
				startActivity(intent);
			}
		});
	}

}
