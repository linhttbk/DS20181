package a20181.ds.com.ds20181.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.RecordAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.customs.InputFilterMinMax;
import a20181.ds.com.ds20181.models.CreateRecordBody;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.models.UpdateRecordBody;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.patterns.Command;
import a20181.ds.com.ds20181.patterns.CommandCallBack;
import a20181.ds.com.ds20181.patterns.CommandStack;
import a20181.ds.com.ds20181.patterns.ErrorCallback;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.emitter.Emitter;

public class RecordContentFragment extends BaseFragment implements RecordAdapter.ItemClickListener, AppConstant, CommandCallBack, ErrorCallback {

    @BindView(R.id.rcv_content_recorded)
    RecyclerView rcvContent;


    private RecordAdapter recordAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FileFilm fileFilm = null;
    private String userId = app.getCurrentUser().getUserId();

    private CommandStack commandStack = new CommandStack();

    Emitter.Listener clickRecord = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                //  final JSONObject data = new JSONObject(args[0].toString());
                final JSONObject data = (JSONObject) args[0];
                final String userActiveId = data.getString("userId");
                final String recordId = data.getString("recordId");
                final String userName = data.getString("name");
                if (userActiveId.equals(userId)) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User();
                        user.setUserId(userActiveId);
                        user.setName(userName);
                        recordAdapter.setUserActives(recordId, user);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Emitter.Listener unFocusRecord = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                //   final JSONObject data = new JSONObject(args[0].toString());
                final JSONObject data = (JSONObject) args[0];
                final String userActiveId = data.getString("userId");
                final String recordId = data.getString("recordId");
                final String userName = data.getString("name");
                if (userActiveId.equals(userId)) return;
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User user = new User();
                            user.setUserId(userActiveId);
                            user.setName(userName);
//                        Toast.makeText(getActivity(), userId + " Unfocus record ", Toast.LENGTH_SHORT).show();
                            recordAdapter.clearUserActives(recordId, user);
                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Emitter.Listener editRecord = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                // final JSONObject data = new JSONObject(args[0].toString());
                final JSONObject data = (JSONObject) args[0];
                JSONObject user = data.getJSONObject("user");
                final String userActiveId = user.getString("_id");
                final String userName = user.getString("name");

                final JSONObject record = data.getJSONObject("record");
                final String recordId = record.getString("_id");
                final String content = record.getString("content");
                final String spealker = record.getString("speaker");
                final long time = record.getLong("time");
                if (userActiveId.equals(userId)) return;
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recordAdapter.update(recordId, spealker, content, time);
//                            JsonObject jsonObject = new JsonObject();
//                            jsonObject.addProperty("userId",userActiveId);
//                            jsonObject.addProperty("recordId", recordId);
//                            jsonObject.addProperty("userName",app.getCurrentUser().getName());
//                            getSocket().emit(EVENT_UN_FOCUS_RECORD,jsonObject);

                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Emitter.Listener importRecord = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray array = (JSONArray) args[0];
                if (array != null) {
                    final List<FileRecord> results = new ArrayList<>();
                    int size = array.length();
                    for (int i = 0; i < size; i++) {

                        JSONObject data = array.getJSONObject(i);
                        FileRecord record = new FileRecord();
                        record.setId(data.getString("_id"));
                        record.setSpeaker(data.getString("speaker"));
                        record.setContent(data.getString("content"));
                        record.setTime(data.getLong("time"));
                        record.setCallBack(RecordContentFragment.this);

                        final JSONArray activeArray = data.getJSONArray("userOn");
                        List<User> userOns = new ArrayList<>();
                        if (activeArray != null) {
                            for (int j = 0; j < activeArray.length(); i++) {
                                JSONObject active = activeArray.getJSONObject(i);
                                User user = new User();
                                user.setUserId(active.getString("id"));
                                user.setName(active.getString("name"));
                                userOns.add(user);
                            }
                        }
                        record.setUserActives(userOns);
                        results.add(record);

                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recordAdapter.add(results);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.record_content_fragment;
    }

    public static RecordContentFragment newInstance(FileFilm fileFilm) {
        Bundle args = new Bundle();
        RecordContentFragment fragment = new RecordContentFragment();
        fragment.fileFilm = fileFilm;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        if (getContext() != null) {
            recordAdapter = new RecordAdapter(getContext(), this);
            rcvContent.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvContent.setAdapter(recordAdapter);
        }
    }


    private void exportPDF() {
        if (recordAdapter == null) return;
        List<FileRecord> fileRecords = recordAdapter.getAll();
        if (fileRecords.isEmpty()) {
            Toast.makeText(getContext(), "File đang trống", Toast.LENGTH_SHORT).show();
        } else {
            if (getActivity() != null)
                ((MainActivity) getActivity()).showExportFragment(fileRecords);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_record_content, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_compare) {
            return true;
        } else if (id == R.id.action_export) {
            exportPDF();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        super.initData();
        initFileRecord();
        commandStack.setCallBack(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getSocket().on(EVENT_CLICK_RECORD, clickRecord);
        getSocket().on(EVENT_UN_FOCUS_RECORD, unFocusRecord);
        getSocket().on(EVENT_EDIT_RECORD, editRecord);
        getSocket().on(EVENT_IMPORT_RECORD, importRecord);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showAllIcon(true);
        }
    }


    private void initFileRecord() {

        User user = app.getCurrentUser();
        if (user == null) {
            return;
        }
        ((MainActivity) getActivity()).showLoading(true);
        Log.e("initFileRecord: ", user.getUserId() + " xx " + app.getCurrentUser().getCookie() + " " + fileFilm.getId());
        Observable<List<FileRecord>> request = AppClient.getAPIService().getRecordFile(app.getCurrentUser().getCookie(), fileFilm.getId());

        Disposable disposable = request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (getActivity() != null)
                            ((MainActivity) getActivity()).showLoading(false);
                        List<FileRecord> result = (ArrayList) o;
                        for (FileRecord record : result) {
                            record.setCallBack(RecordContentFragment.this);
                        }
                        recordAdapter.set(result);
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

    @Override
    public void onItemClick(View view, int position) {
        User user = app.getCurrentUser();
        if (user == null) {
            return;
        }
        if (!fileFilm.isCreator(user.getUserId()) && !fileFilm.isWriteAble(user.getUserId())) {
            showDialogPermission();
        } else {
            FileRecord fileRecord = recordAdapter.getItem(position);
            if (fileRecord != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userId", app.getCurrentUser().getUserId());
                    jsonObject.put("recordId", fileRecord.getId());
                    jsonObject.put("name", app.getCurrentUser().getName());
                    getSocket().emit(EVENT_CLICK_RECORD, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showDialogUpdateRecord(fileRecord, position);
            }
        }
    }

    private void showDialogUpdateRecord(final FileRecord record, final int position) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_update_record);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtContent = dialog.findViewById(R.id.edtContent);
//        final EditText edtHour = dialog.findViewById(R.id.edtHour);
//        final EditText edtMinutes = dialog.findViewById(R.id.edtMinutes);
//        final EditText edtSecond = dialog.findViewById(R.id.edtSecond);
        final EditText edtTime = dialog.findViewById(R.id.edtTime);
        edtName.setText(record.getSpeaker());
        edtContent.setText(record.getContent());
        edtTime.setText(StringUtils.formatLongToDate(record.getTime()));
        //   edtHour.setText(StringUtils.getHourFromTime(record.getTime()));
        //  edtMinutes.setText(StringUtils.getMinuteFromTime(record.getTime()));
        //   edtSecond.setText(StringUtils.getSecondFromTime(record.getTime()));
        final Button update = dialog.findViewById(R.id.btn_update);
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  dialog.dismiss();
                if (app.getCurrentUser() == null) return;
                else {
                    String name = edtName.getText().toString().trim();
                    String content = edtContent.getText().toString().trim();
                    String date = edtTime.getText().toString().trim();

                    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(content) || StringUtils.isEmpty(date)) {
                        Toast.makeText(getContext(), getString(R.string.msg_fill_infor), Toast.LENGTH_SHORT).show();

                    } else if (!StringUtils.isDateValid(date)) {
                        Toast.makeText(getContext(), R.string.time_invalid, Toast.LENGTH_SHORT).show();

                    } else {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(true);
                        }
                        ((MainActivity) getActivity()).showLoading(true);
//
                        long time = StringUtils.convertStringToLong(date);
                        UpdateRecordBody.Options options = new UpdateRecordBody.Options(record.getFileId(), name, time, content);
                        UpdateRecordBody updateRecordBody = new UpdateRecordBody();
                        updateRecordBody.setOptions(options);
                        dialog.dismiss();
                        updateRecord(record, updateRecordBody, position);

                    }
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userId", app.getCurrentUser().getUserId());
                    jsonObject.put("recordId", record.getId());
                    jsonObject.put("name", app.getCurrentUser().getName());
                    getSocket().emit(EVENT_UN_FOCUS_RECORD, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        edtMinutes.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
//        edtSecond.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
        dialog.show();
    }

    private void showDialogPermission() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quyền truy cập");
        builder.setMessage("Bạn không có quyền chỉnh sửa File này");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void undoRedoRecord(final FileRecord item, String recordId, UpdateRecordBody recordBody, final int position, final boolean isUndo) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showLoading(true);
        }
        Disposable disposable = AppClient.getAPIService().updateRecord(app.getCookie(), recordId, recordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileRecord>() {
                    @Override
                    public void accept(FileRecord result) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        if (position != -1)
                            recordAdapter.update(position, result);
                        else
                            recordAdapter.addItem(result);
                        if (item == null) return;
                        item.setCallBack(RecordContentFragment.this);
                        if (isUndo) {
                            commandStack.updateRedo(item);
                        } else {
                            commandStack.updateUndo(item);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        compositeDisposable.add(disposable);

    }

    private void updateRecord(final FileRecord record, UpdateRecordBody recordBody, final int position) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showLoading(true);
        }
        Disposable disposable = AppClient.getAPIService().updateRecord(app.getCookie(), record.getId(), recordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileRecord>() {
                    @Override
                    public void accept(FileRecord result) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), getString(R.string.msg_update_success), Toast.LENGTH_SHORT).show();
                        }
                        recordAdapter.update(position, result);
                        commandStack.doCommand(record);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showAllIcon(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        getSocket().off(EVENT_CLICK_RECORD);

    }

    @Subscribe
    public void onAppAction(AppAction action) {
        if (action == AppAction.UNDO_CLICK) {
            commandStack.undo();
        } else if (action == AppAction.REDO_CLICK) {
            commandStack.redo();
        } else if (action == AppAction.ADD_CLICK) {
            if (getActivity() != null)
                ((MainActivity) getActivity()).switchFragment(RecordStreamFragment.newInstance(fileFilm.getId()));
        }
    }

    @Override
    public void undo(Command command) {
        if (command instanceof FileRecord) {
            FileRecord record = (FileRecord) command;

            UpdateRecordBody.Options options = new UpdateRecordBody.Options(record.getFileId(), record.getSpeaker(), record.getTime(), record.getContent());
            UpdateRecordBody updateRecordBody = new UpdateRecordBody();
            updateRecordBody.setOptions(options);
            String recordId = record.getId();
            FileRecord item = recordAdapter.getRecordByRecordById(recordId);
            int position = recordAdapter.getPositionByRecordById(recordId);
            undoRedoRecord(item, recordId, updateRecordBody, position, true);

        }
    }

    @Override
    public void redo(Command command) {
        if (command instanceof FileRecord) {
            FileRecord record = (FileRecord) command;
            UpdateRecordBody.Options options = new UpdateRecordBody.Options(record.getFileId(), record.getSpeaker(), record.getTime(), record.getContent());
            UpdateRecordBody updateRecordBody = new UpdateRecordBody();
            updateRecordBody.setOptions(options);
            String recordId = record.getId();
            int position = recordAdapter.getPositionByRecordById(recordId);
            FileRecord item = recordAdapter.getRecordByRecordById(recordId);
            undoRedoRecord(item, recordId, updateRecordBody, position, false);

        }
    }

    @Override
    public void errorUndo() {
        Toast.makeText(getContext(), "Không thể Undo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorRedo() {
        Toast.makeText(getContext(), "Không thể Redo", Toast.LENGTH_SHORT).show();
    }

}
