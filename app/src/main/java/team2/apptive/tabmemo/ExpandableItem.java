package team2.apptive.tabmemo;

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
    }

    public static class ChildItem {
        String title;
        String hint;
        String id;
    }

    public static class ChildHolder {
        TextView title;
        TextView hint;
    }

    public static class GroupHolder {
        TextView title;
    }

}



