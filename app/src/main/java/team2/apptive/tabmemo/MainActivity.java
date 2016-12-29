package team2.apptive.tabmemo;

import android.app.Dialog;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
	private final long FINISH_INTERVAL_TIME = 2000;

	private Dialog mMainDialog; // Dialog for adding or modifying a category

	private long backPressedTime = 0;
	private DBHelper dbHelper = null;
	private Button bt_addNewCategory = null;
	ListView categoryListView;
	ArrayList<String> categoryItems = new ArrayList<>();

	ArrayAdapter<String> categoryListAdapter;
	private String currentCategory = "전체 메모";
	private TextView tvToolbarCategoryTitle = null;
	private int category_position;
	private String categoryTitle;
  boolean modify = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("");

		// toolbar category title
		tvToolbarCategoryTitle = (TextView) findViewById(R.id.tv_toolbar_category_title);
		tvToolbarCategoryTitle.setText(currentCategory);

		categoryListView = (ListView) findViewById(R.id.navigation_list);

		categoryListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryItems);

		// memo listview fragment
		showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

		categoryListView.setAdapter(categoryListAdapter);
		categoryListView.setMinimumHeight(20);
		// categoryListView.setPadding(5, 0, 0, 0);


		// Open DB
		dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);
		dbHelper.deleteNullMemo(); // db 정리

		// toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// refresh toolbar title
		refreshToolbarCategoryTitle();

		// DrawerLayout
		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		//drawer.setDrawerListener(toggle);
    toggle.syncState();


		// Button (add new memo)
		Button bt_addNewMemo = (Button) findViewById(R.id.bt_add_new_memo);
		bt_addNewMemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAddNewMemoClick();
			}
		});

		// Button (add new category)
		bt_addNewCategory = (Button) findViewById(R.id.navigation_button);
		bt_addNewCategory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMainDialog = createDialog(v);
				mMainDialog.show();
			}
		});

		// Make category List items from db
		makeItemsForCategoryList(categoryItems);

		// Set long click event listener to category items
		categoryListView.setOnItemLongClickListener(new ListViewItemLongClickListener());

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


		// Set click event to Button "전체 메모"
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

		// Set click event to Button "미분류"
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

	class ListViewItemLongClickListener implements  AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
		{
			categoryTitle = categoryItems.get(position).substring(2);
      modify = true;
      category_position = position;
			mMainDialog = createDialog(parent);
			System.out.println(view);
			mMainDialog.show();
			return false;
		}
	}


	private AlertDialog createDialog(View v) {
		final View innerView = getLayoutInflater().inflate(R.layout.category_add_message_box, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
		ab.setView(innerView);
		mMainDialog = ab.create();

		final EditText input = (EditText) innerView.findViewById(R.id.et_categoryTitle);
		Button right_bt = (Button) innerView.findViewById(R.id.bt_right);
		Button left_bt = (Button) innerView.findViewById(R.id.bt_left);

    input.setText(input.getText().toString().equals("제목없음") ? "" : categoryTitle);
		input.setSelection(input.length());

		right_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = input.getText().toString();
				if (value.equals("")) {
					Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
				}
				else {
					String newCategoryName = "# " + value;
					if(modify)
					{
						categoryItems.set(category_position, newCategoryName);
					}
					else {
						categoryItems.add(newCategoryName);
						dbHelper.newInsert("제목없음", newCategoryName);
					}
					categoryListAdapter.notifyDataSetChanged();
					setDismiss(mMainDialog);
					setCurrentCategory(newCategoryName);
					refreshToolbarCategoryTitle();

					DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
					drawer.closeDrawer(GravityCompat.START);

					// 카테고리에 맞는 항목들 보여줌
					showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));

					if(modify)
						showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

					modify = false;
					categoryTitle = "";
				}
			}

		});

		left_bt.setOnClickListener(new View.OnClickListener() {
			@Override
        public void onClick(View v) {
				try {
					dbHelper.deleteCategory(categoryItems.get(category_position));
					categoryItems.remove(category_position);
				}
				catch (IndexOutOfBoundsException e)
				{
					System.out.println("Exception: " + e);
					Toast.makeText(v.getContext(), "삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show();
				}
					setCurrentCategory("전체 메모");

					refreshToolbarCategoryTitle();

					showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

					categoryListAdapter.notifyDataSetChanged();
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

		dbHelper.newInsert("제목없음", currentCategory.equals("미분류") ? "전체 메모" : currentCategory);
		showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));
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
		super.onResume();

		System.out.println("onResume!");
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

	@Override
	protected void onStop() {
		super.onStop();
	}
}
