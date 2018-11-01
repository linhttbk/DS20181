package a20181.ds.com.ds20181;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

import a20181.ds.com.ds20181.customs.BaseApplication;

public class MainApplication extends BaseApplication implements AppConstant {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
