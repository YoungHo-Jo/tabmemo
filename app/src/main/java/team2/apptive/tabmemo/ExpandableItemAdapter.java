package team2.apptive.tabmemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by solar on 2016-11-20.
 */

/**
 * Adapter for our list of {@link ExpandableItem.GroupItem}s.
 */
public class ExpandableItemAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
	private LayoutInflater inflater;
	private List<ExpandableItem.GroupItem> items;
	private View view;
	private ExpandableItem.ChildHolder holder;
	private DBHelper dbHelper;

	// Constructor
	public ExpandableItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<ExpandableItem.GroupItem> items) {
		this.items = items;
	}

	@Override
	public ExpandableItem.ChildItem getChild(int groupPosition, int childPosition) {
		return items.get(groupPosition).cItems.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		ExpandableItem.ChildItem item = getChild(groupPosition, childPosition);

		System.out.println("getRealChildView GroupPostion: " + groupPosition + " chilPosition " + childPosition);
		if (convertView == null) {
			holder = new ExpandableItem.ChildHolder();
			convertView = inflater.inflate(R.layout.child_list_item, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.tv_clickableTextMemo);

			convertView.setTag(holder);
			view = convertView;
		} else {
			holder = (ExpandableItem.ChildHolder) convertView.getTag();
		}


		holder.title.setText(item.title);

		return convertView;
	}


	@Override
	public int getRealChildrenCount(int groupPosition) {
		return items.get(groupPosition).cItems.size();
	}

	@Override
	public ExpandableItem.GroupItem getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ExpandableItem.GroupHolder holder;
		ExpandableItem.GroupItem item = getGroup(groupPosition);
		if (convertView == null) {
			holder = new ExpandableItem.GroupHolder();
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		} else {
			holder = (ExpandableItem.GroupHolder) convertView.getTag();
		}

		holder.title.setText(item.title);


		System.out.println("getGroupView!!");

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}


}
