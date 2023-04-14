package com.surveybaba.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import com.surveybaba.model.ProjectModule;

import java.util.ArrayList;

public class AdapterProjectActivity extends RecyclerView.Adapter<AdapterProjectActivity.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<ProjectModule> projectActivityList;
    // Interface
    AdapterProjectActivityListener adapterProjectActivityListener;


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterProjectActivity(Context context, ArrayList<ProjectModule> projectActivityList, AdapterProjectActivityListener adapterProjectActivityListener){
        this.context = context;
        this.projectActivityList = projectActivityList;
        this.adapterProjectActivityListener = adapterProjectActivityListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterProjectActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_project_activity_adapter_layout, parent, false);
        return new AdapterProjectActivity.ViewHolder(view);
    }

//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterProjectActivity.ViewHolder holder, int position) {
        ProjectModule projectModule = projectActivityList.get(position);
        holder.projectActivityItemName.setText(projectModule.getName());
    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return projectActivityList.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView projectActivityItemImage;
        TextView projectActivityItemName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectActivityItemName = itemView.findViewById(R.id.projectActivityItemName);
            projectActivityItemImage = itemView.findViewById(R.id.projectActivityItemImage);
            // Item Click Listener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    adapterProjectActivityListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterProjectActivityListener{
        void OnItemClick(int position);
    }


}
