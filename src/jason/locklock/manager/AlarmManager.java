package jason.locklock.manager;

import android.content.Context;
import jason.locklock.listener.ProtectListener.LockWay;

/**
 * 根据当前的防盗模式进行报警
 * 
 * @author liuzhenhui
 * 
 */
public class AlarmManager {
	private LockWay curLockWay = LockWay.None;
	private Context mContext = null;

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝对外接口＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	public void setProtectWay(LockWay lockWay) {
		curLockWay = lockWay;
		if (curLockWay == LockWay.None && VoiceManager.getInstance().isAlarm()) {
			VoiceManager.getInstance().stop();
		}
	}

	public void headSetIn() {
		switch (curLockWay) {
		case HeadSet:
			stop();
			break;
		case AcOrUsb:
		case None:
			// do nothing
			break;
		}
	}

	public void headSetOut() {
		switch (curLockWay) {
		case HeadSet:
			alarm();
			break;
		case AcOrUsb:
		case None:
			// do nothing
			break;
		}
	}

	public void acOrUsbIn() {
		switch (curLockWay) {
		case AcOrUsb:
			stop();
			break;
		case HeadSet:
		case None:
			// do nothing
			break;
		}
	}

	public void acOrUsbOut() {
		switch (curLockWay) {
		case AcOrUsb:
			alarm();
			break;
		case HeadSet:
		case None:
			// do nothing
			break;
		}
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝内部实现＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	private void alarm() {
		VoiceManager.getInstance().play();
		ShakeManager.getInstance().shake();
	}

	private void stop() {
		VoiceManager.getInstance().stop();
		ShakeManager.getInstance().stop();
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝格式化代码＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

	public void init(Context context) {
		mContext = context;
		VoiceManager.getInstance().init(mContext);
		ShakeManager.getInstance().init(mContext);
	}

	// 退出时将资源全部释放，同时把防盗方式取消
	public void unInit() {
		mContext = null;
		curLockWay = LockWay.None;
		VoiceManager.getInstance().unInit();
		ShakeManager.getInstance().unInit();
	}

	private AlarmManager() {
	}

	private static AlarmManager instance = null;

	public static AlarmManager getInstance() {
		if (instance == null) {
			instance = new AlarmManager();
		}
		return instance;
	}

}
