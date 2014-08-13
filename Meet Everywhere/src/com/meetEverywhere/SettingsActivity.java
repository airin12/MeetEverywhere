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
import com.meetEverywhere.database.AccountsDAO;

public class SettingsActivity extends Activity {

	private Configuration configuration = Configuration.getInstance();
	private SeekBar bluetoothSearchingFrequencyBar;
	private SeekBar gpsSearchingRadiusBar;
	private SeekBar percentageOfIdenticalTagsBar;
	private TextView bluetoothSearchingFrequencyInfo;
	private TextView gpsSearchingRadiusInfo;
	private TextView percentageOfIdenticalTagsProgressInfo;
	private CheckBox bluetoothCheckBox;
	private CheckBox gpsCheckBox;
	private int secondsBetweenRefreshingBluetooth;
	private int searchingRadiusInKilometres;
	private int percentageOfIdenticalTags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		bluetoothSearchingFrequencyInfo = (TextView) findViewById(R.id.SettingsActivity_bluetoothDeviceSearchigFrequencyInformation);
		secondsBetweenRefreshingBluetooth = configuration.getBluetoothSecsTimeBetweenRefreshing();

		bluetoothSearchingFrequencyBar = (SeekBar) findViewById(R.id.SettingsActivity_bluetoothCustomizeDeviceSearchingFrequencyBar);
		bluetoothSearchingFrequencyBar.setEnabled(configuration.isBluetoothUsed());
		bluetoothSearchingFrequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onStopTrackingTouch(SeekBar arg0) {
						bluetoothSearchingFrequencyBar.setSecondaryProgress(bluetoothSearchingFrequencyBar.getProgress());
					}

					public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
						secondsBetweenRefreshingBluetooth = progress;
						StringBuilder textForSearchingFrequencyInfo = createBluetoothSearchingFrequencyInfo(progress);
						bluetoothSearchingFrequencyInfo.setText(textForSearchingFrequencyInfo);
						if(progress == 0) {
							bluetoothSearchingFrequencyBar.setEnabled(false);
							bluetoothCheckBox.setChecked(false);
						}

					}

					public void onStartTrackingTouch(SeekBar arg0) { }
				});
		
		bluetoothCheckBox = (CheckBox) findViewById(R.id.SettingsActivity_bluetoothCheckBox);
		bluetoothCheckBox.setChecked(configuration.isBluetoothUsed());
		
		if (bluetoothCheckBox.isChecked()) {
			StringBuilder textForSearchingFrequencyInfo = createBluetoothSearchingFrequencyInfo(secondsBetweenRefreshingBluetooth);
			bluetoothSearchingFrequencyInfo.setText(textForSearchingFrequencyInfo);
		}
		
		bluetoothCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					bluetoothSearchingFrequencyBar.setEnabled(true);
					bluetoothSearchingFrequencyBar.setProgress(secondsBetweenRefreshingBluetooth);
					StringBuilder textForSearchingFrequencyInfo = createBluetoothSearchingFrequencyInfo(secondsBetweenRefreshingBluetooth);
					bluetoothSearchingFrequencyInfo.setText(textForSearchingFrequencyInfo);

				} else {
					bluetoothSearchingFrequencyBar.setEnabled(false);
					bluetoothSearchingFrequencyInfo.setText(R.string.SettingsActivity_notEnabled);
				}
			}
		});
		


		searchingRadiusInKilometres = (int) configuration.getGpsScanningRadiusInKilometres();
		gpsSearchingRadiusInfo = (TextView) findViewById(R.id.SettingsActivity_gpsDeviceSearchingRadiusInformation);
		
		gpsSearchingRadiusBar = (SeekBar) findViewById(R.id.SettingsActivity_gpsCustomizeDeviceSearchingRadiusBar);
		gpsSearchingRadiusBar.setEnabled(configuration.isGPSUsed());
		gpsSearchingRadiusBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				gpsSearchingRadiusBar.setSecondaryProgress(gpsSearchingRadiusBar.getProgress());
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				searchingRadiusInKilometres = progress;
				StringBuilder textForSearchingRadiusInfo = createGpsSearchingRadiusInfo(progress);
				gpsSearchingRadiusInfo.setText(textForSearchingRadiusInfo);
				if(progress == 0) {
					gpsSearchingRadiusBar.setEnabled(false);
					gpsCheckBox.setChecked(false);
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {	}
		});

		gpsCheckBox = (CheckBox) findViewById(R.id.SettingsActivity_gpsCheckBox);
		gpsCheckBox.setChecked(configuration.isGPSUsed());
		
		if (gpsCheckBox.isChecked()) {
			StringBuilder textForSearchingRadiusInfo = createGpsSearchingRadiusInfo(searchingRadiusInKilometres);
			gpsSearchingRadiusInfo.setText(textForSearchingRadiusInfo);
		}
		
		gpsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					gpsSearchingRadiusBar.setEnabled(true);
					gpsSearchingRadiusBar.setProgress(searchingRadiusInKilometres);
					StringBuilder textForSearchingRadiusInfo = createGpsSearchingRadiusInfo(searchingRadiusInKilometres);
					gpsSearchingRadiusInfo.setText(textForSearchingRadiusInfo);
				} else {
					gpsSearchingRadiusBar.setEnabled(false);
					gpsSearchingRadiusInfo.setText(R.string.SettingsActivity_notEnabled);
				}
			}
		});
		
		
		
		percentageOfIdenticalTagsProgressInfo = (TextView) findViewById(R.id.SettingsActivity_percentageOfTagsToBeIdenticalInformation);
		percentageOfIdenticalTags = configuration.getDesiredTagsCompatibility();
		
		StringBuilder textForChosenPercentageOfIdenticalTagsInfo = createPercentageOfIdenticalTagsProgressInfo(percentageOfIdenticalTags);
		percentageOfIdenticalTagsProgressInfo.setText(textForChosenPercentageOfIdenticalTagsInfo);
		
		percentageOfIdenticalTagsBar = (SeekBar) findViewById(R.id.SettingsActivity_setPercentageOfTagsToBeIdenticalBar);
		percentageOfIdenticalTagsBar.setProgress(percentageOfIdenticalTags);
		percentageOfIdenticalTagsBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				percentageOfIdenticalTagsBar.setSecondaryProgress(percentageOfIdenticalTagsBar.getProgress());
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				percentageOfIdenticalTags = progress;
				StringBuilder textForChosenPercentageOfIdenticalTagsInfo = createPercentageOfIdenticalTagsProgressInfo(progress);
				percentageOfIdenticalTagsProgressInfo.setText(textForChosenPercentageOfIdenticalTagsInfo);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {	}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	private StringBuilder createBluetoothSearchingFrequencyInfo(int progress) {
		StringBuilder textForSearchingFrequencyInfo = new StringBuilder();
		textForSearchingFrequencyInfo.append(getText(R.string.SettingActivity_bluetoothDeviceSearchigFrequencyInformationText))
									 .append(" ").append(progress).append(" ");
		if (progress >= 5) {
			textForSearchingFrequencyInfo.append(getText(R.string.SettingsActivity_FiveOrMoreSecondsText));
		} else if (progress == 1) {
			textForSearchingFrequencyInfo.append(getText(R.string.SettingActivity_oneSecondText));
		} else if (progress == 0) {
			textForSearchingFrequencyInfo = new StringBuilder(getText(R.string.SettingsActivity_notEnabled));
		} else {
			textForSearchingFrequencyInfo.append(getText(R.string.SettingsActivity_twoToFourSecondsText));
		}
		return textForSearchingFrequencyInfo;
	}

	private StringBuilder createGpsSearchingRadiusInfo(int progress) {
		StringBuilder textForSearchingRadiusInfo = new StringBuilder();
		textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_gpsDeviceSearchingRadiusInformationText))
								  .append(" ")
								  .append(progress)
								  .append(" ");
		if (progress >= 2) {
			textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_twoOrMoreKilometers));
		} else if (progress == 0) {
			textForSearchingRadiusInfo = new StringBuilder(getText(R.string.SettingsActivity_notEnabled));
		}	
		else {
			textForSearchingRadiusInfo.append(getText(R.string.SettingsActivity_oneKilometer));
		}
		return textForSearchingRadiusInfo;
	}

	private StringBuilder createPercentageOfIdenticalTagsProgressInfo(int progress) {
		StringBuilder textForChosenPercentageOfIdenticalTagsInfo = new StringBuilder();
		String currentText = getText(R.string.SettingsActivity_percentageOfIdenticaTagsInformationText).toString();
		textForChosenPercentageOfIdenticalTagsInfo.append(progress)
												  .append("%");
		return textForChosenPercentageOfIdenticalTagsInfo;
	}
	
	public void goToBlockedContactsActivityAction(View view) {
		startActivity(new Intent(SettingsActivity.this, BlockedContactsActivity.class));
	}

	public void goToFriendsListActivity(View view) {
		startActivity(new Intent(SettingsActivity.this, FriendsListFragment.class));
	}

	public void saveSettingsAndGoBackAction(View view) {
		  configuration.setBluetoothUsed(bluetoothCheckBox.isEnabled());
		  configuration.setGPSUsed(gpsCheckBox.isEnabled());
		  configuration.setBluetoothSecsTimeBetweenRefreshing(secondsBetweenRefreshingBluetooth);
		  configuration.setGpsScanningRadiusInKilometres(searchingRadiusInKilometres);
		  configuration.setDesiredTagsCompatibility(percentageOfIdenticalTags);
		  
		  AccountsDAO accountDAO = AccountsDAO.getInstance(null);
		  accountDAO.saveConfiguration(configuration);
	}
}
