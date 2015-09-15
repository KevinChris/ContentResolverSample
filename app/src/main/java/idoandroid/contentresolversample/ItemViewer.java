package idoandroid.contentresolversample;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import idoandroid.adapter.RecyclerViewAdapter;
import idoandroid.helper.Constants;
import idoandroid.helper.Contact;
import idoandroid.helper.DatabaseHelper;

public class ItemViewer extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    String receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**
         * Get the intent from the previous activity and display the contents according to clicked
         * button in previous activity.
         */
        if (getIntent() != null) {
            receivedIntent = getIntent().getStringExtra("buttonClicked");

            /**
             * If it's retrieve all then get all data from database
             */
            if (receivedIntent.contentEquals("retrieveAll"))
                getData(null);
            /**
             * If it's sync state one retrieve only the data which are sync state is one
             */
            else if (receivedIntent.contentEquals("retrieveSyncStateOne")) {
                String selection = DatabaseHelper.CLOUD_SYNCED + " = " + 1;
                getData(selection);
            }
            /**
             * If sync state is zero then retrieve data accordingly
             */
            else if (receivedIntent.contentEquals("retrieveSyncStateZero")) {
                String selection = DatabaseHelper.CLOUD_SYNCED + " = " + 0;
                getData(selection);
            }
        }
    }

    /**
     * Get the data from content provider application
     *
     * @param selection selection statement for query
     */
    private void getData(String selection) {

        /**
         * Query to get the cursor from database
         */
        Cursor cursor = getContentResolver().query(Constants.RETRIEVE_CONTACTS_ON_SYNC_STATE,
                DatabaseHelper.CONTACT_COLUMNS, selection, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ArrayList<Contact> contactList = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Contact contact = new Contact();
                    contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
                    contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME)));
                    contact.setPhoneNo(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PHONE_NUMBER))));
                    contact.setIsSynced(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CLOUD_SYNCED))));
                    contactList.add(contact);
                    cursor.moveToNext();
                }

                /**
                 * Prepare recycler view adapter and set adapter to recycler view
                 */
                recyclerViewAdapter = new RecyclerViewAdapter(ItemViewer.this, contactList, receivedIntent);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        } else
            Toast.makeText(this, "No Records to Display", Toast.LENGTH_SHORT).show();

        if (cursor != null) cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new AlertDialog.Builder(ItemViewer.this)
                    .setTitle(getString(R.string.about_title))
                    .setMessage(getString(R.string.about_message))
                    .setPositiveButton(getString(R.string.about_dismiss),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
