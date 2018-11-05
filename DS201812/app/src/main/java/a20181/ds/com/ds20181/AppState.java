package a20181.ds.com.ds20181;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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

    public void setCurrentUser(User user) {
        if (user == null) return;
        String data = new Gson().toJson(user);
        PrefUtil.savePreferences(MainApplication.getAppContext(), PREF_USER, data);

    }

    public User getCurrentUser() {
        Context context = MainApplication.getAppContext();
        if (context == null) return null;
        String user = PrefUtil.getPreferences(context, PREF_USER, EMPTY);
        if (user.equals(EMPTY)) return null;
        return new Gson().fromJson(user, User.class);
    }

    public String getCookie() {
        return getCurrentUser().getCookie();
    }

    public void setListUser(List<User> data) {
        if (data == null || data.isEmpty()) return;
        String pref = new Gson().toJson(data);
        PrefUtil.savePreferences(MainApplication.getAppContext(), PREF_LIST, pref);
    }

    public List<User> getListUser() {
        Context context = MainApplication.getAppContext();
        if (context == null) return null;
        String dataList = PrefUtil.getPreferences(context, PREF_LIST, EMPTY);
        if (dataList.equals(EMPTY)) return null;
        List<User> results = new Gson().fromJson(dataList, new TypeToken<List<User>>() {
        }.getType());
        for(int i = 0 ; i < results.size() ; i++){
            User user = results.get(i);
            if(getCurrentUser()==null) break;
            if(getCurrentUser().getUserId().equals(user.getUserId())){
                results.remove(user);
            }
        }
        return results;
    }
}
