package com.surveybaba.ADAPTER;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.surveybaba.MapsActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectLayerModel;
import java.util.List;

// Its a Working Layer
public class LayersAdapter extends RecyclerView.Adapter<LayersAdapter.LayerHolder> {

    Activity activity;
    List<ProjectLayerModel> listLayer;
    onLayerSelected onLayerSelected;
    String BLUE_COLOR = Utility.COLOR_CODE.BLUE;
    String YELLOW_COLOR = Utility.COLOR_CODE.YELLOW;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public LayersAdapter(Activity activity, List<ProjectLayerModel> listLayer, onLayerSelected onLayerSelected) {
        this.activity = activity;
        this.listLayer = listLayer;
        this.onLayerSelected = onLayerSelected;
    }

//------------------------------------------------------- onCreate ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public LayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_layer, parent, false));
    }

//------------------------------------------------------- onBind ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull LayerHolder holder, int position) {
        ProjectLayerModel bin = listLayer.get(position);

        if(bin.getOnly_view().equalsIgnoreCase("f")) {

            holder.layerAdapterLayout.setVisibility(View.VISIBLE);
            holder.txtLayerName.setText(bin.getLayerName());
            int color;
            try {
                if (!Utility.isEmptyString(bin.getLayerLineColor()) && bin.getLayerLineColor().startsWith("#"))
                    color = Color.parseColor(bin.getLayerLineColor());
                else{
                    if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                        color = Color.parseColor(BLUE_COLOR);
                    }
                    else{
                        color = Color.parseColor(YELLOW_COLOR);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                    color = Color.parseColor(BLUE_COLOR);
                }
                else{
                    color = Color.parseColor(YELLOW_COLOR);
                }
            }

            switch (bin.getLayerType()) {
                case MapsActivity.LAYER_TYPE.Point:
                    try {
                        if(!Utility.isEmptyString(bin.getLayerIcon())){
                            Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(bin.getLayerIcon()), Integer.parseInt((bin.getLayerIconWidth()).equals("") ? "50" : bin.getLayerIconWidth()), Integer.parseInt(bin.getLayerIconHeight().equals("") ? "50" : bin.getLayerIconHeight()), true);
                            Glide.with(activity).load(bmp).error(R.drawable.bg_marker_circle_yellow).into(holder.imgLayer);
                        }
                        else{
                            holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("LayerAdapter", e.getMessage());
                        holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                    }
                    break;
                case MapsActivity.LAYER_TYPE.Line:
                    holder.imgLayer.setBackgroundResource(R.drawable.ic_polyline);
                    setTintBackground(holder.imgLayer, color);
                    break;
                case MapsActivity.LAYER_TYPE.Polygon:
                    holder.imgLayer.setBackgroundResource(R.drawable.ic_polygon);
                    setTintBackground(holder.imgLayer, color);
                    break;
            }

            holder.llParent.setTag(position);

            holder.llParent.setOnClickListener(v -> {

                int position1 = (int) v.getTag();
                if(!Utility.getBooleanSavedData(activity,Utility.INSIDE_ZONE)){
                    // when user not inside zone!
                    Utility.getUserInsideAreaAlertDialogBox(activity, DialogInterface::dismiss);
                }
                else{
                    // when user is inside the zone!
                    // when layer is not enable
                    if (!listLayer.get(position1).isEnable()) {
                        Utility.showDoubleBtnDialog(activity, "Alert!!", "This layer is disabled. Do you want to Enable ?", "Enable", "Cancel", (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    listLayer.get(position1).setEnable(true);
                                    listLayer.get(position1).setActive(true);

                                    holder.txtLayerName.setText(listLayer.get(position1).getLayerName());
                                    int color1;
                                    try {
                                        if (!Utility.isEmptyString(listLayer.get(position1).getLayerLineColor()) && listLayer.get(position1).getLayerLineColor().startsWith("#"))
                                            color1 = Color.parseColor(listLayer.get(position1).getLayerLineColor());
                                        else{
                                            if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                                                color1 = Color.parseColor(BLUE_COLOR);
                                            }
                                            else{
                                                color1 = Color.parseColor(YELLOW_COLOR);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                                            color1 = Color.parseColor(BLUE_COLOR);
                                        }
                                        else{
                                            color1 = Color.parseColor(YELLOW_COLOR);
                                        }
                                    }
                                    switch (bin.getLayerType()) {
                                        case MapsActivity.LAYER_TYPE.Point:
                                            try {
                                                if(!Utility.isEmptyString(bin.getLayerIcon())){
                                                    Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(listLayer.get(position1).getLayerIcon()), Integer.parseInt((listLayer.get(position1).getLayerIconWidth().equals("")) ? "50" : (listLayer.get(position1).getLayerIconWidth())), Integer.parseInt(listLayer.get(position1).getLayerIconHeight().equals("") ? "50" : listLayer.get(position1).getLayerIconHeight()), true);
                                                    Glide.with(activity).load(bmp).error(R.drawable.bg_marker_circle_yellow).into(holder.imgLayer);
                                                }
                                                else{
                                                    holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                                            }
                                            break;
                                        case MapsActivity.LAYER_TYPE.Line:
                                            holder.imgLayer.setBackgroundResource(R.drawable.ic_polyline);
                                            setTintBackground(holder.imgLayer, color1);
                                            break;
                                        case MapsActivity.LAYER_TYPE.Polygon:
                                            holder.imgLayer.setBackgroundResource(R.drawable.ic_polygon);
                                            setTintBackground(holder.imgLayer, color1);
                                            break;
                                    }
                                    onLayerSelected.getLayer(listLayer.get(position1), true);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        });
                    }
                    // if layer is enable
                    else {
                        listLayer.get(position1).setActive(true);
                        holder.txtLayerName.setText(listLayer.get(position1).getLayerName());
                        int color1;
                        try {
                            if (!Utility.isEmptyString(listLayer.get(position1).getLayerLineColor()) && listLayer.get(position1).getLayerLineColor().startsWith("#"))
                                color1 = Color.parseColor(listLayer.get(position1).getLayerLineColor());
                            else{
                                if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                                    color1 = Color.parseColor(BLUE_COLOR);
                                }
                                else{
                                    color1 = Color.parseColor(YELLOW_COLOR);
                                }
                            }
                            //color1 = Color.YELLOW;
                        } catch (Exception e) {
                            e.printStackTrace();
                            if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                                color1 = Color.parseColor(BLUE_COLOR);
                            }
                            else{
                                color1 = Color.parseColor(YELLOW_COLOR);
                            }
                        }
                        switch (bin.getLayerType()) {
                            case MapsActivity.LAYER_TYPE.Point:
                                try {
                                    if(!Utility.isEmptyString(bin.getLayerIcon())){
                                        Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(listLayer.get(position1).getLayerIcon()), Integer.parseInt((listLayer.get(position1).getLayerIconWidth().equals("")) ? "50" : (listLayer.get(position1).getLayerIconWidth())), Integer.parseInt(listLayer.get(position1).getLayerIconHeight().equals("") ? "50" : listLayer.get(position1).getLayerIconHeight()), true);
                                        Glide.with(activity).load(bmp).error(R.drawable.bg_marker_circle_yellow).into(holder.imgLayer);
                                    }
                                    else{
                                        holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                                }
                                break;
                            case MapsActivity.LAYER_TYPE.Line:
                                holder.imgLayer.setBackgroundResource(R.drawable.ic_polyline);
                                setTintBackground(holder.imgLayer, color1);
                                break;
                            case MapsActivity.LAYER_TYPE.Polygon:
                                holder.imgLayer.setBackgroundResource(R.drawable.ic_polygon);
                                setTintBackground(holder.imgLayer, color1);
                                break;
                        }

                        onLayerSelected.getLayer(listLayer.get(position1), false);
                    }
                }

            });
        }
        else{
            holder.layerAdapterLayout.setVisibility(View.GONE);
        }

    }

//------------------------------------------------------- Set Tint background -------------------------------------------------------------------------------------------------------------------------------------------------

    private void setTintBackground(ImageView imgLayer, int color) {
        Drawable buttonDrawable = imgLayer.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, color);
        imgLayer.setBackground(buttonDrawable);
    }

//------------------------------------------------------- Get Item Count -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return listLayer.size();
    }

//------------------------------------------------------- View Holder -------------------------------------------------------------------------------------------------------------------------------------------------

    static class LayerHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llParent;
        private final TextView txtLayerName;
        private final ImageView imgLayer;
        private final RelativeLayout layerAdapterLayout;

        LayerHolder(@NonNull View view) {
            super(view);
            txtLayerName = view.findViewById(R.id.txtLayerName1);
            imgLayer =  view.findViewById(R.id.imgLayer1);
            llParent =  view.findViewById(R.id.llParent1);
            layerAdapterLayout = view.findViewById(R.id.layerAdapterLayout1);
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface onLayerSelected {
        void getLayer(ProjectLayerModel projectLayerModel, boolean isRecentlyEnabled);
    }


    private Bitmap decodeBase64Image(String base64Image){
        // decode base64 string
        byte[] bytes= Base64.decode(base64Image,Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }



}