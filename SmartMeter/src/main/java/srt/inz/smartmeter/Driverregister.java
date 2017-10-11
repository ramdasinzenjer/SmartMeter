package srt.inz.smartmeter;

import srt.inz.connnectors.OnRegisterTaskCompleted;
import srt.inz.presenters.RegisterApiTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Driverregister extends Activity{
	
	Button brg;
	
	EditText etn,etun,etpas,etcpas,etph,etmail,etli,etvid;
	String sn,sun,spas,scpas,sphn,smail,slicense,svehid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		etn=(EditText)findViewById(R.id.etname);
		etun=(EditText)findViewById(R.id.etuname);
		etpas=(EditText)findViewById(R.id.etpass);
		etcpas=(EditText)findViewById(R.id.etcps);
		etph=(EditText)findViewById(R.id.etphn);
		etmail=(EditText)findViewById(R.id.etemail);
		etli=(EditText)findViewById(R.id.etlicense);
		etvid=(EditText)findViewById(R.id.etvehid);
		
	}
	
	public void regist(View v)
	{
		sn=etn.getText().toString();
		sun=etun.getText().toString();
		spas=etpas.getText().toString();
		scpas=etcpas.getText().toString();
		sphn=etph.getText().toString();
		smail=etmail.getText().toString();
		slicense=etli.getText().toString();
		svehid=etvid.getText().toString();


              RegisterApiTask task=new RegisterApiTask(getApplicationContext(),smail,sn,spas,sphn,sun,slicense,svehid,
            		  new OnRegisterTaskCompleted() {
				
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

}
