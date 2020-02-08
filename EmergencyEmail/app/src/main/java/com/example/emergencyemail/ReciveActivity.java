package com.example.emergencyemail;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReciveActivity extends BroadcastReceiver{
	private static final String TAG = "PhoneStatReceiver"; 
    public static long time=0;
    private static boolean incomingFlag = false;
    static List<Product> products=new ArrayList<Product>();

    private static String incoming_number = null;
	MediaPlayer mp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){                        

            incomingFlag = false;

            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);        

            Log.i(TAG, "call OUT:"+phoneNumber);                        
         
    }else{                        

            

            TelephonyManager tm = 

                (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);                        



            switch (tm.getCallState()) {

            case TelephonyManager.CALL_STATE_RINGING:

                    incomingFlag = true;

                    incoming_number = intent.getStringExtra("incoming_number");
                    
                    Toast.makeText(context, "ring"+incoming_number, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "RINGING :"+ incoming_number);
                    MainActivity.SendEmailTask sms=new MainActivity.SendEmailTask();
                    sms.no=incoming_number;
                    sms.execute();


                    case TelephonyManager.CALL_STATE_OFFHOOK:

                    if(incomingFlag){

                            Log.i(TAG, "incoming ACCEPT :"+ incoming_number);

                            Toast.makeText(context, "hook", Toast.LENGTH_SHORT).show();
                    }

                    break;



            case TelephonyManager.CALL_STATE_IDLE:                                

                    if(incomingFlag){

                            Log.i(TAG, "incoming IDLE");                                
                            Toast.makeText(context, "idle", Toast.LENGTH_SHORT).show();
                    }
 
                    
                    break;

            } 
		
	 }
	}
}
