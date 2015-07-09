package jason.locklock.utils;

public class JasonRadiusUtil {
	/**
	 * 给定矩形的宽高和当前圆心在上面的相对坐标，返回能够覆盖整个矩形的最小圆的半径
	 * 
	 * @param curX
	 * @param curY
	 * @param height
	 * @param width
	 * @return
	 */
	public static int getMaxRadius(int curX, int curY, int width, int height) {
		// 以矩形左上角为原点，则左边和上边坐标都为0，右边和底边坐标分别为宽高
		return getRadius(curX, curY, 0, 0, width, height);
	}

	/**
	 * 给定圆心坐标和矩形四条边的坐标，计算出在当前圆心下能够覆盖整个矩形的最小圆的半径
	 * 
	 * @param curX圆心x
	 * @param curY圆心y
	 * @param left左边
	 * @param top顶边
	 * @param right右边
	 * @param bottom底边
	 * @return
	 */
	private static int getRadius(int curX, int curY, int mLeft, int mTop,
			int mRight, int mBottom) {
		int result = -1;
		int centerX = (mRight + mLeft) / 2;
		int centerY = (mBottom + mTop) / 2;
		if (curX <= centerX && curY <= centerY) {// 左上角
			result = (int) Math.hypot(mRight - curX, mBottom - curY);
		} else if (curX >= centerX && curY <= centerY) {// 右上角
			result = (int) Math.hypot(curX - mLeft, mBottom - curY);
		} else if (curX <= centerX && curY >= centerY) {// 左下角
			result = (int) Math.hypot(curY - mTop, mRight - curX);
		} else if (curX >= centerX && curY >= centerY) {// 右下角
			result = (int) Math.hypot(curX - mLeft, curY - mTop);
		}
		return result;
	}

	// /**
	// * 计算此时的最大半径，扩散到控件边上就可以了
	// */
	// private void countMaxRadio() {
	// if (viewWidth > viewHeight) {
	// if (eventX < viewWidth / 2) {
	// maxRadius = viewWidth - eventX;
	// } else {
	// maxRadius = viewWidth / 2 + eventX;
	// }
	// } else {
	// if (eventY < viewHeight / 2) {
	// maxRadius = viewHeight - eventY;
	// } else {
	// maxRadius = viewHeight / 2 + eventY;
	// }
	// }
	// }
}
