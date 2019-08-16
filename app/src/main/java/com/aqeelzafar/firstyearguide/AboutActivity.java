package com.aqeelzafar.firstyearguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.techhuntdevelopers.library.builder.AboutBuilder;
import com.techhuntdevelopers.library.views.AboutView;

import static java.security.AccessController.getContext;

public class AboutActivity extends AppCompatActivity {

    FrameLayout about;
    String packageName = "com.aqeelzafar.firstyearguide";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        about = findViewById(R.id.about);

        AboutBuilder builder = AboutBuilder.with(this)
                .setBackgroundColor(R.color.colorPrimaryDark)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.drawable.pic)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Ch Aqeel Zafar")
                .setSubTitle("Android Application Developer")
                .setLinksColumnsCount(4)
                .setBrief("Develop Innovatives")
                .addGooglePlayStoreLink(packageName)
                .addGitHubLink("ChAqeelZafar")
                .addFacebookLink("ch.aqeel.zafar")
                .addTwitterLink("Aqeelay")
                .addInstagramLink("chaudhary.here")
                .addGooglePlusLink("118446581222229710827")
                .addEmailLink("aqeelzafar195@gmail.com")
                .addWhatsappLink("Ch Aqeel Zafar", "+923180178076")
                .addFiveStarsAction(packageName)
                .addMoreFromMeAction("Ch Aqeel")
                .setVersionNameAsAppSubTitle()
                .addShareAction("Universal guide 1st Year All Subjects Guide App")
                .addUpdateAction(packageName)
                .setActionsColumnsCount(2)
                .addFeedbackAction("aqeelzafar195@gmail.com")
                .addRemoveAdsAction((Intent) null)
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();

        about.addView(view);
    }
}
