package com.example.santosh.notedown;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_Search extends ActionBarActivity {


    ND_DataBaseAdapter adapter3= null;
    List<String> singleRow = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<String> texts = new ArrayList<>();
    List<String> time= new ArrayList<>();
    List<String> formatedTime = new ArrayList<>();
    ND_MainActivity mainActivity =null;

    int desc_position= -1;

    ListView mylistview = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nd_search_layout);

        // Setting title of this activity in the Action Bar.
        setTitle("Searched Notes");

        mylistview= (ListView) findViewById(R.id.mySearchlistView);
        mainActivity = new ND_MainActivity();
        adapter3 = new ND_DataBaseAdapter(this);

        // Deleting entries in the arraylist for every new search
        if(!titles.isEmpty()){
            titles.clear(); }

        if(!texts.isEmpty()){
            texts.clear(); }

        if(!time.isEmpty()){
            time.clear(); }

        if(!formatedTime.isEmpty()){
            formatedTime.clear(); }

        // handling the intent when search is performed
        handleIntent(getIntent());

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = titles.get(position);

                String descText = adapter3.retriveSingleText(title);
                Add(title, descText);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("TAG", "on new Intent");
        handleIntent(intent);
    }

    // Add method to navigate to the detail view of the searched Notes
    public void Add(String title, String destext) {
        Intent i = new Intent(this, ND_NoteDetailScreen.class);
        i.putExtra("SAVEDTITLE", title);
        i.putExtra("SAVEDDESC", destext);
        startActivity(i);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // mSampleTxv.setText(intent.getStringExtra(SearchManager.QUERY));
            //  Log.e("TAG", "Query is");
            String query= intent.getStringExtra(SearchManager.QUERY);
            singleRow = adapter3.searchDB(query);

            // Retrieving individual strings (Title, text and Time) from the singleRow list<String>
            for(int i = 0; i<singleRow.size();i++){
                //String title = singleRow.get(i);
                titles.add(singleRow.get(i));
                i++;
                texts.add(singleRow.get(i));
                i++;
                time.add(singleRow.get(i));
            }

            // changing the format of the Timestamp to "MMM dd,  hh:mm a"
            for (int i = 0; i < time.size(); i++) {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat outputFormat = new SimpleDateFormat("MMM dd\nhh:mm a");
                try {
                    formatedTime.add(outputFormat.format(inputFormat.parse(time.get(i))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            // calling the Custom list view.
            if(!titles.isEmpty()){
                ND_CustomListViewAdapter adapter = new ND_CustomListViewAdapter(this, titles, texts, formatedTime);
                mylistview.setAdapter(adapter);
            }
            else {
                ND_ToastMessage.message(getApplicationContext(), "No Notes Found");

            }

        }
    }
}

