
/* Class Name: ND_MainActivity
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */


package santosh.pillai.sp98.notedown;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



/**
 * Created by Santosh on 9/19/2015.
 *
 * This class creates a Dialog Fragment based on what mode the user has selected.
 * 1 - Delete Selected Entries Mode
 * 2.- Delete All Mode
 * 3.- Exit Mode
 * 4.- Welcome Dialog Fragment Mode.
 */


public class ND_DialogFragment extends DialogFragment {

    Button byes, bno, dismiss_button, gotIt_button, add_button;
    TextView dialog_message;
    Communicator communicator;
    int myMode;   // This is made equal to the either 1, 2, 3 or 4.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        myMode = args.getInt("MyMode");
        communicator = (Communicator) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the theme of the Dialog Fragment based on the Mode
        if((myMode==1)|| (myMode==2) || (myMode==3)){

            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light);
        }
        else{
            setStyle(STYLE_NORMAL, R.style.Welcome); // dark screen on the whole scree

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // defining the width and height of the dialog fragment if the mode is 1 (Delete), 2(Delete all)  or 3 (Exit)
        if((myMode==1)|| (myMode==2) || (myMode==3)){
            int width = 550;
            int height = 220;

            if(getDialog()!=null){
                getDialog().getWindow().setLayout(width,  height);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set the Layout of the Dialog fragment based on the Mode.
        View v= null;
        if((myMode==1)|| (myMode==2) || (myMode==3)){
             v = inflater.inflate(R.layout.nd_dialogfragment_layout, null);
        }
       else {
            v= inflater.inflate(R.layout.nd_welcome_guide_layout, null);
        }

        setCancelable(false);  //Dialog fragment can not be cancelled by clicking outside.


        //Initialization of butons
        byes = (Button) v.findViewById(R.id.OK);
        bno = (Button) v.findViewById(R.id.Cancel);
        dismiss_button= (Button) v.findViewById(R.id.dismiss);
        gotIt_button = (Button) v.findViewById(R.id.gotIt);
        add_button = (Button) v.findViewById(R.id.add_button_on_welcome_message);

        dialog_message = (TextView) v.findViewById(R.id.dialog_message);


        //Dismissing the Welcome pop up dialog fragment on clicking the back button
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK)){
                    dismiss();
                    return true;
                }
                else
                    return false;
            }
        });
        

        // Setting the text message and title on the diaglog fragment based on the current Mode.
        switch(myMode){
            case 1:
                getDialog().setTitle("Delete");
                dialog_message.setText("Delete Selected Notes? ");
                dialog_message.setTextSize(17);
                onClickOperations();
                break;

            case 2:

                getDialog().setTitle("Delete");
                dialog_message.setText("Delete All Notes?");
                dialog_message.setTextSize(17);
                onClickOperations();
                break;

            case 3:

                getDialog().setTitle("Exit");
                dialog_message.setText("Exit Note Down? ");
                dialog_message.setTextSize(17);
                onClickOperations();
                break;

            case 4:

                dismiss_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        communicator.welcomeMessageState(true);
                    }
                });

                gotIt_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        communicator.welcomeMessageState(false);

                    }
                });

                add_button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dismiss();
                        Intent i = new Intent(getActivity(), ND_NoteDetailScreen.class);
                        startActivity(i);

                    }
                });

                break;

            default:
        }

        return v;

    }

    // Determines the user action on the Dialog fragments on Mode 1, 2 and 3
    public void onClickOperations(){

        byes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myMode==3){
                    dismiss();
                    ND_MainActivity ma = new ND_MainActivity();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ma.finish();

                }

                communicator.deleteEntries(true, myMode);
                dismiss();
            }
        });

        bno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myMode==3){
                    dismiss();
                }

                communicator.deleteEntries(false, myMode);
                dismiss();
            }
        });


    }

    interface Communicator {
        public static int  deleteAll = 1;
        public static int deleteSelected = 2;
        public void deleteEntries(boolean b, int mode);
        public void welcomeMessageState (boolean b);

    }
}
