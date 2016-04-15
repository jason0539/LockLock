package jason.locklock.view;

import java.util.ArrayList;

import jason.locklock.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * liuzhenhui 16/4/15.上午10:32
 */
public class JasonRevealLinearLayout extends LinearLayout {
    private View mTouchTarget;// 点击的控件

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔，绘制波纹
    private int mTargetWidth;// 点击控件的宽度
    private int mTargetHeight;// 点击控件的高度
    private int mMinBetweenWidthAndHeight;
    private int mMaxBetweenWidthAndHeight;
    private int mMaxRevealRadius;
    private int mRevealRadiusGap;// 波纹扩散的间距
    private int mRevealRadius = 0;// 波纹半径
    private float mCenterX;// 点击事件的x坐标
    private float mCenterY;// 点击事件的y坐标
    private int[] mLocationInScreen = new int[2];// 本布局的位置坐标

    private boolean mShouldDoAnimation = false;// 标示是否需要绘制波纹
    private boolean mIsPressed = false;
    private int INVALIDATE_DURATION = 40;// 每次刷新的时间间隔

    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

    public JasonRevealLinearLayout(Context context) {
        super(context);
        init();
    }

    public JasonRevealLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public JasonRevealLinearLayout(Context context, AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint.setColor(getResources().getColor(R.color.jason_bg_common_green));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocationInScreen);
    }

    // 获取分发事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            View targetView = getTouchTarget(this, x, y);
            if (targetView.isClickable() && targetView.isEnabled()) {
                mTouchTarget = targetView;
                initParametersForChild(event, mTouchTarget);
                postInvalidateDelayed(INVALIDATE_DURATION);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            mIsPressed = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
            mDispatchUpTouchEventRunnable.event = event;
            postDelayed(mDispatchUpTouchEventRunnable, 400);
            return true;
        } else if (action == MotionEvent.ACTION_CANCEL) {
            mIsPressed = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
        }
        return super.dispatchTouchEvent(event);
    }

    // view绘制流程：先绘制背景，再绘制自己（onDraw），接着绘制子元素（dispatchDraw），最后绘制一些装饰等比如滚动条（onDrawScrollBars）
    // 为了防止绘制绘制的子元素把波纹挡住，这里选择在子元素绘制完成再绘制波纹
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mShouldDoAnimation || mTargetWidth < 0 || mTouchTarget == null) {
            return;
        }
        if (mRevealRadius > mMinBetweenWidthAndHeight / 2) {// 当半径超过短边之后，增加扩散速度尽快完成扩散
            mRevealRadius += mRevealRadiusGap * 4;
        } else {// 波纹当半径递增扩散
            mRevealRadius += mRevealRadiusGap;
        }
        this.getLocationOnScreen(mLocationInScreen);// 获取本布局的坐标
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);// 获取点击控件的坐标
        int left = location[0] - mLocationInScreen[0];
        int top = location[1] - mLocationInScreen[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();
        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        canvas.drawCircle(mCenterX, mCenterY, mRevealRadius, mPaint);
        canvas.restore();
        if (mRevealRadius <= mMaxRevealRadius) {
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        } else if (!mIsPressed) {
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    // 为了保证让波纹扩散完再去响应点击事件，这里把点击时间延迟了400毫秒
    // 这里的延时好像不对，对点击事件的延时应该是针对点击的控件，这里做的延时是针对的整个布局的点击事件延时，是在事件分发结束之后才调用的
    @Override
    public boolean performClick() {
        postDelayed(runnable, 400);
        return true;
    }

    Runnable runnable = new Runnable() {
        public void run() {
            // 400毫秒之后去执行真正的点击事件
            realPerformClick();
        }
    };

    /**
     * 对应控件的点击事件
     */
    private void realPerformClick() {
        super.performClick();
    }

    /**
     * 根据触摸点和点击控件，做绘制波纹的初始化准备工作
     *
     * @param event        点击事件，
     * @param mTouchTarget 被点击的控件
     */
    private void initParametersForChild(MotionEvent event, View mTouchTarget) {
        mCenterX = event.getX();
        mCenterY = event.getY();
        mTargetHeight = mTouchTarget.getHeight();
        mTargetWidth = mTouchTarget.getWidth();
        mMinBetweenWidthAndHeight = Math.min(mTargetHeight, mTargetWidth);
        mMaxBetweenWidthAndHeight = Math.max(mTargetHeight, mTargetWidth);
        mRevealRadius = 0;// 波纹半径从0开始
        mShouldDoAnimation = true;
        mIsPressed = true;
        mRevealRadiusGap = mMinBetweenWidthAndHeight / 8;// 通过8个波纹完成扩散

        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        int left = location[0] - mLocationInScreen[0];
        int transformedCenterX = (int) mCenterX - left;
        mMaxRevealRadius = Math.max(transformedCenterX, mTargetWidth
                - transformedCenterX);// 波纹的最大半径，应该为触摸点距控件两边点最大距离
    }

    /**
     * 获取当前布局中被点击的控件
     *
     * @param linearLayoutView
     * @param x                屏幕的绝对坐标x
     * @param y                屏幕的绝对坐标y
     *
     * @return 被点击的控件
     */
    private View getTouchTarget(View linearLayoutView, int x, int y) {
        View targetView = null;
        ArrayList<View> touchableViews = linearLayoutView.getTouchables();
        for (View child : touchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    /**
     * 判断点击事件是否落在该控件上
     *
     * @param child
     * @param x
     * @param y
     *
     * @return
     */
    private boolean isTouchPointInView(View child, int x, int y) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();
        if (child.isClickable() && y >= top && y <= bottom && x >= left
                && left <= right) {
            return true;
        }
        return false;
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        @Override
        public void run() {
            if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
                return;
            }

            if (isTouchPointInView(mTouchTarget, (int) event.getRawX(),
                    (int) event.getRawY())) {
                mTouchTarget.performClick();
            }
        }
    }

    ;
}