package srt.inz.presenters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnLoginTaskCompleted;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class LoginApiTask extends AsyncTask<String,String,String> {
  //  private ProgressDialog progressDialog;
    Context ctx;
    String password,user_id;
    OnLoginTaskCompleted listerner;

    String result;
    LinearLayout linlaHeaderProgress;
    


    public LoginApiTask(Context context,String uid,String pass,LinearLayout linlaHeaderProgress, OnLoginTaskCompleted onLoginTaskCompleted) {
        this.ctx=context;
        this.password=pass;
        this.user_id=uid;
        this.linlaHeaderProgress=linlaHeaderProgress;
        this.listerner=onLoginTaskCompleted;

    }

    @Override
    protected String doInBackground(String... params) {


            String urlParameters = null;
            try {
                urlParameters =  "password=" + URLEncoder.encode(password, "UTF-8") + "&&"
                        + "user_id=" + URLEncoder.encode(user_id, "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            result = Connectivity.excutePost(Constants.LOGIN_URL,
                    urlParameters);
            Log.e("You are at", "" + result);

       return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listerner.OnTaskCompleted(s);
        /* progressDialog.dismiss();*/
        linlaHeaderProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

      /*  progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(Constants.REGISTER_MESSAGE);
        progressDialog.setIndeterminate(true);
        progressDialog.show();*/
        linlaHeaderProgress.setVisibility(View.VISIBLE);

    }
}
