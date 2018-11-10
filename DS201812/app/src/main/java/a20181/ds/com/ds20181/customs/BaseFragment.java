package a20181.ds.com.ds20181.customs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainApplication;
import io.socket.client.Socket;

public abstract class BaseFragment extends Fragment implements AppConstant {
    public abstract int getLayoutResource();

    protected Socket sk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        initView(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sk = ((MainApplication) (getActivity().getApplication())).getSocket();
        sk.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sk.disconnect();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bus.unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public void initView(View view) {

    }

    public void initData() {

    }

    public Socket getSocket() {
        return sk;
    }
}
