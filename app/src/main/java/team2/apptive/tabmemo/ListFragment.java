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

		// listview divider 조정
		listView.setDividerHeight(0);

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

				isLongClicked = false;
				listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						System.out.println("group item is long clicked!");

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


				System.out.println("onGroupClick!! " + groupPosition + " " + id);
				return true;
			}
		});


//		// 자식 클릭
//		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//				// 확인용
//				System.out.println("onChildClicked!! " + groupPosition + " " + childPosition + " " + id);
//
//				// 메모 넣을 framelayout 띄우기
//				// 애니메이션을 적용하였으나 부드럽게 뜨지않음
//				// 맨 아랫단에 위치하여 알파를 조정하면서 올라오기 때문에 그런듯보임
//				final FrameLayout flMemoModifying = (FrameLayout) getActivity().findViewById(R.id.fl_memoInsertion);
//
//				// 애니메이션 새로운 적용
//				Animation fadeInAnimation = new AlphaAnimation(0, 1);
//				fadeInAnimation.setDuration(500);
//				flMemoModifying.setVisibility(View.VISIBLE);
//				flMemoModifying.setAnimation(fadeInAnimation);
//
//				final EditText etMemoInsertion = (EditText) getActivity().findViewById(R.id.et_memoInsertion);
//
//				// childitem 불러오기
//				final ExpandableItem.ChildItem childItem = adapter.getChild(groupPosition, childPosition);
//				final String childId = childItem.id;
//
//				// 메모 넣을 frameLayout 에 기존의 메모 넣기
//				etMemoInsertion.setText(childItem.memo);
//
//
//				// 확인버튼 클릭시
//				getActivity().findViewById(R.id.bt_confirmInsertion).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// 수정된 메모 내용 가져오기
//						String modifiedMemo = etMemoInsertion.getText().toString();
//
//						// adapter 의 getChildView 를 통해서 업데이트 되게 하기 return: string
//						childItem.memo = dbHelper.updateMemo(modifiedMemo, childId);
//
//						// listview가 보이는 상태로 로 돌아가기
//						// 애니메이션
//						Animation fadeOutAnimation = new AlphaAnimation(1, 0);
//						fadeOutAnimation.setDuration(500);
//						flMemoModifying.setVisibility(View.INVISIBLE);
//						flMemoModifying.setAnimation(fadeOutAnimation);
//						hideSoftKeyboard(getActivity());
//					}
//				});
//
//				// 취소버튼 클릭시
//				getActivity().findViewById(R.id.bt_cancelInsertion).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// 저장하지 않고 그냥 돌아가기
//
//						Animation fadeOutAnimation = new AlphaAnimation(1, 0);
//						fadeOutAnimation.setDuration(500);
//						flMemoModifying.setVisibility(View.INVISIBLE);
//						flMemoModifying.setAnimation(fadeOutAnimation);
//						hideSoftKeyboard(getActivity());
//					}
//				});
//				// update child view
//				adapter.getRealChildView(groupPosition, childPosition, true, v, parent);
//
//
//				return false;
//			}
//		});


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

			item.id = id; // give item an id
			item.title = cursor.getString(1); // give item a memo

			citem.memo = cursor.getString(2); // give child item a memo
			citem.id = item.id; // give child item a same id


			item.cItems.add(citem); // connect with item and citem

			items.add(item); // inserting to array
		}

	}

	public void setCategoryForListView(String _category) {
		category = _category;
	}

	public void addNewTitleMemo(String title, String category) {
		String id = dbHelper.newInsert(title, category);

	}

	public void refreshFragment() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		listView.deferNotifyDataSetChanged();
		ft.detach(this).attach(this).commit();
	}

	public ExpandableItemAdapter getAdapter() {
		return adapter;
	}


}