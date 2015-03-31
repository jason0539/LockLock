package jason.locklock.manager;

import android.content.Context;
import android.os.Vibrator;

public class ShakeManager {

	private Vibrator vibrator;

	public void init(Context context) {
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void unInit() {
		vibrator = null;
	}

	public void shake() {
		long[] pattern = { 100, 1000 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, 0); // 重复上面的pattern
	}

	public void stop() {
		vibrator.cancel();
	}

	private static ShakeManager instance;

	private ShakeManager() {

	}

	public static ShakeManager getInstance() {
		if (instance == null) {
			instance = new ShakeManager();
		}
		return instance;
	}
}
