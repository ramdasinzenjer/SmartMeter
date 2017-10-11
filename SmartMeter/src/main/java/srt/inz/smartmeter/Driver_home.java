package srt.inz.smartmeter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnRequestTaskCompleted;
import srt.inz.connnectors.OnRequestfetchTaskCompleted;
import srt.inz.presenters.RequestfareApiTask;
import srt.inz.presenters.RequestfetchApiTask;
import srt.inz.services.NotificationService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Driver_home extends Activity implements LocationListener{
	
	Button breq,bprup; String svhid="10521",parseresult,valuedialog,valuesrc,valuedest,valueid,valuedate,
			valuestatus;
	
	ListView nlist;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;	String resultout,res; ImageView ib;
	
	String stplace; double latitude,longitude; Location location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_home);
		breq=(Button)findViewById(R.id.btreqfarechange);
		bprup=(Button)findViewById(R.id.btprofileupdate);
		nlist=(ListView)findViewById(R.id.notifylist);
		ib=(ImageView)findViewById(R.id.imglc);
		
		SharedPreferences sh=getSharedPreferences("mKey", MODE_WORLD_READABLE);
		svhid=sh.getString("keyvid", "");
		
		startService(new Intent(getApplicationContext(), NotificationService.class));
		
		RequestfromdB();
		getmyloc();
		breq.setEnabled(false);
		breq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				RequestfareApiTask task=new RequestfareApiTask(getApplicationContext(),svhid," req","0",
		            		  new OnRequestTaskCompleted() {
												
								@Override
								public void OnTaskCompleted(String result) {
									// TODO Auto-generated method stub
									
									if(result!=null)
				                      {					
				                        	  Toast.makeText(getApplicationContext(),"Request sent."+result,Toast.LENGTH_SHORT).show();				                          
				                      }
								}
							});     
		              task.execute();				
			}
		});
		
		bprup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),ProfileUpdate.class);
				startActivity(i);
			}
		});
		
		ib.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				new LocationupApiTask().execute();
			}
		});
		
	}

	public void RequestfromdB()
	{
		RequestfetchApiTask task=new RequestfetchApiTask(getApplicationContext(),svhid,
      		  new OnRequestfetchTaskCompleted() {
				
				@Override
				public void OnTaskCompleted(String result) {
					// TODO Auto-generated method stub
					
					parseresult=result;
					
					if(result.contains("success"))
                    {
                     
                      	//  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
                      	keyparser(result);
                        
                    }
					else
					{
						Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
					}
				}
			});	
        task.execute();       
	}
	
	public void keyparser(String result)
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
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("userid", userid);
	            map.put("vh_id", vh_id);
	            map.put("source", source);
	            map.put("destination", destination);
	            map.put("datetime", datetime);
	            map.put("status", status);
	            
	            map.put("notification", "Request from "+userid+" on date : "+datetime+"."+
	            "/n From :"+source+", To :"+destination+".");
	            
	            map.put("dalogmsg", "Source :"+source+"\n Destination :"+destination+"\n User id :"+userid+""
	            		+"\t Status :"+status);
	            
	            //Toast.makeText(getApplicationContext(), ""+sname,Toast.LENGTH_SHORT).show();
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	            nlist.setAdapter(adapter);
	            
	            nlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               @SuppressWarnings("unused")
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), " "+oslist.get(+position).get("userid"), Toast.LENGTH_SHORT).show();
	               
	                valueid=oslist.get(+position).get("userid");
	                valuedate=oslist.get(+position).get("datetime"); 
	               valuesrc=oslist.get(+position).get("source");
	               valuedest=oslist.get(+position).get("destination");
	               valuedialog=oslist.get(+position).get("dalogmsg");
	               valuestatus=oslist.get(+position).get("status");
	                  
             	  /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
          			    Uri.parse("http://maps.google.com/maps?saddr="+valuesrc+"&daddr="+valuedest));*/
          			openDialog();
	               
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	 public void openDialog(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	     if(valuestatus.equals("pending")) 
	     
	    	 {
	    	 alertDialogBuilder.setTitle("Please choose an action!");
		      alertDialogBuilder.setMessage(""+valuedialog);
	    	 alertDialogBuilder.setPositiveButton("Accept & Navigate", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Navigating...",Toast.LENGTH_SHORT).show();
	            new StatuschangeApiTask().execute();
	            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            		    Uri.parse("google.navigation:q="+valuesrc));
	            startActivity(intent);
	        
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("Reject",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        	 Toast.makeText(getApplicationContext(),"Request rejected.",Toast.LENGTH_SHORT).show();
	        
	        	 //finish();
	         }
	      });
	      
	    	 }
	     else
	     {
	    	 alertDialogBuilder.setTitle("Drive!!!");
		      alertDialogBuilder.setMessage("This drive is already accepted.");
	    	 alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
     
		         }
		      });
	     }
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
	 
	 public class StatuschangeApiTask extends AsyncTask<String, String, String>
	 {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters = null;
	        try {
	            urlParameters = "vh_id=" + URLEncoder.encode(svhid, "UTF-8")+"&&"
	            		+"userid=" + URLEncoder.encode(valueid, "UTF-8")+"&&"
	            		+"datetime=" + URLEncoder.encode(valuedate, "UTF-8");
	            
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        resultout = Connectivity.excutePost(Constants.REQSTATUS_URL,
	                urlParameters);
	        Log.e("You are at", "" +resultout);
			return resultout;
			
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resultout.contains("success"))
			{
				MYTOAST("Drive accepted");
			}
			else {
				MYTOAST(""+resultout);
			}
		}
		 
	 }
	 private void MYTOAST(String msg)
	 {
		 Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	 }
	 
		public void getmyloc()
		{
			try {
							
				 LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

				    // Creating a criteria object to retrieve provider
				    Criteria criteria = new Criteria();

				    // Getting the name of the best provider
				    String provider = locationManager.getBestProvider(criteria, true);

				    // Getting Current Location
				    location = locationManager.getLastKnownLocation(provider);
				    

				    if(location!=null){
				            onLocationChanged(location);
				            
				    }

				    locationManager.requestLocationUpdates(provider, 120000, 0, this);
			} catch (Exception e) {
				// TODO: handle exception
				
				e.printStackTrace();
			}
		}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
				Geocoder gc= new Geocoder(this, Locale.ENGLISH);
		        // Getting latitude of the current location
		        latitude =  location.getLatitude();
		
		        // Getting longitude of the current location
		        longitude =  location.getLongitude();
		
		try {
			List<Address> addresses = gc.getFromLocation(latitude,longitude, 1);
			
			if(addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) 
				{
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
					
				}
			
				stplace=strReturnedAddress.toString();
				
				//String[] splited = stplace.split("\\s+");
				  	
				Toast.makeText( getBaseContext(),stplace,Toast.LENGTH_SHORT).show();
			}
		//stores the current address to shared preferene shr.
			
			else{
				Toast.makeText(getBaseContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public class LocationupApiTask extends AsyncTask<String,String,String> {
	    
	    @Override
	    protected String doInBackground(String... params) {


	            String urlParameters = null;
	            try {
	                urlParameters =  "vhid=" + URLEncoder.encode(svhid, "UTF-8") + "&&"
	                		+ "title=" + URLEncoder.encode("rickshaw", "UTF-8")+ "&&"
	                        + "lat=" + URLEncoder.encode(""+latitude, "UTF-8")+ "&&" 
	                        + "lon=" + URLEncoder.encode(""+longitude, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }		

	            res = Connectivity.excutePost(Constants.UPDATELOC_URL,
	                    urlParameters);
	            Log.e("You are at", "" + res);

	       return res;
	    }

	    @Override
	    protected void onPostExecute(String s) {
	        super.onPostExecute(s);
	        
	      //  linlaHeaderProgress.setVisibility(View.GONE);
	        if(res.contains("success"))
	        {
	        Toast.makeText(getApplicationContext(), ""+res, Toast.LENGTH_SHORT).show();	            
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(), ""+res, Toast.LENGTH_SHORT).show();
	        }	        
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }
	}
}
