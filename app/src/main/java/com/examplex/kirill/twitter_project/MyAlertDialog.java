package com.examplex.kirill.twitter_project;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examplex.kirill.twitter_project.adapters.rvAdapter;
import com.examplex.kirill.twitter_project.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.realm.Realm;

@SuppressLint("ValidFragment")
public class MyAlertDialog extends DialogFragment {

    long position;
    EditText editText;
    Messages msg;
    rvAdapter adapter;

    public HashMap<String,String> msgMap;

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("ValidFragment")
    public MyAlertDialog(long position) {
        this.position = position;
    }

    public MyAlertDialog(long editID, rvAdapter adapter,HashMap<String,String> map) {
        this.position = editID;
        this.adapter = adapter;
        this.msgMap = map;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mRef = FirebaseDatabase.getInstance().getReference();

        final Realm realm = Realm.getDefaultInstance();
        msg = realm.where(Messages.class).equalTo(Messages.MSG_ID, position).findFirst();
        String title = "edit item";
        final int listPosition = getPos();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок

        View v =getActivity().getLayoutInflater().inflate(R.layout.activity_edit, null);
        builder.setView(v);
        editText = (EditText) v.findViewById(R.id.edit_item);

        editText.setText(msg.getMsgText().toString(), TextView.BufferType.EDITABLE);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                realm.beginTransaction();
                msg.setMsgText(editText.getText().toString());
                adapter.list.get(listPosition).setMsgText(editText.getText().toString());
                realm.commitTransaction();

                adapter.updatePosition(position);
                mRef.child(user.getUid()).child("magText").child(msg.getFbKey()).setValue(msg.getMsgText());


            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setCancelable(true);
        adapter.updatePosition(position);
        return builder.create();
    }

    public int getPos() {
        for(int i = 0; i < adapter.list.size(); i++)
        {
            if(adapter.list.get(i).getMsgId() == position)
            {
                return i;
            }
        }
        return 0;
    }
}
