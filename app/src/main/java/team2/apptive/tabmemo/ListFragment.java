package team2.apptive.tabmemo;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by solar on 2016-11-20.
 */

public class ListFragment extends Fragment {

	private ArrayList<ExpandableItem.GroupItem> items;
	private AnimatedExpandableListView listView;
	private ExpandableItemAdapter adapter;
	private View view;
	private DBHelper dbHelper;

	public static ListFragment newInstance() {
		return new ListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.list_layout, container, false);

		items = new ArrayList<ExpandableItem.GroupItem>();

		// open db
		dbHelper = new DBHelper(view.getContext(), "Memo.db", null, 1);

		// items
		ExpandableItem.GroupItem item = null;
		ExpandableItem.ChildItem citem = null;

		// make group items to display
		makeGroupItemsForViewByCategory("");

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
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.

				if (listView.isGroupExpanded(groupPosition)) {
					listView.collapseGroupWithAnimation(groupPosition);
				} else {
					listView.expandGroupWithAnimation(groupPosition);
				}


				System.out.println("onGroupClick!! " + groupPosition + " " + id);
				return true;
			}
		});


		// 자식 클릭
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// 확인용
				System.out.println("onChildClicked!! " + groupPosition + " " + childPosition + " " + id);

				// 메모 넣을 framelayout 띄우기
				getActivity().findViewById(R.id.fl_memoInsertion).setVisibility(View.VISIBLE);
				final EditText etMemoInsertion = (EditText) getActivity().findViewById(R.id.et_memoInsertion);

				// childitem 불러오기
				final ExpandableItem.ChildItem childItem = adapter.getChild(groupPosition, childPosition);
				final String childId = childItem.id;

				// 메모 넣을 frameLayout 에 기존의 메모 넣기
				etMemoInsertion.setText(childItem.title);


				// 확인버튼 클릭시
				getActivity().findViewById(R.id.bt_confirmInsertion).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 수정된 메모 내용 가져오기
						String modifiedMemo = etMemoInsertion.getText().toString();

						// adapter 의 getChildView 를 통해서 업데이트 되게 하기 return: string
						childItem.title = dbHelper.updateMemo(modifiedMemo, childId);

						// listview가 보이는 상태로 로 돌아가기
						getActivity().findViewById(R.id.fl_memoInsertion).setVisibility(View.INVISIBLE);
						hideSoftKeyboard(getActivity());
					}
				});

				// 취소버튼 클릭시
				getActivity().findViewById(R.id.bt_cancelInsertion).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 저장하지 않고 그냥 돌아가기
						getActivity().findViewById(R.id.fl_memoInsertion).setVisibility(View.INVISIBLE);
						hideSoftKeyboard(getActivity());
					}
				});

				// update child view
				adapter.getRealChildView(groupPosition, childPosition, false, v, parent);

				return false;
			}
		});


		return view;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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

			item.id = id; // give item an id
			item.title = cursor.getString(1); // give item a title

			citem.title = cursor.getString(2); // give child item a memo
			citem.id = item.id; // give child item a same id


			item.cItems.add(citem); // connect with item and citem

			items.add(item); // inserting to array
		}

	}


}