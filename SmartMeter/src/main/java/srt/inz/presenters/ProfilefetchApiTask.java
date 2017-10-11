package srt.inz.presenters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnProfilefetchTaskCompleted;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

public class ProfilefetchApiTask extends AsyncTask<String, String, String>{

	Context ctx;
	String vid;
	OnProfilefetchTaskCompleted listerner;
    String result;
    LinearLayout linlaHeaderProgress;
    
    public ProfilefetchApiTask(Context context,String vhid, 
    		OnProfilefetchTaskCompleted onProfilefetchTaskCompleted) {
        this.ctx=context;
        this.vid=vhid;  
        this.listerner=onProfilefetchTaskCompleted;

    }
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		String urlParameters = null;
        try {
            urlParameters =  "vh_id=" + URLEncoder.encode(vid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result = Connectivity.excutePost(Constants.REQUESTPROFILE_URL,
                urlParameters);
        Log.e("You are at", "" + result);

        return result;
		
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(String s) {
		// TODO Auto-generated method stub
		super.onPostExecute(s);
		listerner.OnTaskCompleted(s);
	}
	
	

}