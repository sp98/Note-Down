
/* Class Name: ND_MainActivity
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */


package com.example.santosh.notedown;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ND_MainActivity extends ActionBarActivity implements ND_DialogFragment.Communicator{

   // Data Storage options for different columns in the data base
    public List<String> title = new ArrayList<>();
    public List<String> description = new ArrayList<>();
    public List<String> time = new ArrayList<>();
    public List<String> timeStamp = new ArrayList<>();

    // Storage options to keep track of the selected rows on long click
    List<String> tobeDeleted = new ArrayList<>();
    List<Integer> tobeDeletedPosition = new ArrayList<>();


    public static int sortOrder=1;
    private int theActualMode;
    private int singleDeleteMode = 1;
    private int allDeleteMode = 2;
    private int backButtonCount;
    private int singlePosSelected;
    private int checkedItemPosition;

    ND_DataBaseAdapter dataBaseApdater = null;
    ListView customlistview = null;
    Context c = null;

    private boolean confirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nd_activity_main);


        setTitle("Note Down");  // set title of the Activity

        //Initialization
        c = getApplicationContext();
        customlistview = (ListView) findViewById(R.id.mylistView);
        dataBaseApdater = new ND_DataBaseAdapter(this);



        retrieveSavedData();  //retrieves the data from the data base when the app loads first

        loadSavedData(title, description, time);  //loads the data into the custom list view on the main activity



        //Takes the user to detailScreen activity when a row in the List view is clicked. Data is also transfered in Bundle
        customlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Message.message(getApplicationContext(), "Item at" +titles[position]+ "was clicked");
                String titleText = title.get(position);
                String descText = dataBaseApdater.retriveSingleText(titleText);

                Intent i = new Intent(getApplicationContext(), ND_NoteDetailScreen.class);
                i.putExtra("SAVEDTITLE", titleText);
                i.putExtra("SAVEDDESC", descText);
                startActivity(i);
            }
        });




    // Setting the list view to multiple selections.

    customlistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    customlistview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

        // Invoked each time user selects a row on the listview using long click.
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            final int checkedCount = customlistview.getCheckedItemCount();
            mode.setTitle(checkedCount + " " + "Selected");

            singlePosSelected= checkedCount;
            checkedItemPosition= position;

            // adding position of the selected rows in the arraylist. Unique elements gets added and duplicates get deleted.
            if (tobeDeletedPosition.isEmpty()) {
                tobeDeletedPosition.add(position);
            } else {
                boolean notPresent = true;
                for (int i = 0; i < tobeDeletedPosition.size(); i++) {
                    if (tobeDeletedPosition.get(i) == position) {
                        tobeDeletedPosition.remove(i);
                        notPresent = false;
                        break;
                    } else {
                        continue;
                    }

                }
                if (notPresent)
                    tobeDeletedPosition.add(position);
            }

        }


        // when user performs a long click. Inflate the CAB over here.
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflator = mode.getMenuInflater();
            inflator.inflate(R.menu.nd_menu_multipleselection, menu);

            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        // Behavior when delete and share icons are clicked on the CAB
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


            switch (item.getItemId()) {
                case R.id.delete:
                    singleDeleteMode = 1;
                    showTheDialog(singleDeleteMode);
                    mode.finish();
                    return true;

                case R.id.share_date_home:
                    if(singlePosSelected==1){

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, description.get(checkedItemPosition));
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));}

                    else{
                        ND_ToastMessage.message(getApplicationContext(), "Supported for Single Note");
                    }

               /* case R.id.newReminder:
                    if(singlePosSelected==1){
                        DateandTimeDialog dtd= new DateandTimeDialog();
                        FragmentManager manger = getFragmentManager();
                        dtd.show(manger, "Set Date And Time Dialog");

                    }
                    else {
                        ND_ToastMessage.message(getApplicationContext(), "Supported for Single Note");
                    } */
                default:
                    return false;
            }  }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // tobeDeletedPosition.clear();
        }
    });
}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nd_menu_main, menu);

        // Search functionality in the Action bar.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Using Support Library
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

        // API Level 11 and above
        // SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.AddNewNote:
                Intent i = new Intent(this, ND_NoteDetailScreen.class);
                startActivity(i);
                return true;
            case R.id.action_deleteAll:
                allDeleteMode = 2;
                showTheDialog(allDeleteMode);
                return true;
            case R.id.action_sort:
                ND_SettingsDialogBox settingsFrag = new ND_SettingsDialogBox();
                FragmentManager manager = getFragmentManager();
                settingsFrag.show(manager,"Settings Window");
                return true;
            case R.id.action_exit:
                int exit = 3;
                showTheDialog(exit);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //retrieves the data from the data base when the app loads first
    public void retrieveSavedData(){
        title = dataBaseApdater.retriveTitle(sortOrder);
        description = dataBaseApdater.retriveDescription(sortOrder);
        time = dataBaseApdater.retriveTime(sortOrder);

    }



    public void loadSavedData(List title, List description, List time){


        timeStamp = time;
        List<String> formatedTime = new ArrayList<>();

        for (int i = 0; i < timeStamp.size(); i++) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("MMM dd\nhh:mm a");
            try {
                formatedTime.add(outputFormat.format(inputFormat.parse(timeStamp.get(i))));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


        ND_CustomListViewAdapter customListViewAdapter = new ND_CustomListViewAdapter(this, title, description, formatedTime);
        customlistview.setAdapter(customListViewAdapter);


    }


    // invoking the Dialog Fragment when Delete All or CAB Delete options are clicked.
    public void showTheDialog(int myMode) {
        int theMode = myMode;
        FragmentManager manager = getFragmentManager();
        ND_DialogFragment dadaptr = new ND_DialogFragment();
        Bundle b = new Bundle(myMode);
        b.putInt("MyMode", theMode);
        dadaptr.setArguments(b);
        //dadaptr.setStyle(DialogFragment.STYLE_NORMAL, 0);
        dadaptr.show(manager, "My Dialoge");


    }

    // Deleting entries based on the CAB and Delete All
    public void deleteEntries(boolean b, int Selectedmode) {

        confirmation = b;
        theActualMode = Selectedmode;

        if (confirmation) {
            // When Delete All was pressed.
            if (theActualMode == 2) {
                // Message.message(getApplicationContext(), "User aggreed");
                dataBaseApdater.deleteAll(c);
                title = dataBaseApdater.retriveTitle(sortOrder);
                description = dataBaseApdater.retriveDescription(sortOrder);
                time = dataBaseApdater.retriveTime(sortOrder);
                loadSavedData(title, description, time);
                ND_ToastMessage.message(getApplicationContext(), "All Notes deleted");
            }

            // when delete was called from CAB with limited selections.
            if (theActualMode == 1) {
                for (int i = 0; i < tobeDeletedPosition.size(); i++) {
                    int pos2 = tobeDeletedPosition.get(i);
                    tobeDeleted.add(title.get(pos2));
                }


                for (String s1 : tobeDeleted) {
                    dataBaseApdater.delete(s1, c);
                    title = dataBaseApdater.retriveTitle(sortOrder);
                    description = dataBaseApdater.retriveDescription(sortOrder);
                    time = dataBaseApdater.retriveTime(sortOrder);
                    loadSavedData(title, description, time);
                }
                ND_ToastMessage.message(getApplicationContext(), tobeDeletedPosition.size() + " " + "Note(s)" + " " + "Deleted");
            }
        }

        tobeDeletedPosition.clear();
        tobeDeleted.clear();


    }



}
