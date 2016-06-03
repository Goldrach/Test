package com.example.goldrach.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidGPSTrackingActivity extends Activity implements LocationListener {
    ///////
    LocationManager Loc;
    TelephonyManager Tel;

    MyPhoneStateListener    MyListener;
    //for example value of first element
    GPSTracker gps;
    ///////
    boolean var=false;
    String str;
    //String tmp1;
    String tmp;
    EditText textmsg;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_gpstracking);

        /** Called when the activity is first created. */
        gps = new GPSTracker(AndroidGPSTrackingActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // INPPPUT COORDINATES

                //Toast.makeText(getBaseContext(), "Long" + longitude + "Lat" + latitude, Toast.LENGTH_SHORT).show();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

         Loc= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
         Loc.requestLocationUpdates( LocationManager.GPS_PROVIDER,2000,5, this);



          /* Update the listener, and start it */
        MyListener = new MyPhoneStateListener();

        Tel       = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(MyListener , PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //textmsg=(EditText)findViewById(R.id.textView);
    }


    /* Called when the application is minimized */
    @Override
    protected void onPause()
    {
        super.onPause();
        Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
/* Called when the application resumes */
@Override
protected void onResume()
{
    super.onResume();
    Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
}

    /* —————————– */
         /* Start the PhoneState listener */
        /* —————————– */
   /////////////////////////////////////////////////////////////////////////////SIGNALSTRENGTHCHANGED
    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        tmp = latitude + ";" + longitude + ";";

//////////////////////test/////////////////
        try {

            FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            File f = getFileStreamPath("mytextfile.txt");
            //File f =new File("mytextfile.txt");
            if (!(f.exists()) || (f.length() == 0 )) {
               String tmp1 = "Lat;" + "Long; " + "SignalStrength;" + "\n";
                outputWriter.write(tmp1.toString());
                outputWriter.close();
            } else {

                //String tmp=gps.getLatitude()+ ";" + gps.getLongitude() + ";";
                outputWriter.write((tmp + str + "\n"));
                outputWriter.close();
                //display file saved message
                //Toast.makeText(getBaseContext(), "File Updated successfully!",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
//////////////////////test/////////////////
        // \n is for new line
        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
    }





    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }





    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }





    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // write text to file
  // public void WriteBtn(View v) {
        // add-write text into file

///////////GPS////////////
            // create class object
           //gps thingy
////////////END GPS ////////Write File here




    //}//end file write here and write button
    public class MyPhoneStateListener extends PhoneStateListener
    {
        /* Get the Signal strength from the provider, each time there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            var=true;
            int tmp2 = -113+2*signalStrength.getGsmSignalStrength();
            super.onSignalStrengthsChanged(signalStrength);
            //String str1=tmp;
            Integer int1= new Integer(tmp2);
            str=int1.toString();
            Toast.makeText(getApplicationContext(), "Your RSSI " + str, Toast.LENGTH_LONG).show();


        }



    }

    // Read text from file
    public void ReadBtn(View v) {
        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}