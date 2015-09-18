package idoandroid.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import idoandroid.contentresolversample.ItemViewer;
import idoandroid.contentresolversample.R;
import idoandroid.helper.Constants;
import idoandroid.helper.Contact;
import idoandroid.helper.DatabaseHelper;

/**
 * Created by KevinChris on 09-Sep-15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Contact> contactList;
    Context context;
    String receivedIntent;

    public RecyclerViewAdapter(Context context, List<Contact> contactList, String receivedIntent) {
        this.context = context;
        this.contactList = contactList;
        this.receivedIntent = receivedIntent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewName.setText("Name : " + contactList.get(position).getName());
        viewHolder.textViewContact.setText("Contact No : " + String.valueOf(contactList.get(position).getPhoneNo()));
        viewHolder.textViewSyncState.setText("Sync State : " + String.valueOf(contactList.get(position).getIsSynced()));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    /**
     * Validates all the fields
     *
     * @return boolean
     */
    private boolean validateFields(String name, String contact, TextInputLayout layoutName,
                                   TextInputLayout layoutContact) {
        boolean isValidated = true;

        if (name.contentEquals("")) {
            layoutName.setError("Name can't be empty");
            isValidated = false;
        } else {
            layoutName.setError(null);
            layoutName.setErrorEnabled(false);
        }

        if (contact.contentEquals("")) {
            layoutContact.setError("Contact can't be empty");
            isValidated = false;
        } else if (contact.length() < 10) {
            layoutContact.setError("Contact Number must have 10 digits");
            isValidated = false;
        } else {
            layoutContact.setError(null);
            layoutName.setErrorEnabled(false);
        }

        return isValidated;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewContact, textViewSyncState;
        Toolbar toolbar;

        public ViewHolder(View itemView) {
            super(itemView);

            initViews(itemView);

            toolbar.inflateMenu(R.menu.menu_toolbar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_update:
                            /**
                             * Display alert to modify the fields
                             */
                            displayUpdateDialog();
                            break;

                        case R.id.action_delete:
                            /**
                             * Delete the record
                             */
                            deleteRecord();
                            break;
                    }
                    return false;
                }
            });
        }

        /**
         * Display the custom dialog with an option to update the name and contact
         */
        private void displayUpdateDialog() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.custom_alert_dialog, null);
            new AlertDialog.Builder(context)
                    .setTitle("UPDATE DETAILS")
                    .setView(view)
                    .setPositiveButton(context.getString(R.string.button_update),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText editTextName = (EditText) view.findViewById(R.id.editName);
                                    String name = editTextName.getText().toString().trim();

                                    EditText editTextContact = (EditText) view.findViewById(R.id.editContact);
                                    String contact = editTextContact.getText().toString().trim();

                                    TextInputLayout layoutName = (TextInputLayout) view.findViewById(R.id.layoutName);
                                    layoutName.setErrorEnabled(true);
                                    TextInputLayout layoutContact = (TextInputLayout) view.findViewById(R.id.layoutContact);
                                    layoutContact.setErrorEnabled(true);

                                    ContentValues values = new ContentValues();

                                    /**
                                     * Validate the fields and display error if they are empty
                                     */
                                    if (!name.contentEquals("") || !contact.contentEquals("")) {
                                        if (!name.contentEquals(""))
                                            values.put(DatabaseHelper.NAME, name);
                                        if (!contact.contentEquals(""))
                                            values.put(DatabaseHelper.PHONE_NUMBER, contact);


                                        String where = DatabaseHelper.ID + " = ?";
                                        String selectionArgs[] = {String.valueOf(contactList.get(getLayoutPosition()).getId())};

                                        /**
                                         * Update the database using content resolver
                                         */
                                        long updatedRows = context.getContentResolver()
                                                .update(Constants.CONTACTS, values, where, selectionArgs);

                                        if (updatedRows > 0) {
                                            Toast.makeText(context, context.getString(R.string.toast_update_success),
                                                    Toast.LENGTH_SHORT).show();

                                            // Restart the Activity to display the updated date
                                            Intent intent = new Intent(context, ItemViewer.class);
                                            intent.putExtra("buttonClicked", receivedIntent);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();

                                        } else
                                            Toast.makeText(context, context.getString(R.string.toast_update_failed),
                                                    Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(context, "Enter at least one field and update",
                                                Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton(context.getString(R.string.button_dismiss),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

        }

        /**
         * Delete the record from the database
         */
        private void deleteRecord() {
            String where = DatabaseHelper.ID + " = ?";
            String selectionArgs[] = {String.valueOf(contactList.get(getLayoutPosition()).getId())};

            /**
             * Delete method
             */
            long deletedRows = context.getContentResolver().delete(Constants.CONTACTS, where, selectionArgs);

            if (deletedRows > 0) {
                Toast.makeText(context, context.getString(R.string.toast_delete_success),
                        Toast.LENGTH_SHORT).show();

                /**
                 * Remove the deleted item from list and notify the changed items
                 */
                contactList.remove(contactList.get(getLayoutPosition()));
                notifyItemRemoved(getLayoutPosition());
            } else
                Toast.makeText(context, context.getString(R.string.toast_delete_failed),
                        Toast.LENGTH_SHORT).show();
        }

        /**
         * Initialize the views
         *
         * @param itemView view
         */
        private void initViews(View itemView) {
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar_actions);
            textViewName = (TextView) itemView.findViewById(R.id.textName);
            textViewContact = (TextView) itemView.findViewById(R.id.textContact);
            textViewSyncState = (TextView) itemView.findViewById(R.id.textSyncState);
        }
    }
}
