package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater mInflater;

    public List<T> getAll() {
        if (mDataList == null) return new ArrayList<>();
        return mDataList;
    }

    protected List<T> mDataList;
    protected ItemClickListener mItemClickListener;

    protected BaseRecyclerViewAdapter(@NonNull Context context,
                                      ItemClickListener itemClickListener) {
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
        mDataList = new ArrayList<>();
    }

    public void add(List<T> itemList) {
        if(itemList==null||itemList.isEmpty()) return;
        mDataList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        if (mDataList == null) mDataList = new ArrayList<>();
        mDataList.add(t);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < 0 || mDataList == null || position >= mDataList.size()) return;
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void set(List<T> dataList) {
        List<T> clone = new ArrayList<>(dataList);
        mDataList.clear();
        mDataList.addAll(clone);
        notifyDataSetChanged();
    }

    public void update(int position, T t) {
        if (position < 0 || mDataList == null || position >= mDataList.size() || t == null) return;
        mDataList.set(position, t);
        notifyItemChanged(position);
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (position < 0 || position >= mDataList.size()) return null;
        return mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
//        void onPubpubClick(View view,ArrayList<Video> videos, int position);
//        void onItemClick(ArrayList<Video> videos, int position);
    }
}