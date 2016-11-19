package team2.apptive.tabmemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DndListView.DragListener, DndListView.DropListener {

  private ArrayList<String> mGroupList = null;
  private ArrayList<ArrayList<String>> mChildList = null;
  private ArrayList<String> mChildListContent = null;
  private ArrayList<Long> mDbLIst = null;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setLayout();

    mDbLIst = new ArrayList<Long>();
    mGroupList = new ArrayList<String>();
    mChildList = new ArrayList<ArrayList<String>>();
    mChildListContent = new ArrayList<String>();

    mGroupList.add("장보기");
    mGroupList.add("과제");
    mGroupList.add("여자친구");


    mChildListContent.add("트와이스");
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

  private ExpandableListView mListView;

  private void setLayout() {
    mListView = (ExpandableListView) findViewById(R.id.elv_list);
  }
}


