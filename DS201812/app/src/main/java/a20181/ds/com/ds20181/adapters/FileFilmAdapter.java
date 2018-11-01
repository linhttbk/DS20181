package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FileFilmAdapter extends BaseRecyclerViewAdapter<FileFilm> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public FileFilmAdapter(@NonNull Context context, ItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = mInflater.inflate(R.layout.item_header, parent, false);
            return new HeaderHolder(view);
        } else {
            view = mInflater.inflate(R.layout.item_file_film, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FileFilm film = mDataList.get(position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).tvHeader.setText(film.getName());
        } else if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvTitle.setText(film.getName());
            String createAt = StringUtils.formatLongToDate(film.getCreateAt());
            ((ViewHolder) holder).tvDate.setText(StringUtils.isEmpty(createAt) ? AppConstant.EMPTY : createAt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).isHeader()) return TYPE_HEADER;
        return TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDate)
        TextView tvDate;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHeader)
        TextView tvHeader;


        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
