package jason.locklock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class JasonBreathTextView extends TextView {

	private int mLeft;// 左边
	private int mTop;// 上边
	private int mRight;// 右边
	private int mBottom;// 下边
	private int mViewWidth;// 控件宽度
	private int mViewHeight;// 控件高度
	private JasonBreathCircle breathCircle;

	public JasonBreathTextView(Context context) {
		super(context);
	}

	public JasonBreathTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		breathCircle = new JasonBreathCircle(context);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mViewHeight = h;
		mViewWidth = w;
		// 坐标
		mLeft = getLeft();
		mTop = getTop();
		mRight = getRight();
		mBottom = getBottom();
		breathCircle.initParameters(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		breathCircle.setCircleCenter((int) event.getX(), (int) event.getY());
		// JasonLog.log("getY:" + getY());
		// JasonLog.log("getTop" + getTop());
		// JasonLog.log("getX:" + getX());
		// JasonLog.log("getLeft" + getLeft());
		// JasonLog.log("点击事件坐标：event.x=" + event.getX() + ";event.y="
		// + event.getY());
		// JasonLog.log("TextView的四角坐标：textview.left=" + mLeft +
		// ";textview.top="
		// + mTop + ";textview.right=" + mRight + ";textview.bottom="
		// + mBottom);
		// JasonLog.log("textview的宽度:" + mViewWidth + ";高度：" + mViewHeight);
		return super.onTouchEvent(event);
	}

	// 这里选择onDraw方法实现，onDraw是view绘制自身的方法，为了波纹不覆盖到自身到元素，要绘制完波纹再去绘制自身
	// 相反，在revealLayout里面，为了波纹不被子控件挡住，就在dispatchDraw里面绘制子控件了
	@Override
	protected void onDraw(Canvas canvas) {
		breathCircle.draw(canvas);
		super.onDraw(canvas);
	}

	/**
	 * 开始水纹效果
	 */
	public void startReveal() {
		breathCircle.start();
	}

	/**
	 * 停止水纹效果
	 */
	public void stopReveal() {
		breathCircle.stop();
	}

}
