package com.android.daily.sms.scheduler.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsListUtil {

	public static List<String> getAllContactsAsStringList(
			Activity callingActivity) {
		List<String> allContactsStringList = new ArrayList<String>();
		Cursor phones =	callingActivity.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			String contact = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String number = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			allContactsStringList.add(contact+"<"+number+">");
			Log.i("ReadContacts", contact+"<"+number+">");
		}
		return allContactsStringList;
	}
}
