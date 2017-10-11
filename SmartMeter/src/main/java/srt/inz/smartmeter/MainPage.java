package srt.inz.smartmeter;

import srt.inz.connnectors.OnLoginTaskCompleted;
import srt.inz.presenters.LoginApiTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainPage extends Activity {
	
	EditText eun,eps;	Button bl,br;	String sun,spas;
	LinearLayout linlaHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        
        eun=(EditText)findViewById(R.id.edituid);
        eps=(EditText)findViewById(R.id.editpass);
        
        bl=(Button)findViewById(R.id.btnlog);
        br=(Button)findViewById(R.id.btnreg);
        
        linlaHeaderProgress=(LinearLayout)findViewById(R.id.linlaHeaderProgress);
        
        bl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sun=eun.getText().toString();
				spas=eps.getText().toString();
				
				SharedPreferences share=getSharedPreferences("mKey", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("keyvid", sun);
				ed.commit();
				
				
				if (isNetworkConnected()) {
		        	
		        	Toast.makeText(getApplicationContext(), "Network Connectivity Avialable", Toast.LENGTH_SHORT).show();
				
		        	LoginApiTask task=new LoginApiTask(getApplicationContext(), sun, spas,linlaHeaderProgress, new OnLoginTaskCompleted() {

						@Override
						public void OnTaskCompleted(String result) {
							// TODO Auto-generated method stub
							if(result!=null)
		                      {

		                          if(result.contains("success"))
		                          {
		                        	  Intent intent =new Intent(getApplicationContext(),Driver_home.class);
		                              startActivity(intent);
		                              Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
		                          }
		                          else{
		                            //  profileDetailsResuilt= ApiParserClass.Parentdetails(result);
		                              
		                        	  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
		                          }
		                      }
						}
					});
					task.execute();
		        	
				}
		        else
		        {
		        	Toast.makeText(getApplicationContext(), "Network Unavialable. Please Connect to the network", Toast.LENGTH_SHORT).show();
		        }
				
				
			}
		});
        
        br.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Driverregister.class);
				startActivity(i);
				
			}
		});
        
        
    }
    
    
    private boolean isNetworkConnected() {
  	  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

  	  return cm.getActiveNetworkInfo() != null;
  	 }
}
