package com.example.santosh.notedown;

import android.app.Dialog;
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
public class ND_SettingsDialogBox  extends DialogFragment{
    Button ascendingOrder;
    Button descedingOrder;
    Button byCreatedTime;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nd_settingsdialogfragment_layout, null);
        ascendingOrder = (Button) v.findViewById(R.id.ascendingOrder);
        descedingOrder= (Button) v.findViewById(R.id.descendingOrder);
        byCreatedTime = (Button) v.findViewById(R.id.byCreatedTime);


        getDialog().setTitle("Sort");

        ascendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ND_MainActivity.sortOrder=2;
                dismiss();
                //ma.retriveAllData();
                backToMainActivity();


            }
        });

        descedingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ND_MainActivity.sortOrder=3;
                dismiss();
                //ma.retriveAllData();
                backToMainActivity();


            }
        });

        byCreatedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ND_MainActivity.sortOrder=1;
                dismiss();
                //ma.retriveAllData();
                backToMainActivity();

            }
        });



        return v;

    }

    // Starts the main activity again after refreshing the page.
    public void backToMainActivity(){
        Intent i = new Intent(getActivity(),ND_MainActivity.class);
        startActivity(i);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // to remove the title space in the dialog fragment


        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
}

