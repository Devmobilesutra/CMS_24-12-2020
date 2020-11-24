package com.cms.callmanager.Foc_Chargeble.searchableSpinnerForFOC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cms.callmanager.R;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter extends BaseAdapter implements Filterable {
    // View lookup cache
    private static class ViewHolder {
        TextView name;

    }

    private List originalData = null;
    private List filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    public DialogAdapter(Context context, List data) {
        this.filteredData = data ;
        this.originalData = data ;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
   /*    // Get the data item for this position
       String user = getItem(position);
       // Check if an existing view is being reused, otherwise inflate the view
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          // If there's no view to re-use, inflate a brand new view for row
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.dialog_list_item, parent, false);
          viewHolder.name = (TextView) convertView.findViewById(R.id.text);
          // Cache the viewHolder object inside the fresh view
          convertView.setTag(viewHolder);
       } else {
           // View is being recycled, retrieve the viewHolder object from tag
           viewHolder = (ViewHolder) convertView.getTag();
       }
       viewHolder.name.setText(user);

       return convertView;*/




        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_list_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.text);

            // Bind the data efficiently with the holder.

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.name.setText(filteredData.get(position).toString());

        return convertView;
   }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List list = originalData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).toString();
                if (filterableString.toLowerCase().replace("-"," ").contains(filterString.toLowerCase())) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List) results.values;
            notifyDataSetChanged();
        }

    }


}