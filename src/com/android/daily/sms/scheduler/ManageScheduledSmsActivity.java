package com.android.daily.sms.scheduler;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.daily.sms.scheduler.beans.SMSNode;
import com.android.daily.sms.scheduler.service.SmsSenderService;

public class ManageScheduledSmsActivity extends Activity {

	private List<Integer> removeIdList = new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_scheduled_sms);
		SmsSenderService.setCallingActivity(this);
		
	    constructCheckedList();
	    Button removeButton = (Button)findViewById(R.id.removeButton);
	    removeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SmsSenderService.getRemoveIdList().addAll(removeIdList);
				constructCheckedList();
			}
		});
	    Button backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// switch to schedule activity
				Intent manageActivityIntent = new Intent(ManageScheduledSmsActivity.this, SmsSchedulerActivity.class);
				startActivity(manageActivityIntent);
			}
		});
		  Button killButton = (Button)findViewById(R.id.killButton);
		  killButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// kill the running thread
					SmsSenderService.setStopLoop(true);
				}
			});
	}

	private void constructCheckedList() {
		 LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout1);
		    ll.setOrientation(LinearLayout.VERTICAL);
		    ll.removeAllViews();
		    if( SmsSenderService.getSMSList().isEmpty()) {
		    	TextView showText = new TextView(this);
		    	showText.setText("No SMS scheduled!!");
		    	ll.addView(showText);
		    }
		    for(SMSNode node : SmsSenderService.getSMSList()) {
		    	 if(!removeIdList.contains(node.getId())) {
		    		 CheckBox ch = new CheckBox(this);
				ch.setText("Id:" + node.getId() 
						+ "\n isRepeated:"+ (node.isRepeat() ? "Yes" : "No")
						+ "\n To:"	+ node.getToNumber() 
						+ "\n On:" + node.getHours() + ":" + node.getMins() 
						+ "\n" + node.getContent());
			         ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							String entireText = buttonView.getText().toString();
							String[] splitString = entireText.split("\n");
							String idString = splitString[0].substring(splitString[0].indexOf(":")+1);
							int idToRemove = Integer.parseInt(idString);
							int existingIndex = removeIdList.indexOf(idToRemove);
							Log.i("constructCheckedList","id:"+idToRemove + " at Index" + existingIndex);
							if(isChecked) {
								if(existingIndex == -1) {
									removeIdList.add(idToRemove);
								}
							} else {
								if(existingIndex != -1) {
									removeIdList.remove(existingIndex);
								}						
							}
						}
					});
			         ll.addView(ch);
		    	 }
		    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_scheduled_sms, menu);
		return true;
	}

}
