/**
 * 
 */
package com.aoeng.zxing.qr.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aoeng.zxing.qr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @author [Aoeng Zhang] @datatime Aug 31, 2013:4:44:59 PM
 * @Email [zhangshch2008@gmail.com] Aug 31, 2013
 */
public class CreateQRUI extends Activity {
	private EditText etInfo;
	private ImageView imSrc;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_qr_create);
		etInfo = (EditText) this.findViewById(R.id.etInfo);

		imSrc = (ImageView) this.findViewById(R.id.imSrc);
		this.findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {


			private Bitmap bitmap;

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String infoStr = etInfo.getText().toString().trim();
				if (TextUtils.isEmpty(infoStr)) {
					Toast.makeText(CreateQRUI.this, "输入信息不能为空", Toast.LENGTH_SHORT);
					return;
				}
				try {
					bitmap = Create2DCode(new String(infoStr.getBytes("utf-8"), "iso-8859-1"));
					BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
					imSrc.setBackgroundDrawable(bitmapDrawable);

					String filePath = android.os.Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png";
					if (!new File(filePath).exists()) {
						try {
							File file = new File(filePath);
							file.createNewFile();
							FileOutputStream outputStream = new FileOutputStream(file);
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
							outputStream.flush();
							outputStream.close();
							Toast.makeText(CreateQRUI.this, "图片保存成功", 1).show();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		imSrc.setOnLongClickListener(new View.OnLongClickListener() {

			private Bitmap bitmap;

			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String filePath = android.os.Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png";
				if (!new File(filePath).exists()) {

					try {
						File file = new File(filePath);
						file.createNewFile();

						FileOutputStream outputStream = new FileOutputStream(file);

						bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
						outputStream.flush();
						outputStream.close();
						Toast.makeText(CreateQRUI.this, "图片保存成功", 1).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				return false;
			}
		});
	}

	/**
	 * 用字符串生成二维码
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public Bitmap Create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 600, 600);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000; // 0xff000000;// 黑色
				} else {
					pixels[y * width + x] = -1;// -1 相当于0xffffffff 白色
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
