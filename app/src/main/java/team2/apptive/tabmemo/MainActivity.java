package team2.apptive.tabmemo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.width;
import static team2.apptive.tabmemo.R.attr.height;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private final long FINISH_INTERVAL_TIME = 2000;
	private long backPressedTime = 0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
    setTitle("               티 미 미");
//		Display display = getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		width = size.x;
//		width_ = 0.4*width;
//		height = size.y;
//		ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(
//			width_,
//			DrawerLayout.LayoutParams.MATCH_PARENT, 1.0f);
//		EditText editText = new EditText(this);
//		editText = (EditText)findViewById(R.id.category_add_messagebox_edit);
//		String contents = editText.getText().toString();
//
		Button addButton = (Button)findViewById(R.id.navigation_button) ;
		addButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// AlertDialog 객체 선언
				AlertDialog dialog = create_inputDialog();
				// Context 얻고, 해당 컨텍스트의 레이아웃 정보 얻기
				Context context = getApplicationContext();
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 레이아웃 설정
				View layout = inflater.inflate(R.layout.category_add_message_box, (ViewGroup)findViewById(R.id.popup_category));
				// Input 소프트 키보드 보이기
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				// AlertDialog에 레이아웃 추가
				dialog.setView(layout);
				dialog.show();
			}
		}) ;



		// listview fragment
		listFragment = showFragment(ListFragment.newInstance());

		// toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	private AlertDialog create_inputDialog() { //카테고리 눌렀을 시 메시지 박스 V액션
		final ArrayList<String> items = new ArrayList<String>() ;
		final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items) ;
		ListView listview = (ListView) findViewById(R.id.navigation_list) ;
		listview.setAdapter(adapter) ;
		AlertDialog dialogBox = new AlertDialog.Builder(this)
			.setTitle("카테고리를 추가해주세요")
			.setNeutralButton("      추가", new DialogInterface.OnClickListener() {
				// 예 버튼 눌렀을때 액션 구현
				public void onClick(DialogInterface dialog, int which) {
					int count=0;
					EditText editText = (EditText)findViewById(R.id.category_add_messagebox_edit);
					count = adapter.getCount();
					if ( editText.getText().toString() == null ) {
						Toast.makeText(getApplicationContext(), "제대로좀 쳐라", Toast.LENGTH_SHORT).show();
					}
					//공백이 아닐 때 처리할 내용
					if( editText.getText().toString()!= null) {
						items.add("#" + editText.getText().toString());
						adapter.notifyDataSetChanged();
						count++;

					}
				}
			})
			.setPositiveButton("취소      ", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// 아니오 버튼 눌렀을때 액션 구현
				}
			}).create();
		return dialogBox;
	}



	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.pos, fragment, "listFragment").commit();
		return fragment;
	}


	// 새 매모 버튼 클릭 시 사용될 함수
	private void onAddNewMemoClick()
	{
		// (수정필요) 새 메모 타이틀은 여기서
		((ListFragment)listFragment).addNewTitleMemo("new title5", "");
		// new listFragment --> memory ???
		showFragment(ListFragment.newInstance());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			onAddNewMemoClick();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	// 뒤로가기 두번 두르면 종료
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		long tempTime = System.currentTimeMillis();
		long intervalTime = tempTime - backPressedTime;
//#82C2C3
		// drawer 떠있는 상태에서 뒤로가기 버튼 누를때
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}

		else {
			// 종료 옵션
			if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
				super.onBackPressed();
			else {
				backPressedTime = tempTime;
				Toast.makeText(getApplicationContext(), "종료하려면 한 번더 !", Toast.LENGTH_LONG).show();
			}
		}
	}


}
