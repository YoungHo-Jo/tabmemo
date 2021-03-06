package team2.apptive.tabmemo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
	private Dialog deleteConfirmDialog;
	private ListFragment currentFragment = null;

	private long backPressedTime = 0;
	private DBHelper dbHelper = null;
	private Button bt_addNewCategory = null;
	ListView categoryListView;
	ArrayList<String> categoryItems = new ArrayList<>();

	ArrayAdapter<String> categoryListAdapter;
	private String currentCategory = "탭메모";
	private TextView tvToolbarCategoryTitle = null;
	private int category_position;
	private String categoryTitle;
  private boolean modify = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("");

		// toolbar category title
		tvToolbarCategoryTitle = (TextView) findViewById(R.id.tv_toolbar_category_title);
		tvToolbarCategoryTitle.setText(currentCategory);

		categoryListView = (ListView) findViewById(R.id.navigation_list);

		categoryListAdapter = new ArrayAdapter<>(this, R.layout.category_list_item, categoryItems);

		categoryListView.setAdapter(categoryListAdapter);
		categoryListView.setMinimumHeight(20);


		// Open DB
		dbHelper = new DBHelper(getApplicationContext(), "Memo.db", null, 1);
		dbHelper.deleteNullMemo(); // db 정리

		// toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		// DrawerLayout
		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		//drawer.setDrawerListener(toggle);
    toggle.syncState();

		// toolbar 햄버거 아이콘 변경
		toggle.setDrawerIndicatorEnabled(false);

		// set new scaled hamburger icon
		toggle.setHomeAsUpIndicator(R.drawable.category);

		toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(drawer.isDrawerVisible(GravityCompat.START))
					drawer.closeDrawer(GravityCompat.START);
				else
					drawer.openDrawer(GravityCompat.START);
				currentFragment.getAdapter().initializeFocusAndPos();
			}
		});



		// Button (add new memo)
		final Button bt_addNewMemo = (Button) findViewById(R.id.bt_add_new_memo);
		bt_addNewMemo.setOnClickListener(addNewMemoButtonListener);

		// Button (add new category)
		bt_addNewCategory = (Button) findViewById(R.id.navigation_button);
		bt_addNewCategory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				modify = false;

				mMainDialog = createDialog(v);
				mMainDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

				Window window = mMainDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				lp.y = Gravity.CENTER - 200;
				mMainDialog.getWindow().setAttributes(lp);
				mMainDialog.show();
			}
		});


		// Make category List items from db
		makeItemsForCategoryList(categoryItems);

		// Set Current category
		if(categoryItems.size() != 0)
			currentCategory = categoryItems.get(0);

		// refresh toolbar title
		refreshToolbarCategoryTitle();

		// memo listView fragment
		currentFragment = showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

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

		drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				bt_addNewMemo.setBackgroundResource(R.drawable.information);
				bt_addNewMemo.setOnClickListener(informationButtonListener);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				bt_addNewMemo.setBackgroundResource(R.drawable.add_memo);
				bt_addNewMemo.setOnClickListener(addNewMemoButtonListener);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
			}
		});



	}

	class ListViewItemLongClickListener implements  AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
		{
			categoryTitle = categoryItems.get(position);
      modify = true;
      category_position = position;

			mMainDialog = createDialog(view);
			mMainDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

			Window window = mMainDialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.y = Gravity.CENTER - 200;
			mMainDialog.getWindow().setAttributes(lp);

			mMainDialog.show();


			return true;
		}
	}

	// Listener for add new button
	private View.OnClickListener addNewMemoButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			currentFragment.getAdapter().initializeFocusAndPos();
			onAddNewMemoClick();
		}
	};

	// Listener for information button
	private View.OnClickListener informationButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			View informationDialogView = getLayoutInflater().inflate(R.layout.information_dialog, null);
			AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
			// Set view
			ab.setView(informationDialogView);
			// Make a dialog
			final Dialog informationDialog = ab.create();
			// Set buttons
			Button goToMarketButton = (Button) informationDialogView.findViewById(R.id.goToMarketButton);

			goToMarketButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String marketAddress ="https://play.google.com/store/apps/details?id=team2.apptive.tabmemo";
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketAddress)));
				}
			});

			informationDialog.show();
		}
	};


	private AlertDialog createDialog(View v) {
		final View innerView = getLayoutInflater().inflate(R.layout.category_add_message_box, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
		ab.setView(innerView);
		mMainDialog = ab.create();

		final EditText categoryEditText = (EditText) innerView.findViewById(R.id.et_categoryTitle);
		final Button right_bt = (Button) innerView.findViewById(R.id.bt_right);
		Button left_bt = (Button) innerView.findViewById(R.id.bt_left);

    categoryEditText.setText((modify) ? categoryTitle : "");
		categoryEditText.setSelection(categoryEditText.length());

		// Confirm button
		right_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String categoryName = categoryEditText.getText().toString();
				if (categoryName.equals("")) {
					Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
				}
				else {
					if (modify)
					{
						categoryItems.set(category_position, categoryName);
						dbHelper.updateCategoryName(categoryTitle, categoryName);
					}
					else
					{
						categoryItems.add(categoryName);
						dbHelper.newInsert("제목없음", categoryName);
					}
					// 중복제거
					categoryItems.clear();
					makeItemsForCategoryList(categoryItems);
					categoryListAdapter.notifyDataSetChanged();


					setDismiss(mMainDialog);


					setCurrentCategory(categoryName);
					refreshToolbarCategoryTitle();

					DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
					drawer.closeDrawer(GravityCompat.START);

					if(modify) // 카테고리에 맞는 항목들 보여줌
					{
						currentFragment = showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));
					}
					else
					{
						currentFragment = showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));
					}

					modify = false;
					categoryTitle = "";
				}
			}

		});

		// Delete Button
		left_bt.setOnClickListener(new View.OnClickListener() {
			@Override
        public void onClick(View v)
				{

					DeleteConfirmDialog deleteConfirmDialogMaker = new DeleteConfirmDialog(getLayoutInflater(), null, null);
					deleteConfirmDialog = deleteConfirmDialogMaker.createDialog();


					View.OnClickListener deleteConfirm = new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// Exception (카테고리가 하나만 남아 있을 경우 --> 삭제 불가)
							if(categoryItems.size() == 1)
							{
								Toast.makeText(v.getContext(), "삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show();
								setDismiss(deleteConfirmDialog);
								setDismiss(mMainDialog);
								return;
							}

							try
							{
								dbHelper.deleteCategory(categoryItems.get(category_position));
								categoryItems.remove(category_position);
							}
							catch (IndexOutOfBoundsException e)
							{
								Toast.makeText(v.getContext(), "삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show();
							}
							finally
							{
								setDismiss(deleteConfirmDialog);
								setDismiss(mMainDialog);
							}

							setCurrentCategory(categoryItems.get(0));

							refreshToolbarCategoryTitle();

							currentFragment = showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));

							categoryListAdapter.notifyDataSetChanged();

//							setDismiss(deleteConfirmDialog);
//							setDismiss(mMainDialog);
						}
					};

					View.OnClickListener deleteCancel = new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							setDismiss(deleteConfirmDialog);
							setDismiss(mMainDialog);
						}
					};

					// Set Click Listener
					deleteConfirmDialogMaker.setConfirmClickListener(deleteConfirm);
					deleteConfirmDialogMaker.setCancelClickListener(deleteCancel);

					// Show
					deleteConfirmDialog.show();


				}
		});


		// 엔터 키 무시
		categoryEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
//					System.out.println("enter || action done");
					right_bt.performClick();
					return true;
				}
				return false;
			}
		});

		return ab.create();
	}

	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	private ListFragment showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.pos, fragment, "listFragment").commit();
		return (ListFragment) fragment;
	}


	// 새 매모 버튼 클릭 시 사용될 함수
	private void onAddNewMemoClick() {
		dbHelper.newInsert("제목없음", currentCategory);
		currentFragment = showFragment(ListFragment.newInstance().setIsAddedNewMemo(true).setCategoryForListView(currentCategory));
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
			if (0 <= intervalTime && intervalTime <= FINISH_INTERVAL_TIME)
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
			String category = cursor.getString(0); // db: category

			items.add(category); // new categoryItems for category
		}
	}


	public void setCurrentCategory(String _category)
	{
		currentCategory = _category;
	}

	public void makeListFragmentByCategory()
	{
		currentFragment = showFragment(ListFragment.newInstance().setCategoryForListView(currentCategory));
	}

	public void refreshToolbarCategoryTitle()
	{
		tvToolbarCategoryTitle.setText(currentCategory);
	}




}
