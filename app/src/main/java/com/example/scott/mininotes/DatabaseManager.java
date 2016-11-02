package com.example.scott.mininotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Database for MiniNotes
 * Created by Scott on 10/9/2016.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SavedNotes";
    public static final int DATABASE_VERSION = 1;
    public static final String HISTORY_TABLE = "tblNotes";
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String SUBJECT = "subject";
    public static final String NOTE = "note";
    private Context appContext;


    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        appContext = context;
    }

    /**
     * Create database
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // build the create table statement
        String sqlCreate = "create table " + HISTORY_TABLE + " ( "
                + ID + " integer primary key autoincrement, "
                + DATE + " text, "
                + SUBJECT + " text, "
                + NOTE + " text "
                + ")";
        try {
            db.execSQL(sqlCreate);
        } catch (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Insert MiniNote Entry into Database
     *
     * @param subject
     * @param date
     * @param note
     * @return
     */


    public long insert(String subject, String date, String note) {
        long newId = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DATE, date);
            values.put(SUBJECT, subject);
            values.put(NOTE, note);

            newId = db.insert(HISTORY_TABLE, null, values);
            db.close();
        } catch (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
        return newId;
    }

    /**
     * Autocomplete subject and date textfields.
     *
     * @param context
     * @param column
     * @return
     */


    public ArrayAdapter<String> fillAutoCompleteTextFields(Context context, String column) {

        ArrayAdapter<String> adapter = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            // select distinct values in column
            Cursor cursor = db.query(true, HISTORY_TABLE, new String[]{column},
                    null, null, null, null, column, null);

            int numberOfRecords = cursor.getCount();
            if (numberOfRecords > 0) {
                cursor.moveToFirst();
                String[] autoTextOptions = new String[numberOfRecords];
                for (int i = 0; i < numberOfRecords; i++) {
                    autoTextOptions[i] = cursor.getString(cursor.getColumnIndex(column));
                    cursor.moveToNext();
                }
                adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        autoTextOptions);
                db.close();
            }
        } catch (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
        return adapter;


    }


    /**
     * Return results based on subject or date search
     * @param columnName
     * @param columnValue
     * @return
     */

    public ArrayList<String> selectByColumn(String columnName, String columnValue) {

        ArrayList<String> historyList = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(HISTORY_TABLE, null, columnName + "=?",
                    new String[]{columnValue}, null, null, columnName);

 //           Cursor cursor = db.query("tblNotes", new String[]{"subject"}, "subject=?",
 //                   new String[]{"quail"}, null, null, "subject");

//            Cursor cursor = db.query(“candy_Tbl”, null, “candy_name=?,
//            “1.99”, null, null, “candy_name”);


            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String oneRecord = "";
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    oneRecord += cursor.getString(i) + " ";
                }
                historyList.add(oneRecord);
                cursor.moveToNext();
            }
        } catch (SQLException se) {
            Toast.makeText(appContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
        return historyList;
    }


}
