package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.FileRecord;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends BaseRecyclerViewAdapter<FileRecord> {

    public RecordAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.record_item,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        FileRecord fileRecord = mDataList.get(position);
        ((ViewHolder)holder).tvRecorderName.setText(fileRecord.getUserId());
        ((ViewHolder)holder).tvRecordTime.setText(fileRecord.getTime());
        ((ViewHolder)holder).tvRecordContent.setText(fileRecord.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder.itemView.getRootView(), position);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_recorder_name)
         TextView tvRecorderName;

        @BindView(R.id.tv_recorder_time)
         TextView tvRecordTime;

        @BindView(R.id.tv_record_content)
         TextView tvRecordContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
