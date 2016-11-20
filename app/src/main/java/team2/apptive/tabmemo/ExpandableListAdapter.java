package team2.apptive.tabmemo;

/**
 * Created by solar on 2016-11-20.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapter extends ArrayAdapter<ListViewItem> {
    private ArrayList<ListViewItem> listItems;
    private Context context;

    public ExpandableListAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> listItems) {
        super(context, textViewResourceId, listItems);
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = null;
        ListViewItem listItem = listItems.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item, null);

            LinearLayout textViewWrap = (LinearLayout) convertView.findViewById(R.id.text_wrap);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            LinearLayout llcontent = (LinearLayout) convertView.findViewById(R.id.ll_content);
            holder = new ListViewHolder(llcontent);

            // setViewWrap IS REQUIRED
            holder.setViewWrap(textViewWrap);

        } else {
            holder = (ListViewHolder) convertView.getTag();
        }

        // THIS IS REQUIRED
        holder.getViewWrap().setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, listItem.getCurrentHeight()));

        // holder.getTextView().setText(listItem.getMemo());
        holder.getLinearLayout().setScaleY(10);

        holder.getTextView().setCompoundDrawablesWithIntrinsicBounds(listItem.getDrawable(), 0, 0, 0);



        convertView.setTag(holder);

        // setHolder IS REQUIRED
        listItem.setHolder(holder);

        return convertView;
    }

}