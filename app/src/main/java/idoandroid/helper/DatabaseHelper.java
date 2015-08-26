package idoandroid.helper;

/**
 * Created by KevinChris on 24-Aug-15.
 */
public class DatabaseHelper {

    /**
     * ColumnsName
     */
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String PHONE_NUMBER = "phone_number";
    public final static String CLOUD_SYNCED = "cloud_synced";

    public static final String[] CONTACT_COLUMNS = {
            ID, NAME, PHONE_NUMBER, CLOUD_SYNCED
    };
}
