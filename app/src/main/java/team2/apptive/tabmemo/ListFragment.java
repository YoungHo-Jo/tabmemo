package team2.apptive.tabmemo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by solar on 2016-11-20.
 */

public class ListFragment extends Fragment {

    private ArrayList<ExpandableItem> mItemArray;
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

        ArrayList<ExpandableItem.GroupItem> items = new ArrayList<ExpandableItem.GroupItem>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 100; i++) {
            ExpandableItem.GroupItem item = new ExpandableItem.GroupItem();

            item.title = "Group " + i;

            for (int j = 0; j < i; j++) {
                ExpandableItem.ChildItem child = new ExpandableItem.ChildItem();
                child.title = "자식sdfdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd " + j;
                child.hint = "추가 메모";

                item.cItems.add(child);
            }

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
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });


        return view;
    }
}