package sdm.g15_100330606_100330630.usermanager.asyncTasks;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sdm.g15_100330606_100330630.usermanager.R;
import sdm.g15_100330606_100330630.usermanager.activities.MainActivity;
import sdm.g15_100330606_100330630.usermanager.database.User;
import sdm.g15_100330606_100330630.usermanager.database.UserViewModel;

public class RandomUserAPI extends AsyncTask<Void, Void, List<User>> {

    // Parameters of search
    private String searchNationalities;
    private String searchGender;
    private int searchNumUsers;
    private String searchRegisterDate;

    // Activity context reference
    private final WeakReference<AppCompatActivity> contextReference;

    public RandomUserAPI(Activity context, String searchNationalities, String searchGender, int searchNumUsers, String searchRegisterDate) {
        this.contextReference    = new WeakReference<>( (AppCompatActivity) context );
        this.searchNationalities = searchNationalities;
        this.searchGender        = searchGender;
        this.searchNumUsers      = searchNumUsers;
        this.searchRegisterDate  = searchRegisterDate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Show progress bar
        this.contextReference.get().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        // List of users
        List<User> newUsers = new ArrayList<>();

        // Build URL
        String fullURL = buildUrl();

        try {
            // Create a URL object holding our url
            fullURL = fullURL.replace(" ", "%20"); // Translate spaces to url encoding
            URL url = new URL( fullURL );

            // Create a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set methods and timeouts
            connection.setRequestMethod( contextReference.get().getString( R.string.api_request_method ) );
            connection.setReadTimeout( contextReference.get().getResources().getInteger( R.integer.api_request_read_timeout ) );
            connection.setConnectTimeout( contextReference.get().getResources().getInteger( R.integer.api_request_connection_timeout ) );
            // Connect to our url
            connection.connect();

            // Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader( connection.getInputStream() );
            // Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader( streamReader );
            StringBuilder stringBuilder = new StringBuilder();
            // Check if the line we are reading is not null
            String inputLine;
            while( ( inputLine = reader.readLine() ) != null ){
                stringBuilder.append( inputLine );
            }
            // Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            // Parse list of users
            newUsers = parseUsers( stringBuilder.toString() );

            // Get UserViewModel
            UserViewModel userViewModel = ViewModelProviders.of( contextReference.get() ).get(UserViewModel.class);

            // Insert users
            userViewModel.insertAll( newUsers );

        } catch(Exception e) {
            e.printStackTrace();
        }

        return newUsers;
    }

    @Override
    protected void onPostExecute(List<User> newUsers) {
        super.onPostExecute(newUsers);

        AppCompatActivity context = this.contextReference.get();

        // Hide progress bar
        context.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);

        // Toast
        Toast toast = Toast.makeText(context, newUsers.size() + " out of " + this.searchNumUsers + " " + contextReference.get().getString(R.string.notification_users_added), Toast.LENGTH_SHORT);
        toast.show();

        // Notification (empty intent to remove notification on click or swipe)
        Notification.Builder notificationBuilder = new Notification.Builder( context )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle( newUsers.size() + " out of " + this.searchNumUsers + " " + contextReference.get().getString(R.string.notification_users_added) )
            .setAutoCancel(true)
            .setTicker( contextReference.get().getString(R.string.notification_users_added_ticker) )
            .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0));

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService( MainActivity.NOTIFICATION_SERVICE );
        if ( notificationManager != null ) {
            notificationManager.notify(0, notification);
        }
    }

    private List<User> parseUsers( String result ) throws Exception {
        List<User> users = new ArrayList<>();

        // Json object
        JSONObject jsonObject = new JSONObject( result );
        JSONArray jsonArray = jsonObject.optJSONArray("results");

        // Loop through all users
        for (int i = 0; i < jsonArray.length(); i++) {

            // Get JSON user
            JSONObject actualJsonObject = jsonArray.getJSONObject(i);

            // Get registered date
            String registered = actualJsonObject.getString("registered");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date registeredDate = format.parse( registered );

            if (
                this.searchRegisterDate != null &&
                ( ! this.searchRegisterDate.equals( contextReference.get().getResources().getString( R.string.default_insert_users_start_date ) ) )
            ) {
                Date limitRegisterDate = format.parse( this.searchRegisterDate + " 00:00:00" );

                // Skip user (registered date invalid)
                if ( registeredDate.before( limitRegisterDate ) ) {
                    continue;
                }
            }

            // Get name
            JSONObject nameFull = actualJsonObject.getJSONObject("name");
            String name = nameFull.getString("first") + " " + nameFull.getString("last");

            // Get gender
            String gender = actualJsonObject.getString("gender");
            if ( gender.equals( contextReference.get().getString(R.string.gender_male) ) ) {
                gender = contextReference.get().getString(R.string.gender_M);
            } else if ( gender.equals( contextReference.get().getString(R.string.gender_female) ) ) {
                gender = contextReference.get().getString(R.string.gender_F);
            } else {
                gender = contextReference.get().getString(R.string.gender_none);
            }

            // Get picture
            JSONObject pictureFull = actualJsonObject.getJSONObject("picture");
            String picture = pictureFull.getString("large");

            // Get username and password
            JSONObject loginFull = actualJsonObject.getJSONObject("login");
            String username = loginFull.getString("username");
            String password = loginFull.getString("password");

            // Get location
            JSONObject locationFull = actualJsonObject.getJSONObject("location");
            String location = locationFull.getString("street") + ", " + locationFull.getString("city") + ", " + locationFull.getString("state");

            // Add user to list
            users.add( new User(name, registered, gender, picture, username, password, location) ); //creamos un objeto Fruta y lo insertamos en la lista
        }

        return users;
    }

    private String buildUrl() {

        String fullURL = contextReference.get().getString( R.string.api_base_url );

        String[] nationalitiesArray = contextReference.get().getResources().getStringArray( R.array.nationalities );
        // Validate nationalities (not null, not first position, is in array)
        if (
            this.searchNationalities != null &&
            ( ! this.searchNationalities.equals( nationalitiesArray[0] ) ) &&
            ( Arrays.asList( nationalitiesArray ).contains( this.searchNationalities ) )
        ) {
            fullURL += "&nat=" + this.searchNationalities;
        }

        String[] genderArray = contextReference.get().getResources().getStringArray( R.array.genders );
        // Validate gender (not null, not first position, is in array)
        if (
            this.searchGender != null &&
            ( ! this.searchGender.equals( genderArray[0] ) ) &&
            ( Arrays.asList( genderArray ).contains( this.searchGender ) )
        ) {
            fullURL += "&gender=" + this.searchGender;
        }

        // Validate number of users (is within limits)
        if ( 0 >= this.searchNumUsers || this.searchNumUsers > 5000 ) {
            this.searchNumUsers = 1;
        }

        // Number of users
        fullURL += "&results=" + this.searchNumUsers;

        // Validate date (not null, not default value)
        if (
            this.searchRegisterDate != null &&
            ( ! this.searchRegisterDate.equals( contextReference.get().getResources().getString( R.string.default_insert_users_start_date ) ) )
        ) {
            fullURL += "&registered=" + this.searchRegisterDate + " 00:00:00";
        }

        return fullURL;
    }
}
