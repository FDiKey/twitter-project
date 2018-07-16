package com.examplex.kirill.twitter_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import com.examplex.kirill.twitter_project.adapters.rvAdapter;
import com.examplex.kirill.twitter_project.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class MainActivity extends AppCompatActivity {
    private RealmList<Messages> list = new RealmList<>();
    final Context context = this;
    private String[] tempList;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    List<String> tasks;
    public HashMap<String,String> msgMap;

    EditText addEdit;
    Realm realm;
    rvAdapter adapter;
    private AlertDialog.Builder ad;
    private MyAlertDialog editAd;
    private RecyclerView rv;
    Messages msg = new Messages();
    long editID = 0;
    boolean authorize = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        realm = Realm.getDefaultInstance();
        initFB();
//        initData();
        initNovigation();

    }



    private void initFB() {
        mRef = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = mAuth.getInstance().getCurrentUser();


        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!authorize) {

                    Realm realm = Realm.getDefaultInstance();
                    List<Messages> l = realm.where(Messages.class).findAll();
                    int cnt = 0;
                    GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {
                    };
                    msgMap = dataSnapshot.child("magText").getValue(t);
                    authorize = true;
                    initData();
                }
                else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });


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
                Realm realm;

                Messages msg = new Messages();
                String temp = addEdit.getText().toString();
                String s = mRef.child(user.getUid()).child("magText").push().getKey();
                mRef.child(user.getUid()).child("magText").child(s).setValue(temp);
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                msg.setMsgText(temp);
                msg.setMsgId(idNextVal(realm));
                msg.setMsgDate(new Date());
                msg.setFbKey(s);
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
        for (Map.Entry<String, String> value : msgMap.entrySet()) {
            Messages m = new Messages();
            realm.beginTransaction();
            m.setFbKey(msgMap.keySet().toString());
            m.setMsgId(idNextVal(realm));
            m.setMsgDate(new Date());
            m.setFbKey(value.getKey());
            m.setMsgText(value.getValue());
            realm.insert(m);
            realm.commitTransaction();
        }
        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.hasFixedSize();
        list = initList();
        adapter = new rvAdapter(list);
        rv.setAdapter(adapter);


        final SwipeController swipeController = new SwipeController(new SwipeControllerAction() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);

                editID = adapter.list.get(position).getMsgId();
                editAd = new MyAlertDialog(editID, adapter, msgMap);
                editAd.show(getSupportFragmentManager(),"MyAlertDialog");

            }
            int p;
            @Override
            public void onRightClicked(final int position) {
                super.onRightClicked(position);
                p = position;
                final Messages deletedModel = list.get(position);
                final int deletedPosition = position;
                Realm realm = Realm.getDefaultInstance();
                mRef.child(user.getUid()).child("magText").child(list.get(position).getFbKey()).removeValue();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Messages> result = realm.where(Messages.class)
                                .equalTo(Messages.MSG_ID,list.get(deletedPosition).getMsgId())
                                .findAll();
                        result.deleteAllFromRealm();
                    }
                });

                adapter.removeItem(position);

            }
        }); // SWIPES LEFT AND RIGHT

        ItemTouchHelper helper = new ItemTouchHelper(swipeController);
        helper.attachToRecyclerView(rv);

        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }
    public RealmList<Messages> initList(){
        RealmList list = new RealmList();
        realm = Realm.getDefaultInstance();
        list.addAll(realm.where(Messages.class).findAll());
        for(int i =0; i < list.size(); i++)
        {
            System.out.println(list.get(i) );
        }
        System.out.println("11111111");
        return list;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}

            if(resultCode == RESULT_OK) {
                long position = data.getLongExtra("position", 999999);
                int cnt = adapter.updatePosition(position);
                Toast.makeText(this, "" + cnt, Toast.LENGTH_SHORT).show();
            } else {

            }
    }




    class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

//        if (oldVersion == 0) {
            final RealmObjectSchema userSchema = schema.get("Messages");

           userSchema.addField(Messages.MSG_ID, long.class);
           userSchema.addField("fbKey", String.class);
//        }
        newVersion = oldVersion++;
    }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
    public long idNextVal (Realm realm){
        Number maxId;
        maxId = (Number) (realm.where(Messages.class).max(Messages.MSG_ID));
        long nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        return  nextId;

    }




}
