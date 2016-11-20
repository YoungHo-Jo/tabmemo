package team2.apptive.tabmemo;

import android.app.Activity;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

        // db new inserting
        dbHelper.newInsert("title 1", "");
        dbHelper.newInsert("title 2", "");
        dbHelper.newInsert("title 3", "");
        dbHelper.newInsert("title 4", "");
        dbHelper.newInsert("title 5", "");



        // from db, inserting to group item and child item
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select * from MEMO", null);
        while(cursor.moveToNext())
        {
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




//
//
//        // Populate our list with groups and it's children
//        for (int i = 1; i < 100; i++) {
//            ExpandableItem.GroupItem item = new ExpandableItem.GroupItem();
//
//            // DB 로 처리해야할 부분
//
//
//            // 메모 타이틀
//            item.title = "Group " + i;
//
//            // 메모 들어갈 자식 리스트 불러옴
//            ExpandableItem.ChildItem child = new ExpandableItem.ChildItem();
//
//            // 메모가 들어갈 자리
//            child.title = "자식sdfdddddddddddddddddddddddddddddddddddddddddddddd" +
//                    "dddddddddddddddddddddddddddddddddddddddddddddd";
//
//
//
//            item.cItems.add(child);
//            items.add(item);
//        }





        adapter = new ExpandableItemAdapter(view.getContext());
        adapter.setData(items);

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
                }
                else
                {
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

                // (미구현) 뷰를 넘겨서 텍스트 처리하고 O X 버튼으로 돌아오기
                System.out.println("onChildClicked!! " + groupPosition + " " + childPosition + " " + id);

                // chilitem 불러오기
                ExpandableItem.ChildItem childItem = adapter.getChild(groupPosition, childPosition);
                String childId = childItem.id;

                // 임시적으로 id 기반 디비에서 찾아 "memo" 넣기
                // adapter 의 getChildView 를 통해서 업데이트 되게 하기 return: string
                childItem.title = dbHelper.updateMemo("memo!!", childId);

                // update child view
                adapter.getRealChildView(groupPosition, childPosition, false, v, parent);

                return false;
            }
        });





        return view;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void myViewSwitcher(View view)
    {
        ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.editTextSwitcher);
        switcher.showNext(); //or switcher.showPrevious();
    }


}