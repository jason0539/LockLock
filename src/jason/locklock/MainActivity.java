package jason.locklock;

import jason.locklock.listener.ProtectListener;
import jason.locklock.view.JasonDialog;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainActivity extends Activity {
	private JasonDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		dialog = new JasonDialog(this);
		ProtectListener.getInstance().init(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialog.dismiss();
		ProtectListener.getInstance().unInit();
	}

	@Override
	public void onBackPressed() {
		dialog.setTitle("退出应用").setMessage("如果退出应用，将不再进行防盗监控，建议通过home键将应用放到后台")
				.setRightText("退出")
				.setRightOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MainActivity.this.finish();
					}
				}).setCancleAble(false).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("关于")) {
			new JasonDialog(MainActivity.this).setTitle("关于")
					.setMessage("\n" + "版本:1.0" + "\n" + "作者:jason0539"+ "\n")
					.setLeftVisible(false).show();
		} else if (item.getTitle().equals("说明")) {
			new JasonDialog(MainActivity.this)
					.setTitle("说明")
					.setMessage(
							"\n" + "电源线:插入USB线,直流电或者usb供电,只要断电就会报警。" + "\n"
									+ "\n" + "耳机线:插入耳机，耳机拔出报警"+ "\n")
					.setLeftVisible(false).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, "说明");
		menu.add(1, 2, 1, "关于");
		return true;
	}
}
