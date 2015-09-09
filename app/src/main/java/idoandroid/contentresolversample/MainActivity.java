package idoandroid.contentresolversample;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import idoandroid.helper.Constants;
import idoandroid.helper.Contact;
import idoandroid.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextContact;
    TextInputLayout layoutName, layoutContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);

        layoutName = (TextInputLayout) findViewById(R.id.layoutName);
        layoutContact = (TextInputLayout) findViewById(R.id.layoutContact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieve all the contacts
     * @param view view of the Button
     */
    public void retrieveAll(View view) {

        getData(null);
    }

    /**
     * Retrieve the contacts which are in status 1
     *
     * @param view view of the Button
     */
    public void retrieveSyncStateOne(View view) {

        String selection = DatabaseHelper.CLOUD_SYNCED + " = " + 1;
        getData(selection);
    }

    /**
     * Retrieve the contacts which are in status 0
     *
     * @param view view of the Button
     */
    public void retrieveSyncStateZero(View view) {

        String selection = DatabaseHelper.CLOUD_SYNCED + " = " + 0;
        getData(selection);
    }

    /**
     * Get the data from content provider application
     *
     * @param selection selection statement for query
     */
    private void getData(String selection) {

        Cursor cursor = getContentResolver().query(Constants.RETRIEVE_CONTACTS_ON_SYNC_STATE, DatabaseHelper.CONTACT_COLUMNS, selection, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                List<Contact> contactList = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Contact contact = new Contact();
                    contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
                    contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME)));
                    contact.setPhoneNo(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PHONE_NUMBER))));
                    contact.setIsSynced(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CLOUD_SYNCED))));
                    contactList.add(contact);
                    cursor.moveToNext();
                }

                if (contactList.size() > 0) {
                    for (Contact contact : contactList) {
                        Toast.makeText(this, contact.getId() + " " + contact.getName() + " "
                                + contact.getPhoneNo() + " " + contact.getIsSynced(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if (cursor != null) cursor.close();
    }

    public void saveContact(View view) {
        if (validateFields()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.NAME, editTextName.getText().toString().trim());
            values.put(DatabaseHelper.PHONE_NUMBER, editTextContact.getText().toString().trim());
            values.put(DatabaseHelper.CLOUD_SYNCED, generateRandom());

            Uri uri = getContentResolver().insert(Constants.RETRIEVE_CONTACTS, values);

            if (uri != null)
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    private int generateRandom() {
        return (Math.random() > 0.5) ? 1 : 0;
    }

    /**
     * Validates all the fields
     *
     * @return boolean
     */
    private boolean validateFields() {
        boolean isValidated = true;

        if (editTextName.getText().toString().contentEquals("")) {
            layoutName.setError("Name can't be empty");
            isValidated = false;
        } else {
            layoutName.setError(null);
            layoutName.setErrorEnabled(false);
        }

        if (editTextContact.getText().toString().contentEquals("")) {
            layoutContact.setError("Contact can't be empty");
            isValidated = false;
        } else {
            layoutContact.setError(null);
            layoutName.setErrorEnabled(false);
        }

        return isValidated;
    }
}
