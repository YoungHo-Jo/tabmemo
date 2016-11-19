package team2.apptive.tabmemo;

/**
 * Created by solar on 2016-11-19.
 */

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

  private int mLayoutId;
  private int mGrabHandleId;
  private boolean mDragOnLongPress;
  private View view = null;
  private DBHelper dbHelper = null;

  // grabHandledID를 통해서 눌렀을때 동작할 위치 지정 가능
  public ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
    mLayoutId = layoutId;
    mGrabHandleId = grabHandleId;
    mDragOnLongPress = dragOnLongPress;
    setHasStableIds(true);
    setItemList(list);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    String id = mItemList.get(position).second;
    System.out.println(id);


    dbHelper = new DBHelper(view.getContext(), "Memo.db", null, 1);

    holder.mTitle.setText(dbHelper.getTitle(id));
    holder.mMemo.setText(dbHelper.getMemo(id));

    System.out.println("onBIndViewHolder called!");

    dbHelper.close();

  }


  @Override
  public long getItemId(int position) {
    return mItemList.get(position).first;
  }

  public class ViewHolder extends DragItemAdapter.ViewHolder {
    public TextView mTitle;
    public TextView mMemo;

    public ViewHolder(final View itemView) {
      super(itemView, mGrabHandleId, mDragOnLongPress);
      mTitle = (TextView) itemView.findViewById(R.id.tv_title);
      mMemo = (TextView) itemView.findViewById(R.id.tv_memo);

      System.out.println("ViewHolder Constructor called!!");
    }

    @Override
    public void onItemClicked(View view) {
      Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClicked(View view) {
      Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
      return true;
    }
  }
}