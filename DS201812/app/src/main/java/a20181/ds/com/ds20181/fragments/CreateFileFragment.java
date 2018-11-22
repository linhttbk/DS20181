package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.UserShareAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.BodyFilePost;
import a20181.ds.com.ds20181.models.ListUserResponse;
import a20181.ds.com.ds20181.models.Owner;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.ListUtil;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreateFileFragment extends BaseFragment implements AppConstant {
    @BindView(R.id.tagLayout)
    TagContainerLayout tagLayout;
    @BindView(R.id.autoComplete)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.edtNameFile)
    EditText edtNameFile;
    UserShareAdapter adapter;
    private List<User> shareList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @BindView(R.id.btnDone)
    Button btn_done;
    @BindView(R.id.edtDate)
    EditText edtDate;
    private long timeSelected = 0;
    @BindView(R.id.edtDesc)
    EditText edtDesc;

    public static CreateFileFragment newInstance() {
        Bundle args = new Bundle();
        CreateFileFragment fragment = new CreateFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.create_file_film_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        tagLayout.setEnableCross(true);
    }

    @Override
    public void initData() {
        super.initData();
        shareList = new ArrayList<>();
        if (app.getCurrentUser() == null) return;

        List<User> allUser = app.getListUser();
        if (allUser != null) {
            adapter = new UserShareAdapter(getContext(), 0, allUser);
            autoCompleteTextView.setAdapter(adapter);
        } else {
            adapter = new UserShareAdapter(getContext(), 0, new ArrayList<User>());
            autoCompleteTextView.setAdapter(adapter);
            ((MainActivity) getActivity()).showLoading(true);
            Disposable disposable = AppClient.getAPIService().getAllUser(app.getCookie())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ListUserResponse>() {
                        @Override
                        public void accept(ListUserResponse listUserResponse) throws Exception {
                            ((MainActivity) getActivity()).showLoading(false);
                            List<User> users = listUserResponse.getData();

                            app.setListUser(users);
                            adapter.replace(app.getListUser());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                    });
            compositeDisposable.add(disposable);
        }
        adapter.setItemClick(new UserShareAdapter.ItemClick() {
            @Override
            public void onItemClick(User user) {
                if (user != null) {
                    boolean contain = false;
                    for (User share : shareList) {
                        if (share.getUserId().equals(user.getUserId())) {
                            contain = true;
                            break;
                        }
                    }
                    if (contain) {
                        Toast.makeText(getContext(), "Người dùng đã tồn tại", Toast.LENGTH_SHORT).show();

                    } else {
                        tagLayout.addTag(user.getName());
                        User share = new User();
                        share.setName(user.getName());
                        share.setUserId(user.getUserId());
                        share.setPer(user.getPer());
                        shareList.add(share);
                    }
                    autoCompleteTextView.dismissDropDown();

                }
            }
        });

        tagLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                tagLayout.removeTag(position);
                if (position >= 0 && position < shareList.size()) {
                    shareList.remove(position);
                }
            }
        });

    }

    @OnClick(R.id.imgDate)
    public void onClickDate() {
        showDialogDate();
    }

    private void showDialogDate() {
        final View dialogView = View.inflate(getContext(), R.layout.dialog_date_meeting, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());

                timeSelected = calendar.getTimeInMillis();
                String dateDisplay = StringUtils.convertLongToDate(timeSelected);
                edtDate.setText(dateDisplay);
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @OnClick(R.id.btnAddFile)
    public void addFile() {
        User currentUser = app.getCurrentUser();
        if (currentUser == null) return;
        String nameFile = edtNameFile.getText().toString();
        String desc = edtDesc.getText().toString();


        if (StringUtils.isEmpty(nameFile)) {
            Toast.makeText(getContext(), "Bạn chưa nhập tên File", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeSelected == 0) {
            Toast.makeText(getContext(), "Bạn chưa chọn thời gian ", Toast.LENGTH_SHORT).show();
            return;
        }
        ((MainActivity) getActivity()).showLoading(true);
        List<Owner> shares = new ArrayList<>();
        if (!ListUtil.isEmpty(shareList)) {
            for (User user : shareList) {
                Log.e("addFile: ", user.getUserId());
                boolean contain = false;
                for (int i = 0; i < shares.size(); i++) {
                    Owner owner = shares.get(i);
                    Log.e("addFile: ", owner.getId());
                    if (owner.getId().equals(user.getUserId())) {
                        Log.e("addFile: ", "True");
                        contain = true;
                        break;
                    }
                }
                if (!contain) {
                    shares.add(new Owner(user.getUserId(), user.getPer()));
                    Log.e("addFile", " Per =  " + user.getPer());
                }

            }


        }
        BodyFilePost fileResponse = new BodyFilePost();
        fileResponse.setName(nameFile);
        fileResponse.setCreateAt(System.currentTimeMillis());
        fileResponse.setOwners(shares);
        fileResponse.setDate(timeSelected);
        fileResponse.setDescription(desc);
        Disposable disposable = AppClient.getAPIService().createFile(app.getCookie(), fileResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileFilm>() {
                    @Override
                    public void accept(FileFilm fileFilm) throws Exception {
                        Log.e("accept: ", fileFilm.getId());
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), "Tạo file thành công", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        compositeDisposable.add(disposable);
    }

    //    private static JsonObject generateRegistrationRequest(String nameFile, long creatAt, List<String> shares) {
//        try {
//            JSONObject subJsonObject = new JSONObject();
//            subJsonObject.put("name", nameFile);
//            subJsonObject.put("creatAt", creatAt);
//            JsonArray array = new JsonArray();
//            for (String id : shares) {
//                array.add(id);
//            }
//            subJsonObject.put("owners", array);
//            JsonParser jsonParser = new JsonParser();
//            JsonObject gsonObject = (JsonObject) jsonParser.parse(subJsonObject.toString());
//            return gsonObject;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    @Subscribe
    public void onAppAction(AppAction action) {
        if (action == AppAction.FINISH_STREAM) {
            btn_done.setVisibility(View.VISIBLE);
            edtNameFile.setEnabled(false);
            autoCompleteTextView.setEnabled(false);
        }
    }

    @OnClick(R.id.btnDone)
    public void onClickDone() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}
