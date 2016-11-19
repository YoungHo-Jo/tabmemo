package team2.apptive.tabmemo;

/**
 * Created by solar on 2016-11-19.
 */

public class ListViewItem {
  private String title;
  private String memo;
  private String childMemo;
  private String id;
  private boolean isStared;

  public void setTitle(String _title) {title = _title;}

  public void setMemo(String _memo) {memo = _memo;}

  public void setChildMemo(String _chileMemo) {childMemo = _chileMemo;}

  public void setId(String _id) {id = _id;}

  public void setStared(boolean _isStared) {isStared = _isStared;}

  public String getTitle() {return this.title;}

  public String getMemo() {return this.memo;}

  public String getChildMemo() {return this.childMemo;}

  public String getId() {return this.id;}

  public boolean getStared() {return this.isStared;}



}
