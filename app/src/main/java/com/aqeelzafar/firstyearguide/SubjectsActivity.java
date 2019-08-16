package com.aqeelzafar.firstyearguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aqeelzafar.firstyearguide.Adapters.SubjectsAdapter;
import com.aqeelzafar.firstyearguide.Models.TitleName;
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

public class SubjectsActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    List<TitleName> subjectList = new ArrayList<>();
    CardView loadingCard, textCard;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);


        loadAd();
        recyclerView = findViewById(R.id.subjects_recycler);
        loadingCard = findViewById(R.id.subjects_loading_card);
        textCard = findViewById(R.id.subjects_cardView);

        isLoading(true);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("data").document("3.11thHSSC-1").collection("subjects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("subject_name").toString();
                                String id = document.getId();
                                subjectList.add(new TitleName(name, id));
                            }



                            setRecycler();
                            isLoading(false);
                        } else {
                            Toast.makeText(SubjectsActivity.this, "Failed to connect Internet", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void setRecycler(){
        recyclerView.setAdapter(new SubjectsAdapter(subjectList, SubjectsActivity.this));
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

        MobileAds.initialize(this, getString(R.string.App_id));
        adView = findViewById(R.id.subjects_adView);

        AdRequest request = new AdRequest.Builder().build();

        adView.loadAd(request);

    }
}
