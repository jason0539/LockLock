package jason.locklock.view;

import jason.locklock.R;
import jason.locklock.listener.ProtectListener;
import jason.locklock.listener.ProtectListener.LockWay;
import jason.locklock.manager.AlarmManager;
import jason.locklock.manager.VoiceManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChoiceButtonLayout extends LinearLayout {
	private JasonBreathTextView tv_state;
	private Button bn_ac;
	private Button bn_headset;
	private Button bn_none;
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
		bn_ac = (Button) findViewById(R.id.bn_choice_ac);
		bn_headset = (Button) findViewById(R.id.bn_choice_headset);
		bn_none = (Button) findViewById(R.id.bn_choice_none);
		bn_ac.setOnClickListener(getOnClickLis());
		bn_headset.setOnClickListener(getOnClickLis());
		bn_none.setOnClickListener(getOnClickLis());
	}

	private OnClickListener getOnClickLis() {

		return new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (arg0.getId()) {
				case R.id.bn_choice_ac:
					if (VoiceManager.getInstance().isAlarm()) {
						// 正在报警按键无效
						return;
					}
					if (ProtectListener.getInstance().isAcOrUsbConnect()) {
						ProtectListener.getInstance().setProtectWay(
								LockWay.AcOrUsb);
						tv_state.setText("正在使用电源线防盗");
						tv_state.startReveal();
					} else {
						Toast.makeText(mContext, "您没有插入电源线，请插入电源线重试",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.bn_choice_headset:
					if (VoiceManager.getInstance().isAlarm()) {
						// 正在报警按键无效
						return;
					}
					if (VoiceManager.getInstance().isHeadSetOn()) {
						ProtectListener.getInstance().setProtectWay(
								LockWay.HeadSet);
						tv_state.setText("正在使用耳机线防盗");
						tv_state.startReveal();
					} else {
						Toast.makeText(mContext, "您没有插入耳机线，请插入耳机线重试",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.bn_choice_none:
					AlarmManager.getInstance().acOrUsbIn();
					AlarmManager.getInstance().headSetIn();
					ProtectListener.getInstance().setProtectWay(LockWay.None);
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
