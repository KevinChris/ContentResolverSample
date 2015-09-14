package idoandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import idoandroid.contentresolversample.R;
import idoandroid.helper.Contact;

/**
 * Created by KevinChris on 09-Sep-15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Contact> contactList;

    public RecyclerViewAdapter(List<Contact> contactList) {
        this.contactList = contactList;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewContact, textViewSyncState;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textName);
            textViewContact = (TextView) itemView.findViewById(R.id.textContact);
            textViewSyncState = (TextView) itemView.findViewById(R.id.textSyncState);
        }
    }
}
