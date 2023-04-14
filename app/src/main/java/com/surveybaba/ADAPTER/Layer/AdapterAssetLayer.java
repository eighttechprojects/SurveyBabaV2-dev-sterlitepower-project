package com.surveybaba.ADAPTER.Layer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.surveybaba.ADAPTER.AdapterLayerList;
import com.surveybaba.MapsActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectLayerModel;

import java.util.List;

public class AdapterAssetLayer extends RecyclerView.Adapter<AdapterAssetLayer.ViewHolder> {

    Activity context;
    List<ProjectLayerModel> list;
    AdapterLayerList.onItemClick onItemClick;
    String BLUE_COLOR   = Utility.COLOR_CODE.BLUE;
    String YELLOW_COLOR = Utility.COLOR_CODE.YELLOW;


//------------------------------------------------------------ Constructor ---------------------------------------------------------------------------------------------------------------------

    public AdapterAssetLayer(Activity context, List<ProjectLayerModel> list, AdapterLayerList.onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;
    }

//------------------------------------------------------------ On Create View Holder ---------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_row_layer_list, parent, false);
        return new ViewHolder(itemView);
    }

//------------------------------------------------------------ On Bind View Holder ---------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProjectLayerModel bin = list.get(position);
        // Layer Name
        holder.txtLayerName.setText(bin.getLayerName());
        // Layer Mode Name
//        if(bin.getOnly_view().equalsIgnoreCase("f")){
            holder.txtLayerModeName.setText(Utility.ASSET_LAYER);
//        }
//        else{
//            holder.txtLayerModeName.setText(Utility.BASE_ONLINE_LAYER);
//        }
        // Layer Name Color
        holder.txtLayerName.setTextColor(bin.isEnable()? Color.WHITE:Color.BLACK);
        // Layer Mode Name Color
        holder.txtLayerModeName.setTextColor(bin.isEnable()? Color.WHITE:Color.GRAY);
        // Layer Enable
        holder.llParent.setSelected(bin.isEnable());
        //
        holder.imgCheck.setImageResource(bin.isEnable()?R.drawable.tick:R.drawable.untick);

        // Line Color ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

        }
        catch (Exception e) {
            e.printStackTrace();
            if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                color = Color.parseColor(BLUE_COLOR);
            }
            else{
                color = Color.parseColor(YELLOW_COLOR);
            }
        }

        // Layer Type ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        switch (bin.getLayerType()) {
            case MapsActivity.LAYER_TYPE.Point:
                try
                {
                    if(!Utility.isEmptyString(bin.getLayerIcon())){
                        Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(bin.getLayerIcon()),Integer.parseInt((bin.getLayerIconWidth()).equals("") ? "50" : bin.getLayerIconWidth()), Integer.parseInt(bin.getLayerIconHeight().equals("") ? "50": bin.getLayerIconHeight()), true);
                        Glide.with(context).load(bmp).error(R.drawable.bg_marker_circle_yellow).into(holder.imgLayer);
                    }
                    else{
                        holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
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


        //------------------------------------------------------------ setOnClickListener ---------------------------------------------------------------------------------------------------------------------

        holder.llParent.setOnClickListener(v -> {

            int pos = (int) v.getTag();
            list.get(pos).setEnable(!list.get(pos).isEnable());
            holder.txtLayerName.setTextColor(list.get(pos).isEnable()? Color.WHITE:Color.BLACK);
            holder.txtLayerModeName.setTextColor(list.get(pos).isEnable()? Color.WHITE:Color.GRAY);
            holder.llParent.setSelected(list.get(pos).isEnable());
            holder.imgCheck.setImageResource(list.get(pos).isEnable()?R.drawable.tick:R.drawable.untick);

            // Layer Color ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
            int color1 = 0;
            try {
                if (!Utility.isEmptyString(list.get(pos).getLayerLineColor()) && list.get(pos).getLayerLineColor().startsWith("#"))
                    color1 = Color.parseColor(list.get(pos).getLayerLineColor());
                else
                if(bin.getLayerType().equalsIgnoreCase(MapsActivity.LAYER_TYPE.Line)){
                    color1 = Color.parseColor(BLUE_COLOR);
                }
                else{
                    color1 = Color.parseColor(YELLOW_COLOR);
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

            // Layer Type ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
            switch (list.get(pos).getLayerType()) {
                case MapsActivity.LAYER_TYPE.Point:
                    try
                    {
                        if(!Utility.isEmptyString(bin.getLayerIcon())){
                            Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(list.get(pos).getLayerIcon()),Integer.parseInt((list.get(pos).getLayerIconWidth().equals("")) ? "50" : (list.get(pos).getLayerIconWidth())), Integer.parseInt(list.get(pos).getLayerIconHeight().equals("") ? "50" : list.get(pos).getLayerIconHeight()), true);
                            Glide.with(context).load(bmp).error(R.drawable.bg_marker_circle_yellow).into(holder.imgLayer);
                        }
                        else{
                            holder.imgLayer.setBackgroundResource(R.drawable.bg_marker_circle_yellow);
                        }
                    }
                    catch (Exception e)
                    {
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

            onItemClick.onDataChanged();

        });

    }

//------------------------------------------------------------ Get Item Count ---------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

//------------------------------------------------------------ Set Tint Background ---------------------------------------------------------------------------------------------------------------------

    private void setTintBackground(ImageView imgLayer, int color) {
        Drawable buttonDrawable = imgLayer.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, color);
        imgLayer.setBackground(buttonDrawable);
    }


//------------------------------------------------------------ View Holder ---------------------------------------------------------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLayerName;

        TextView txtLayerModeName;
        ImageView imgCheck;
        ImageView imgLayer;
        LinearLayout llParent;

        ViewHolder(View itemView) {
            super(itemView);
            txtLayerName = itemView.findViewById(R.id.txtLayerName);
            txtLayerModeName = itemView.findViewById(R.id.txtLayerModeName);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            imgLayer = itemView.findViewById(R.id.imgLayer);
            llParent = itemView.findViewById(R.id.llParent);
        }

    }

//------------------------------------------------------------ Interface ---------------------------------------------------------------------------------------------------------------------

    public interface onItemClick
    {
        void onDataChanged();
    }

//------------------------------------------------------------ decodeBase64Image ---------------------------------------------------------------------------------------------------------------------

    private Bitmap decodeBase64Image(String base64Image){
        // decode base64 string
        byte[] bytes= Base64.decode(base64Image,Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}



