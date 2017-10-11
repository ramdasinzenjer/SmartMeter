package srt.inz.presenters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.connnectors.OnRegisterTaskCompleted;
import srt.inz.connnectors.OnRequestTaskCompleted;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RequestfareApiTask extends AsyncTask<String,String,String> {
  //  private ProgressDialog progressDialog;
    Context ctx;
    String vehicleid,reqfare,status;
    OnRequestTaskCompleted listerner;

    String result;
    
    public RequestfareApiTask(Context context, String vehid,String reqfare, String stat, OnRequestTaskCompleted onRequestTaskCompleted) {
        this.ctx=context;

        this.reqfare=reqfare;
        this.status=stat;
        this.vehicleid=vehid;
        this.listerner=onRequestTaskCompleted;

    }

    @Override
    protected String doInBackground(String... params) {


            String urlParameters = null;
            try {
                urlParameters = "requset_fare=" + URLEncoder.encode(reqfare, "UTF-8") + "&&"
                        + "veh_id=" + URLEncoder.encode(vehicleid, "UTF-8") + "&&"
                        + "status=" + URLEncoder.encode(status, "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            result = Connectivity.excutePost(Constants.REQUESTFARE_URL,
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

      /*  progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(Constants.REGISTER_MESSAGE);
        progressDialog.setIndeterminate(true);
        progressDialog.show();*/

    }
}
