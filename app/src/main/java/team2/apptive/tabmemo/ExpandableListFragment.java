package team2.apptive.tabmemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leocardz.aelv.library.Aelv;
import com.leocardz.aelv.library.AelvCustomAction;

import java.util.ArrayList;

/**
 * Created by solar on 2016-11-20.
 */

public class ExpandableListFragment extends Fragment {

    View view = null;
    private ArrayList<ListViewItem> listItems;

    public static ExpandableListFragment newInstance() {
        return new ExpandableListFragment();
     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_layout, null);


        ListView listView = (ListView) view.findViewById(R.id.list);

        listItems = new ArrayList<ListViewItem>();
        mockItems();

        ExpandableListAdapter adapter = new ExpandableListAdapter(view.getContext(), R.layout.list_item, listItems);

        listView.setAdapter(adapter);

        // Setup
        // Aelv aelv = new Aelv(true, 200, listItems, listView, adapter);
        final Aelv aelv = new Aelv(true, 200, listItems, listView, adapter, new AelvCustomAction() {
            @Override
            public void onEndAnimation(int position) {
                System.out.println("onEndAni");
                listItems.get(position).setDrawable(listItems.get(position).isOpen() ? R.drawable.up : R.drawable.down);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("onItemClicked");
                aelv.toggle(view, position);
            }
        });


        return view;
    }



    private void mockItems() {
        final int COLLAPSED_HEIGHT_1 = 150, COLLAPSED_HEIGHT_2 = 200, COLLAPSED_HEIGHT_3 = 250;
        final int EXPANDED_HEIGHT_1 = 250, EXPANDED_HEIGHT_2 = 300, EXPANDED_HEIGHT_3 = 350, EXPANDED_HEIGHT_4 = 400;

        ListViewItem listItem = new ListViewItem("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_1, EXPANDED_HEIGHT_1, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_2, EXPANDED_HEIGHT_2, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_3, EXPANDED_HEIGHT_3, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_2, EXPANDED_HEIGHT_4, false);
        listItems.add(listItem);

        listItem = new ListViewItem("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_1, EXPANDED_HEIGHT_4, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_2, EXPANDED_HEIGHT_4, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_3, EXPANDED_HEIGHT_3, false);
        listItems.add(listItem);

        listItem = new ListViewItem("Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.");
        // setUp IS REQUIRED
        listItem.setUp(COLLAPSED_HEIGHT_1, EXPANDED_HEIGHT_4, false);
        listItems.add(listItem);
    }




}
