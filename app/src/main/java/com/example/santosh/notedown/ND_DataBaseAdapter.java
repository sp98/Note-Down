/* Class Name: ND_DataBaseAdapter
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */

package com.example.santosh.notedown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_DataBaseAdapter {

    DBHelper helper;

    ND_DataBaseAdapter(Context c){
        helper=  new DBHelper( c);
    }

    // Searching the Database using the searched query in Searchable Activity.
    public List<String> searchDB(String query){
        String squery = query;
        String truncated = null;

        List<String> retrived =new ArrayList<>();
        if(!retrived.isEmpty()){
            retrived.clear();
        }
        SQLiteDatabase db1 = helper.getWritableDatabase();
        String[] columns = {DBHelper.TITLE, DBHelper.DESCRIPTION, DBHelper.TIME};

        Cursor cursor = db1.query(DBHelper.TABLE_NAME, columns, DBHelper.TITLE + " LIKE '%"+query+"%'"+ " "+ "OR" +" "+  DBHelper.DESCRIPTION + " LIKE '%"+query+"%'" ,null, null, null, DBHelper.TIME+ " " + "DESC");

        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(DBHelper.TITLE);
            String title = cursor.getString(index1);
            // Log.d("Title", "is" + title);
            retrived.add(title);

            int index2= cursor.getColumnIndex(DBHelper.DESCRIPTION);
            String text = cursor.getString(index2);
            if(text.length()>20) {
                truncated = text.substring(0, 19) + "...";
                //Log.d("Text", "is" + truncated);
                retrived.add(truncated); }
            else{
                // Log.d("Text", "is" + text);
                retrived.add(text);
            }

            int index3 = cursor.getColumnIndex(DBHelper.TIME);
            String time =  cursor.getString(index3);
            retrived.add(time);
        }
        return retrived;

    }


    // delete individual row in Main Activity when delete was pressed on CAB
    public void delete(String title, Context c){
        SQLiteDatabase db1 = helper.getWritableDatabase();
        String[] args1 = {title};
        db1.delete(DBHelper.TABLE_NAME, DBHelper.TITLE+"=?", args1);
    }


    //Delete All(drop table)
    public void deleteAll(Context c){
        SQLiteDatabase db2 = helper.getWritableDatabase();
        int old = DBHelper.DB_VERSION;
        int new1 = old+1;
        helper.onUpgrade(db2, old, new1);
    }

    // Inserting data into data table.
    public long insertData(String title, String text){
        SQLiteDatabase db1 = helper.getWritableDatabase();
        Long e_id = rowPresent(title, text);
        if(e_id== -1){
            ContentValues contentValues = new ContentValues();
            contentValues.put (helper.TITLE, title);
            contentValues.put(helper.DESCRIPTION, text);
            contentValues.put(helper.TIME, getDateTime());

            long id =  db1.insert(helper.TABLE_NAME, null, contentValues);
            return id;
        }

        else {
            long overwritten = overwriteData(e_id, title, text);
            return overwritten;
        }
    }


    // Checking if row is already present in Database or not.
    public long rowPresent(String title, String text){

        long id=-1;

        SQLiteDatabase db1 = helper.getWritableDatabase();
        String [] columns = {DBHelper.UID};
        Cursor cursor = db1.query(DBHelper.TABLE_NAME, columns, DBHelper.TITLE +"= '"+title+"'", null, null, null, null);

        while (cursor.moveToNext()){
            int index_id = cursor.getColumnIndex(DBHelper.UID);
            id = cursor.getLong(index_id);       }

        return id;
    }


    // Overwriting data in case if the row is already present.
    public long overwriteData(long existingID, String ttxt, String mtxt){
        long overwritten = -2;
        SQLiteDatabase db1 = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TITLE, ttxt);
        cv.put(DBHelper.DESCRIPTION, mtxt);
        cv.put(DBHelper.TIME, getDateTime() );

        String [] args1 = {String.valueOf(existingID)};

        db1.update(DBHelper.TABLE_NAME, cv, DBHelper.UID +"=?", args1);
        return overwritten;

    }

    // Getting the Current timestamp to be stored in the datbase.
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Retrieving ID
    public long getID(String ttxt){
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {DBHelper.UID};
        Cursor cursor = db2.query(DBHelper.TABLE_NAME, columns, DBHelper.TITLE +"= '"+ttxt+"'", null, null, null, null);
        long existingID=0 ;

        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(DBHelper.TITLE);
            existingID = cursor.getLong(index1);

        }
        return existingID;
    }


    // Retrieving titles
    public List retriveTitle(int sortOrder){
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {DBHelper.TITLE};
        // String columnTime = DBHelper.TIME;

        Cursor cursor = null;

        switch(sortOrder){

            case 1:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null,   DBHelper.TIME+ " " + "DESC");
                break;

            case 2:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "ASC" );
                break;

            case 3:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "DESC" );
                break;

            default:
                break;
        }



        List<String> retriveTitles = new ArrayList<>();
        //int count = 0;
        while (cursor.moveToNext()){

            int index1 = cursor.getColumnIndex(DBHelper.TITLE);
            String title = cursor.getString(index1);
            retriveTitles.add (title);
            // count++;
        }
        return retriveTitles;
    }



    // Retrieving Description Text
    public List retriveDescription(int sortOrder){
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {DBHelper.DESCRIPTION};
        Cursor cursor = null;
        switch(sortOrder){

            case 1:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null,   DBHelper.TIME+ " " + "DESC");
                break;

            case 2:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "ASC" );
                break;

            case 3:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "DESC" );
                break;

            default:
                break;
        }


        List<String> retriveText=new ArrayList<>();
        int i = 0;
        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            String text = cursor.getString(index1);
            retriveText.add(text);

            i++; }
        return retriveText;
    }


    // Retrive the single description of the Note when title is passed on as a parameter.
    public String retriveSingleText(String title){

        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {DBHelper.DESCRIPTION};
        Cursor cursor = db2.query(DBHelper.TABLE_NAME, columns, DBHelper.TITLE +"= '"+title+"'", null, null, null, null);

        String  retriveSingleText= null;

        while (cursor.moveToNext()){
            int index1 = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            retriveSingleText = cursor.getString(index1);

        }
        return retriveSingleText;
    }

    // Retrieving time from the database.
    public List retriveTime(int sortOrder){
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {DBHelper.TIME};
        Cursor cursor =null;
        switch(sortOrder){

            case 1:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null,   DBHelper.TIME+ " " + "DESC");
                break;

            case 2:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "ASC" );
                break;

            case 3:
                cursor = db2.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.TITLE+ " " + "DESC" );
                break;

            default:
                break;
        }


        List<String> retriveTime = new ArrayList<>();
        while (cursor.moveToNext()){

            int index1 = cursor.getColumnIndex(DBHelper.TIME);
            String time =  cursor.getString(index1);
            retriveTime.add(time);
        }
        return retriveTime;
    }

    public void sortAplhabetically(){}







    /*
     * Creating a Database schema for the app
     */
    public class DBHelper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "Mydatbase1";
        private static final String TABLE_NAME = "MyTable1";
        private static final int  DB_VERSION = 45;
        private static final String UID = "_id";
        private static final String TITLE="Title";
        private static final String DESCRIPTION="Description";
        private static final String TIME = "TIME";

        //Queries
        private static final String CREATE_TABLE = " CREATE TABLE "+TABLE_NAME+ " ("+UID+"  INTEGER PRIMARY KEY AUTOINCREMENT , "+TITLE+" VARCHAR(255), "+DESCRIPTION+" VARCHAR(255), "+TIME+"  VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private static final String UPDATE_TIME ="CREATE TRIGGER update_time" +
                "  AFTER UPDATE ON " +DBHelper.TABLE_NAME + " FOR EACH ROW" +
                "  BEGIN " +
                "UPDATE " + DBHelper.TABLE_NAME +
                "  SET " + DBHelper.TIME+ " = current_timestamp" +
                "  WHERE " + DBHelper.UID + " = old." + DBHelper.UID + ";" +
                "  END";



        private Context context;
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
            this.context = context;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);  //CREATE TABLE TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT , Name VARCHAR(255);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);

        }
    }


}
