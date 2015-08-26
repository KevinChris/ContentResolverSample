package idoandroid.helper;

import android.net.Uri;

import java.io.File;

/**
 * Created by KevinChris on 24-Aug-15.
 */
public class Constants {

    private static final int SYNCED = 0;
    private static final int NOT_SYNCED = 1;

    /**
     * Authority name can be of any constant String.
     */
    final static String AUTHORITY_NAME = "ContentProviderSample.Contacts";

    /**
     * Base name can be of any constant String
     */
    private final static String BASE_NAME = "iDoAndroid";

    /**
     * Uri to retrieve all contacts
     */
    public final static Uri RETRIEVE_CONTACTS = Uri.parse("content://" + AUTHORITY_NAME + File.separator
            + BASE_NAME + "contacts");

    /**
     * Uri to retrieve contacts on basis of sync state
     */
    public final static Uri RETRIEVE_CONTACTS_ON_SYNC_STATE = Uri.parse("content://" + AUTHORITY_NAME + File.separator
            + BASE_NAME + "contacts/sync");
}
