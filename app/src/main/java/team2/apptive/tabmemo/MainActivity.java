package team2.apptive.tabmemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements DndListView.DragListener, DndListView.DropListener{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
}
