package team2.apptive.tabmemo;

/**
 * Created by solar on 2016-11-20.
 */

import android.widget.LinearLayout;
import android.widget.TextView;

import com.leocardz.aelv.library.AelvListViewHolder;

import java.io.LineNumberReader;


public class ListViewHolder extends AelvListViewHolder {
    private TextView textView;
    private LinearLayout llView;

    public ListViewHolder(TextView textView) {
        super();
        this.textView = textView;
    }

    public ListViewHolder(LinearLayout linear) {
        super();
        this.llView = linear;
    }

    public TextView getTextView() {
        return textView;
    }

    public LinearLayout getLinearLayout()
    {
        return llView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setLinearLayout(LinearLayout linearLayout) {this.llView = linearLayout;}
}