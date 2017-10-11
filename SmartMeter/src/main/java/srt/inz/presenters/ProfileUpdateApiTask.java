package srt.inz.presenters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnProfileupTaskCompleted;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ProfileUpdateApiTask extends AsyncTask<String, String, String>{
	
	
	 Context ctx;
	    String email,password,name,user_id,phone,vehicleid;
	    OnProfileupTaskCompleted listerner;

	    String result;
	    


	    public ProfileUpdateApiTask(Context context, String email, String name, String pass, String phn, String uid, String vehid, OnProfileupTaskCompleted onProfileupTaskCompleted) {
	        this.ctx=context;
	        this.email=email;
	        this.password=pass;
	        this.name=name;
	        this.phone=phn;
	        this.user_id=uid;
	        this.vehicleid=vehid;
	        this.listerner=onProfileupTaskCompleted;

	    }

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		String urlParameters = null;
        try {
            urlParameters = "email=" + URLEncoder.encode(email, "UTF-8") + "&&"
                    + "password=" + URLEncoder.encode(password, "UTF-8") + "&&"
                    +"name=" + URLEncoder.encode(name, "UTF-8") + "&&"
                    + "user_id=" + URLEncoder.encode(user_id, "UTF-8") + "&&"
                      + "phone=" + URLEncoder.encode(phone, "UTF-8") + "&&"
                      + "vehicleid=" + URLEncoder.encode(vehicleid, "UTF-8");
            
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result = Connectivity.excutePost(Constants.PROFILEUP_URL,
                urlParameters);
        Log.e("You are at", "" + result);
		return result;
	}
	@Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listerner.OnTaskCompleted(s);
        /* progressDialog.dismiss();*/
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

}
