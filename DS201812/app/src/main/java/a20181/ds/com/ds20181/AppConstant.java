package a20181.ds.com.ds20181;

import com.squareup.otto.Bus;

import a20181.ds.com.ds20181.customs.GlobalBus;

public interface AppConstant {
    boolean DEBUG = true;
    String SOCKET_URL = "http://192.168.0.107:1232";
    AppState app = AppState.getInstance();
    Bus bus = GlobalBus.getBus();
    int CODE_200 = 200;
    int CODE_1001 = 1001;
    int CODE_1002 = 1002;
    String PREF_USER = "login";
    String PREF_LIST = "listUser";
    String PREF_COOKIES = "ck";
    String EMPTY = "";
    String EVENT_TEST = "test";
    String EVENT_ADD = "add";
}
