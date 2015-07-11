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

/**
 * @author jason0539
 * 
 */
public class JasonDialog {
	private Dialog dialog;
	private TextView tv_title;
	private TextView tv_message;
	private TextView tv_leftButton;
	private TextView tv_rightButton;

	public JasonDialog(Context context) {
		dialog = new Dialog(context);
		Window window = dialog.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setContentView(R.layout.jason_dialog_main);
		findDialogViews(window);
	}

	private void findDialogViews(Window window) {
		tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
		tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
		tv_leftButton = (TextView) window.findViewById(R.id.tv_dialog_left);
		tv_rightButton = (TextView) window.findViewById(R.id.tv_dialog_right);
		tv_leftButton.setOnClickListener(onDismissClickListener);
		tv_rightButton.setOnClickListener(onDismissClickListener);
	}

	private final OnClickListener onDismissClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public JasonDialog setCancelable(boolean cancle) {
		dialog.setCancelable(cancle);
		return this;
	}

	public JasonDialog setTitle(String string) {
		if (TextUtils.isEmpty(string)) {
			return this;
		}
		tv_title.setText(string);
		return this;
	}

	public JasonDialog setContentText(String string) {
		if (TextUtils.isEmpty(string)) {
			return this;
		}
		tv_message.setText(string);
		return this;
	}

	public JasonDialog setLeftButtonText(String string) {
		if (TextUtils.isEmpty(string)) {
			return this;
		}
		tv_leftButton.setText(string);
		return this;
	}

	public JasonDialog setLeftButtonOnClickListener(
			OnClickListener clickListener) {
		if (clickListener == null) {
			return this;
		}
		tv_leftButton.setOnClickListener(clickListener);
		return this;
	}

	public JasonDialog setLeftButtonVisibility(boolean visible) {
		tv_leftButton.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public JasonDialog setRightButtonVisibility(boolean visible) {
		tv_rightButton.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public JasonDialog setRightButtonText(String string) {
		if (null == string || TextUtils.isEmpty(string)) {
			return this;
		}
		tv_rightButton.setText(string);
		return this;
	}

	public JasonDialog setRightButtonOnClickListener(
			OnClickListener clickListener) {
		if (null == clickListener) {
			return this;
		}
		tv_rightButton.setOnClickListener(clickListener);
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

	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		dialog.setOnDismissListener(listener);
	}

}
