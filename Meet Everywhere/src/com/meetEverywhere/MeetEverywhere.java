package com.meetEverywhere;

import java.io.FileNotFoundException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.meetEverywhere.bluetooth.BluetoothChooseDeviceActivity;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.bluetooth.BluetoothService;
import com.meetEverywhere.common.Configuration;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MeetEverywhere extends Activity {

	//sprawdzaæ mo¿liwoœæ po³¹czenia z internetem i dostêp do GPSa oraz istnienia konta google
	//dodaæ przy pierwszej konfiguracji zapytanie czy numer telefonu jest poprawny (zaznaczyæ, ¿e podanie nieprawdziwego numeru spowoduje, ¿e nikt znajomy go niw wykryje) 
	//nastepnie poprawic ten system tak zeby autoryzacja byla przez sms
	
	
	/*
	 * TODO : Stworzyæ Service, które bêdzie cyklicznie sprawdzaæ czy tagi przechowywane lokalnie
	 * s¹ zsynchronizowane z serwerem.
	 */
	
	private final String gcmId = "719842226591";
	private final String userName = "meuser";
	private final String password = "mepassword";
	
	private SharedPreferences userSettings;
	private ImageView userImage;
	private Configuration configuration;
	
	
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

        
        
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (userName, password.toCharArray());
		    }
		});

        ((FrameLayout)findViewById(R.id.bluetooth_button)).setOnClickListener(new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	        	startActivity(new Intent(MeetEverywhere.this, BluetoothChooseDeviceActivity.class));
	        }
	    });
        
        ((FrameLayout)findViewById(R.id.close_button)).setOnClickListener(new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	            stopService(new Intent(MeetEverywhere.this, PositionTracker.class));
	            
	            System.exit(0);
	            finish();	       
	        }
	    });
        
        OnClickListener userSettingsOnClickListener = new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	        	startActivity(new Intent(MeetEverywhere.this, ProfileEdition.class));
	        }
        };
        
        userImage = (ImageView)findViewById(R.id.userImage);
        userImage.setImageBitmap(Configuration.getInstance().getUser().getPicture());
        
        
        userImage.setOnClickListener(userSettingsOnClickListener);
//        ((TextView)findViewById(R.id.userDescription)).setOnClickListener(userSettingsOnClickListener);
        
        ((FrameLayout)findViewById(R.id.profile_edition_button)).setOnClickListener(new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	        	startActivity(new Intent(MeetEverywhere.this, TagsEdition.class));
	        }
	    });
        
        /*
        ((Button)findViewById(R.id.searchTagListsEdit)).setOnClickListener(new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	        	startActivity(new Intent(MeetEverywhere.this, SearchTagsEdition.class));
	        }
	    });
        
        */
        /*
        ((Button)findViewById(R.id.friendsListEdit)).setOnClickListener(new OnClickListener() {
	        //@Override
	        public void onClick(View arg0) {
	        	startActivity(new Intent(MeetEverywhere.this, FriendsEdition.class));
	        }
	    });
        */
        
        userSettings = getSharedPreferences(SharedPreferencesKeys.preferencesName, Activity.MODE_PRIVATE);
        boolean initialized = userSettings.getBoolean(SharedPreferencesKeys.initialization, false);
        
        //if(!initialized)
        //	startActivity(new Intent(MeetEverywhere.this, ProfileEdition.class));
            
        /* Wystartuj us³ugê Bluetooth. */
        startService(new Intent(MeetEverywhere.this, BluetoothService.class));
        
        if(!isTrackingServiceRunning())
        	startService(new Intent(MeetEverywhere.this, PositionTracker.class));
                
    }

    private void refreshDisplayedUserNameAndDescription() {
        TextView user =  ((TextView)findViewById(R.id.logged_as));
        user.setText(configuration.getUser().getNickname());
        TextView description =  ((TextView)findViewById(R.id.userDescription));
        description.setText(configuration.getUser().getDescription());
	}

	private void prepareConfigAndBluetoothDispatcher() {
       	configuration = Configuration.getInstance();
    	BluetoothDispatcher dispatcher = BluetoothDispatcher.getInstance();
    	dispatcher.setHandler(new Handler(getMainLooper()));
    	dispatcher.setTempContextHolder(getApplicationContext());		
	}

	@Override
    protected void onResume() {
        super.onResume();
        refreshDisplayedUserNameAndDescription();
        //userImage = (ImageView)findViewById(R.id.userImage);
        //userImage.setImageBitmap(BitmapFactory.decodeFile("/res/drawable-hdpi/ic_launcher.png"));
        
        realoadMeetEveywhere();
        
    }
    
    private class ImageLoader extends AsyncTask<String, Object, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				return BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(params[0])));
			} catch (FileNotFoundException e) {
				return null;
			}
		}
    	
		@Override
		protected void onPostExecute(Bitmap param){
			if(param != null)
				userImage.setImageBitmap(param);
		}
    }
    
    public void realoadMeetEveywhere() {
    	userImage.setImageBitmap(Configuration.getInstance().getUser().getPicture());
    }
}
