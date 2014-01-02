/**
 * 
 */
package com.aoeng.zxing.qr.view;

import java.util.Collection;
import java.util.HashSet;

import com.aoeng.zxing.qr.R;
import com.aoeng.zxing.qr.camera.CameraManager;
import com.google.zxing.ResultPoint;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author [ShaoCheng Zhang] Sep 4, 2013:5:23:40 PM
 * @Email [zhangshch2008@gmail.com]
 */
public class ViewfinderView2 extends View {

	/*
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;
	/*
	 * 四个绿色对角边的长度
	 */
	private int screenRate;
	/*
	 * 四个绿色对角边的宽度
	 */
	private static final int CORNER_WIDTH = 10;
	/*
	 * 扫描框中间线的宽度
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;
	/*
	 * 扫描框中的中间线 与扫描狂左右的间隙
	 */
	private static final int MIDDLE_LINE_MARRGIN = 5;
	/*
	 * 手机的屏幕密度
	 */
	private static float density;
	/*
	 * 字体大小
	 */
	private static final int TEXT_SIZE = 16;
	/*
	 * 字体距离扫描框下面的距离
	 */
	private static final int TEXT_PADDING_TOP = 30;
	/*
	 * 画笔对象的引用
	 */
	private Paint paint;
	/*
	 * 中间滑动的最顶端
	 */
	private int slideTop;
	/*
	 * 中间滑动的最低端
	 */
	private int slideBottom;
	/*
	 * 中间线每次移动的距离
	 */
	private static final int SPEEN_DISTANCE = 2;
	private Bitmap resultBitmap;
	private int maskColor;
	private int resultColor;
	private int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;

	/**
	 * @param context
	 * @param attrs
	 */
	public ViewfinderView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		density = context.getResources().getDisplayMetrics().density;
		// 像素转换成 dp
		screenRate = (int) (20 * density);
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 中间的扫描框，修改大小可以去 CameraManager 里面修改
		Rect rect = CameraManager.get().getFramingRect();
		if (rect == null) {
			return;
		}
		// 初始化中间线滑动的最上边和最下边
		if (isFirst) {
			isFirst = true;
			slideTop = rect.top;
			slideBottom = rect.bottom;
		}
		// 获取屏幕的宽和高
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		// 画出扫描框外面的阴影部分，共四个部分
		canvas.drawRect(0, 0, width, rect.bottom, paint);
		canvas.drawRect(0, rect.top, rect.left, rect.bottom + 1, paint);
		canvas.drawRect(rect.right + 1, rect.top, width, rect.bottom + 1, paint);
		canvas.drawRect(0, rect.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, rect.left, rect.top, paint);
		} else {
			// 画扫描框边上的角，总共8个部分
			paint.setColor(Color.GREEN);
			canvas.drawRect(rect.left, rect.top, rect.left + screenRate, rect.top + CORNER_WIDTH, paint);
			canvas.drawRect(rect.left, rect.top, rect.left + CORNER_WIDTH, rect.top + screenRate, paint);
			canvas.drawRect(rect.right - screenRate, rect.top, rect.right, rect.top + CORNER_WIDTH, paint);
			canvas.drawRect(rect.right - CORNER_WIDTH, rect.top, rect.right, rect.top + screenRate, paint);
			canvas.drawRect(rect.left, rect.bottom - CORNER_WIDTH, rect.left + screenRate, rect.bottom, paint);
			canvas.drawRect(rect.left, rect.bottom - screenRate, rect.left + CORNER_WIDTH, rect.bottom, paint);
			canvas.drawRect(rect.right - screenRate, rect.bottom - CORNER_WIDTH, rect.right, rect.bottom, paint);
			canvas.drawRect(rect.right - CORNER_WIDTH, rect.bottom - screenRate, rect.right, rect.bottom, paint);

			// 绘制中间的线,每次刷新界面，中间的线往下移动 SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= rect.bottom) {
				slideTop = rect.top;
			}
			canvas.drawRect(rect.left + MIDDLE_LINE_MARRGIN, slideTop - MIDDLE_LINE_WIDTH / 2, rect.right - MIDDLE_LINE_MARRGIN, slideTop + MIDDLE_LINE_WIDTH / 2, paint);

			// 画扫描框下面的文字
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("Syatem", Typeface.BOLD));
			canvas.drawText(getResources().getString(R.string.scan_text), rect.left, (float) (rect.bottom + (float) TEXT_PADDING_TOP * density), paint);
			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(rect.left + point.getX(), rect.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(rect.left + point.getX(), rect.top + point.getY(), 3.0f, paint);
				}
			}

			// 只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, rect.left, rect.top, rect.right, rect.bottom);

		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
