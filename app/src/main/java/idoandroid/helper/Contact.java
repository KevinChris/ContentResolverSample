package idoandroid.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KevinChris on 23-Aug-15.
 */
public class Contact implements Parcelable {
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
    private int Id;
    private String Name;
    private long PhoneNo;
    private int IsSynced;

    public Contact() {

    }

    protected Contact(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        PhoneNo = in.readLong();
        IsSynced = in.readInt();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        PhoneNo = phoneNo;
    }

    public int getIsSynced() {
        return IsSynced;
    }

    public void setIsSynced(int isSynced) {
        IsSynced = isSynced;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeLong(PhoneNo);
        dest.writeInt(IsSynced);
    }
}
