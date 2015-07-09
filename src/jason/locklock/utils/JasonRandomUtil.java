package jason.locklock.utils;

import java.util.Random;

public class JasonRandomUtil {
	/**
	 * 产生随机数，介于min和max之间
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}
}
