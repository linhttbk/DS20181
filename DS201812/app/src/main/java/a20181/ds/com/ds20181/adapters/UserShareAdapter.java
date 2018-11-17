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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private ItemClick itemClick;

    public UserShareAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mItems = objects;
        clones = new ArrayList<>(objects);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface ItemClick {
        void onItemClick(User user);
    }

    public void replace(List<User> list) {
        if (list == null || list.isEmpty()) return;
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPer(1);
        }
        mItems.clear();
        clones.clear();
        mItems.addAll(list);
        clones.addAll(list);
        Log.e("replace: ", "hello");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e("getView: ", "Hello" + position + " " + mItems.size());
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_file, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final User user = mItems.get(position);
        if (user != null) {
            ((ViewHolder) holder).tvName.setText(user.getName());
            if (user.getPer() == 1) ((ViewHolder) holder).btnRead.setChecked(true);
            if (user.getPer() == 2) ((ViewHolder) holder).btnWrite.setChecked(true);
            ((ViewHolder) holder).tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClick != null) itemClick.onItemClick(user);
                }
            });
            ((ViewHolder) holder).radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.btnRead:
                            user.setPer(1);
                            break;
                        case R.id.btnWrite:
                            user.setPer(2);
                            break;
                    }
                }
            });
        }

        return row;
    }

    public class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.btnRead)
        RadioButton btnRead;
        @BindView(R.id.btnWrite)
        RadioButton btnWrite;
        @BindView(R.id.radioGroup)
        RadioGroup radioGroup;

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
                            if (user.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                            User user = (User) object;
                            user.setPer(1);
                            mItems.add(user);
                        }
                    }
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
