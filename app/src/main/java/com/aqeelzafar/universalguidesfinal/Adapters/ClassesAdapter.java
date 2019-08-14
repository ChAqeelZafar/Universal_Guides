package com.aqeelzafar.universalguidesfinal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aqeelzafar.universalguidesfinal.Models.TitleName;
import com.aqeelzafar.universalguidesfinal.R;
import com.aqeelzafar.universalguidesfinal.SubjectsActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.Holder> {

    List<TitleName> classesList;
    Context ctx ;
    InterstitialAd interstitialAd;

    public ClassesAdapter(List<TitleName> classesList, Context ctx) {
        this.classesList = classesList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MobileAds.initialize(ctx,
                ctx.getString(R.string.test_App_id));

        interstitialAd = new InterstitialAd(ctx);
        interstitialAd.setAdUnitId(ctx.getString(R.string.test_ad_interstial_id));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        interstitialAd.loadAd(adRequest);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewholder, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final TitleName className = classesList.get(position);
        holder.classText.setText(className.getName());
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("classId", className.getId()).apply();
                if(haveNetworkConnection()) {


                    if(interstitialAd.isLoaded()) {
                        // Step 1: Display the interstitial
                        interstitialAd.show();
                        // Step 2: Attach an AdListener
                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // Step 2.1: Load another ad
                                AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                                        .build();
                                interstitialAd.loadAd(adRequest);

                                // Step 2.2: Start the new activity
                                Intent intent = new Intent(ctx, SubjectsActivity.class);
                                //intent.putExtra("classId", className.getId());
                                ctx.startActivity(intent);
                            }
                        });
                    }
// If it has not loaded due to any reason simply load the next activity
                    else {
                        Intent intent = new Intent(ctx, SubjectsActivity.class);
                        //intent.putExtra("classId", className.getId());
                        ctx.startActivity(intent);
                    }



                }
                else{
                    Toast.makeText(ctx, "Internet is Unavailble\nConnect to the Internet", Toast.LENGTH_SHORT).show();
                    //ctx.startActivity(new Intent(ctx, SubjectsActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView parentCard;
        TextView classText;
        public Holder(@NonNull View itemView) {
            super(itemView);
            parentCard = itemView.findViewById(R.id.classesviewholder_cardview);
            classText = itemView.findViewById(R.id.classesviewholder_text);
        }
    }



    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
