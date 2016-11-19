package team2.apptive.tabmemo;

/**
 * Created by solar on 2016-11-19.
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ListFragment extends Fragment {

  private ArrayList<Pair<Long, String>> mItemArray;
  private DragListView mDragListView;

  private View view = null;
  private View itemView = null;

  public static ListFragment newInstance() {
    return new ListFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.list_layout, container, false);

    mDragListView = (DragListView) view.findViewById(R.id.drag_list_view);
    mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
    mDragListView.setDisableReorderWhenDragging(false);
    mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
      @Override
      public void onItemDragStarted(int position) {
        Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onItemDragEnded(int fromPosition, int toPosition) {
        if (fromPosition != toPosition) {

          Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
        }

      }
    });



//    mItemArray = new ArrayList<>();
//    for (int i = 0; i < 40; i++) {
//      mItemArray.add(new Pair<>(Long.valueOf(i), "Item " + i));
//    }

    final DBHelper dbHelper = new DBHelper(getContext(), "Memo.db", null, 1);
    dbHelper.newInsert("title 1", "");
    dbHelper.newInsert("title 2", "");
    dbHelper.newInsert("title 3", "");
    dbHelper.newInsert("title 4", "");
    dbHelper.newInsert("title 5", "");

    mItemArray = new ArrayList<>();
    Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select * from MEMO", null);
    while(cursor.moveToNext()) {
      String pos = cursor.getString(7);

      long lpos = Long.parseLong(pos);
      mItemArray.add(new Pair<>(lpos, cursor.getString(6)));
    }

    dbHelper.printData();


    setupListRecyclerView();
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  private void setupListRecyclerView() {
    mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
    ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.list_item, R.id.bt_drag, false); // 이부분으로 어디를 눌렀을때 드래그되는지 id값으로 설정가능
    // image 대신에 text 넣으면 text 눌렀을때 동작
    mDragListView.setAdapter(listAdapter, true);
    mDragListView.setCanDragHorizontally(false);
    mDragListView.setCustomDragItem(new MyDragItem(getContext(), R.layout.list_item));


    System.out.println("setupListRecyclerView Called!");
  }


  private static class MyDragItem extends DragItem {

    public MyDragItem(Context context, int layoutId) {
      super(context, layoutId);
    }

    @Override
    public void onBindDragView(View clickedView, View dragView) {

      System.out.println("onBindDragView: 옮기기 시작할 때 불러온다");
//      dragView.findViewById(R.id.tv_memo).setVisibility(View.INVISIBLE);
//      dragView.findViewById(R.id.tv_childMemo).setVisibility(View.INVISIBLE);
      CharSequence text = ((TextView) clickedView.findViewById(R.id.tv_title)).getText();
      ((TextView) dragView.findViewById(R.id.tv_title)).setText(text);


      dragView.setBackgroundColor(dragView.getResources().getColor(R.color.colorPrimaryDark));
    }
  }
}