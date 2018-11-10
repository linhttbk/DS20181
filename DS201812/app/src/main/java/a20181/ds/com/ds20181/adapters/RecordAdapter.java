package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends BaseRecyclerViewAdapter<FileRecord> {

    public RecordAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.record_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        FileRecord fileRecord = mDataList.get(position);
        ((ViewHolder) holder).tvRecorderName.setText(fileRecord.getSpeaker());
        ((ViewHolder) holder).tvRecordTime.setText(StringUtils.timeConversion(fileRecord.getTime()));
        ((ViewHolder) holder).tvRecordContent.setText(fileRecord.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder.itemView.getRootView(), position);
            }
        });
    }

    @Override
    public void update(int position, FileRecord record) {
        super.update(position, record);
        if (mDataList.size() > 0) {
            Collections.sort(mDataList, new Comparator<FileRecord>() {
                @Override
                public int compare(FileRecord record, FileRecord t1) {
                    if (record.getTime() == t1.getTime()) return 0;
                    if (record.getTime() > t1.getTime()) return 1;
                    return -1;
                }
            });
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvRecorderName;
        @BindView(R.id.tvTime)
        TextView tvRecordTime;
        @BindView(R.id.tvContent)
        TextView tvRecordContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
