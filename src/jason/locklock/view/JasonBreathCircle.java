package jason.locklock.view;

import jason.locklock.R;
import jason.locklock.utils.JasonRadiusUtil;
import jason.locklock.utils.JasonRandomUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * 一个会呼吸的气泡
 * 
 * @author liuzhenhui
 * 
 */
public class JasonBreathCircle {
	private static int DIFFUSE_GAP = 2; // 扩散半径增量
	private static final int INVALIDATE_DURATION = 10; // 每次刷新的时间间隔

	private Context mContext;
	private boolean needToDrawReveal = false;// 绘画标志位
	// 圆形自身的一些属性
	private boolean isLargerMode = true;// 呼吸模式
	private Paint mPaintReveal;// 画笔
	private int mCircleCenterX;// 圆心x
	private int mCircleCenterY;// 圆心y
	private int mCurRadius;// 当前半径
	private int mMaxRadius;// 最大半径
	// 依附的控件的一些属性，利用高度宽度计算当前触摸点的位置
	private View mParentView;// 依附的控件
	private int mParentHeight;// 控件高度
	private int mParentWidth;// 控件宽度

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝初始化方法（必须调用）＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

	/**
	 * 实例化一个圆，之后要调用initParameters初始化该圆的属性，再之后就可以draw了
	 * 
	 * @param context
	 */
	public JasonBreathCircle(Context context) {
		mContext = context;
		initPaint();
	}

	/**
	 * 传入view，用来初始化坐标，半径,默认以中心为圆心开始画圆
	 * 
	 * @param view
	 */
	public void initParameters(View view) {
		this.mParentView = view;
		// 获取当前依附控件的属性
		mParentHeight = mParentView.getHeight();
		mParentWidth = mParentView.getWidth();
		// 初始化圆的属性
		mMaxRadius = (int) Math.hypot(view.getHeight(), view.getWidth()) / 2;
		// 控件的宽度高度求出初始圆心
		mCircleCenterX = mParentWidth / 2;
		mCircleCenterY = mParentHeight / 2;
	}

	/**
	 * 传入画布
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if (needToDrawReveal) {
			// JasonLog.log("圆心：x=" + mCircleCenterX + ";y=" + mCircleCenterY);
			// JasonLog.log("当前半径:" + mCurRadius);
			// JasonLog.log("最大半径:" + mMaxRadius);
			canvas.save();
			canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCurRadius,
					mPaintReveal);
			canvas.restore();
			if (isLargerMode && mCurRadius < mMaxRadius) {
				mCurRadius += DIFFUSE_GAP;// 波纹递增
				postRevealInvalidate();
			} else if (mCurRadius > 0 && !isLargerMode) {
				// 画完一个周期从头再画
				mCurRadius -= DIFFUSE_GAP;// 波纹递增
				postRevealInvalidate();
			} else {// 转换模式
				isLargerMode = !isLargerMode;
				// 随机选择坐标作为圆心,从0到最右边中间取x，从0到底边取y
				setCircleCenter(JasonRandomUtil.nextInt(0, mParentWidth),
						JasonRandomUtil.nextInt(0, mParentHeight));
				// 圆心更换后，缩小前，把当前半径设为最大，防止边上出现空白覆盖不满
				if (!isLargerMode) {
					mCurRadius = mMaxRadius;
				}
				postRevealInvalidate();
			}
		}
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝对外接口＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	/**
	 * 开始呼吸
	 */
	public void start() {
		if (needToDrawReveal) {
			return;
		}
		needToDrawReveal = true;
		postRevealInvalidate();
	}

	/**
	 * 停止呼吸
	 */
	public void stop() {
		if (!needToDrawReveal) {
			return;
		}
		needToDrawReveal = false;
		reset();
		postRevealInvalidate();
	}

	/**
	 * 设置圆心
	 * 
	 * @param x
	 * @param y
	 */
	public void setCircleCenter(int x, int y) {
		mCircleCenterX = x;
		mCircleCenterY = y;
		mMaxRadius = JasonRadiusUtil.getMaxRadius(mCircleCenterX,
				mCircleCenterY, mParentWidth, mParentHeight);
	}

	/**
	 * 设置画圆为空心还是实心，默认实心
	 * 
	 * @param isHollow
	 */
	public void setHollow(boolean isHollow) {
		mPaintReveal.setStyle(isHollow ? Paint.Style.STROKE : Paint.Style.FILL);
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝内部实现＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	/**
	 * 重置
	 */
	private void reset() {
		mCurRadius = 0;
		isLargerMode = true;
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		mPaintReveal = new Paint();
		mPaintReveal.setColor(mContext.getResources().getColor(
				R.color.jason_bg_common_green_light));
		mPaintReveal.setAntiAlias(true);
	}

	/**
	 * 重绘
	 */
	private void postRevealInvalidate() {
		mParentView.postInvalidateDelayed(INVALIDATE_DURATION);
	}
}
