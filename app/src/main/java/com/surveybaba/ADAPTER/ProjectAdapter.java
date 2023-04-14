package com.surveybaba.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.surveybaba.DTO.ProjectDTO;
import com.surveybaba.R;
import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends BaseAdapter {

    private Context context;
    private List<ProjectDTO> values;
    private List<ProjectDTO> filteredValues;
    private LayoutInflater mInflater;
    private Filter mFilter = new ItemFilter();

    public ProjectAdapter(Context context, List<ProjectDTO> values) {
        this.context = context;
        this.values = values;
        this.filteredValues = values;
        mInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredValues.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null ) {
            convertView = mInflater.inflate(R.layout.list_project, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setDetails(position);
        return convertView;
    }

    class ViewHolder {
        private View view;
        private TextView tvName;
        ViewHolder(View view) {
            this.view = view;
            tvName = (TextView) view.findViewById(R.id.tvName);
        }

        private void setDetails(int position) {
            ProjectDTO dto = values.get(position);
            tvName.setText(dto.getProject());
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<ProjectDTO> list = values;
            int count = list.size();
            final ArrayList<ProjectDTO> nlist = new ArrayList<ProjectDTO>(count);
            String filterableString ;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getProject();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                    continue;
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredValues = (List<ProjectDTO>) results.values;
            notifyDataSetChanged();
        }
    }

}
