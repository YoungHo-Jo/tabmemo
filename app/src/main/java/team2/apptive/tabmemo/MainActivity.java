package team2.apptive.tabmemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private Dialog mMainDialog;
	private final long FINISH_INTERVAL_TIME = 2000;
	private long backPressedTime = 0;
	private Fragment listFragment = null;
	private DBHelper dbHelper = null;
	private EditText input = null;
	private Button addButton = null;
	ListView categoryListView;
	ArrayList<String> items = new ArrayList<>();
	ArrayList<String> color_buttons = new ArrayList<>();
	ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("");

		categoryListView = (ListView) findViewById(R.id.navigation_list);

		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
		// listview fragment
		categoryListView.setAdapter(adapter);


		listFragment = showFragment(ListFragment.newInstance());
		// Open DB
		dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);
		dbHelper.deleteNullMemo(); // db 정리

		categoryListView.setAdapter(adapter);

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
		 addButton = (Button) findViewById(R.id.navigation_button);

		// category items

		// from db
		makeItemsForCategoryList(items);
		// setting category listview

	}

	public void onClickView(View v){
		switch (v.getId()) {
			case R.id.navigation_button:
				mMainDialog = createDialog();
				WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
				wm.copyFrom(mMainDialog.getWindow().getAttributes());
				wm.height=225;
				mMainDialog.getWindow().setGravity(Gravity.TOP);
				mMainDialog.show();
				break;
		}
	}

	private AlertDialog createDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.category_add_message_box, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setView(innerView);

		mMainDialog = ab.create();

		final EditText input = (EditText)innerView.findViewById(R.id.Messagebox_edit);
		Button right_bt = (Button)innerView.findViewById(R.id.bt_right);
		Button left_bt = (Button)innerView.findViewById(R.id.bt_left);
		RadioGroup Colgroup = (RadioGroup)findViewById(R.id.color_radio);
		Colgroup.setOnCheckedChangeListener(mRadioCheck);



		right_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int result = 0;
				String value = input.getText().toString();
				System.out.println(value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				String non = "";
				result = value.compareTo(non);
				if (result == 0) {
					Toast.makeText(getApplicationContext(), "제대로좀 쳐라", Toast.LENGTH_SHORT).show();//					mCustomDialog.dismiss();
				}
				else{;
					System.out.println(value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					items.add(" # " + value);
					adapter.notifyDataSetChanged();
					setDismiss(mMainDialog);
				}
			}

		});

		left_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setDismiss(mMainDialog);
			}
		});
//		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				setDismiss(mMainDialog);
//			}
//		});
//
//		ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				setDismiss(mMainDialog);
//			}
//		});

		return  ab.create();
	}

	RadioGroup.OnCheckedChangeListener mRadioCheck =
		new RadioGroup.OnCheckedChangeListener(){
			public void onCheckedChanged(RadioGroup group, int checkedId){
				if(group.getId() == R.id.color_radio){
					switch (checkedId){
						case R.id.first_col:
							break;
						case R.id.second_col:
							break;
						case R.id.third_col:
							break;
						case R.id.fourth_col:
							break;
						case R.id.fifth_col:
							break;
						case R.id.sixth_col:
							break;
						case R.id.seventh_col:
							break;
						case R.id.eight_col:
							break;
					}
				}
			}
		};

	private void setDismiss(Dialog dialog){
		if(dialog!=null&&dialog.isShowing())
			dialog.dismiss();
	}

	private Fragment showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(fragment, "listFragment");
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

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.

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



//	public void onClickView(View v) {
//		switch (v.getId()) {
//			case R.id.navigation_button:
//				mCustomDialog = new CustomDialog(this,
//					leftClickListener,
//					rightClickListener);
//					mCustomDialog.show();
//				break;
//			case R.id.Messagebox_edit:
//				input = (EditText)findViewById(R.id.Messagebox_edit);
//				break;
//		}
//	}
//
//  private View.OnClickListener rightClickListener = new View.OnClickListener() {
//		public void onClick(View v) {
////			final LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
////			final View view = inflater.inflate(R.layout.category_add_message_box,null);
////			final EditText input = (EditText)view.findViewById(R.id.Messagebox_edit);
//			int result = 0;
//			String value = input.getText().toString();
//			System.out.println(value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//			String non = "";
//			result = value.compareTo(non);
//				if (result == 0) {
//					Toast.makeText(getApplicationContext(), "제대로좀 쳐라", Toast.LENGTH_SHORT).show();//					mCustomDialog.dismiss();
//				}
//				else{;
//			System.out.println(value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//					items.add(" # " + value);
//					adapter.notifyDataSetChanged();
//					mCustomDialog.dismiss();
//				}
//		}
//	};
//
//	private View.OnClickListener leftClickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			mCustomDialog.dismiss();
//		}
//	};





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


	public ListFragment getCurrentListFragment()
	{
		return (ListFragment) listFragment;
	}

	@Override
	protected void onResume() {
		//((ListFragment)listFragment).getAdapter().notifyDataSetChanged();
		super.onResume();

		System.out.println("onResume!");
	}
}
