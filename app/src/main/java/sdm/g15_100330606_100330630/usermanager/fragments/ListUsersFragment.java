package sdm.g15_100330606_100330630.usermanager.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import sdm.g15_100330606_100330630.usermanager.R;
import sdm.g15_100330606_100330630.usermanager.adapters.UserListAdapter;
import sdm.g15_100330606_100330630.usermanager.database.User;
import sdm.g15_100330606_100330630.usermanager.database.UserViewModel;

public class ListUsersFragment extends Fragment {

    private ListView listView;
    private AppCompatActivity a;
    private UserViewModel userViewModel;

    public ListUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get activity context (needed in the userViewModel)
        a = (AppCompatActivity) getActivity();

        // Initialize userViewModel
        userViewModel = ViewModelProviders.of( a ).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set layout
        View v = inflater.inflate(R.layout.fragment_list_users, container, false);

        // Set title
        getActivity().setTitle( R.string.action_list_users );

        // Back button
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if ( appCompatActivity.getSupportActionBar() != null ){
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get listView
        listView = v.findViewById(R.id.list_users);

        // Watches live updates on the database
        userViewModel.getAll().observe(a, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> allUsers) {
                // Update list
                listView.setAdapter( new UserListAdapter( a, allUsers ));
            }
        });

        return v;
    }
}
