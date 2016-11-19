package team2.apptive.tabmemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements DndListView.DragListener, DndListView.DropListener{

  // 임시
  private DBHelper dbHelper = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    DndListView listView = (DndListView) findViewById(android.R.id.list);
    listView.setDragListener(this);
    listView.setDropListener(this);

    dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);

    dbHelper.newInsert("first", "");

    dbHelper.updatePosition(2, 2080264067);

    dbHelper.printData();
  }

  // drag event 발생 시 구현
  @Override
  public void drag(int from, int to) {

  }

  // drop event 발생 시 구현현
  @Override
  public void drop(int from, int to) {


    // 임시
    long timeId = 0;
    dbHelper.updatePosition(to, timeId);

  }
}
