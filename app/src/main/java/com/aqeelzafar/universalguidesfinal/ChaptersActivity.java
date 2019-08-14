package com.aqeelzafar.universalguidesfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.aqeelzafar.universalguidesfinal.Adapters.ChaptersAdapter;
import com.aqeelzafar.universalguidesfinal.Models.TitleName;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChaptersActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    List<TitleName> chapterList = new ArrayList<>();
    CardView loadingCard, textCard;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);


        loadAd();
        recyclerView = findViewById(R.id.chapters_recycler);
        loadingCard = findViewById(R.id.chapters_loading_card);
        textCard = findViewById(R.id.chapters_cardView);

        isLoading(true);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("data").document(PreferenceManager.getDefaultSharedPreferences(this).getString("classId", "defaultStringIfNothingFound")).collection("subjects").document(PreferenceManager.getDefaultSharedPreferences(this).getString("subjectId", "defaultStringIfNothingFound")).collection("chapters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("chapter_name").toString();
                                String id = document.getId();
                                chapterList.add(new TitleName(name, id));
                            }



                            setRecycler();
                            isLoading(false);
                        } else {
                            Toast.makeText(ChaptersActivity.this, "Failed to connect Internet", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    void setRecycler(){
        recyclerView.setAdapter(new ChaptersAdapter(chapterList, ChaptersActivity.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void isLoading(boolean isShow){
        if(isShow){
            textCard.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            loadingCard.setVisibility(View.VISIBLE);
        }
        else{
            textCard.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            loadingCard.setVisibility(View.GONE);
        }
    }

    void loadAd(){

        MobileAds.initialize(this, getString(R.string.test_App_id));
        adView = findViewById(R.id.chapters_adView);
        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        adView.loadAd(request);

    }
}