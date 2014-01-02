package com.aoeng.zxing.qr.ui;

import com.aoeng.zxing.qr.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String intent = getIntent().getStringExtra("code");
		TextView txt = (TextView) findViewById(R.id.txtcode);
		txt.setText(intent);

	}

}
