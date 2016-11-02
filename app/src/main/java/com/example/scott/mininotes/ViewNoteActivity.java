package com.example.scott.mininotes;

/**
 * Controller for View Activity of MiniNotes
 * Created by Scott Englehart on 10/9/2016.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewNoteActivity extends AppCompatActivity {

    private DatabaseManager dbManager;

    /**
     * onCreate
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        dbManager = new DatabaseManager( this );
        // 
        ArrayAdapter dateAdapter = dbManager.fillAutoCompleteTextFields( this, DatabaseManager.DATE );
        if ( dateAdapter != null ) {
            AutoCompleteTextView dateEntry = (AutoCompleteTextView)findViewById(R.id.date_entry);
            dateEntry.setAdapter(dateAdapter);
        }
        ArrayAdapter subjectAdapter = dbManager.fillAutoCompleteTextFields( this, DatabaseManager.SUBJECT );
        if ( subjectAdapter !=  null ) {
            AutoCompleteTextView subjectEntry = (AutoCompleteTextView)findViewById(R.id.subject_entry);
            subjectEntry.setAdapter(subjectAdapter);
        }
    }

    /**
     * Search by Subject or Date
     * @param v
     */

    public void showDataByColumn( View v ) {

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if ( imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus( ).getWindowToken(), 0);
        }

        ArrayList<String> results = new ArrayList<String>();
        RadioButton rDateButton = (RadioButton)findViewById(R.id.radio_date);
        RadioButton rSubjectButton = (RadioButton)findViewById(R.id.radio_subject);

        // Search by Date
        if ( rDateButton.isChecked( )) {
            AutoCompleteTextView dateEntry = (AutoCompleteTextView)findViewById(R.id.date_entry);
            String columnValue = dateEntry.getText( ).toString( );
            if ( columnValue.isEmpty( ) ) {
                Toast.makeText( this, R.string.errorDate, Toast.LENGTH_LONG).show();
            } else {
                results = dbManager.selectByColumn( DatabaseManager.DATE, columnValue);
                String header = DatabaseManager.DATE.toUpperCase( ) + ": " + columnValue;
                results.add( 0, header);
                displayData( results);
            }
        }
        // Search by Subject
        else if ( rSubjectButton.isChecked( )){
            AutoCompleteTextView subjectEntry = (AutoCompleteTextView)findViewById(R.id.subject_entry);
            String columnValue = subjectEntry.getText( ).toString( );
            if ( columnValue.isEmpty( ) ) {
                Toast.makeText( this, R.string.errorSubject, Toast.LENGTH_LONG).show();
            } else {
                results = dbManager.selectByColumn( DatabaseManager.SUBJECT, columnValue);
                String header = DatabaseManager.SUBJECT.toUpperCase( ) + ": " + columnValue;
                results.add( 0, header);
                displayData( results);
            }
        }

    }

    /**
     * Display the search results.
     * @param data
     */

    public void displayData( ArrayList<String> data) {
        TextView historyDisplay = (TextView) findViewById(R.id.db_contents);
        String historyData = "";
        for ( String s : data ) {
            historyData += s + "\n";
        }
        historyDisplay.setText( historyData);
    }


    // Inflate the menu; adds items to the action bar
    // if it is present
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater( ).inflate(R.menu.view, menu );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
        switch (item.getItemId()) {
            case R.id.compose:
                saveNotes(null );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNotes( View v ) {
        startActivity( new Intent( getApplicationContext( ),
                MainActivity.class));
    }


}
