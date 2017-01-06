package team2.apptive.tabmemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

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
	private boolean isLongClicked = false;
	private boolean isAddedNewMemo = false;
	private int isScrolling = 0;

	public static ListFragment newInstance() {
		return new ListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.list_layout, container, false);

		// Open Db
		dbHelper = new DBHelper(view.getContext(), "Memo.db", null, 1);

		// make group categoryItems to display
		makeGroupItemsForViewByCategory(category);

		// listview 에 보이게할 내용을 categoryListAdapter 에 연결
		adapter = new ExpandableItemAdapter(view.getContext());
		adapter.setData(items);

		// listivew에 categoryListAdapter 연결
		listView = (AnimatedExpandableListView) view.findViewById(R.id.ll_expandable);
		listView.setGroupIndicator(null);
		listView.setAdapter(adapter);

		listView.setDivider(null);

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
				String tag = "ListViewGroupClick";
				Log.d(tag, "GroupClicked: gP: " + groupPosition);
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.

				// Initializing
				adapter.setEditingGroupPosition(-1);
				if(adapter.getEditingView() != null || adapter.isEditingMemo())
				{
					adapter.getEditingView().clearFocus();
					return true;
				}


				if (!adapter.isNewMemo()) // is not long clicked
				{
					if (listView.isGroupExpanded(groupPosition))
					{
						listView.collapseGroupWithAnimation(groupPosition);
						Log.d(tag, "Collapsing gP: " + groupPosition);
					}
					else
					{
						listView.expandGroupWithAnimation(groupPosition);
						Log.d(tag, "Expanding gP: " + groupPosition);
					}
				}


				// 새로 추가한 메모가 결과적으로 빈 메모이고 삭제된 후 그 밑에있던 메모가 펼쳐지는 경우 제거하기
//				else if (!isAddedNewMemo) {
//					Log.d(tag, "새로 추가된 메모가 빈메모이고 삭제된 후 그 밑에있던 메모가 펼쳐지는 경우 제거");
//					listView.collapseGroupWithAnimation(0);
//				}

				Log.d(tag, "Focus of clicked view: " + v.hasFocus());
				Log.d(tag, "CurrentFocusView: " + ((Activity)listView.getContext()).getCurrentFocus());
				return true;
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String tag = "ListViewLongClick";

				TextView titleView = (TextView) view.findViewById(R.id.tv_title);

				if(adapter.isEditingMemo())
				{
					Log.d(tag, "Clear Focus");
					adapter.getEditingView().clearFocus();
				}

				if (titleView == null) {
					// this is memo long clicked
					// not title long clicked
					View inflatedTitleView = inflater.inflate(R.layout.list_item, null);
					titleView = (TextView) inflatedTitleView.findViewById(R.id.tv_title);
					position--;
				}

				final int realGroupPosition = getRealGroupPosition(position);
				Log.d(tag, "LongClicked: realGroupPosition: " + realGroupPosition);

				// 메모 제목 수정
				View modifyingMemoTitleView = inflater.inflate(R.layout.memo_title_modified_message_box, null);
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
				dialogBuilder.setView(modifyingMemoTitleView);

				final Dialog memoTitleDialog = dialogBuilder.create();

				final EditText etMemoTitle = (EditText) modifyingMemoTitleView.findViewById(R.id.et_modifyingMemoTitle);
				etMemoTitle.setText(items.get(realGroupPosition).title.equals("제목없음") ?
								"" : items.get(realGroupPosition).title);

				Button btDeleteMemo = (Button) modifyingMemoTitleView.findViewById(R.id.bt_deleteMemo);
				final Button btConfirmTitle = (Button) modifyingMemoTitleView.findViewById(R.id.bt_confirmTitle);

				btConfirmTitle.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d("TitleDialog", "Confirm");
						String newTitle = etMemoTitle.getText().toString();
						if (newTitle.equals(""))
							newTitle = "제목없음";
						String titleId = items.get(realGroupPosition).id;

						dbHelper.updateTitle(newTitle, titleId);
						items.get(realGroupPosition).title = newTitle;
						adapter.notifyDataSetChanged();
						memoTitleDialog.dismiss();

					}
				});

				btDeleteMemo.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d("TitleDialog", "Delete");
						for(int i = realGroupPosition; i < items.size(); i++) // More natural view for deleting memo
						{
							if(listView.isGroupExpanded(i + 1))
							{
								listView.expandGroup(i);
							}
							else if(!listView.isGroupExpanded(i+1))
							{
								listView.collapseGroup(i);
							}
						}

						dbHelper.deleteByTime(items.get(realGroupPosition).id);
						items.remove(realGroupPosition);
						adapter.notifyDataSetChanged();
						memoTitleDialog.dismiss();
					}
				});

				// Ignore enter key
				etMemoTitle.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_ENTER)
						{
							System.out.println("KeyEvent.KEYCODE_ENTER");
							btConfirmTitle.performClick();
							return true;
						}

						return false;
					}
				});


				// 다이얼로그 위치조정
				memoTitleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				Window window = memoTitleDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				lp.y = Gravity.CENTER - 200;
				memoTitleDialog.getWindow().setAttributes(lp);

				// Show dialog
				memoTitleDialog.show();
				// Set Selection of edit text
				etMemoTitle.setSelection(etMemoTitle.length());

				Log.d(tag, "CurrentFocusView: " + ((Activity)parent.getContext()).getCurrentFocus());
				return true;
			}
		});

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
					isScrolling = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				String tag = "ListViewOnScroll";

//				Log.d(tag, "fistVisibleItem: " + firstVisibleItem + " visibleITemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);

				// Is editing?
				if(adapter.isEditingMemo() && isScrolling == 1)
				{
					int realMemoPosition = getRealEditingMemoPosition(adapter.getEditingGroupPosition());
					int lastVisibleItem = firstVisibleItem + visibleItemCount;
//					Log.d(tag, "memo Pos: " + realMemoPosition);

					// 수정하던 메모가 리스트 위쪽으로 사라질 경우
					if(firstVisibleItem > realMemoPosition)
					{
						Log.d(tag, "Memo is upper of ListView memo Pos(" + realMemoPosition + ") lastVisibleItem(" + lastVisibleItem + ")");
						adapter.setEditingGroupPosition(-1);
						adapter.getEditingView().clearFocus();
					}
					// 수정하던 메모가 리스트 아래쪽으로 사라질 경우
					else if(totalItemCount > visibleItemCount)
					{
						if(lastVisibleItem <= realMemoPosition)
						{
							Log.d(tag, "Memo is below of ListView memo Pos(" + realMemoPosition + ") lastVisibleItem(" + lastVisibleItem + ")");
							adapter.setEditingGroupPosition(-1);
							adapter.getEditingView().clearFocus();
						}
					}
				}
			}
		});


		// New memo will be expanded and have a cursor on it
		if (isAddedNewMemo)
		{
			Log.d("ListView", "newMemo --> expandFirstItem");
			listView.expandGroupWithAnimation(0);
			adapter.setIsNewMemo(true);

			// Initializing boolean value
			isAddedNewMemo = false;
		}

		listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d("ListView", "Focus " + !hasFocus + " --> " + hasFocus + " changed");
			}
		});

		listView.setItemsCanFocus(true);

		return view;
	}


	public void makeGroupItemsForViewByCategory(String category) {
		ExpandableItem.GroupItem item;
		ExpandableItem.ChildItem citem;
		Cursor cursor;

		// from db, inserting to group item and child item
		// 카테고리 설정 시 여기서 디비로 불러올때 사용할 방법을 정하면된다
		// 현재 시간 내림차순으로 정렬해서 출력한다.
		// 나중에 여기에서 isstared과 time 을 잘 조합해서 출력하면 중요표시된것도 가능

		cursor = dbHelper.getWritableDatabase().rawQuery("select * from MEMO where category = '" + category + "'" + " order by time desc", null);

		while (cursor.moveToNext()) {
			String id = cursor.getString(7); // db: position
			item = new ExpandableItem.GroupItem(); // new group item
			citem = new ExpandableItem.ChildItem(); // new child item

			if (cursor.getString(2) != null && cursor.getString(2).equals(""))
				dbHelper.updatMemoToNull(id);
			if (cursor.getString(1).equals("")) // preventing title "" shown
				dbHelper.updatMemoToNull(id);

			if (cursor.getString(2) != null) {
				item.id = id; // give item an id
				item.title = cursor.getString(1); // give item a memo

				citem.memo = cursor.getString(2); // give child item a memo
				citem.id = item.id; // give child item a same id

				item.cItems.add(citem); // connect with item and citem

				items.add(item); // inserting to array
			}
		}

	}

	public ListFragment setCategoryForListView(String _category) {
		category = _category;
		return this;
	}


	public ExpandableItemAdapter getAdapter() {
		return adapter;
	}

	public ListFragment setIsAddedNewMemo(boolean _isAddedNewMemo) {
		isAddedNewMemo = _isAddedNewMemo;

		return this;
	}

	// 실제 listView 의 Position 값을 이용해서, GroupPosition 을 반환
	public int getRealGroupPosition(int currentPosition) {
		int innerCount = 0;
		int realGroupPosition = 0;
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			if (innerCount == currentPosition) {
				realGroupPosition = i;
				break;
			}

			innerCount++;
			if (listView.isGroupExpanded(i))
				innerCount++;
		}
		return realGroupPosition;
	}

	// adapter 의 GroupPosition(item 셋의 순서) 를 이용해서, 실제 listView 의 memo 위치를 반환
	public int getRealEditingMemoPosition(int editingGroupPositionInAdapter)
	{
		int realEditingMemoPosition = 0;

		for(int i = 0; i <= editingGroupPositionInAdapter; i++)
		{
			if(listView.isGroupExpanded(i))
				realEditingMemoPosition++;
		}

		return realEditingMemoPosition + editingGroupPositionInAdapter;
	}

}

