package com.examplex.kirill.twitter_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.examplex.kirill.twitter_project.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.vk.sdk.VKScope;
import java.util.List;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;


public class SplashActivity  extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListner;
    List<String> tasks;



    DatabaseReference mRef;
//    DatabaseReference mRef;

    private String scope[] = new String[]{VKScope.FRIENDS};
    public RealmConfiguration config;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        initAuth();


        Realm.init(this);
        config = new RealmConfiguration.Builder()
                .schemaVersion(4)
                .migration(new RealmMigrations())
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.delete(Messages.class);
        realm.commitTransaction();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, Authorize.class);
                startActivity(i);
                finish();
            }
        }, 0);

    }

    private void initAuth() {

        mAuth = FirebaseAuth.getInstance();
        mListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)
                {

                }else{

                }
            }
        };
    }

    public long idNextVal (Realm realm)

    {
        Number maxId;
        maxId = (Number) (realm.where(Messages.class).max(Messages.MSG_ID));
        long nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        return  nextId;

    }

}
class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

//        if (oldVersion == 0) {
        final RealmObjectSchema userSchema = schema.get("Messages");

        userSchema.addField("fbKey", String.class);
//        }
        newVersion = oldVersion++;
    }
}

