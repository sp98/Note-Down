/* Class Name: ND_NoteDetailScreen
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */


package com.example.santosh.notedown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_NoteDetailScreen extends Activity {

    private boolean savePressed = false;
    private int backPressCount;
    public static boolean toEnable = true;

    ND_DataBaseAdapter adptr = null;

    Button save;
    EditText mainText, titleText;
    TextView maxTitleLength;
    Boolean showRemindericon = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nd_notedetailscreen_layout);
        backPressCount=1;
        setTitle("Add/Edit Notes");
        mainText = (EditText) findViewById(R.id.MainText);
        titleText = (EditText) findViewById(R.id.TitleText);
        maxTitleLength = (TextView) findViewById(R.id.maxTitleLength);

        mainText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mainText.setHint("");
            }
        });

        titleText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                titleText.setHint("");
            }
        });


       // retriving the title and descrption form the bundle objec that was sent via the intent from Main Activity
        Intent i = getIntent();
        Bundle bdl=getIntent().getExtras();

        // Retrieving text from Main Activity when particular row was clicked.
        String mt = i.getStringExtra("SAVEDDESC");
        String tt = i.getStringExtra("SAVEDTITLE");
        savePressed = true;

        mainText.setText(mt);
        titleText.setText(tt);
        mainText.setSelection(mainText.getText().length());

        adptr= new ND_DataBaseAdapter( this);


        save= (Button) findViewById(R.id.save);
        save.setEnabled(false);

        // Enabling the edittexts when clicked.
        mainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save.setEnabled(true);
                if (!toEnable)
                    unFreeze(titleText, mainText, save);
            }
        });

        // Enabling the SAVE button only when the user starts typing in the Main text editText view.
        mainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                savePressed= false;
                save.setEnabled(true);
                //saveEnabled = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Enabling the save button when the user starts typing on the title Text editText veiw.
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                savePressed= false;
                save.setEnabled(true);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reminderIcon();

    }

    public void reminderIcon(){
        if(titleText.length()>0){

            long id = adptr.rowPresent(titleText.getText().toString(), mainText.getText().toString());
            if(id>0){
                showRemindericon=true;
            }
        }

    }

    // Controlling back navigation from this activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if((mainText.length()==0 || titleText.length()==0)){
                Intent intent = new Intent(this, ND_MainActivity.class);
                startActivity(intent);
                return true;

            }
            if (((mainText.length()!=0)||titleText.length()!=0) ){

                if (savePressed==true){
                    Intent intent = new Intent(this, ND_MainActivity.class);
                    startActivity(intent);
                    // Message.message(getApplicationContext(), " Note was not saved");
                    return true;
                }
                if (backPressCount==1){
                    ND_ToastMessage.message(getApplicationContext(), " Press SAVE to save changes.");
                    backPressCount= 2;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            backPressCount=1;
                        }
                    }, 2000);
                    return true;
                }
            }
            if(backPressCount==2 && savePressed==false){
                Intent intent = new Intent(this, ND_MainActivity.class);
                startActivity(intent);
                ND_ToastMessage.message(getApplicationContext(), " Note was not saved");

                return true; }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nd_menu_notedetailscreen, menu);
        return true;

    }



    // When Save button is clicked
    public void savingNote(View v) {
        titleText.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        mainText.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        reminderIcon();

        backPressCount=1;
        String mtxt = mainText.getText().toString();
        String ttxt = titleText.getText().toString();
        long id = 0;


        if(TextUtils.isEmpty(ttxt)){
            ND_ToastMessage.message(this, "Please Enter a Title");
            return;
        }
        else{
            id= adptr.insertData(ttxt, mtxt);

        }

        if(id>-1) {
            savePressed= true;
            ND_ToastMessage.message(this, "Saved");
            freeze(titleText, mainText, save);

        } else if (id== -2){
            savePressed= true;
            ND_ToastMessage.message(this, "Updated");
            freeze(titleText, mainText, save);
        }
        else
            ND_ToastMessage.message(this, "Note was not Saved. Please Try Again!");
    }


    public void freeze(EditText titleText, EditText mainText, Button save){
        toEnable = false;
        titleText.setCursorVisible(false);
        mainText.setCursorVisible(false);
        save.setEnabled(false);


    }

    public void unFreeze (EditText titleText, EditText mainText, Button save){
        toEnable = true;
        titleText.setCursorVisible(true);
        mainText.setCursorVisible(true);
        // save.setEnabled(true);


    }

    // Sharing the data with other Applications.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.share_data:
                if(mainText.length()>0){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mainText.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);}

                else{
                    ND_ToastMessage.message(getApplicationContext(), "Nothing to Share");
                }
                break;
            case R.id.newReminder:
                if((titleText.length()==0) && (mainText.length()==0)){

                    ND_ToastMessage.message(getApplicationContext(), "Please Create a Note first");
                }

                else if(save.isEnabled()){
                    ND_ToastMessage.message(getApplicationContext(), "Please Save the Note first"); }

                else{

                 /*   DateandTimeDialog dtd= new DateandTimeDialog();
                    FragmentManager manger = getFragmentManager();
                    dtd.show(manger, "Set Date And Time Dialog");

                    // start calendar
                   */
                }


                //showRemindericon= false;
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
