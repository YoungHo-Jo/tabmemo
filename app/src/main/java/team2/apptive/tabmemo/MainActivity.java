package team2.apptive.tabmemo;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

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
	ArrayList<String> categoryItems = new ArrayList<>();
	ArrayList<String> color_buttons = new ArrayList<>();
	private RadioGroup mRgline1;
	private RadioGroup mRgline2;
	int radiocheckId;
	ArrayAdapter<String> categoryListAdapter;
	private String currentCategory = "전체 메모";
	private TextView tvToolbarCategoryTitle = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("");

		// font
		Typekit.getInstance()
						.addNormal(Typekit.createFromAsset(this, "fonts/Spoqa_Han_Sans_Regular_win_subset.ttf"));

		// toolbar category title
		tvToolbarCategoryTitle = (TextView) findViewById(R.id.tv_toolbar_category_title);
		tvToolbarCategoryTitle.setText(currentCategory);

		categoryListView = (ListView) findViewById(R.id.navigation_list);

		categoryListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryItems);
		// listview fragment
		categoryListView.setAdapter(categoryListAdapter);
		categoryListView.setPadding(5, 0, 0, 0);



		listFragment = showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

		// Open DB
		dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);
		dbHelper.deleteNullMemo(); // db 정리

		categoryListView.setAdapter(categoryListAdapter);

		// toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
    toolbar.setBackground(new ColorDrawable(0xead233));

		// DrawerLayout
		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//    drawer.setDrawerListener(toggle);
    toggle.syncState();


		// NavigationView
//		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//		navigationView.setNavigationItemSelectedListener(this);

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

		// Make category List items from db
		makeItemsForCategoryList(categoryItems);

		// category list item click event
		categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setCurrentCategory(categoryItems.get(position));
				makeListFragmentByCategory();
				refreshToolbarCategoryTitle();
				drawer.closeDrawer(GravityCompat.START);
			}
		});

//    categoryListView.setOnLongClickListener(new AdapterView.OnItemLongClickListener(){
//      @Override
//      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            mMainDialog = createDialog();
//            WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
//            wm.copyFrom(mMainDialog.getWindow().getAttributes());
//            wm.height = 225;
//            wm.width = 255;
//            mMainDialog.getWindow().setGravity(Gravity.TOP);
//            mMainDialog.show();
//        }
//    });

		// ( 수정 필요 !!!)
		// Category All Memo Button Click Event
		Button btCategoryAll = (Button) findViewById(R.id.btCategoryAll);
		btCategoryAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentCategory("전체 메모");
				makeListFragmentByCategory();
				refreshToolbarCategoryTitle();
				drawer.closeDrawer(GravityCompat.START);
			}
		});

		// (수정필요 !!!)
		Button btCategoryUnSorted = (Button) findViewById(R.id.btCategoryUnSorted);
		btCategoryUnSorted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentCategory("미분류");
				makeListFragmentByCategory();
				refreshToolbarCategoryTitle();
				drawer.closeDrawer(GravityCompat.START);
			}
		});

	}

	private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId != -1) {
				mRgline2.setOnCheckedChangeListener(null);
				mRgline2.clearCheck();
				mRgline2.setOnCheckedChangeListener(listener2);
				radiocheckId = checkedId;
				System.out.println("!!!!!!!!!!!!!!!  "+radiocheckId);
				System.out.println("!!!!!!!!!!!!!!!  "+checkedId);
			}
		}
	};

	private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId != -1) {
				mRgline1.setOnCheckedChangeListener(null);
				mRgline1.clearCheck();
				mRgline1.setOnCheckedChangeListener(listener1);
				radiocheckId = checkedId;
			}
		}
	};

	public void onClickView(View v) {
		switch (v.getId()) {
			case R.id.navigation_button:
				mMainDialog = createDialog();
				WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
				wm.copyFrom(mMainDialog.getWindow().getAttributes());
				wm.height = 225;
				wm.width = 255;
				mMainDialog.getWindow().setGravity(Gravity.TOP);
				mMainDialog.show();
				break;
		}
	}

	private AlertDialog createDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.category_add_message_box, null);
    final View innerView2 = getLayoutInflater().inflate(R.layout.activity_main, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(innerView.getContext());
		ab.setView(innerView);
		mMainDialog = ab.create();

		mRgline1 = (RadioGroup)innerView.findViewById(R.id.color_radio);
		mRgline1.clearCheck();
		mRgline1.setOnCheckedChangeListener(listener1);
		mRgline2 = (RadioGroup)innerView.findViewById(R.id.color_radio2);
		mRgline2.clearCheck();
		mRgline2.setOnCheckedChangeListener(listener2);

		final EditText input = (EditText) innerView.findViewById(R.id.Messagebox_edit);
		Button right_bt = (Button) innerView.findViewById(R.id.bt_right);
		Button left_bt = (Button) innerView.findViewById(R.id.bt_left);

    final Toolbar toolbar = (Toolbar) innerView2.findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


		right_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = input.getText().toString();
				if (value.equals("")) {
					Toast.makeText(getApplicationContext(), "공백입니다.", Toast.LENGTH_SHORT).show();//					mCustomDialog.dismiss();
				}
				else {
					categoryItems.add("# " + value);
					categoryListAdapter.notifyDataSetChanged();
					dbHelper.newInsert("제목 없음", "# " + value);
					setDismiss(mMainDialog);
					setCurrentCategory("# " + value);
					refreshToolbarCategoryTitle();

					DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
					drawer.closeDrawer(GravityCompat.START);
//					System.out.println("!!!!!!!!!!!!!!!  "+radiocheckId);
//					if(radiocheckId == R.id.first_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFF));
//					if(radiocheckId == R.id.second_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xe9a99a));
//					if(radiocheckId == R.id.third_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x87b14b));
//					if(radiocheckId == R.id.fourth_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x6b7fb9));
//					if(radiocheckId == R.id.fifth_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xb16d51));
//					if(radiocheckId == R.id.sixth_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x5587a1));
//					if(radiocheckId == R.id.seventh_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xd2407));
//					if(radiocheckId == R.id.eight_col)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xebc851));
					showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));
				}
			}

		});

		left_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setDismiss(mMainDialog);
			}
		});

		return ab.create();
	}

	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
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

		dbHelper.newInsert("제목 없음", currentCategory.equals("미분류") ? "전체 메모" : currentCategory);
		showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));
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
				Toast.makeText(getApplicationContext(), "종료하시려면 한 번더 눌러주세요.", Toast.LENGTH_LONG).show();
			}
		}
	}


	public void makeItemsForCategoryList(ArrayList<String> items) {

		// from db, inserting to group item and child item
		// 카테고리 설정 시 여기서 디비로 불러올때 사용할 방법을 정하면된다
		// 현재 시간 내림차순으로 정렬해서 출력한다.
		// 나중에 여기에서 isstared과 time 을 잘 조합해서 출력하면 중요표시된것도 가능
		Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select distinct category from MEMO order by time asc", null);
		while (cursor.moveToNext()) {
			System.out.println(cursor.getColumnCount());
			String category = cursor.getString(0); // db: category
			if(category.equals("전체 메모"))
				continue;

			items.add(category); // new categoryItems for category
		}
	}


	@Override
	protected void onResume() {
		//((ListFragment)listFragment).getAdapter().notifyDataSetChanged();
		super.onResume();

		System.out.println("onResume!");
	}

	 // font
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
	}

	public void setCurrentCategory(String _category)
	{
		currentCategory = _category;
	}

	public void makeListFragmentByCategory()
	{
		showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));
	}

	public void refreshToolbarCategoryTitle()
	{
		tvToolbarCategoryTitle.setText(currentCategory);
	}

}
