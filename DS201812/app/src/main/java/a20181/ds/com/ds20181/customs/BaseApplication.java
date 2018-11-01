package a20181.ds.com.ds20181.customs;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class BaseApplication extends Application {
    private static WeakReference<Context> context;

    public static Context getAppContext() {
        if (context == null) return null;
        return context.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<Context>(this);
        onCreateApplication();
    }

    public void onCreateApplication() {

    }
}
