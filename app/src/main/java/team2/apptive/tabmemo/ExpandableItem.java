package team2.apptive.tabmemo;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by solar on 2016-11-20.
 */

public class ExpandableItem {

	public static class GroupItem {
		String title;
		ArrayList<ChildItem> cItems = new ArrayList<ChildItem>();
		String id;
		String categoryColor;
	}

	public static class ChildItem {
		String memo;
		String id;
	}

	public static class ChildHolder {
		EditableTextView memo;
	}

	public static class GroupHolder {
		TextView title;
	}

}



