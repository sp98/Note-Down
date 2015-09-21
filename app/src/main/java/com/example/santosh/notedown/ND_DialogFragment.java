package com.example.santosh.notedown;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_DialogFragment extends DialogFragment {

    Button byes, bno;
    TextView dialog_message;
    Communicator communicator;
    int myMode;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.nd_dialogfragment_layout, null);
        setCancelable(false);


        byes = (Button) v.findViewById(R.id.OK);
        bno = (Button) v.findViewById(R.id.Cancel);
        dialog_message = (TextView) v.findViewById(R.id.dialog_message);
        //Message.message(getActivity(), "Dialog Started");

        Bundle args = getArguments();
        myMode = args.getInt("MyMode");



        switch(myMode){
            case 1:
                getDialog().setTitle("Delete");
                dialog_message.setText("Delete Selected Notes");
                break;

            case 2:
                getDialog().setTitle("Delete");
                dialog_message.setText("Delete All Notes?");
                break;

            case 3:
                getDialog().setTitle("Exit");
                dialog_message.setText("Exit Note Down?");

                break;
            case 4:
                getDialog().setTitle("Welcome!");
                dialog_message.setText("Welcome to NOTE DOWN. \n An Easy way to create Notes and share them across your friends and Colleagues.");
                byes.setText("Dismiss");
                bno.setText("OK");
                break;

            default:
        }




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

                if(myMode==4){
                    dismiss();
                    communicator.welcomeMessageState(true);
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

                if(myMode==4){
                    dismiss();
                    communicator.welcomeMessageState(false);
                }

                communicator.deleteEntries(false, myMode);
                dismiss();
            }
        });

        return v;


    }

    interface Communicator {
        public static int  deleteAll = 1;
        public static int deleteSelected = 2;
        public void deleteEntries(boolean b, int mode);
        public void welcomeMessageState (boolean b);

    }
}
