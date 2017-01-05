package team2.apptive.tabmemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
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
	private ExpandableItem.ChildHolder holder;
	private DBHelper dbHelper;

	private EditableTextView editingView = null;

	private boolean isNewMemo = false;
	private boolean isEditingMemo = false;
	private int editingGroupPosition = -1;


	private long timer = 0;


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
	public View getRealChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {
		String tag = "AdapterGetRealChildView";
		final ExpandableItem.ChildItem item = getChild(groupPosition, childPosition);

		Log.d(tag, "getRealChildView gP(" + groupPosition + ") cP:(" + childPosition + ")");
		if (convertView == null)
		{
			holder = new ExpandableItem.ChildHolder();
			convertView = inflater.inflate(R.layout.child_list_item, parent, false);
			holder.memo = (EditableTextView) convertView.findViewById(R.id.tv_clickableTextMemo);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ExpandableItem.ChildHolder) convertView.getTag();
		}

		if(groupPosition == editingGroupPosition)
			Log.d(tag, "has focus: " + holder.memo.hasFocus());

		// Set memo content
		holder.memo.setText(item.memo);

		// 메모 수정 클릭
		holder.memo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditableTextView editView = (EditableTextView) v;
				System.out.println("holder.memo is Clicked! current state " + editView.isEditMode() + " view: " + editView);
				System.out.println("GroupPosition: " + groupPosition + " childPosition: " + childPosition);

				editView.requestFocus(); // focusing on memo

				System.out.println("editView has focus: " + editView.hasFocus());
			}
		});

		final ExpandableItemAdapter eia = this;
		holder.memo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String tag = "AdapterOnFocusChange";
				System.out.println(v.getParent());

				// Debug message
				Log.d(tag, "Focus: " + !hasFocus + " --> " + hasFocus + " changed");

				// Type casting to EditableTextView
				final EditableTextView focusedEditView = (EditableTextView) v;

				// Open DB
				dbHelper = new DBHelper(focusedEditView.getContext(), "Memo.db", null, 1);

				// Get InputMethodManager for controlling keyboard
				InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); // for keyboard


				// This is edit mode
				if (v.hasFocus()) {

					isEditingMemo = true;

					// Set edit mode to true to write memo
					focusedEditView.setEditMode(true);

					// Set cursor
					holder.memo.setSelection(holder.memo.length());

					// Save current editing groupPosition
					editingGroupPosition = groupPosition;

					// 키보드 열기
//					inputMethodManager.showSoftInput(focusedEditView, InputMethodManager.SHOW_FORCED);
					Log.d(tag, "Show keyboard");
					// Save editing view
					editingView = focusedEditView;

					timer = System.currentTimeMillis();
				}

				// This is not edit mode
				else {
					// Debug Message
					Log.d(tag, "Save Memo");

					focusedEditView.setEditMode(false);

//					focusedEditView.clearFocus();
					MainActivity mainActivity = (MainActivity) focusedEditView.getContext();
					mainActivity.requestFocusOnFocusLinearLayout();

					item.memo = focusedEditView.getText().toString();

					// empty memo
					if (item.memo.equals(""))
					{
						Log.d(tag, "Empty Memo");
						// Update DB
						dbHelper.updatMemoToNull(item.id);
					}
					else // non-empty memo
					{
						dbHelper.updateMemo(item.memo, item.id); // Store Memo
					}

					// Hide Keyboard
					inputMethodManager.hideSoftInputFromWindow(focusedEditView.getWindowToken(), 0);
					Log.d(tag, "Hide keyboard");

					// Update data set
					// Toast
//					Toast.makeText(v.getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

					// Initializing
					Log.d(tag, "Before Set: isEditingMemo(" + isEditingMemo + ") isNewMemo(" + isNewMemo + ") editingGroupPos(" + editingGroupPosition + ") editingView(" + editingView + ")");
					isEditingMemo = false;
					editingView = null;
					isNewMemo = false;
					editingGroupPosition = -1;
					Log.d(tag, "After Set: isEditingMemo(" + isEditingMemo + ") isNewMemo(" + isNewMemo + ") editingGroupPos(" + editingGroupPosition + ") editingView(" + editingView + ")");


					Activity activity = (Activity) v.getContext();
					Log.d(tag, "GetCurrentFocus before notifyData..." + activity.getCurrentFocus());
					eia.notifyDataSetChanged();
					Log.d(tag, "GetCurrentFocus after notifyData..." + activity.getCurrentFocus());
				}

				dbHelper.close();


			}
		});

		// new Memo for spreading memo and focus it
		if (isNewMemo && groupPosition == 0 && childPosition == 0) {
			// Debug Message
			Log.d(tag, "New Memo: focusing");

			// Edit mode
			holder.memo.requestFocus();
			isEditingMemo = true;
			editingGroupPosition = 0;
			editingView = holder.memo;
		}

		// Control memos that is not in edit mode
//		if(groupPosition != editingGroupPosition)
//		{
//			holder.memo.setEditMode(false);
//		}
//		else
//			Log.d(tag, "gp(" + groupPosition + ") == egP(" + editingGroupPosition + ")");


		Activity activity = (Activity) parent.getContext();
		Log.d(tag, "CurrentFocusView: gP(" + groupPosition + ") cP(" + childPosition + ") view(" +  activity.getCurrentFocus() + ")");

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

//	@Override
//	public boolean areAllItemsEnabled() {
//		return true;
//	}

	public void setIsNewMemo(boolean _isNewMemo) {
		isNewMemo = _isNewMemo;
	}

	public boolean isNewMemo() {return isNewMemo;}

	@Override
	public void notifyDataSetChanged() {
		Log.d("Adapter", "notifyDataSetChanged");
		for (int i = 0; i < items.size(); i++) {
			if(isNewMemo)
				continue;
			if (items.get(i).cItems.get(0).memo.equals("")) {
				items.remove(i);
				Log.d("Adapter", "Remove empty memo in data set");
			}
		}
		Log.d("Adapter", "CurrentFocusView(" + ((Activity)inflater.getContext()).getCurrentFocus() + ")");
		super.notifyDataSetChanged();
	}


	public int getEditingGroupPosition()
	{
		return editingGroupPosition;
	}

	public boolean isEditingMemo()
	{
		return isEditingMemo;
	}

	public EditableTextView getEditingView()
	{
		return editingView;
	}

	public void setEditingGroupPosition(int groupPosition)
	{
		editingGroupPosition = groupPosition;
	}


}
