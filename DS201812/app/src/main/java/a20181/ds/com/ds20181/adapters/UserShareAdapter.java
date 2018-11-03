package a20181.ds.com.ds20181.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserShareAdapter extends ArrayAdapter<User> {
    private List<User> mItems;
    private List<User> clones;

    public UserShareAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mItems = new ArrayList<>(objects);
        clones = new ArrayList<>(objects);
    }


    public void replace(List<User> list) {
        if (list == null || list.isEmpty()) return;
        mItems.clear();
        clones.clear();
        mItems.addAll(list);
        clones.addAll(list);
        Log.e("replace: ", "hello");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e("getView: ", "Hello");
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_file, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        ((ViewHolder) holder).tvName.setText(mItems.get(position).getName());
        return row;
    }

    public class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;

        public ViewHolder(View row) {
            ButterKnife.bind(this, row);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                List<User> suggestions = new ArrayList<>();
                if (charSequence != null) {
                    if (clones != null && !clones.isEmpty()) {
                        for (User user : clones) {
                            if (user.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                                suggestions.add(user);
                            }
                        }
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mItems.clear();
                if (filterResults != null && filterResults.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) filterResults.values) {
                        if (object instanceof User) {
                            mItems.add((User) object);
                        }
                    }
                    clones.clear();
                    clones.addAll(mItems);
                    notifyDataSetChanged();
                } else if (charSequence == null) {
                    // no filter, add entire original list back in
                    mItems.addAll(clones);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
