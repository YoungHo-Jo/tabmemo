package team2.apptive.tabmemo;

import android.app.LauncherActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;


import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import com.leocardz.aelv.library.Aelv;
import com.leocardz.aelv.library.AelvCustomAction;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private LayoutInflater inflater = null;
  private DragListView mDragListView;
  private ArrayList<Pair<Long, String>> mItemArray;
  private DBHelper dbHelper = null;





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


      showFragment(ExpandableListFragment.newInstance());

  }



//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//
//    ArrayList<ListViewItem> items = new ArrayList<ListViewItem>();
//
//    for(int i = 1; i < 100; i++) {
//      ListViewItem item = new ListViewItem();
//
//      item.setTitle("Group " + i);
//
//      for(int j = 0; j < i; j++) {
//        item.setMemo();
//        child.title = "Awesome item " + j;
//        child.hint = "Too awesome";
//
//        item.items.add(child);
//      }
//
//      items.add(item);
//    }
//
//    adapter = new ExampleAdapter(this);
//    adapter.setData(items);
////(AnimatedExpandableListView)findViewById(R.id.elv_list);
//    listView = (AnimatedExpandableListView)findViewById(R.id.elv_list);
//    listView.setAdapter(adapter);
//
//    // In order to show animations, we need to use a custom click handler
//    // for our ExpandableListView.
//    listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//      @Override
//      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        // We call collapseGroupWithAnimation(int) and
//        // expandGroupWithAnimation(int) to animate group
//        // expansion/collapse.
//        if (listView.isGroupExpanded(groupPosition)) {
//          listView.collapseGroupWithAnimation(groupPosition);
//        } else {
//          listView.expandGroupWithAnimation(groupPosition);
//        }
//        return true;
//      }
//
//    });
//    showFragment(ListFragment.newInstance());
//
//








//
//
//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//
//    showFragment(ListFragment.newInstance());
//
//
//    // Adding Memo Button
//    Button btAddMemo = (Button) findViewById(R.id.bt_addMemo);
//    btAddMemo.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        // 메모 추가 버튼 동작
//
//        // Getting memo DB
//        dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);
//
//        // 입력이 비었는지 판단하여
//        // 추가 할 것인지 판다
//
//        // 새로운 뷰를 통해서 입력을 받아야함
//
//      }
//    });
//
//  }




  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.pos, fragment, "fragment").commit();
  }




}


