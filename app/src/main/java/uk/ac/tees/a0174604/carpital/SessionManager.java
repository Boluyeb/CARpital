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

    private static final String IS_LOGIN = "IsLoggedIn";

    //    variables to create, keys for the editor whose values won't change
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phoneNum";
    public static final String KEY_PWD = "pwd";

    //    constructor
    public SessionManager(Context context) {
        this.context = context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String name, String email, String phoneNum, String pwd) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phoneNum);
        editor.putString(KEY_PWD, pwd);

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
        return userData;
    }

    //    check if logged in
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
}
