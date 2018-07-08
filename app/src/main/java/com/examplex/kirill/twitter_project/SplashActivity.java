package com.examplex.kirill.twitter_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.examplex.kirill.twitter_project.models.Messages;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class SplashActivity  extends AppCompatActivity {

    private String scope[] = new String[]{VKScope.FRIENDS};
    public RealmConfiguration config;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Realm.init(this);
        config = new RealmConfiguration.Builder()
                .schemaVersion(3)
                .migration(new RealmMigrations())
                .build();
        Realm.setDefaultConfiguration(config);
        VKSdk.login(this, scope);
        realm = Realm.getDefaultInstance();



        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name"));
                        request.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);

                                VKList vkList = (VKList) response.parsedModel;

                                String[] str = new String[vkList.getCount()];

                                Messages msg = new Messages();

                                for(int i = 0 ; i < vkList.getCount(); i++) {
                                    VKApiUserFull friends = ((VKList<VKApiUserFull>) response.parsedModel)
                                            .get(i);
                                    str[i] = friends.first_name;
                                    msg.setMsgId(idNextVal(realm));
                                    msg.setMsgText(str[i]);
                                    msg.setMsgDate(new Date());
                                    realm.beginTransaction();
                                    realm.insert(msg);
                                    realm.commitTransaction();
                                }

                            }
                        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 0);

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

        userSchema.addField(Messages.MSG_ID, long.class);
//        }
        newVersion = oldVersion++;
    }
}

