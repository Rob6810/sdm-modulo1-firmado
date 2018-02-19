package sdm.g15_100330606_100330630.usermanager.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.URL;

public class UserPictureAsyncTask extends AsyncTask<String, Void, Bitmap> {

    // Reference to the ImageView
    private final WeakReference<ImageView> pictureImageViewReference;

    // Constructor
    public UserPictureAsyncTask( ImageView pictureImageView ) {
        this.pictureImageViewReference = new WeakReference<>( pictureImageView );
    }

    @Override
    protected Bitmap doInBackground(String... pictureURL) {

        // Download picture
        Bitmap picture = null;

        try {
            URL url = new URL( pictureURL[0] );
            picture = BitmapFactory.decodeStream( url.openConnection().getInputStream() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return picture;
    }

    @Override
    protected void onPostExecute(Bitmap picture) {
        super.onPostExecute(picture);

        // Set the picture in the ImageView
        ImageView pictureImageView = pictureImageViewReference.get();

        if ( pictureImageView != null && picture != null ) {
            pictureImageView.setImageBitmap( picture );
        }
    }
}
