package team2.apptive.tabmemo;

import android.app.Activity;
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

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_layout, container, false);

        items = new ArrayList<ExpandableItem.GroupItem>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 100; i++) {
            ExpandableItem.GroupItem item = new ExpandableItem.GroupItem();

            // DB 로 처리해야할 부분


            // 메모 타이틀
            item.title = "Group " + i;

            // 메모 들어갈 자식 리스트 불러옴
            ExpandableItem.ChildItem child = new ExpandableItem.ChildItem();

            // 메모가 들어갈 자리
            child.title = "자식sdfdddddddddddddddddddddddddddddddddddddddddddddd" +
                    "dddddddddddddddddddddddddddddddddddddddddddddd";



            item.cItems.add(child);
            items.add(item);
        }

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
                return true;
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