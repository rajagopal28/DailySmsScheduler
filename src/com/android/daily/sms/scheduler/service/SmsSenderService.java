package com.android.daily.sms.scheduler.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.android.daily.sms.scheduler.beans.SMSNode;
import com.android.daily.sms.scheduler.util.SmsSenderUtil;

public class SmsSenderService extends Thread {
	private static List<SMSNode> activeSMSList = new ArrayList<SMSNode>();
	private static List<Integer> removeIdList = new ArrayList<Integer>();
	private static List<SMSNode> inActiveSMSList = new ArrayList<SMSNode>();
	private static Activity callingActivity;
	private static boolean stopLoop = true;
	private static int currentNodeId = 0;
	private Handler handler = null;

	public SmsSenderService(Handler handler) {
		this.handler = handler;
	}

	public static void setCallingActivity(Activity callerActivity) {
		callingActivity = callerActivity;
	}

	public static synchronized List<SMSNode> getSMSList() {
		return activeSMSList;
	}

	public static void setStopLoop(boolean stopLoopArg) {
		stopLoop = stopLoopArg;
	}

	public static boolean getStopLoop() {
		return stopLoop;
	}

	public static int getCurrentNodeId() {
		return currentNodeId++;
	}

	@Override
	public void run() {
		if (!stopLoop) {
			Log.i("SmsSenderService", "List length : " + activeSMSList.size());
			for (SMSNode node : activeSMSList) {
				if (getRemoveIdList().contains(node.getId())) {
					removeIdList.remove((Integer) node.getId());
					if (!inActiveSMSList.contains(node)) {
						inActiveSMSList.add(node);
					}
				} else {
					Calendar currentTime = GregorianCalendar.getInstance();
					if (node.getHours() == currentTime
							.get(Calendar.HOUR_OF_DAY)
							&& node.getMins() == currentTime
									.get(Calendar.MINUTE)) {
						if (!inActiveSMSList.contains(node) && isNotRepeatedOnSameMinute(node.getLastSentTimeStamp())) {
							SmsSenderUtil.sendSMS(node, callingActivity);
							node.setLastSentTimeStamp(new Date());
							// remove if the sms is not repeat
							if (!node.isRepeat()) {
								inActiveSMSList.add(node);
							}
						}
					}// else end
				}// end not in list
			}// end for
			try {
				sleep(500);
			} catch (InterruptedException e) {
				Log.e("SmsSenderService", e.getMessage());
			}
			// remove those which are marked inactive
			if (!inActiveSMSList.isEmpty()) {
				activeSMSList.removeAll(inActiveSMSList);
			}
			handler.postDelayed(this, 500);
		}
	}

	private boolean isNotRepeatedOnSameMinute(Date lastSentTimeStamp) {
		boolean isNotInSameMinute = true;
		if(lastSentTimeStamp != null) {
			long currentTimeInMills = new Date().getTime();
			isNotInSameMinute = ((currentTimeInMills - lastSentTimeStamp.getTime())/1000)>=60;// divide by 1000 --> seconds .60seconds is the minute wait
		}
		return isNotInSameMinute;
	}

	public static synchronized List<Integer> getRemoveIdList() {
		return removeIdList;
	}
}
