package sdm.g15_100330606_100330630.usermanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sdm.g15_100330606_100330630.usermanager.R;
import sdm.g15_100330606_100330630.usermanager.asyncTasks.UserPictureAsyncTask;
import sdm.g15_100330606_100330630.usermanager.database.User;

public class UserListAdapter extends BaseAdapter {

    private List<User> listUsers;
    private AppCompatActivity activity;
    private LayoutInflater inflater = null;

    public UserListAdapter(AppCompatActivity activity, List<User> listUsers) {
        this.listUsers = listUsers;
        this.activity = activity;
        this.inflater = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return this.listUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return this.listUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.listUsers.get(i).getUid();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Inflate view if necessary
        if( view == null) {
            view = inflater.inflate(R.layout.list_users_row, null);
        }

        // Get the user
        final User user = listUsers.get( i );

        // Picture ImageView
        ImageView pictureImageView = view.findViewById(R.id.picture);
        new UserPictureAsyncTask( pictureImageView ).execute( user.getPicture() );

        // Name TextView
        TextView nameTextView = view.findViewById(R.id.name);
        nameTextView.setText( user.getName() );

        // Date TextView
        TextView registeredTextView = view.findViewById(R.id.registered);
        registeredTextView.setText( user.getRegistered() );

        // Gender TextView
        TextView genderTextView = view.findViewById(R.id.gender);
        genderTextView.setText( user.getGender() );

        // Check Orientation
        int currentOrientation = this.activity.getResources().getConfiguration().orientation;

        if ( currentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
            // Landscape (User and Password)
            TextView userTextView = view.findViewById(R.id.user);
            if ( userTextView != null ) {
                userTextView.setText( user.getUsername() );
            }

            TextView passwordTextView = view.findViewById(R.id.password);
            if ( passwordTextView != null ) {
                passwordTextView.setText( user.getPassword() );
            }
        }
        else {
            // Portrait (Location)
            ImageButton locationImageButton = view.findViewById(R.id.location);
            locationImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String map = activity.getString( R.string.gmaps_base_url ) + user.getLocation();
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( map ) );
                    activity.startActivity(intent);
                }
            });
        }

        return view;
    }
}
