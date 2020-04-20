package com.raju.attendencetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SingleOptionDailog extends DialogFragment {
    int position = 0;
    public interface SingleChoiceListner{
        void onPositiveButtonClicked(String arr[],int position);
        void onNegativeButtonClicked();
    }
    SingleChoiceListner mLisner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mLisner=(SingleChoiceListner)context;
        }catch (Exception e){
            throw new ClassCastException(getActivity().toString()+"Error");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final String arr[]={"Sport Leave","Medical Leave","Duty Leave"};
        builder.setTitle("Select your option")
                .setSingleChoiceItems(arr, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         position=which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         mLisner.onPositiveButtonClicked(arr,position);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          mLisner.onNegativeButtonClicked();
                    }
                });
        return builder.create();
    }
}
