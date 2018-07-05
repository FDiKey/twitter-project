package com.examplex.kirill.twitter_project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.examplex.kirill.twitter_project.adapters.rvAdapter;
import com.examplex.kirill.twitter_project.models.Messages;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {
    private RealmList<Messages> list = new RealmList<>();
    final Context context = this;
    EditText addEdit;
    Realm realm;
    rvAdapter adapter;
    private AlertDialog.Builder ad;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        initData();
        initNovigation();

    }

    private void initNovigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AlertInit();

        Drawer.Result drawer =  new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggle(true)
                .addDrawerItems( new PrimaryDrawerItem()
                                .withName(R.string.d_List)
                                .withIdentifier(1),
                        new PrimaryDrawerItem()
                                .withName(R.string.d_add)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName(R.string.d_profile)
                                .withIdentifier(3)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch(drawerItem.getIdentifier())
                        {
                            case 1:{

                                break;
                            }
                            case 2:{

                                AlertInit();
                                ad.show();
                                break;
                            }
                            case 3:{

                                break;
                            }
                        }
                    }
                })
                .build();



    }

    private void AlertInit() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_item,null);
        ad = new AlertDialog.Builder(context);
        ad.setTitle(R.string.adTitle);
        ad.setView(view);
        final EditText addEdit = (EditText) view.findViewById(R.id.add_edit);
        ad.setPositiveButton(R.string.d_add, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Messages msg = new Messages();
                String temp = addEdit.getText().toString();
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                msg.setMsgText(temp);
                msg.setMsgDate(new Date());
                realm.insert(msg);
                realm.commitTransaction();
                adapter.addItem(msg);
            }

        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        }).create();


    }

    private void initData() {
        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.hasFixedSize();
        list = initList();
        adapter = new rvAdapter(list);
        rv.setAdapter(adapter);


    }

    public RealmList<Messages> initList(){
        RealmList list = new RealmList();
        Realm realm = Realm.getDefaultInstance();
        list.addAll(realm.where(Messages.class).findAll());
        return list;
    }






//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        // Get the layout inflater
//        LayoutInflater inflater = this.getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.add_item, null))
//                // Add action buttons
//                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }
}
