package com.meetEverywhere;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.meetEverywhere.bluetooth.BluetoothDeviceSearchService;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.bluetooth.BluetoothListAdapter;
import com.meetEverywhere.bluetooth.BluetoothService;
import com.meetEverywhere.common.Configuration;

public class MeetEverywhere extends Activity {

	/*
	 * TODO : Stworzyæ Service, który bêdzie cyklicznie sprawdza³ czy tagi przechowywane lokalnie
	 * s¹ zsynchronizowane z serwerem.
	 */

    private ImageView userImage;
    private Configuration configuration;
    private BluetoothDispatcher dispatcher;

    private boolean isTrackingServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String className = PositionTracker.class.getCanonicalName();
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        prepareConfigAndBluetoothDispatcher();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        refreshDisplayedUserNameAndDescription();

        ((FrameLayout) findViewById(R.id.bluetooth_button)).setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View arg0) {
                //startActivity(new Intent(MeetEverywhere.this, BluetoothChooseDeviceActivity.class));
                Intent intent = new Intent(MeetEverywhere.this, FoundTagsActivity.class);
                intent.putExtra("typeOfAdapter", "byOwnTags");
                startActivity(intent);
            }
        });


        ((FrameLayout) findViewById(R.id.gps_button)).setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View arg0) {
                startActivity(new Intent(MeetEverywhere.this, SearchTagsEdition.class));
            }
        });

        ((FrameLayout) findViewById(R.id.close_button)).setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View arg0) {
                stopService(new Intent(MeetEverywhere.this, PositionTracker.class));
                finish();
            }
        });

        
        userImage = (ImageView) findViewById(R.id.userImage);
        
        if(Configuration.getInstance().getUser().getPicture() != null)
        	userImage.setImageBitmap(Configuration.getInstance().getUser().getPicture());
                
        /* Wystartuj us³ugê Bluetooth. */
        startService(new Intent(MeetEverywhere.this, BluetoothService.class));

        if (BluetoothDispatcher.getInstance().getBluetoothListAdapter() == null) {
            BluetoothDispatcher.getInstance().setBluetoothListAdapter(
                    new BluetoothListAdapter(getApplicationContext(), 0));
        }

        if (!dispatcher.isDiscoveringServiceActivated()) {
            dispatcher.setDiscoveringServiceActivated(true);
            //dispatcher.setFlagDiscoveryFinished(true);
            startService(new Intent(MeetEverywhere.this,
                    BluetoothDeviceSearchService.class));
        }


        // Position Tracker - czeka na lepsze czasy -> pod³¹czenie z serwerem
//        if(!isTrackingServiceRunning())
//        	startService(new Intent(MeetEverywhere.this, PositionTracker.class));

    }

    private void refreshDisplayedUserNameAndDescription() {
        TextView user = ((TextView) findViewById(R.id.logged_as));
        user.setText(configuration.getUser().getNickname());
        TextView description = ((TextView) findViewById(R.id.userDescription));
        description.setText(configuration.getUser().getDescription());
    }

    private void prepareConfigAndBluetoothDispatcher() {
        configuration = Configuration.getInstance();
        dispatcher = BluetoothDispatcher.getInstance();
        dispatcher.setHandler(new Handler(getMainLooper()));
        dispatcher.setTempContextHolder(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDisplayedUserNameAndDescription();
        realoadMeetEveywhere();

    }
    
    public void closeAppAction(View view) {
    	finish();
    	System.exit(0);
    }

    public void realoadMeetEveywhere() {
    	if(Configuration.getInstance().getUser().getPicture() != null)
    		userImage.setImageBitmap(Configuration.getInstance().getUser().getPicture());
    }

    public void goToProfileEditionActivityAction(View view) {
        startActivity(new Intent(MeetEverywhere.this, ProfileEdition.class));
    }

    public void openManageContactsActivity(View view) {
        startActivity(new Intent(this, ManageContactsActivity.class));
    }

    public void goToSettingsActivityAction(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
