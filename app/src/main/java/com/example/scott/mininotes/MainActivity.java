package com.example.scott.mininotes;

/**
 * Controller for Main Activity of MiniNotes
 * A lot of the code in this app is copied and adapted from Tipper.
 * Created by Scott Englehart on 10/9/2016.
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager dbManager;
    private String date;


    /**
     * onCreate
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DatabaseManager(this);
        // set date
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        TextView dateText = (TextView) findViewById(R.id.date_text);
        dateText.setText("Date: " + date);
    }


    /**
     * Handler for save button
     * Save note to database
     *
     * @param v
     */
    public void saveNote(View v) {

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // get subject
        EditText subjectText = (EditText) findViewById(R.id.subject_text);
        String subject = subjectText.getText().toString();
        if (subject.isEmpty()) {
            Toast.makeText(this, R.string.errorSubject, Toast.LENGTH_LONG).show();
            return;
        }
        // get note
        EditText noteText = (EditText) findViewById(R.id.note_text);
        String note = noteText.getText().toString();
        if (note.isEmpty()) {
            Toast.makeText(this, R.string.errorNote, Toast.LENGTH_LONG).show();
            return;
        }
        if (note.length() > 125) {
            Toast.makeText(this, R.string.errorLongNote, Toast.LENGTH_LONG).show();
            return;
        }
        // save to database and reset
        dbManager.insert(subject, date, note);
        Toast.makeText(this, R.string.noteSaved, Toast.LENGTH_LONG).show();
        noteText.setText("");
        subjectText.setText("");

    }

    /**
     * Start view note activity
     *
     * @param v
     */
    public void viewNotes(View v) {
        startActivity(new Intent(getApplicationContext(),
                ViewNoteActivity.class));
    }

    /**
     * Inflate the menu; adds items to the action bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Only one menu item to select
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view:
                viewNotes(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
