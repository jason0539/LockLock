package jason.locklock.view;

import jason.locklock.R;
import jason.locklock.utils.JasonRadiusUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Button;

/**
 * 对所继承对控件实现波纹效果，可继承button,textview等
 * 
 * @author liuzhenhui
 * 
 */
public class JasonRevealButton extends Button {
	private static final int INVALIDATE_DURATION = 10; // 每次刷新的时间间隔
	private static int DIFFUSE_GAP = 5; // 扩散半径增量，默认是以长按的速度扩散，比较慢
	private static int THE_V_LONG_PRESS_THAN_SHORT_PRESS = 3;// 长按与点击事件波纹扩散倍数
	private static int TAP_TIMEOUT; // 判断点击和长按的时间
	// 控件本身的一些属性
	private int viewWidth; // 控件宽度和高度
	private int viewHeight;
	private int pointX; // 控件原点坐标（左上角）
	private int pointY;
	// 波纹的属性
	private Paint bottomPaint; // 底色画笔
	private Paint colorPaint;// 波纹画笔
	private int maxRadius; // 扩散的最大半径
	private int curRadius;// 当前半径
	// 点击事件
	private int eventX;// 触摸点，即波纹圆心
	private int eventY;
	private long downTime = 0;// 按下时间
	private boolean isPushButton; // 记录是否按钮被按下

	public JasonRevealButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
	}

	/**
	 * 初始化画笔资源
	 */
	private void initPaint() {
		// 这里采用了两只色笔，一只画波纹背景，一只画波纹，其实
		colorPaint = new Paint();
		bottomPaint = new Paint();
		colorPaint.setColor(getResources().getColor(
				R.color.jason_bg_common_green_light));
		bottomPaint.setColor(getResources().getColor(
				R.color.jason_bg_common_green));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.viewWidth = w;
		this.viewHeight = h;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 只需要取一次时间
			if (downTime == 0) {
				downTime = SystemClock.elapsedRealtime();
			}
			eventX = (int) event.getX();
			eventY = (int) event.getY();
			// 计算最大半径
			maxRadius = JasonRadiusUtil.getMaxRadius(eventX, eventY, viewWidth,
					viewHeight);
			isPushButton = true;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (SystemClock.elapsedRealtime() - downTime < TAP_TIMEOUT) {
				DIFFUSE_GAP *= THE_V_LONG_PRESS_THAN_SHORT_PRESS;// 点击事件为长按速度的三倍
				postInvalidate();
			} else {//如果是长按，则松开后立刻取消扩散
				clearData();
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 清理改变的数据（初始化数据）
	 */
	private void clearData() {
		downTime = 0;
		DIFFUSE_GAP = 10;
		isPushButton = false;
		curRadius = 0;
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isPushButton) {// 按钮按下则去绘制波纹
			canvas.save();
			// 绘制按下后的整个背景
			canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight, bottomPaint);
			// 绘制扩散圆形背景
			canvas.drawCircle(eventX, eventY, curRadius, colorPaint);
			canvas.restore();
			// 直到半径等于最大半径
			if (curRadius < maxRadius) {
				postInvalidateDelayed(INVALIDATE_DURATION, pointX, pointY,
						pointX + viewWidth, pointY + viewHeight);
				curRadius += DIFFUSE_GAP;
			} else {
				clearData();
			}
		}
		super.onDraw(canvas);
	}
}
