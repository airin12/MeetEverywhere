package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.meetEverywhere.common.Configuration;

public class SettingsActivity extends Activity {
	
	private SeekBar bluetoothSearchingFrequencyBar;
	private SeekBar gpsSearchingRadiusBar;
	private SeekBar percentageOfIdenticalTagsBar;
	private TextView bluetoothSearchingFrequencyInfo;
	private TextView gpsSearchingRadiusInfo;
	private int secondsBetweenRefreshingBluetooth;
	private int searchingRadiusInKilometres;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		bluetoothSearchingFrequencyBar = (SeekBar)findViewById(R.id.SettingsActivity_bluetoothCustomizeDeviceSearchingFrequencyBar);
		gpsSearchingRadiusBar = (SeekBar)findViewById(R.id.SettingsActivity_gpsCustomizeDeviceSearchingRadiusBar);
		
		bluetoothSearchingFrequencyInfo = (TextView)findViewById(R.id.SettingsActivity_bluetoothDeviceSearchigFrequencyInformation);
		
		((CheckBox)findViewById(R.id.SettingsActivity_bluetoothCheckBox)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					bluetoothSearchingFrequencyBar.setEnabled(true);
					secondsBetweenRefreshingBluetooth = Configuration.getInstance().getBluetoothSecsTimeBetweenRefreshing();
					bluetoothSearchingFrequencyBar.setProgress(secondsBetweenRefreshingBluetooth);
					
					bluetoothSearchingFrequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
						public void onStopTrackingTouch(SeekBar arg0) {
							bluetoothSearchingFrequencyBar.setSecondaryProgress(bluetoothSearchingFrequencyBar.getProgress());
						}
						
						public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
							secondsBetweenRefreshingBluetooth = progress;
							
							StringBuilder textForSearchingFrequencyInfo = new StringBuilder();
							textForSearchingFrequencyInfo.append(R.string.SettingActivity_bluetoothDeviceSearchigFrequencyInformationText)
														 .append(" ")
														 .append(progress)
														 .append(" ");					
							if(progress == 0 || progress >= 5) {
								textForSearchingFrequencyInfo.append(getText(R.string.SettingsActivity_zeroOrFiveOrMoreSecondsText));
							}
							else if(progress == 1) {
								textForSearchingFrequencyInfo.append(getText(R.string.SettingActivity_oneSecondText));
							}
							else {
								textForSearchingFrequencyInfo.append(getText(R.string.SettingsActivity_twoToFourSecondsText));
							}	
						}
						
						public void onStartTrackingTouch(SeekBar arg0) {
						}
					
					});
				}
				else {
					bluetoothSearchingFrequencyBar.setEnabled(false);
					bluetoothSearchingFrequencyInfo.setText(R.string.SettingsActivity_notEnabled);
				}
			}
		});
		
		((CheckBox)findViewById(R.id.SettingsActivity_gpsCheckBox)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					gpsSearchingRadiusBar.setEnabled(true);
					gpsSearchingRadiusBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
						public void onStopTrackingTouch(SeekBar seekBar) {
							gpsSearchingRadiusBar.setSecondaryProgress(gpsSearchingRadiusBar.getProgress());
						}
						
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							searchingRadiusInKilometres = progress;
							
							StringBuilder textForSearchingRadiusInfo = new StringBuilder();
							textForSearchingRadiusInfo.append(R.string.SettingsActivity_gpsDeviceSearchingRadiusInformationText)
													  .append(" ")
													  .append(searchingRadiusInKilometres)
													  .append(" ");
							if(searchingRadiusInKilometres == 0 || searchingRadiusInKilometres > 2) {
								textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_zeroOrtwoOrMoreKilometers));
							}
							else {
								textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_oneKilometer));
							}
						}
						
						public void onStartTrackingTouch(SeekBar seekBar) {
						}
					});
					
					
					searchingRadiusInKilometres = gpsSearchingRadiusBar.getProgress();
					
					StringBuilder textForSearchingRadiusInfo = new StringBuilder();
					textForSearchingRadiusInfo.append(R.string.SettingsActivity_gpsDeviceSearchingRadiusInformationText)
											  .append(" ")
											  .append(searchingRadiusInKilometres)
											  .append(" ");
					if(searchingRadiusInKilometres == 0 || searchingRadiusInKilometres > 2) {
						textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_zeroOrtwoOrMoreKilometers));
					}
					else {
						textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_oneKilometer));
					}
				}
				else {
					gpsSearchingRadiusBar.setEnabled(false);
					gpsSearchingRadiusInfo.setText(R.string.SettingsActivity_notEnabled);
				}
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void goToBlockedContactsActivityAction(View view) {
		startActivity(new Intent(SettingsActivity.this, BlockedContactsActivity.class));
	}
	
	public void goToFriendsListActivity(View view) {
		startActivity(new Intent(SettingsActivity.this, FriendsListFragment.class));
	}
	
	public void saveSettingsAndGoBackAction(View view) {
		//Configuration.getInstance().setBluetoothMillisTimeBetweenRefreshing(bluetoothMillisTimeBetweenRefreshing)
	}

}
