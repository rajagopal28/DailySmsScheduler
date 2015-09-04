package com.android.daily.sms.scheduler;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.daily.sms.scheduler.beans.SMSNode;
import com.android.daily.sms.scheduler.service.SmsSenderService;
import com.android.daily.sms.scheduler.util.ContactsListUtil;

public class SmsSchedulerActivity extends Activity {

	private List<String> contactslist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_scheduler);
		SmsSenderService.setCallingActivity(this);
		if (contactslist == null) {
			// populate list in format ContactName<number>
			contactslist = ContactsListUtil.getAllContactsAsStringList(this);
		}
		// Creating the instance of ArrayAdapter containing list of language
		// names
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, contactslist);
		// read the contacts and create a list
		AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		actv.setThreshold(1);// will start working from first character
		actv.setAdapter(adapter);// setting the adapter data into the
									// AutoCompleteTextView
		actv.setTextColor(Color.RED);

		Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
		scheduleButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TimePicker scheduledTimePicker = (TimePicker) findViewById(R.id.scheduledTimePicker);
				int chosenHour = scheduledTimePicker.getCurrentHour();
				int chosenMinute = scheduledTimePicker.getCurrentMinute();

				AutoCompleteTextView contactView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
				String contactValue = contactView.getText().toString();

				EditText contentTextView = (EditText) findViewById(R.id.contentView);
				String content = contentTextView.getText().toString();
				if (contactValue.trim().length() > 0
						&& content.trim().length() > 0) {
					int startOfNumber = contactValue.indexOf("<");
					int endOfNumber = contactValue.indexOf(">");
					String toNumber = contactValue.substring(startOfNumber + 1,
							endOfNumber);
					toNumber = toNumber.replace("-", "");
					CheckBox isRepeatBox = (CheckBox) findViewById(R.id.checkBox1);

					SMSNode newSMS = new SMSNode();
					newSMS.setHours(chosenHour);
					newSMS.setMins(chosenMinute);
					newSMS.setToNumber(toNumber);
					newSMS.setId(SmsSenderService.getCurrentNodeId());
					newSMS.setRepeat(isRepeatBox.isChecked());
					newSMS.setContent(content);
					SmsSenderService.getSMSList().add(newSMS);
					// Enable thread if not active
					if (SmsSenderService.getStopLoop()) {
						SmsSenderService.setStopLoop(false);
						Handler handler = new Handler();
						
						SmsSenderService myThread = new SmsSenderService(handler);
						handler.postDelayed(myThread, 500);
					}
					Log.i("SmsSchedulerActivity",
							"Added Node : " + newSMS.toString());
					Toast.makeText(getBaseContext(), "Scheduled!!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "Fields Empty!!!",
							Toast.LENGTH_SHORT).show();
				}// end else

			}
		});

		Button manageScheduledButton = (Button) findViewById(R.id.manageButton);
		manageScheduledButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// switch to manage activity
				Intent manageActivityIntent = new Intent(
						SmsSchedulerActivity.this,
						ManageScheduledSmsActivity.class);
				startActivity(manageActivityIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms_scheduler, menu);
		return true;
	}

}
