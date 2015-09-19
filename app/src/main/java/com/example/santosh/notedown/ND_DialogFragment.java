package com.example.santosh.notedown;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_DialogFragment extends DialogFragment {

    Button byes, bno;
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
        //Message.message(getActivity(), "Dialog Started");

        Bundle args = getArguments();
        myMode = args.getInt("MyMode");


        switch(myMode){
            case 1:
                getDialog().setTitle("Delete Selected Notes?");
                break;

            case 2:
                getDialog().setTitle("Delete All Notes?");
                break;

            case 3:
                getDialog().setTitle("Exit Quick Notes?");

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

        return v;


    }

    interface Communicator {
        public static int  deleteAll = 1;
        public static int deleteSelected = 2;
        public void deleteEntries(boolean b, int mode);

    }
}
