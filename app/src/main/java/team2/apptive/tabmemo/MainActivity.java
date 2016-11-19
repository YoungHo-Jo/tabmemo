package team2.apptive.tabmemo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private ArrayList<ArrayList<String>> mChildList = null;
  private ArrayList<String> mChildListContent = null;
  private ArrayList<Long> mDbList = null;
  private ArrayList<String> mGroupList = null;
  private ExpandableListView mListView;
  private LayoutInflater inflater = null;



  private DragListView mDragListView;
  private ArrayList<Pair<Long, String>> mItemArray;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

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
    showFragment(ListFragment.newInstance());


  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.pos, fragment, "fragment").commit();
  }

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
//    mChildListContent.add("죽을죄를 지었습니다.제때제때 하겠습니다.한번만봐주십시오.장보기.TT ");
//    mChildListContent.add("이러지도 못하는데 저러지도 못하네 \n" +
//      "그저 바라보며 ba-ba-ba-baby \n" +
//      "매일 상상만 해 이름과 함께 \n ");
//
//    mChildList.add(mChildListContent);
//    mChildList.add(mChildListContent);
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
//  }




}


