package idoandroid.contentresolversample;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import idoandroid.helper.Constants;
import idoandroid.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName, editTextContact;
    TextInputLayout layoutName, layoutContact;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);

        buttonSave = (Button) findViewById(R.id.button);
        buttonSave.setOnClickListener(this);

        layoutName = (TextInputLayout) findViewById(R.id.layoutName);
        layoutName.setErrorEnabled(true);
        layoutContact = (TextInputLayout) findViewById(R.id.layoutContact);
        layoutContact.setErrorEnabled(true);
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
        if (id == R.id.action_about) {
            new AlertDialog.Builder(MainActivity.this)
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

    /**
     * Retrieve all the contacts
     *
     * @param view view of the Button
     */
    public void retrieveAll(View view) {

        Intent intent = new Intent(MainActivity.this, ItemViewer.class);
        intent.putExtra("buttonClicked", "retrieveAll");
        startActivity(intent);
    }

    /**
     * Retrieve the contacts which are in status 1
     *
     * @param view view of the Button
     */
    public void retrieveSyncStateOne(View view) {

        Intent intent = new Intent(MainActivity.this, ItemViewer.class);
        intent.putExtra("buttonClicked", "retrieveSyncStateOne");
        startActivity(intent);
    }

    /**
     * Retrieve the contacts which are in status 0
     *
     * @param view view of the Button
     */
    public void retrieveSyncStateZero(View view) {

        Intent intent = new Intent(MainActivity.this, ItemViewer.class);
        intent.putExtra("buttonClicked", "retrieveSyncStateZero");
        startActivity(intent);
    }

    /**
     * Save the record to database on click of save button
     *
     * @param view view of button
     */
    public void saveContact(View view) {
        if (validateFields()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.NAME, editTextName.getText().toString().trim());
            values.put(DatabaseHelper.PHONE_NUMBER, editTextContact.getText().toString().trim());
            values.put(DatabaseHelper.CLOUD_SYNCED, generateRandom());

            Uri uri = getContentResolver().insert(Constants.CONTACTS, values);

            if (uri != null)
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generate the random number between 0 or 1 to maintain the sync state
     *
     * @return 0 or 1
     */
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
        } else if (editTextContact.getText().toString().length() < 10) {
            layoutContact.setError("Contact Number must have 10 digits");
            isValidated = false;
        } else {
            layoutContact.setError(null);
            layoutName.setErrorEnabled(false);
        }

        return isValidated;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                saveContact(v);
                break;
        }
    }
}
