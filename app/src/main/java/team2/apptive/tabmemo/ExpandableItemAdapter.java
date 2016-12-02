package team2.apptive.tabmemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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
	private EditableTextView edittingTextView;
	private DBHelper dbHelper;
	private boolean isNewMemo = false;
	private Context mContext;

	// Constructor
	public ExpandableItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;
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
	public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final ExpandableItem.ChildItem item = getChild(groupPosition, childPosition);

		// System.out.println("getRealChildView GroupPostion: " + groupPosition + " chilPosition " + childPosition);
		if (convertView == null) {
			holder = new ExpandableItem.ChildHolder();
			convertView = inflater.inflate(R.layout.child_list_item, parent, false);
			holder.memo = (EditableTextView) convertView.findViewById(R.id.tv_clickableTextMemo);
			convertView.setTag(holder);
		} else {
			holder = (ExpandableItem.ChildHolder) convertView.getTag();
		}

		holder.memo.setText(item.memo);

		// 메모 수정 클릭
		holder.memo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditableTextView editView = (EditableTextView) v;
				System.out.println("holder.memo is Clicked! current state " + editView.isEditMode() + " view: " + editView);
				// editView.setEditMode(true); // editable memo
				editView.requestFocus(); // focusing on memo
			}
		});

		final ExpandableItemAdapter eia = this;
		holder.memo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("holder.memo.hasFocus changed: " + hasFocus);
				final EditableTextView focusedEditView = (EditableTextView) v;
				dbHelper = new DBHelper(focusedEditView.getContext(), "Memo.db", null, 1);
				InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); // for keyboard
				if (hasFocus) {
					focusedEditView.setEditMode(true);
					inputMethodManager.showSoftInput(focusedEditView, InputMethodManager.SHOW_FORCED);

				} else {
					// Store Memo
					((EditableTextView) v).setEditMode(false);
					isNewMemo = false;
					System.out.println("EditMode is false --> Save memo!!");
					item.memo = focusedEditView.getText().toString();
					if (item.memo.equals("")) { // empty memo
						System.out.println("Empty memo --> Delete memo!!");
						dbHelper.updatMemoToNull(item.id);
						eia.notifyDataSetChanged();
					} else // non-empty memo
						dbHelper.updateMemo(focusedEditView.getText().toString(), item.id);
					// Remove Keyboard
					inputMethodManager.hideSoftInputFromWindow(focusedEditView.getWindowToken(), 0);
				}
			}
		});


		// (개선필요) 한 곳에서 쭉 적다가, 리스트뷰를 내리거나 올려서 수정하던 메모가 destroy 되면 저장이 되지 않는 문제 발생
		// textwatcher를 통해서 실시간으로 저장을 하였으나, 다른 focused 메모 혹은 자식 메모간 혼란으로 인해 다른 메모에 같은 내용이 저장되는 등 버그가 다수 발견
		// 방향: focued 된 view는 destroy 되지않게하고 계속 유지할 수 있게 해주면 좋을듯하다.
		// 위의 경우가 이러나는 일은, 적는도중 리스트를 내렸을때에 해당되므로, 다른 곳을 한번만이라도 눌러서 포커스를 바꾸면 저장이되기 때문에,
		// 임시적으로 내용을 저장하고, 그 것을 리스트를 불러올때 다시 불러와주는 형식으로 진행하고 저장은 포커스가 바뀔떄만 해주는것이 버그를 에방할 수 있을 것이라 판단됨됨		// 또한 자식 메모에 키보드를 통해서 입력 시도시, 여러번 눌러야 하고, 커서의 위치가 부자연스러운등 개선이 필요

		// new Memo for spreading memo and focus it
		if (isNewMemo && groupPosition == 0 && childPosition == 0) {
			holder.memo.setEditMode(true);
			holder.memo.requestFocus();
		}

		// cursor 끝으로
		holder.memo.setSelection(holder.memo.length());

		// 리스트뷰 올라가면 텍스트 사라지는 문제 해결하기
		// if(holder.memo.isEditMode() && holder.memo.isFocused())



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
		LinearLayout colorPostion = (LinearLayout) convertView.findViewById(R.id.listItemCategoryColor);
		colorPostion.setBackground(new ColorDrawable(Color.parseColor(item.categoryColor)));

		// System.out.println("getGroupView!!");


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

	public ExpandableItem.ChildHolder getChildHolder() {
		return holder;
	}

	public void setIsNewMemo(boolean _isNewMemo) {
		isNewMemo = _isNewMemo;
	}

	public boolean isNewMemo() {return isNewMemo;}

	@Override
	public void notifyDataSetChanged() {
		System.out.println("notifydataSetChanged!!");
		for (int i = 0; i < items.size(); i++) {
			if(isNewMemo)
				continue;
			if (items.get(i).cItems.get(0).memo.equals("")) {
				items.remove(i);
				System.out.println("Empty Memo --> Delete From dataSet");
			}
		}
		super.notifyDataSetChanged();
	}

	public EditableTextView getEdittingListView()
	{
		return edittingTextView;
	}

}
