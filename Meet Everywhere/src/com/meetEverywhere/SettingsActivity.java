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
	private CheckBox bluetoothCheckBox;
	private CheckBox gpsCheckBox;
	private int secondsBetweenRefreshingBluetooth;
	private int searchingRadiusInKilometres;
	private int percentageOfIdenticalTags;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Configuration configuration = Configuration.getInstance();
		
		bluetoothSearchingFrequencyBar = (SeekBar)findViewById(R.id.SettingsActivity_bluetoothCustomizeDeviceSearchingFrequencyBar);
		gpsSearchingRadiusBar = (SeekBar)findViewById(R.id.SettingsActivity_gpsCustomizeDeviceSearchingRadiusBar);
		percentageOfIdenticalTagsBar = (SeekBar)findViewById(R.id.SettingsActivity_setPercentageOfTagsToBeIdenticalBar);
		
		bluetoothSearchingFrequencyInfo = (TextView)findViewById(R.id.SettingsActivity_bluetoothDeviceSearchigFrequencyInformation);
		gpsSearchingRadiusInfo = (TextView)findViewById(R.id.SettingsActivity_gpsDeviceSearchingRadiusInformation);
		
		bluetoothSearchingFrequencyBar.setEnabled(configuration.isBluetoothUsed());
		gpsSearchingRadiusBar.setEnabled(configuration.isGPSUsed());
		
		bluetoothCheckBox = (CheckBox)findViewById(R.id.SettingsActivity_bluetoothCheckBox);
		gpsCheckBox = (CheckBox)findViewById(R.id.SettingsActivity_gpsCheckBox);
		
		bluetoothCheckBox.setChecked(configuration.isBluetoothUsed());
		gpsCheckBox.setChecked(configuration.isGPSUsed());
		
		secondsBetweenRefreshingBluetooth = Configuration.getInstance().getBluetoothSecsTimeBetweenRefreshing();
		searchingRadiusInKilometres = (int) Configuration.getInstance().getGpsScanningRadiusInKilometres();
		
		if(bluetoothCheckBox.isChecked()) {
		}
		
		bluetoothCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					bluetoothSearchingFrequencyBar.setEnabled(true);
					bluetoothSearchingFrequencyBar.setProgress(secondsBetweenRefreshingBluetooth);
					
					bluetoothSearchingFrequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
						public void onStopTrackingTouch(SeekBar arg0) {
							bluetoothSearchingFrequencyBar.setSecondaryProgress(bluetoothSearchingFrequencyBar.getProgress());
						}
						
						public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
							secondsBetweenRefreshingBluetooth = progress;
							
							StringBuilder textForSearchingFrequencyInfo = new StringBuilder();
							textForSearchingFrequencyInfo.append(getText(R.string.SettingActivity_bluetoothDeviceSearchigFrequencyInformationText))
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
						
							bluetoothSearchingFrequencyInfo.setText(textForSearchingFrequencyInfo);
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
		
		gpsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					gpsSearchingRadiusBar.setEnabled(true);

					gpsSearchingRadiusBar.setProgress(searchingRadiusInKilometres);
					
					gpsSearchingRadiusBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
						public void onStopTrackingTouch(SeekBar seekBar) {
							gpsSearchingRadiusBar.setSecondaryProgress(gpsSearchingRadiusBar.getProgress());
						}
						
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							searchingRadiusInKilometres = progress;
							
							StringBuilder textForSearchingRadiusInfo = new StringBuilder();
							textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_gpsDeviceSearchingRadiusInformationText))
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
					
				}
				else {
					gpsSearchingRadiusBar.setEnabled(false);
					gpsSearchingRadiusInfo.setText(R.string.SettingsActivity_notEnabled);
				}
			}
		});
		
		percentageOfIdenticalTags = Configuration.getInstance().getDesiredTagsCompatibility();
		percentageOfIdenticalTagsBar.setProgress(percentageOfIdenticalTags);
		
		percentageOfIdenticalTagsBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				percentageOfIdenticalTagsBar.setSecondaryProgress(percentageOfIdenticalTagsBar.getProgress());
			}
			
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				percentageOfIdenticalTags = progress;
				
				StringBuilder textForChosenPercentageOfIdenticalTagsInfo = new StringBuilder();
				String currentText = getText(R.string.SettingsActivity_percentageOfIdenticaTagsInformationText).toString();
				textForChosenPercentageOfIdenticalTagsInfo.append(currentText .subSequence(currentText.length()-2, currentText.length()))
														  .append(percentageOfIdenticalTags)
														  .append("%");
														  
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
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

