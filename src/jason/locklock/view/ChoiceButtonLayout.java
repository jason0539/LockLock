package jason.locklock.view;

import jason.locklock.R;
import jason.locklock.manager.AlarmManager;
import jason.locklock.manager.AlarmManager.AlarmWay;
import jason.locklock.manager.VoiceManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author jason0539
 */
public class ChoiceButtonLayout extends LinearLayout {
	private JasonBreathTextView tv_state;
	private Button btn_acAlarm;
	private Button btn_headsetAlarm;
	private Button btn_stopAlarm;
	private Context mContext;

	public ChoiceButtonLayout(Context context) {
		super(context);
	}

	public ChoiceButtonLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.button_choice, this);// 后面不用this则无法find里面的view
		tv_state = (JasonBreathTextView) findViewById(R.id.tv_choice_state);
		btn_acAlarm = (Button) findViewById(R.id.bn_choice_ac);
		btn_headsetAlarm = (Button) findViewById(R.id.bn_choice_headset);
		btn_stopAlarm = (Button) findViewById(R.id.bn_choice_stop);
		btn_acAlarm.setOnClickListener(getOnClickLis());
		btn_headsetAlarm.setOnClickListener(getOnClickLis());
		btn_stopAlarm.setOnClickListener(getOnClickLis());
	}

	private OnClickListener getOnClickLis() {

		return new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (arg0.getId()) {
				case R.id.bn_choice_ac:
					if (VoiceManager.getInstance().isAlarming()) {
						return;
					}
					if (AlarmManager.getInstance().isAcOrUsbConnected()) {
						AlarmManager.getInstance().setProtectWay(
								AlarmWay.AcOrUsb);
						tv_state.setText("正在使用电源线防盗");
						tv_state.startReveal();
					} else {
						Toast.makeText(mContext, "您没有插入电源线，请插入电源线重试",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.bn_choice_headset:
					if (VoiceManager.getInstance().isAlarming()) {
						return;
					}
					if (VoiceManager.getInstance().isHeadSetConnected()) {
						AlarmManager.getInstance().setProtectWay(
								AlarmWay.HeadSet);
						tv_state.setText("正在使用耳机线防盗");
						tv_state.startReveal();
					} else {
						Toast.makeText(mContext, "您没有插入耳机线，请插入耳机线重试",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.bn_choice_stop:
					AlarmManager.getInstance().setAcOrUsbConnected();
					AlarmManager.getInstance().setHeadSetConnected();
					AlarmManager.getInstance().setProtectWay(AlarmWay.Stop);
					tv_state.setText("已经停止防盗设置");
					tv_state.stopReveal();
					break;
				default:
					break;
				}
			}
		};
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝水纹特效代码＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

}
