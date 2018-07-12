package com.examplex.kirill.twitter_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examplex.kirill.twitter_project.models.Messages;

import io.realm.Realm;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edittext;
    Button btn_cancel, btn_save;
    Intent intent;
    long position;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initData();
        intent = getIntent();
        position = intent.getLongExtra("position",101l );
        Toast.makeText(this, ""+position,Toast.LENGTH_SHORT).show();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        Messages temp = realm.where(Messages.class).equalTo(Messages.MSG_ID, position).findFirst();
        if(temp != null) {
            edittext.setText(temp.getMsgText(), TextView.BufferType.EDITABLE);
        }
        else{
            Toast.makeText(this, "fatal", Toast.LENGTH_SHORT).show();
        }

    }

    private void initData() {
//        edittext = (EditText) findViewById(R.id.edit_text);
//        btn_cancel= (Button) findViewById(R.id.edit_btn_cancel);
//        btn_save= (Button) findViewById(R.id.edit_btn_save);
//        btn_cancel.setOnClickListener(this);
//        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.edit_btn_save: {
//
//                Messages msg = realm.where(Messages.class).equalTo(Messages.MSG_ID, position).findFirst();
//                if(msg != null)
//                {
//                    realm.beginTransaction();
//                    msg.setMsgText(edittext.getText().toString());
//                    realm.commitTransaction();
//                    Intent intent = new Intent();
//                    intent.putExtra("position", msg.getMsgId());
//
//                    setResult(RESULT_OK,intent);
//                    finish();
//                } else {
//                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//            case R.id.edit_btn_cancel:{
//                finish();
//                break;
//            }
//        }
//    }
}
