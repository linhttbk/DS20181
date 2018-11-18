package a20181.ds.com.ds20181.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.FilesAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFileFragment extends BaseFragment implements FilesAdapter.ItemClickListener {

    @BindView(R.id.rcv_pdf_file)
    RecyclerView rcvListPdf;

    FilesAdapter filesAdapter;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_list_pdf_file;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this,view);
        if (getContext() != null){
            filesAdapter = new FilesAdapter(getContext(),this);
            rcvListPdf.setLayoutManager(new GridLayoutManager(getContext(),2));
            rcvListPdf.setAdapter(filesAdapter);

        }
    }

    @Override
    public void initData() {
        super.initData();
        filesAdapter.set(getAllPdfFile());
    }

    public static ListFileFragment newInstance(){
        ListFileFragment fileFragment = new ListFileFragment();
        return fileFragment;
    }

    private List<String> getAllPdfFile(){
        String folderPath = getActivity().getApplication().getFilesDir().getAbsolutePath() + File.separator + "PdfFile";

        File folder = new File(folderPath);
        if (folder.isFile() || !folder.exists()){
            return new ArrayList<>();
        }

        return Arrays.asList(folder.list());
    }

    @Override
    public void onItemClick(View view, int position) {
        showFile(filesAdapter.getItem(position));
    }

    public void showFile(String path){
        String folderPath = getActivity().getApplication().getFilesDir().getAbsolutePath() + File.separator + "PdfFile";
        String filePath = folderPath + File.separator + path;
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_pdf_file);
        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                (int) (getResources().getDisplayMetrics().heightPixels * 0.8));
        PDFView pdfView = dialog.findViewById(R.id.pdf_view);

        Log.d("asdfsadf", "showFile: " + path);

        if (StringUtils.isEmpty(filePath)){
            return;
        }

        pdfView.fromFile(new File(filePath))
                .load();
        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
}
