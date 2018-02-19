package sdm.g15_100330606_100330630.usermanager.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sdm.g15_100330606_100330630.usermanager.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Execute after timeout
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Start main activity
                startActivity( new Intent( SplashActivity.this, MainActivity.class ) );

                // Close splash activity
                finish();
            }
        }, getResources().getInteger(R.integer.splash_timeout ));
    }
}
