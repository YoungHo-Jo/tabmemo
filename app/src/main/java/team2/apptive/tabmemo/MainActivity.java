package team2.apptive.tabmemo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private final long FINISH_INTERVAL_TIME = 2000;
  private long backPressedTime = 0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);


    showFragment(ListFragment.newInstance());


  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.pos, fragment, "fragment").commit();
  }


  // 새 매모 버튼 클릭 시 사용될 함수
  private void onAddNewMemoClick()
  {

  }

  // 뒤로가기 두번 두르면 종료
  @Override
  public void onBackPressed() {
    long tempTime = System.currentTimeMillis();
    long intervalTime = tempTime - backPressedTime;

    if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
      super.onBackPressed();
    else {
      backPressedTime = tempTime;
      Toast.makeText(getApplicationContext(), "종료하려면 한 번더 !", Toast.LENGTH_LONG).show();
    }
  }
}
