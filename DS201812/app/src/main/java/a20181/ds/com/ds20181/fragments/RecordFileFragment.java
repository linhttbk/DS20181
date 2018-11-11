package a20181.ds.com.ds20181.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.BaseRecyclerViewAdapter;
import a20181.ds.com.ds20181.adapters.FileFilmAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecordFileFragment extends BaseFragment implements BaseRecyclerViewAdapter.ItemClickListener, AppConstant {
    @BindView(R.id.rcvList)
    RecyclerView recyclerView;
    FileFilmAdapter adapter;
    private List<FileFilm> mItems = new ArrayList<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Dialog dialog;

    public static RecordFileFragment newInstance() {
        Bundle args = new Bundle();
        RecordFileFragment fragment = new RecordFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.record_file_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        if (getContext() != null) {
            adapter = new FileFilmAdapter(getContext(), this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void initData() {
        super.initData();
        initFileFilm();

    }

    private void initFileFilm() {
        if (getActivity() != null)
            ((MainActivity) getActivity()).showLoading(true);

        User user = app.getCurrentUser();
        if (user == null) return;
        Log.e("initFileFilm: ", user.getUserId() + " xx" + app.getCurrentUser().getCookie());
        Observable<List<FileFilm>> request1 = AppClient.getAPIService().getAllCreatorFile(app.getCurrentUser().getCookie(), user.getUserId());
        Observable<List<FileFilm>> request2 = AppClient.getAPIService().getAllOwnerFile(app.getCurrentUser().getCookie(), user.getUserId());
        Observable<Object> objectObservable = Observable.zip(request1, request2, new BiFunction<List<FileFilm>, List<FileFilm>, Object>() {

            @Override
            public List<FileFilm> apply(List<FileFilm> results1, List<FileFilm> results2) throws Exception {
                List<FileFilm> results = new ArrayList<>();
                if (results1 != null && !results1.isEmpty()) {

                    FileFilm film = new FileFilm();
                    film.setName(getString(R.string.creator_file));
                    film.setHeader(true);
                    results.add(film);
                    results.addAll(results1);
                }
                if (results2 != null && !results2.isEmpty()) {
                    FileFilm film = new FileFilm();
                    film.setName(getString(R.string.owner_file));
                    film.setHeader(true);
                    results.add(film);
                    results.addAll(results2);
                }
                return results;
            }
        });
        Disposable disposable = objectObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (getActivity() != null)
                            ((MainActivity) getActivity()).showLoading(false);
                        List<FileFilm> result = (ArrayList) o;
                        adapter.set(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @OnClick(R.id.btnAdd)
    public void onAddClick() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).switchFragment(CreateFileFragment.newInstance());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        FileFilm film = adapter.getItem(position);
        if (film == null || film.isHeader() || app.getCurrentUser() == null) return;
        switch (view.getId()) {
            case R.id.root:
                String id = film.getId();
                ((MainActivity) getActivity()).showContentRecord(id);
                break;
            case R.id.imgMore:
                final PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.getMenuInflater().inflate(R.menu.dialog_menu, popup.getMenu());
                //Show icon for menu
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals(POPUP_CONSTANT)) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.share_file:
                                popup.dismiss();
                                break;
                        }
                        return false;
                    }
                });

                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
