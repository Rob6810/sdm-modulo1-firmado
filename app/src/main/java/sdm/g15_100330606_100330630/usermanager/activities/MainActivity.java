package sdm.g15_100330606_100330630.usermanager.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import sdm.g15_100330606_100330630.usermanager.fragments.MainFragment;
import sdm.g15_100330606_100330630.usermanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.activity_main);

        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get fragment manager
        FragmentManager fragmentManager = getFragmentManager();

        // Display MainFragment if no other fragments are in the backstack (after orientation change for example)
        if ( fragmentManager.getBackStackEntryCount() == 0 ) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.mainActivityFragment, new MainFragment());
            ft.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
