package team2.apptive.tabmemo;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by solar on 2017-01-11.
 */

public class DeleteConfirmDialog {

	private LayoutInflater inflater;
	private View.OnClickListener confirmClickListener;
	private View.OnClickListener cancelClickListener;
	private Button confirmButton = null;
	private Button cancelButton = null;

	// Constructor
	public DeleteConfirmDialog(LayoutInflater _inflater, View.OnClickListener _confirm, View.OnClickListener _cancel)
	{
		inflater = _inflater;
		confirmClickListener = _confirm;
		cancelClickListener = _cancel;
	}

	public Dialog createDialog()
	{
		View dialogView = inflater.inflate(R.layout.delete_confirm_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
		builder.setView(dialogView);

		// Create dialog
		Dialog deleteConfirmDialog = builder.create();

		// Button
		confirmButton = (Button) dialogView.findViewById(R.id.deleteConfirmButton);
		confirmButton.setOnClickListener(confirmClickListener);

		// Button
		cancelButton = (Button) dialogView.findViewById(R.id.deleteCancelButton);
		cancelButton.setOnClickListener(cancelClickListener);

		return deleteConfirmDialog;
	}

	public void setConfirmClickListener(View.OnClickListener _confirm)
	{
		confirmClickListener = _confirm;
		if(confirmButton != null)
		{
			confirmButton.setOnClickListener(confirmClickListener);
		}
	}

	public void setCancelClickListener(View.OnClickListener _cancel)
	{
		cancelClickListener = _cancel;
		if(cancelButton != null)
		{
			cancelButton.setOnClickListener(cancelClickListener);
		}
	}
}