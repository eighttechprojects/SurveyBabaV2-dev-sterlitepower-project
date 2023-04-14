package com.surveybaba.ADAPTER;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surveybaba.ADAPTER.Layer.AdapterAssetLayer;
import com.surveybaba.ADAPTER.Layer.AdapterBaseOnlineLayer;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.LayerModel;
import com.surveybaba.model.ProjectLayerModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 21/12/2017.
 */

public class AdapterLayerList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<LayerModel> list;
    onItemClick onItemClick;

//------------------------------------------------------------ Constructor ---------------------------------------------------------------------------------------------------------------------

    public AdapterLayerList(Activity context, List<LayerModel> list, onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;
    }

//------------------------------------------------------- Get item View Type ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getViewType().equalsIgnoreCase(Utility.VIEW_TYPE_BASE_ONLINE_LAYER_STR)){ return Utility.VIEW_TYPE_BASE_ONLINE_LAYER;}
        if (list.get(position).getViewType().equalsIgnoreCase(Utility.VIEW_TYPE_ASSET_LAYER_STR)){ return Utility.VIEW_TYPE_ASSET_LAYER;}
        return 0;
    }

//------------------------------------------------------------ On Create View Holder ---------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){

            case Utility.VIEW_TYPE_BASE_ONLINE_LAYER:
                return new BaseOnlineLayerViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_row_base_online_layer_view, parent, false));

            case Utility.VIEW_TYPE_ASSET_LAYER:
                return new AssetLayerViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_row_asset_layer_view, parent, false));

            default:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_row_layer_list, parent, false));

        }
    }

//------------------------------------------------------------ On Bind View Holder ---------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LayerModel bin = list.get(position);

        switch (getItemViewType(position)){

            case Utility.VIEW_TYPE_BASE_ONLINE_LAYER:
                if(holder instanceof BaseOnlineLayerViewHolder) {
                    BaseOnlineLayerViewHolder baseOnlineLayerViewHolder = (BaseOnlineLayerViewHolder) holder;
                    ArrayList<ProjectLayerModel> baseOnlineLayerList = new ArrayList<>();
                        for(int i=0; i<bin.getList().size(); i++){
                            if(bin.getList().get(i).getOnly_view().equalsIgnoreCase("f")){}
                            else{
                                baseOnlineLayerList.add(bin.getList().get(i));
                            }
                        }
                        if(baseOnlineLayerList.size() > 0){
                            baseOnlineLayerViewHolder.txtLabel.setVisibility(View.VISIBLE);
                            baseOnlineLayerViewHolder.viewLine.setVisibility(View.VISIBLE);
                            baseOnlineLayerViewHolder.rvBaseOnlineLayer.setVisibility(View.VISIBLE);

                            AdapterBaseOnlineLayer adapterBaseOnlineLayer = new AdapterBaseOnlineLayer(context, baseOnlineLayerList, () -> onItemClick.onDataChanged());
                            Utility.setToVerticalRecycleView(context,baseOnlineLayerViewHolder.rvBaseOnlineLayer,adapterBaseOnlineLayer);
                        }
                        else{
                            baseOnlineLayerViewHolder.txtLabel.setVisibility(View.GONE);
                            baseOnlineLayerViewHolder.viewLine.setVisibility(View.GONE);
                            baseOnlineLayerViewHolder.rvBaseOnlineLayer.setVisibility(View.GONE);
                        }
                 }
                break;

            case Utility.VIEW_TYPE_ASSET_LAYER:
                if(holder instanceof AssetLayerViewHolder){
                    AssetLayerViewHolder assetLayerViewHolder = (AssetLayerViewHolder) holder;
                    ArrayList<ProjectLayerModel> assetLayerList = new ArrayList<>();
                        for(int i=0; i<bin.getList().size(); i++){
                            if(bin.getList().get(i).getOnly_view().equalsIgnoreCase("f"))
                            {
                                assetLayerList.add(bin.getList().get(i));
                            }
                        }
                        if(assetLayerList.size() > 0){
                            assetLayerViewHolder.txtLabel.setVisibility(View.VISIBLE);
                            assetLayerViewHolder.viewLine.setVisibility(View.VISIBLE);
                            assetLayerViewHolder.rvAssetLayer.setVisibility(View.VISIBLE);
                            AdapterAssetLayer adapterAssetLayer = new AdapterAssetLayer(context, assetLayerList, () -> onItemClick.onDataChanged());
                            Utility.setToVerticalRecycleView(context,assetLayerViewHolder.rvAssetLayer,adapterAssetLayer);
                        }
                        else{
                            assetLayerViewHolder.txtLabel.setVisibility(View.GONE);
                            assetLayerViewHolder.viewLine.setVisibility(View.GONE);
                            assetLayerViewHolder.rvAssetLayer.setVisibility(View.GONE);
                        }
                }
                break;
        }

    }

//------------------------------------------------------------ Get Item Count ---------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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

    static class AssetLayerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rvAssetLayer;
        LinearLayout ll_Arrow;
        ImageView arrowImageView;
        TextView txtLabel;

        View viewLine;

        AssetLayerViewHolder(View itemView) {
            super(itemView);
            rvAssetLayer   = itemView.findViewById(R.id.rvAssetLayer);
            ll_Arrow       = itemView.findViewById(R.id.ll_Arrow);
            arrowImageView = itemView.findViewById(R.id.arrowImageView);
            txtLabel       = itemView.findViewById(R.id.txtLabel);
            viewLine       = itemView.findViewById(R.id.viewLine);
        }

    }

    static class BaseOnlineLayerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvBaseOnlineLayer;
        LinearLayout ll_Arrow;
        ImageView arrowImageView;
        TextView txtLabel;

        View viewLine;

        BaseOnlineLayerViewHolder(View itemView) {
            super(itemView);
            rvBaseOnlineLayer = itemView.findViewById(R.id.rvBaseOnlineLayer);
            ll_Arrow          = itemView.findViewById(R.id.ll_Arrow);
            arrowImageView    = itemView.findViewById(R.id.arrowImageView);
            txtLabel          = itemView.findViewById(R.id.txtLabel);
            viewLine       = itemView.findViewById(R.id.viewLine);
        }

    }


//------------------------------------------------------------ Interface ---------------------------------------------------------------------------------------------------------------------

    public interface onItemClick
    {
        void onDataChanged();
    }



}