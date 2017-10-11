package srt.inz.connnectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
 
    String REGISTER_URL="http://192.168.1.16:81/smartmeter/driver_api/mRegister.php?";
    String LOGIN_URL="http://192.168.1.16:81/smartmeter/driver_api/mLogin.php?";
    String REQUESTFARE_URL="http://192.168.1.16:81/smartmeter/driver_api/mRequest.php?";
    String REQUESTPROFILE_URL="http://192.168.1.16:81/smartmeter/driver_api/mProfilefetch.php?";
    String REQUESTGETTER_URL="http://192.168.1.16:81/smartmeter/driver_api/mRequestfetch.php?";
    String PROFILEUP_URL="http://192.168.1.16:81/smartmeter/driver_api/mProupdate.php?";
    String REQSTATUS_URL="http://192.168.1.16:81/smartmeter/driver_api/mReqstatUpdate.php?";
    String UPDATELOC_URL="http://192.168.1.16:81/smartmeter/driver_api/mUpdateLoc.php?";
    
    //Details

    String ID="id";
    String NAME="Name";
    String PASSWORD="Password";
    String EMAIL="Email";
    String LOGINSTATUS="LoginStatus";
    String USERID="id";

    //Sharedpreference

    String PREFERENCE_PARENT="Parent_Pref";

   
}
