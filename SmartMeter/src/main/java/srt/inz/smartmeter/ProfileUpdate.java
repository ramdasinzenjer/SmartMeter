package srt.inz.smartmeter;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.OnProfilefetchTaskCompleted;
import srt.inz.connnectors.OnProfileupTaskCompleted;
import srt.inz.presenters.ProfileUpdateApiTask;
import srt.inz.presenters.ProfilefetchApiTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileUpdate extends Activity{
	
    Button buppro;
	
	EditText etn,etun,etpas,etcpas,etph,etmail;
	String sn,sun,spas,scpas,sphn,smail,svehid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_update);
		
		etn=(EditText)findViewById(R.id.etnameup);
		etun=(EditText)findViewById(R.id.etunameup);
		etpas=(EditText)findViewById(R.id.etpassup);
		etcpas=(EditText)findViewById(R.id.etcpsup);
		etph=(EditText)findViewById(R.id.etphnup);
		etmail=(EditText)findViewById(R.id.etemailup);
		
		
		SharedPreferences sh=getSharedPreferences("mKey", MODE_WORLD_READABLE);
		svehid=sh.getString("keyvid", "");
		
		RequestfromdB();
				
	}
	
	
	public void updateprofile(View v)
	{
		sn=etn.getText().toString();
		sun=etun.getText().toString();
		spas=etpas.getText().toString();
		scpas=etcpas.getText().toString();
		sphn=etph.getText().toString();
		smail=etmail.getText().toString();



		ProfileUpdateApiTask task=new ProfileUpdateApiTask(getApplicationContext(),smail,sn,spas,sphn,sun,svehid,
            		  new OnProfileupTaskCompleted() {
						
						@Override
						public void OnTaskCompleted(String result) {
							// TODO Auto-generated method stub
							if(result!=null)
		                      {

		                          if(result.contains("User Exists"))
		                          {
		                              Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
		                          }
		                          else{
		                            //  profileDetailsResuilt= ApiParserClass.Parentdetails(result);
		                              Intent intent =new Intent(getApplicationContext(),MainPage.class);
		                              startActivity(intent);
		                        	  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
		                          }
		                      }
							
						}
						
					});
              
              task.execute();
	}
	
	public void RequestfromdB()
	{
		ProfilefetchApiTask task=new ProfilefetchApiTask(getApplicationContext(),svehid,
      		  new OnProfilefetchTaskCompleted() {
				
				@Override
				public void OnTaskCompleted(String result) {
					// TODO Auto-generated method stub
					
					
					
					if(result.contains("success"))
                    {
                     
                      	  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
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
				String name=data1.getString("name");
				String email=data1.getString("email");
				String phone=data1.getString("phone");
				String password=data1.getString("password");
				String user_id=data1.getString("user_id");
				
				etn.setText(name); etun.setText(user_id); etmail.setText(email);
				etpas.setText(password); etph.setText(phone);
				
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}

	
	
}
