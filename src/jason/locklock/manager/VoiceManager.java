package jason.locklock.manager;

import jason.locklock.R;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class VoiceManager {
	private MediaPlayer mediaPlayer = null;
	private Context mContext = null;
	private boolean isAlarm = false;
	private AudioManager audioManager = null;
	private int maxVolumn;// 最大音量
	private Timer timer;
	private int currentVolumn;

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝对外接口＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	/**
	 * 用最大媒体音量播放警报
	 */
	public void play() {
		if (isAlarm) {
			return;
		}
		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			mediaPlayer.setOnPreparedListener(preparedListener);
			mediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		currentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		ensureMaxVolume();
	}

	/**
	 * 停止播放警报并恢复音量
	 */
	public void stop() {
		if (!isAlarm) {
			return;
		}
		isAlarm = false;
		mediaPlayer.stop();
		timer.cancel();
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn,
				0);
	}

	/**
	 * 是否正在播放警报
	 */
	public boolean isAlarm() {
		return isAlarm;
	}

	/**
	 * 耳机是否插入
	 */
	public boolean isHeadSetOn() {
		return audioManager.isWiredHeadsetOn();
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝内部实现＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	private final OnPreparedListener preparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					ensureMaxVolume();
				}
			}, 0, 100);
			isAlarm = true;
		}
	};

	private void ensureMaxVolume() {
		if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < maxVolumn) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolumn,
					0);
		}
	}

	// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝格式化代码＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	public void init(Context context) {
		mContext = context;
		mediaPlayer = MediaPlayer.create(context, R.raw.notice);
		mediaPlayer.setLooping(true);
		audioManager = (AudioManager) mContext
				.getSystemService(mContext.AUDIO_SERVICE);
		maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	public void unInit() {
		stop();
		mediaPlayer.reset();
		mediaPlayer.release();
	}

	private static VoiceManager instance = null;

	public static VoiceManager getInstance() {
		if (instance == null) {
			instance = new VoiceManager();
		}
		return instance;
	}

	private VoiceManager() {

	}
}
