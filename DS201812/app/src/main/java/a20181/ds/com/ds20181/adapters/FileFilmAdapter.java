package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileFilmAdapter extends BaseRecyclerViewAdapter<FileFilm> implements AppConstant {
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        FileFilm film = mDataList.get(position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).tvHeader.setText(film.getName());
        } else if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvTitle.setText(film.getName());
            String createAt = StringUtils.formatLongToDate(film.getCreateAt());
            ((ViewHolder) holder).tvDate.setText(StringUtils.isEmpty(createAt) ? AppConstant.EMPTY : createAt);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder.itemView, position);
            }
        });
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
        @BindView(R.id.root)
        RelativeLayout root;
        @BindView(R.id.imgMore)
        ImageView imgMore;

        @OnClick(R.id.root)
        public void onItemClick() {
            mItemClickListener.onItemClick(this.root, getAdapterPosition());
        }

        @OnClick(R.id.imgMore)
        public void onMoreClick() {
            mItemClickListener.onItemClick(this.imgMore, getAdapterPosition());
        }


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
