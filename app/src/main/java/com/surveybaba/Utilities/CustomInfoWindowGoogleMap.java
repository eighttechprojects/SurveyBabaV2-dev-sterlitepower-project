package com.surveybaba.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.surveybaba.ADAPTER.AdapterSurveyMarker;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.R;
import com.surveybaba.model.BinTimeline;

import java.util.ArrayList;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {

       // View view = ((Activity)context).getLayoutInflater().inflate(R.layout.survey_custom_marker_view, null);
        return null;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
//        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_info_window, null);
//        LinearLayout llCustomView = view.findViewById(R.id.llCustomView);
//        TextView txtTitleOnly = view.findViewById(R.id.txtTitleOnly);
//        TextView tvdatetime = view.findViewById(R.id.tvdatetime);
//        TextView txtDescription = view.findViewById(R.id.txtDescription);
//        TextView txtLatlong = view.findViewById(R.id.txtLatlong);
//        ImageView imgMap = view.findViewById(R.id.imgMap);
//
//        if(marker.getTag() instanceof BinTimeline)
//        {
//            BinTimeline binTimeline = (BinTimeline) marker.getTag();
//            if(binTimeline!=null) {
//                llCustomView.setVisibility(View.VISIBLE);
//                txtTitleOnly.setVisibility(View.GONE);
//                tvdatetime.setText("Date: " + binTimeline.getRecordDate());
//                txtDescription.setText("Description: " + binTimeline.getDescription());
//                txtLatlong.setText("Latlong: " + binTimeline.getLat() + "," + binTimeline.getLongi());
//                imgMap.setImageBitmap(ImageFileUtils.getBitmapFromFilePath(binTimeline.getImageFilePath()));
//                //        Picasso.with(context).load(binTimeline.getImageFilePath()).into(imgMap);
//            }
//        }
//        else if(marker.getTag() instanceof String)
//        {
//            String title = (String) marker.getTag();
//            if(!Utility.isEmptyString(title)) {
//                txtTitleOnly.setText(title);
//                llCustomView.setVisibility(View.GONE);
//                txtTitleOnly.setVisibility(View.VISIBLE);
//            }
//        }
//         if(marker.getTag() instanceof FormDetailData){
//
//                try{
//                    ArrayList<FormDetailData> formDetailData = (ArrayList<FormDetailData>) marker.getTag();
//                    assert formDetailData != null;
//                    // Dialog Box
//                    Dialog dialog = new Dialog(context);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.survey_custom_marker_view);
//                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    // Close Button
//                    Button surveyFormCloseButton = dialog.findViewById(R.id.surveyFormCloseButton);
//                    surveyFormCloseButton.setOnClickListener(v -> dialog.dismiss());
//                    // RecycleView
//                    RecyclerView surveyFormRecycleView = dialog.findViewById(R.id.surveyFormRecycleView);
//                    // Adapter
//                    AdapterSurveyMarker adapterSurveyMarker = new AdapterSurveyMarker(context,formDetailData);
//                    // Set Adapter
//                    surveyFormRecycleView.setAdapter(adapterSurveyMarker);
//                    // Set Layout
//                    surveyFormRecycleView.setLayoutManager(new LinearLayoutManager(context));
//                    // Dialog Box Show
//                    dialog.show();
//                }
//                catch (Exception e){
//                    Log.e("SurveyActivity", e.getMessage());
//                }
//
//        }
        return null;
    }

    /*@Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
.inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);

        TextView hotel_tv = view.findViewById(R.id.hotels);
        TextView food_tv = view.findViewById(R.id.food);
        TextView transport_tv = view.findViewById(R.id.transport);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);

        hotel_tv.setText(infoWindowData.getHotel());
        food_tv.setText(infoWindowData.getFood());
        transport_tv.setText(infoWindowData.getTransport());

        return view;
    }*/
}