package jason.locklock.view;

import jason.locklock.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class JasonDialog {
	private Dialog dialog;
	private TextView tv_title;
	private TextView tv_message;
	private TextView tv_left;
	private TextView tv_right;

	public JasonDialog(Context context) {
		dialog = new Dialog(context);
		Window window = dialog.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setContentView(R.layout.jason_dialog_main);
		findViews(window);
	}

	public JasonDialog setCancleAble(boolean cancle) {
		dialog.setCancelable(cancle);
		return this;
	}

	public JasonDialog setTitle(String string) {
		if (null == string || TextUtils.isEmpty(string)) {
			return this;
		}
		tv_title.setText(string);
		return this;
	}

	public JasonDialog setMessage(String string) {
		if (null == string || TextUtils.isEmpty(string)) {
			return this;
		}
		tv_message.setText(string);
		return this;
	}

	public JasonDialog setLeftText(String string) {
		if (null == string || TextUtils.isEmpty(string)) {
			return this;
		}
		tv_left.setText(string);
		return this;
	}

	public JasonDialog setLeftOnClickListener(OnClickListener clickListener) {
		if (clickListener == null) {
			return this;
		}
		tv_left.setOnClickListener(clickListener);
		return this;
	}

	public JasonDialog setLeftVisible(boolean visible) {
		tv_left.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public JasonDialog setRightVisible(boolean visible) {
		tv_right.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public JasonDialog setRightText(String string) {
		if (null == string || TextUtils.isEmpty(string)) {
			return this;
		}
		tv_right.setText(string);
		return this;
	}

	public JasonDialog setRightOnClickListener(OnClickListener clickListener) {
		if (null == clickListener) {
			return this;
		}
		tv_right.setOnClickListener(clickListener);
		return this;
	}

	public void show() {
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	public void dismiss() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public boolean isShow() {
		return dialog.isShowing();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		dialog.setOnDismissListener(listener);
	}

	private void findViews(Window window) {
		tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
		tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
		tv_left = (TextView) window.findViewById(R.id.tv_dialog_left);
		tv_right = (TextView) window.findViewById(R.id.tv_dialog_right);
		tv_left.setOnClickListener(onDismissClickListener);
		tv_right.setOnClickListener(onDismissClickListener);
	}

	private final OnClickListener onDismissClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};
}
