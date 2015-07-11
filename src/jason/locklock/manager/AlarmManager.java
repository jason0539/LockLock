package jason.locklock.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * @author jason0539
 * 
 */
public class AlarmManager {
	private final String HeadSetState = "state";
	private boolean isAcOrUsbConnected = false;
	private AlarmWay curLockWay = AlarmWay.Stop;
	private Context mContext;

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝对外接口＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	public void setProtectWay(AlarmWay lockWay) {
		curLockWay = lockWay;
		if (curLockWay == AlarmWay.Stop) {
			VoiceManager.getInstance().stop();
		}
	}

	public void setHeadSetConnected() {
		switch (curLockWay) {
		case HeadSet:
			stop();
			break;
		case AcOrUsb:
		case Stop:
			break;
		}
	}

	public void headSetOut() {
		switch (curLockWay) {
		case HeadSet:
			alarm();
			break;
		case AcOrUsb:
		case Stop:
			break;
		}
	}

	public void setAcOrUsbConnected() {
		isAcOrUsbConnected = true;
		switch (curLockWay) {
		case AcOrUsb:
			stop();
			break;
		case HeadSet:
		case Stop:
			break;
		}
	}

	public void setAcOrUsbDisconnected() {
		isAcOrUsbConnected = false;
		switch (curLockWay) {
		case AcOrUsb:
			alarm();
			break;
		case HeadSet:
		case Stop:
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

	public boolean isAcOrUsbConnected() {
		return isAcOrUsbConnected;
	}

	public enum AlarmWay {
		Stop, AcOrUsb, HeadSet
	}

	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (arg1.hasExtra(BatteryManager.EXTRA_PLUGGED)) {
				int batteryOriginal = arg1.getIntExtra(
						BatteryManager.EXTRA_PLUGGED, 0);
				switch (batteryOriginal) {
				case 0:
					setAcOrUsbDisconnected();
					break;
				case BatteryManager.BATTERY_PLUGGED_AC:
				case BatteryManager.BATTERY_PLUGGED_USB:
				case BatteryManager.BATTERY_PLUGGED_WIRELESS:
					setAcOrUsbConnected();
					break;
				}
			} else if (arg1.hasExtra(HeadSetState)) {
				int state = arg1.getIntExtra(HeadSetState, 0);
				switch (state) {
				case 1:
					setHeadSetConnected();
					break;
				default:
					headSetOut();
					break;
				}
			}
		}
	};

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝格式化代码＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	public void init(Context context) {
		mContext = context;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(broadcastReceiver, filter);
		VoiceManager.getInstance().init(mContext);
		ShakeManager.getInstance().init(mContext);
	}

	public void unInit() {
		if (instance != null) {
			mContext.unregisterReceiver(broadcastReceiver);
			curLockWay = AlarmWay.Stop;
			VoiceManager.getInstance().unInit();
			ShakeManager.getInstance().unInit();
		}
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
