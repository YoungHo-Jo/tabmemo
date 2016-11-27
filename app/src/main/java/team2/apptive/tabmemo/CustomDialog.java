package team2.apptive.tabmemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import team2.apptive.tabmemo.R;

public class CustomDialog extends Dialog{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
    lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    lpWindow.dimAmount = 0.8f;
    getWindow().setAttributes(lpWindow);

    setContentView(R.layout.category_add_message_box);

    setLayout();
    setClickListener(mLeftClickListener , mRightClickListener);
  }

  public CustomDialog(Context context) {
    // Dialog 배경을 투명 처리 해준다.
    super(context , android.R.style.Theme_Translucent_NoTitleBar);
  }

//  public CustomDialog(Context context , String title ,
//                      View.OnClickListener singleListener) {
//    super(context , android.R.style.Theme_Translucent_NoTitleBar);
//    this.mLeftClickListener = singleListener;
//  }

  public CustomDialog(Context context ,
                      View.OnClickListener leftListener , View.OnClickListener rightListener) {
    super(context , android.R.style.Theme_Translucent_NoTitleBar);
    this.mLeftClickListener = leftListener;
    this.mRightClickListener = rightListener;
  }

  private void setClickListener(View.OnClickListener left , View.OnClickListener right){
    if(left!=null && right!=null){
      mLeftButton.setOnClickListener(left);
      mRightButton.setOnClickListener(right);
    }else if(left!=null && right==null){
      mLeftButton.setOnClickListener(left);
    }else {

    }
  }

  private Button mLeftButton;
  private Button mRightButton;

  private View.OnClickListener mLeftClickListener;
  private View.OnClickListener mRightClickListener;

  /*
   * Layout
   */
  private void setLayout(){
    mLeftButton = (Button) findViewById(R.id.bt_left);
    mRightButton = (Button) findViewById(R.id.bt_right);
  }

}
