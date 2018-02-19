package sdm.g15_100330606_100330630.usermanager.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sdm.g15_100330606_100330630.usermanager.R;
import sdm.g15_100330606_100330630.usermanager.asyncTasks.RandomUserAPI;

public class InsertUsersFragment extends Fragment {

    public InsertUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set layout
        View v = inflater.inflate(R.layout.fragment_insert_users, container, false);

        // Set title
        getActivity().setTitle( R.string.action_insert_users );

        // Back button
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if ( appCompatActivity.getSupportActionBar() != null ){
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Click on button submit insert users
        Button buttonActionSubmitInsertUsers = v.findViewById(R.id.actionSubmitInsertUsers);

        // TextView parameters
        final Spinner  searchNationalitiesTextView = v.findViewById(R.id.insert_users_nationality_value);
        final Spinner  searchGenderSpinner         = v.findViewById(R.id.insert_users_gender_value);
        final TextView searchNumUsersTextView      = v.findViewById(R.id.insert_users_number_value);
        final TextView searchRegisterDateTextView  = v.findViewById(R.id.insert_users_register_date_value);

        buttonActionSubmitInsertUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get values from fields
                String searchNationalities = searchNationalitiesTextView.getSelectedItem().toString();
                String searchGender = searchGenderSpinner.getSelectedItem().toString();
                int searchNumUsers;
                try {
                    searchNumUsers = Integer.parseInt(searchNumUsersTextView.getText().toString());
                } catch (Exception e) {
                    //e.printStackTrace();
                    searchNumUsers = 1;
                }

                String searchRegisterDate = searchRegisterDateTextView.getText().toString();

                // Generate async api call
                new RandomUserAPI( getActivity(), searchNationalities, searchGender, searchNumUsers, searchRegisterDate).execute();

                // Return to main fragment
                getActivity().onBackPressed();
            }
        });

        // Nationalities spinner
        String[] nationalities = getResources().getStringArray( R.array.nationalities );
        ArrayAdapter<String> nationalitiesArrayAdapter = new ArrayAdapter<>( getActivity(), R.layout.spinner_item, nationalities);
        searchNationalitiesTextView.setAdapter( nationalitiesArrayAdapter );

        // Gender spinner
        String[] genders = getResources().getStringArray( R.array.genders );
        ArrayAdapter<String> gendersArrayAdapter = new ArrayAdapter<>( getActivity(), R.layout.spinner_item, genders);
        searchGenderSpinner.setAdapter( gendersArrayAdapter );

        // Click on num users
        searchNumUsersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Number picker
                final NumberPicker numberPicker = new NumberPicker( getActivity()) ;
                numberPicker.setMaxValue( getResources().getInteger(R.integer.num_picker_max) );
                numberPicker.setMinValue( getResources().getInteger(R.integer.num_picker_min) );

                // Alert dialog to display number picker
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
                builder.setTitle( getString(R.string.num_picker_title) );

                builder.setPositiveButton( getString(R.string.num_picker_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedNumber = "" + numberPicker.getValue();
                        searchNumUsersTextView.setText( selectedNumber );
                    }
                });

                builder.setView( numberPicker );

                builder.create().show();
            }
        });

        // Click on register date
        searchRegisterDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Date
                Calendar calendar = new GregorianCalendar();

                // Date picker
                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        NumberFormat formatter = new DecimalFormat("00");
                        String selectedDate = year + "-" + formatter.format( month + 1 ) + "-" + formatter.format( day );
                        searchRegisterDateTextView.setText( selectedDate );
                    }
                }, ( calendar.get(Calendar.YEAR) -4 ), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // Reset value on cancel
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        searchRegisterDateTextView.setText( getString(R.string.default_insert_users_start_date) );
                    }
                });

                // Show date picker
                datePickerDialog.show();
            }
        });

        return v;
    }
}
