package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.History;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends BaseRecyclerViewAdapter<History> {

    public HistoryAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.record_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        History history = getItem(position);
        if (history != null && holder instanceof  ViewHolder) {
            ((ViewHolder) holder).tvActiveUsers.setVisibility(View.GONE);
            ((ViewHolder) holder).vwDivider.setBackgroundResource(R.color.divider);
            ((ViewHolder) holder).tvRecorderName.setText(history.getAuthor());
            ((ViewHolder) holder).tvRecordTime.setText(StringUtils.formatLongToDate(history.getTime()));
            ((ViewHolder) holder).tvRecordContent.setText(history.getMessage());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvRecorderName;
        @BindView(R.id.tvTime)
        TextView tvRecordTime;
        @BindView(R.id.tvContent)
        TextView tvRecordContent;

        @BindView(R.id.tv_active_users)
        TextView tvActiveUsers;

        @BindView(R.id.vw_divider)
        View vwDivider;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
