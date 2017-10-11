package srt.inz.services;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.OnRequestfetchTaskCompleted;
import srt.inz.presenters.RequestfetchApiTask;
import srt.inz.smartmeter.Driver_home;
import srt.inz.smartmeter.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint("NewApi") public class NotificationService extends Service{
	
	private MyThread mythread;
    public boolean isRunning = false; String svhid,parseresult;
    private static String TAG = NotificationService.class.getSimpleName();
	
	public NotificationService()
	{
		
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 Log.d(TAG, "onCreate");  
		 SharedPreferences sh=getSharedPreferences("mKey", MODE_WORLD_READABLE);
			svhid=sh.getString("keyvid", "");
	        mythread  = new MyThread();
		Toast.makeText(getApplicationContext(), "Service created", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	//	new Myasync().execute();
		
		if(!isRunning){
            mythread.start();
            isRunning = true;
        }
		
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class MyThread extends Thread{
        static final long DELAY = 10000;
        @Override
        public void run(){          
            while(isRunning){
                Log.d(TAG,"Running");
                try {                   
                	getreqfromdB();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
         
    }
	
	
	public void notification(Intent inte,String svd)
	{
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); 	
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, inte, 0);	
		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the first param to 0
		Notification mNotification = new Notification.Builder(getApplicationContext())		
			.setContentTitle("Request Recieved")
			.setContentText("You have recieved new request fron "+svd+ ".")
			.setTicker("New Message Alert!")
			/*.setNumber(++numMessages)*/
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent)
			.setSound(soundUri)		
			//.addAction(0, "View", pIntent)
			//.addAction(0, "Stop", pIntent)			
			.build();
		
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		// If you want to hide the notification after it was selected, do the code below
		// myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(0, mNotification);
		
		// Toast.makeText(getApplicationContext(), "Message recieved", Toast.LENGTH_LONG).show();
	}
	
	
	public void noti_parser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String userid=data1.getString("userid");
				String vh_id=data1.getString("vh_id");
				String source=data1.getString("source");
				String destination=data1.getString("destination");
				String datetime=data1.getString("datetime");
				String status=data1.getString("status");
				
				String mnotification= userid;
				
				if (status.equals("0")) {
					
					Intent intentAlarm= new Intent(getBaseContext(), Driver_home.class);
					notification(intentAlarm,mnotification);
				} else {

					
				}
				
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void getreqfromdB()
	{
		

		RequestfetchApiTask task=new RequestfetchApiTask(getApplicationContext(),svhid,
	      		  new OnRequestfetchTaskCompleted() {
					
					@Override
					public void OnTaskCompleted(String result) {
						// TODO Auto-generated method stub
						
						parseresult=result;
						
						if(result.contains("success"))
	                    {
	                     
	                     // 	  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
	                      	noti_parser(result);
	                        
	                    }
						else
						{
							Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
						}
					}
				});
	        task.execute();	
	}
}
