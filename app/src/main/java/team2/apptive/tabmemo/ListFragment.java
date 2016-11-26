package team2.apptive.tabmemo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by solar on 2016-11-20.
 */

public class ListFragment extends Fragment {

	private ArrayList<ExpandableItem.GroupItem> items = new ArrayList<ExpandableItem.GroupItem>();
	private AnimatedExpandableListView listView;
	private ExpandableItemAdapter adapter;
	private View view;
	private DBHelper dbHelper = null;
	private String category = "";
	private boolean isLongClicked;
	private boolean isAddedNewMemo;

	public static ListFragment newInstance() {
		return new ListFragment();
	}


	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		System.out.println("ListView onCreateView Called!");
		view = inflater.inflate(R.layout.list_layout, container, false);

		// Open Db
		dbHelper = new DBHelper(view.getContext(), "Memo.db", null, 1);

		// make group items to display
		makeGroupItemsForViewByCategory(category);

		// listview 에 보이게할 내용을 adapter 에 연결
		adapter = new ExpandableItemAdapter(view.getContext());
		adapter.setData(items);

		// listivew에 adapter 연결
		listView = (AnimatedExpandableListView) view.findViewById(R.id.ll_expandable);
		listView.setAdapter(adapter);

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				isLongClicked = false;
				listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

						TextView titleView = (TextView) view.findViewById(R.id.tv_title);
						System.out.println("group item is long clicked! " + titleView.getText());
						titleView.setText("haha");


						// 타이틀 수정 기능 필요

						isLongClicked = true;
						return true;
					}
				});

				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (!isLongClicked) { // is not long clicked
					if (listView.isGroupExpanded(groupPosition)) {
						listView.collapseGroupWithAnimation(groupPosition);
					} else {
						listView.expandGroupWithAnimation(groupPosition);
					}
				}

				listView.requestFocus();
				System.out.println("onGroupClick!! " + groupPosition + " " + id + " focused?: " + listView.isFocused());
				return true;
			}
		});

		// new memo will be expanded and have a cursor on it
		if(isAddedNewMemo) {
			listView.expandGroupWithAnimation(0);
			adapter.setIsNewMemo(true);
		}

		return view;
	}

	public void makeGroupItemsForViewByCategory(String category) {
		ExpandableItem.GroupItem item;
		ExpandableItem.ChildItem citem;

		// from db, inserting to group item and child item
		// 카테고리 설정 시 여기서 디비로 불러올때 사용할 방법을 정하면된다
		// 현재 시간 내림차순으로 정렬해서 출력한다.
		// 나중에 여기에서 isstared과 time 을 잘 조합해서 출력하면 중요표시된것도 가능
		Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select * from MEMO where category = '" + category + "'" + " order by time desc", null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(7); // db: position
			item = new ExpandableItem.GroupItem(); // new group item
			citem = new ExpandableItem.ChildItem(); // new child item

			if(cursor.getString(2) != null) {
				item.id = id; // give item an id
				item.title = cursor.getString(1); // give item a memo

				citem.memo = cursor.getString(2); // give child item a memo
				citem.id = item.id; // give child item a same id


				item.cItems.add(citem); // connect with item and citem

				items.add(item); // inserting to array
			}
		}

	}

	public void setCategoryForListView(String _category) {
		category = _category;
	}

	public void addNewTitleMemo(String title, String category) {
		dbHelper.newInsert(title, category);
		isAddedNewMemo = true;
	}

	public void refreshFragment() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		listView.deferNotifyDataSetChanged();
		ft.detach(this).attach(this).commit();
	}

	public ExpandableItemAdapter getAdapter() {
		return adapter;
	}

	public ListFragment setIsAddedNewMemo(boolean _isAddedNewMemo) {
		isAddedNewMemo = _isAddedNewMemo;
		return this;
	}
}