package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentStreamBCAdapter extends BaseRecyclerViewAdapter<FileRecord> {

    public ContentStreamBCAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_stream, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvTime.setText(StringUtils.timeConversion(getItem(position).getTime()));
            ((ViewHolder) holder).tvContent.setText(getItem(position).getContent());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvContent)
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


