package team2.apptive.tabmemo;

import android.content.DialogInterface;
import android.database.Cursor;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private final long FINISH_INTERVAL_TIME = 2000;
	private long backPressedTime = 0;
	private Fragment listFragment = null;
	private DBHelper dbHelper = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("");

		// listview fragment
		listFragment = showFragment(ListFragment.newInstance());

		// Open DB
		dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);

		// toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// DrawerLayout
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();


		// NavigationView
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// Button (add new memo)
		Button bt_addNewMemo = (Button) findViewById(R.id.bt_add_new_memo);
		bt_addNewMemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAddNewMemoClick();
			}
		});

		// Button (add new category)
		final Button addButton = (Button) findViewById(R.id.navigation_button);

		// category items
		final ArrayList<String> items = new ArrayList<>();

		// from db
		makeItemsForCategoryList(items);

		// setting category listview
		final ArrayAdapter adapter = new ArrayAdapter(addButton.getContext(), android.R.layout.simple_list_item_1, items);
		final ListView categoryListView = (ListView) findViewById(R.id.navigation_list);
		categoryListView.setAdapter(adapter);

		addButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				final AlertDialog.Builder alert = new AlertDialog.Builder(addButton.getContext());
				final EditText input = new EditText(addButton.getContext());
				final Button black = (Button) findViewById(R.id.black_color);

				alert.setTitle("카테고리를 입력하세요");
				alert.setView(black);
				alert.setView(input);

				alert.setPositiveButton("만들기", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						if (value.equals("")) {
							Toast.makeText(alert.getContext(), "제대로좀 쳐라", Toast.LENGTH_SHORT).show();
						}
						//공백이 아닐 때 처리할 내용
						else {
							items.add("# " + input.getText().toString());

							// insert to db
							dbHelper.newInsert("제목없음", "# " + input.getText().toString());
							adapter.notifyDataSetChanged();
							dialog.dismiss();
							alert.create();
						}
					}
				});
				alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//Cancel
						dialog.dismiss();
						alert.create();
					}
				});
				alert.show();
			}
		});


	}


	private Fragment showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.pos, fragment, "listFragment").commit();
		return fragment;
	}


	// 새 매모 버튼 클릭 시 사용될 함수
	private void onAddNewMemoClick() {
		// (수정필요) 새 메모 타이틀은 여기서
		// ((ListFragment) listFragment).addNewTitleMemo("No Title", "");
		// new listFragment --> memory ???


		dbHelper.newInsert("no title", "");

		listFragment = showFragment(ListFragment.newInstance().setIsAddedNewMemo(true));
	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

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
		} else {
			// 종료 옵션
			if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
				super.onBackPressed();
			else {
				backPressedTime = tempTime;
				Toast.makeText(getApplicationContext(), "종료하려면 한 번더 !", Toast.LENGTH_LONG).show();
			}
		}

	}
//
//	public static void hideSoftKeyboard(Activity activity) {
//		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//	}


	public static void showSoftKeyboard() {

	}

	public void makeItemsForCategoryList(ArrayList<String> items) {

		// from db, inserting to group item and child item
		// 카테고리 설정 시 여기서 디비로 불러올때 사용할 방법을 정하면된다
		// 현재 시간 내림차순으로 정렬해서 출력한다.
		// 나중에 여기에서 isstared과 time 을 잘 조합해서 출력하면 중요표시된것도 가능
		Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select * from MEMO where category != \"\" order by time desc", null);
		while (cursor.moveToNext()) {
			String category = cursor.getString(4); // db: category

			items.add(category); // new items for category
		}
	}

	public void readCategoriesFromDB()
	{

	}

	public ListFragment getCurrentListFragment()
	{
		return (ListFragment) listFragment;
	}
}
