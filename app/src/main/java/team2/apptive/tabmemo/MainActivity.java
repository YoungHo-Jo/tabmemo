package team2.apptive.tabmemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;
import team2.apptive.tabmemo.AnimatedExpandableListView;
import team2.apptive.tabmemo.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private ArrayList<ArrayList<String>> mChildList = null;
  private ArrayList<String> mChildListContent = null;
  private ArrayList<Long> mDbList = null;
  private ArrayList<String> mGroupList = null;
  private ExpandableListView mListView;
  private LayoutInflater inflater = null;
  private AnimatedExpandableListView listView;
  private ExampleAdapter adapter;
  private DragListView mDragListView;
  private ArrayList<Pair<Long, String>> mItemArray;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    List<GroupItem> items = new ArrayList<GroupItem>();

    for(int i = 1; i < 100; i++) {
      GroupItem item = new GroupItem();

      item.title = "Group " + i;

      for(int j = 0; j < i; j++) {
        ChildItem child = new ChildItem();
        child.title = "Awesome item " + j;
        child.hint = "Too awesome";

        item.items.add(child);
      }

      items.add(item);
    }

    adapter = new ExampleAdapter(this);
    adapter.setData(items);
//(AnimatedExpandableListView)findViewById(R.id.elv_list);
    listView = (AnimatedExpandableListView)findViewById(R.id.elv_list);
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
    showFragment(ListFragment.newInstance());


  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.pos, fragment, "fragment").commit();
  }

  private static class GroupItem {
    String title;
    List<ChildItem> items = new ArrayList<ChildItem>();
  }

  private static class ChildItem {
    String title;
    String hint;
  }

  private static class ChildHolder {
    TextView title;
    TextView hint;
  }

  private static class GroupHolder {
    TextView title;
  }

  /**
   * Adapter for our list of {@link GroupItem}s.
   */
  private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;
    private List<GroupItem> items;

    public ExampleAdapter(Context context) {
      inflater = LayoutInflater.from(context);
    }
    public void setData(List<GroupItem> items) {
      this.items = items;
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
      return items.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      ChildHolder holder;
      ChildItem item = getChild(groupPosition, childPosition);
      if (convertView == null) {
        holder = new ChildHolder();
        convertView = inflater.inflate(R.layout.child_list_item, parent, false);
        holder.title = (TextView) convertView.findViewById(R.id.child_text);
        holder.hint = (TextView) convertView.findViewById(R.id.child_text2);
        convertView.setTag(holder);
      } else {
        holder = (ChildHolder) convertView.getTag();
      }

      holder.title.setText(item.title);
      holder.hint.setText(item.hint);

      return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
      return items.get(groupPosition).items.size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
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
      GroupHolder holder;
      GroupItem item = getGroup(groupPosition);
      if (convertView == null) {
        holder = new GroupHolder();
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.title = (TextView) convertView.findViewById(R.id.tv_title);
        convertView.setTag(holder);
      } else {
        holder = (GroupHolder) convertView.getTag();
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

  }
//    DndListView mlistView = (DndListView) findViewById(R.id.dndList_memoList);
//    mlistView.setDragListener(this);
//    mlistView.setDropListener(this);
//
//    // making adapter for Drag and Drop List view
//    DndListViewAdapter adapter = new DndListViewAdapter();
//
//    mlistView.setAdapter(adapter);
//
//
//    for(Integer i = 0; i < 10; i++)
//    {
//      adapter.addItem(i.toString(), "title " + i.toString(),
//        "memo " + i.toString(), "childmemo " + i.toString());
//    }
//    View view = inflater.inflate(R.layout.list_layout, container, false);
//
//  showFragment(ListFragment.newInstance());
//  private void showFragment(Fragment fragment) {
//    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
 //   transaction.replace(R.id.pos, fragment, "fragment").commit();


  //  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    setContentView(R.layout.activity_main);
//    inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//
//    mGroupList = new ArrayList<>();
//    mDbList = new ArrayList<Long>();;
//    mChildList = new ArrayList<ArrayList<String>>();
//    mChildListContent = new ArrayList<String>();
//
//    mGroupList.add("장보기");
//    mGroupList.add("장보기");
//    mGroupList.add("장보기");
//
//    mChildListContent.add("트와이스");

//
//    mChildList.add(mChildListContent);

//
//    DndListView listView = (DndListView) findViewById(R.id.list);
//    listView.setDragListener(this);
//    listView.setDropListener(this);
//
//    View view = inflater.inflate(R.layout.expandable_list_layout, null);
//    mListView = (ExpandableListView) view.findViewById(R.id.elv_list);
//
//    mListView.setAdapter(new BaseExpandableAdapter(view.getContext(), mGroupList, mChildList));
//
//    dndlistViewAdapter = new DndListViewAdapter();
//
//
//    // 그룹 클릭 했을 경우 이벤트
//    mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//      @Override
//      public boolean onGroupClick(ExpandableListView parent, View v,
//                                  int groupPosition, long id) {
//        Toast.makeText(getApplicationContext(), "g click = " + groupPosition,
//          Toast.LENGTH_SHORT).show();
//        return false;
//      }
//    });
//    // 차일드 클릭 했을 경우 이벤트
//    mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//      @Override
//      public boolean onChildClick(ExpandableListView parent, View v,
//                                  int groupPosition, int childPosition, long id) {
//        Toast.makeText(getApplicationContext(), "c click = " + childPosition,
//          Toast.LENGTH_SHORT).show();
//        return false;
//      }
//    });
//
//    // 그룹이 닫힐 경우 이벤트
//    mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//      @Override
//      public void onGroupCollapse(int groupPosition) {
//        Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
//          Toast.LENGTH_SHORT).show();
//      }
//    });
//
//    // 그룹이 열릴 경우 이벤트
//    mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//      @Override
//      public void onGroupExpand(int groupPosition) {
//        Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
//          Toast.LENGTH_SHORT).show();
//      }
//    });

  }



