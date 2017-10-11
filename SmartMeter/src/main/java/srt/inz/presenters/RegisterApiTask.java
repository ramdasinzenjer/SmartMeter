package srt.inz.presenters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnRegisterTaskCompleted;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class RegisterApiTask extends AsyncTask<String,String,String> {
  //  private ProgressDialog progressDialog;
    Context ctx;
    String email,password,name,user_id,phone,license,vehicleid;
    OnRegisterTaskCompleted listerner;

    String result;
    


    public RegisterApiTask(Context context, String email, String name, String pass, String phn, String uid,String licen, String vehid, OnRegisterTaskCompleted onRegisterTaskCompleted) {
        this.ctx=context;
        this.email=email;
        this.password=pass;
        this.name=name;
        this.phone=phn;
        this.user_id=uid;
        this.license=licen;
        this.vehicleid=vehid;
        this.listerner=onRegisterTaskCompleted;

    }

    @Override
    protected String doInBackground(String... params) {


            String urlParameters = null;
            try {
                urlParameters = "email=" + URLEncoder.encode(email, "UTF-8") + "&&"
                        + "password=" + URLEncoder.encode(password, "UTF-8") + "&&"
                        +"name=" + URLEncoder.encode(name, "UTF-8") + "&&"
                        + "user_id=" + URLEncoder.encode(user_id, "UTF-8") + "&&"
                        + "vehicleid=" + URLEncoder.encode(vehicleid, "UTF-8") + "&&"
                        + "license=" + URLEncoder.encode(license, "UTF-8") + "&&"
                        + "phone=" + URLEncoder.encode(phone, "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            result = Connectivity.excutePost(Constants.REGISTER_URL,
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
