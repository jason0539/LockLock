package jason.locklock.listener;

import jason.locklock.manager.AlarmManager;
import jason.locklock.utils.JasonLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.Toast;

public class ProtectListener {
	private final String HeadSetState = "state";
	private boolean isAcOrUsbConnected = false;
	private Context mContext;
	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝内部实现＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 电源线相关
			if (arg1.hasExtra(BatteryManager.EXTRA_PLUGGED)) {
				int batteryOriginal = arg1.getIntExtra(
						BatteryManager.EXTRA_PLUGGED, 0);
				switch (batteryOriginal) {
				case 0:
					// log("电源线拔出");
					AlarmManager.getInstance().acOrUsbOut();
					isAcOrUsbConnected = false;
					break;
				case BatteryManager.BATTERY_PLUGGED_AC:
				case BatteryManager.BATTERY_PLUGGED_USB:
				case BatteryManager.BATTERY_PLUGGED_WIRELESS:
					// log("电源线插入");
					AlarmManager.getInstance().acOrUsbIn();
					isAcOrUsbConnected = true;
					break;
				}
			}
			// 耳机线相关
			if (arg1.hasExtra(HeadSetState)) {
				int state = arg1.getIntExtra(HeadSetState, 0);
				switch (state) {
				case 1:
					// log("耳机插入");
					AlarmManager.getInstance().headSetIn();
					break;

				default:
					// log("耳机拔出");
					AlarmManager.getInstance().headSetOut();
					break;
				}
			}

		}
	};

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝对外接口＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	/**
	 * 设置加锁方式
	 * 
	 * @param lockWay
	 */
	public void setProtectWay(LockWay lockWay) {
		AlarmManager.getInstance().setProtectWay(lockWay);
	}

	public boolean isAcOrUsbConnect() {
		return isAcOrUsbConnected;
	}

	public enum LockWay {
		None, AcOrUsb, HeadSet
	}

	private final void log(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		JasonLog.log(msg);
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝格式化代码＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	public void init(Context context) {
		this.mContext = context;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(broadcastReceiver, filter);
		AlarmManager.getInstance().init(context);// 初始化警报播放管理类
	}

	public void unInit() {
		if (instance != null) {
			mContext.unregisterReceiver(broadcastReceiver);
			AlarmManager.getInstance().unInit();
		}
	}

	private ProtectListener() {
	}

	private static ProtectListener instance = null;

	public static ProtectListener getInstance() {
		if (instance == null) {
			instance = new ProtectListener();
		}
		return instance;
	}
}
