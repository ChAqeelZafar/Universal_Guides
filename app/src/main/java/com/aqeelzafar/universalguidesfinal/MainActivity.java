package com.aqeelzafar.universalguidesfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aqeelzafar.universalguidesfinal.Adapters.ClassesAdapter;
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
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    List<TitleName> classesList = new ArrayList<>();
    ImageView imgLogo;

    CardView loadingCard;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAd();
        recyclerView = findViewById(R.id.classes_recycler);
        loadingCard = findViewById(R.id.classes_loading_card);
        imgLogo = findViewById(R.id.main_img_logo);

        isLoading(true);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("class").toString();
                                String id = document.getId();


                                classesList.add(new TitleName(name, id));
                            }



                            setRecycler();
                            isLoading(false);
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to connect Internet", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    void setRecycler(){
        recyclerView.setAdapter(new ClassesAdapter(classesList, MainActivity.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void isLoading(boolean isShow){
        if(isShow){
            imgLogo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            loadingCard.setVisibility(View.VISIBLE);
        }
        else{
            imgLogo.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            loadingCard.setVisibility(View.GONE);
        }
    }

    void loadAd(){

        MobileAds.initialize(this, getString(R.string.test_App_id));
        adView = findViewById(R.id.classes_adView);

        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        adView.loadAd(request);

    }
}
