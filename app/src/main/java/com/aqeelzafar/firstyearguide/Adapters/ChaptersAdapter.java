package com.aqeelzafar.firstyearguide.Adapters;

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

import com.aqeelzafar.firstyearguide.Models.TitleName;
import com.aqeelzafar.firstyearguide.PicturesActivity;
import com.aqeelzafar.firstyearguide.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.Holder> {


    List<TitleName> chapterList;
    Context ctx ;
    InterstitialAd interstitialAd;

    public ChaptersAdapter(List<TitleName> chapterList, Context ctx) {
        this.chapterList = chapterList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MobileAds.initialize(ctx,
                ctx.getString(R.string.App_id));

        interstitialAd = new InterstitialAd(ctx);
        interstitialAd.setAdUnitId(ctx.getString(R.string.chapteer_interstial_id));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewholder, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final TitleName chapterName = chapterList.get(position);
        holder.classText.setText(chapterName.getName());
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("chapterId", chapterName.getId()).apply();

                if(haveNetworkConnection()) {


                    if(interstitialAd.isLoaded()) {
                        // Step 1: Display the interstitial
                        interstitialAd.show();
                        // Step 2: Attach an AdListener
                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // Step 2.1: Load another ad
                                AdRequest adRequest = new AdRequest.Builder()
                                        .build();
                                interstitialAd.loadAd(adRequest);

                                // Step 2.2: Start the new activity
                                Intent intent = new Intent(ctx, PicturesActivity.class);
                                ctx.startActivity(intent);
                            }
                        });
                    }
// If it has not loaded due to any reason simply load the next activity
                    else {
                        Intent intent = new Intent(ctx, PicturesActivity.class);
                        ctx.startActivity(intent);
                    }



                }
                else{
                    Toast.makeText(ctx, "Internet is Unavailble\nConnect to the Internet", Toast.LENGTH_LONG).show();
                    //ctx.startActivity(new Intent(ctx, SubjectsActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
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
