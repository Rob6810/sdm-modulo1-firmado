package sdm.g15_100330606_100330630.usermanager.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sdm.g15_100330606_100330630.usermanager.R;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Back button
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if ( appCompatActivity.getSupportActionBar() != null ){
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        // Set layout
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Set title
        getActivity().setTitle( R.string.app_name);

        // Click on button insert users
        Button buttonActionInsertUsers = v.findViewById(R.id.action_insert_users);

        buttonActionInsertUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get fragment manager and replace with insert users fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.mainActivityFragment, new InsertUsersFragment());
                ft.commit();
            }
        });

        // Click on button list users
        Button buttonActionListUsers = v.findViewById(R.id.action_list_users);

        buttonActionListUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get fragment manager and replace with list users fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.mainActivityFragment, new ListUsersFragment());
                ft.commit();
            }
        });

        return v;
    }
}
