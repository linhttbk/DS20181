package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a20181.ds.com.ds20181.R;
import butterknife.ButterKnife;

public class RecordFileFragment extends Fragment {

    public static RecordFileFragment newInstance() {
        Bundle args = new Bundle();
        RecordFileFragment fragment = new RecordFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_file_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
