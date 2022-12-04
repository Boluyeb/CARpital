package uk.ac.tees.a0174604.carpital;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

//For shared preferences so we can always use the values of the session in all parts of the application
public class SessionManager {

    //    instance variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    //    specify the activity to call the  class
    Context context;

//    session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    // User session variables to create, keys for the editor whose values won't change
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phoneNum";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_PROFILEIMG = "profileImg";


//    remember me preference variables.
//    check if this has been stored or not in the shared prefrence
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_REMEMBERPHONENUMBER = "phoneNum";
    public static final String KEY_REMEMBERPWD = "pwd";


    //    constructor
    public SessionManager(Context context, String sessionName) {
        this.context = context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

//login session methods

    public void createLoginSession(String name, String email, String phoneNum, String pwd, String profileImg) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phoneNum);
        editor.putString(KEY_PWD, pwd);
        editor.putString(KEY_PROFILEIMG, profileImg);

//        commit changes to editor
        editor.commit();
    }

    //    to store key value pair and get them
    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_NAME, usersSession.getString(KEY_NAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_PWD, usersSession.getString(KEY_PWD, null));
        userData.put(KEY_PROFILEIMG, usersSession.getString(KEY_PROFILEIMG, null));
        return userData;
    }

    //    check if stored in shared preference
    public boolean checkLogin() {
//        check if login is true
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

//    Remember Me Session methods
public void createRemeberMeSession(String phoneNum, String pwd) {
    editor.putBoolean(IS_REMEMBERME, true);

//    store the varaibles in the shared preference
    editor.putString(KEY_REMEMBERPHONENUMBER, phoneNum);
    editor.putString(KEY_REMEMBERPWD, pwd);

//        commit changes to editor
    editor.commit();
}

//to key value pairs to get them from shared preference
//    to store key value pair and get them
public HashMap<String, String> getDetailsFromRememberSession() {
    HashMap<String, String> userData = new HashMap<>();
    userData.put(KEY_REMEMBERPHONENUMBER, usersSession.getString(KEY_REMEMBERPHONENUMBER, null));
    userData.put(KEY_REMEMBERPWD, usersSession.getString(KEY_REMEMBERPWD, null));
    return userData;
}

//check if exists in shared preference
    public boolean checkRemember() {
//        check if login is true
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {
            return true;
        } else {
            return false;
        }
    }


}
