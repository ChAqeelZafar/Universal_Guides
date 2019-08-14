package com.aqeelzafar.universalguidesfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.slider.imagesliderlib.OImageSlider;


public class PicturesActivity extends AppCompatActivity  {

    String classId, subjectId, chapterId;
    FirebaseFirestore firestore;
    ArrayList<String> listUrl;


    CardView loadingCard;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        loadingCard = findViewById(R.id.pictures_loading_card);
        isLoading(true);
        //loadAd();



        listUrl = new ArrayList<>();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        classId = prefs.getString("classId", "defaultStringIfNothingFound");

        subjectId = prefs.getString("subjectId", "defaultStringIfNothingFound");

        chapterId = prefs.getString("chapterId", "defaultStringIfNothingFound");

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("data").document(classId).collection("subjects").document(subjectId).collection("chapters").document(chapterId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    listUrl = (ArrayList<String>) document.get("links");
                    loadImages();

                }else{
                    Toast.makeText(PicturesActivity.this, "Failed to connect Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }




    public void loadImages(){
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<listUrl.size(); i++){
            String sTemp = listUrl.get(i);
            sTemp = sTemp.replace("/open?", "/uc?");

            temp.add(sTemp);
            //temp.add("https://drive.google.com/uc?id="+listUrl.get(i));
        }
        OImageSlider oImageSlider = new OImageSlider();
        oImageSlider.setColorActive(Color.WHITE) //bottom indicator active color
                .setColorInactive(Color.GRAY)// bottom indicator inactive color
                .setPlaceholder(R.drawable.loading)
                .setImageUrls(temp) // arraylist of image urls
                .setCurrentImage(0) // the selected image ( starting from 0 )
                .setBackgroundColor(Color.BLACK) // background color of activity
                .start(this);
        finish();
//        for (int i = 0; i < listUrl.size(); i++) {
//            TextSliderView sliderView = new TextSliderView(this);
//            sliderView
//                    .image(listUrl.get(i))
//                    .setBackgroundColor(Color.BLACK)
//                    .setProgressBarVisible(true);
//            mDemoSlider.addSlider(sliderView);
//        }
//
//
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        isLoading(false);
    }



    void isLoading(boolean isShow){
        if(isShow){
            loadingCard.setVisibility(View.VISIBLE);
        }
        else{
            loadingCard.setVisibility(View.GONE);
        }
    }

    void loadAd(){

//        MobileAds.initialize(this, getString(R.string.test_App_id));
//        adView = findViewById(R.id.pictures_adView);
//
//        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//
//        adView.loadAd(request);

    }
}
