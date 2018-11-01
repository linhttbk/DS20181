package a20181.ds.com.ds20181;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.utils.PrefUtil;

public class AppState implements AppConstant {
    private static AppState instance;

    private AppState() {

    }

    static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public  void setCurrentUser(User user) {
        if (user == null) return;
        String data = new Gson().toJson(user);
        PrefUtil.savePreferences(MainApplication.getAppContext(), PREF_USER, data);

    }

    public static User getCurrentUser() {
        Context context = MainApplication.getAppContext();
        if (context == null) return null;
        String user = PrefUtil.getPreferences(context, PREF_USER, EMPTY);
        if (user.equals(EMPTY)) return null;
        return new Gson().fromJson(user, User.class);
    }

}
