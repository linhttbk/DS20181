package a20181.ds.com.ds20181;

import com.squareup.otto.Bus;

import a20181.ds.com.ds20181.customs.GlobalBus;
import io.socket.client.Socket;

public interface AppConstant {
    boolean DEBUG = true;
    //String SOCKET_URL = "http://192.168.0.107:1232";
    // String SOCKET_URL = "http://192.168.0.107:8000";
    String SOCKET_URL = "https://dfp-server.herokuapp.com";
    String BASE_URL = "https://dfp-server.herokuapp.com/api/";
    AppState app = AppState.getInstance();
    Bus bus = GlobalBus.getBus();
    int CODE_200 = 200;
    int CODE_204 = 204;
    int CODE_1001 = 1001;
    int CODE_1002 = 1002;
    String PREF_USER = "login";
    String PREF_LIST = "listUser";
    String PREF_COOKIES = "ck";
    String EMPTY = "";
    String EVENT_TEST = "test";
    String EVENT_ADD = "add";
    String EVENT_CLICK_RECORD = "click_record";
    String EVENT_UN_FOCUS_RECORD = "un_focus_record";
    String EVENT_EDIT_RECORD = "edit_record";
    String EVENT_IMPORT_RECORD = "create_many_record";

    String POPUP_CONSTANT = "mPopup";
    String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    int REQUEST_READ_STORAGE = 112;
    int FILE_REQUEST_CODE = 97;


    int PERMISSION_READ = 1;
    int PERMISSION_WRITE = 2;
}
