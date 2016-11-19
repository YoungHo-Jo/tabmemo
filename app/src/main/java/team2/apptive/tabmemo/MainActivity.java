package team2.apptive.tabmemo;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DndListView.DragListener, DndListView.DropListener {

  private ArrayList<ArrayList<String>> mChildList = null;
  private ArrayList<String> mChildListContent = null;
  private ArrayList<Long> mDbLIst = null;
  private ArrayList<Pair> mGroupList = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setLayout();
    mGroupList = new ArrayList<>();
    mDbLIst = new ArrayList<Long>();;
    mChildList = new ArrayList<ArrayList<String>>();
    mChildListContent = new ArrayList<String>();

    mGroupList.add(new Pair<>("장보기","time"));
    mGroupList.add(new Pair<>("장보기","time"));
    mGroupList.add(new Pair<>("장보기","time"));

    mChildListContent.add(new "트와이스");
    mChildListContent.add("죽을죄를 지었습니다.제때제때 하겠습니다.한번만봐주십시오.장보기.TT ");
    mChildListContent.add("이러지도 못하는데 저러지도 못하네 \n" +
      "그저 바라보며 ba-ba-ba-baby \n" +
      "매일 상상만 해 이름과 함께 \n ");

    mChildList.add(mChildListContent);
    mChildList.add(mChildListContent);
    mChildList.add(mChildListContent);

    DndListView listView = (DndListView) findViewById(android.R.id.list);
    listView.setDragListener(this);
    listView.setDropListener(this);
  }

  // drag event 발생 시 구현
  @Override
  public void drag(int from, int to) {

  }

  // drop event 발생 시 구현현
  @Override
  public void drop(int from, int to) {

  }
  mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList, mChildList));

  // 그룹 클릭 했을 경우 이벤트
  mListView.setOnGroupClickListener(new OnGroupClickListener() {
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v,
    int groupPosition, long id) {
      Toast.makeText(getApplicationContext(), "g click = " + groupPosition,
        Toast.LENGTH_SHORT).show();
      return false;
    }
  });
  // 차일드 클릭 했을 경우 이벤트
  mListView.setOnChildClickListener(new OnChildClickListener() {
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
    int groupPosition, int childPosition, long id) {
      Toast.makeText(getApplicationContext(), "c click = " + childPosition,
        Toast.LENGTH_SHORT).show();
      return false;
    }
  });

  // 그룹이 닫힐 경우 이벤트
  mListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
    @Override
    public void onGroupCollapse(int groupPosition) {
      Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
        Toast.LENGTH_SHORT).show();
    }
  });

  // 그룹이 열릴 경우 이벤트
  mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
    @Override
    public void onGroupExpand(int groupPosition) {
      Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
        Toast.LENGTH_SHORT).show();
    }
  });
}
  private ExpandableListView mListView;

  private void setLayout() {
    mListView = (ExpandableListView) findViewById(R.id.elv_list);
  }

}


