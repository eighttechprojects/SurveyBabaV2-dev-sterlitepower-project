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

public class AdapterProjectAssign  extends RecyclerView.Adapter<AdapterProjectAssign.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<ProjectModule> projectAssignList;
    // Interface
    AdapterProjectAssign.AdapterProjectAssignListener adapterProjectAssignListener;


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public AdapterProjectAssign(Context context, ArrayList<ProjectModule> projectAssignList, AdapterProjectAssign.AdapterProjectAssignListener adapterProjectAssignListener){
        this.context = context;
        this.projectAssignList = projectAssignList;
        this.adapterProjectAssignListener = adapterProjectAssignListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public AdapterProjectAssign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_project_adapter_layout, parent, false);
        return new AdapterProjectAssign.ViewHolder(view);
    }


//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull AdapterProjectAssign.ViewHolder holder, int position) {

        ProjectModule projectModule = projectAssignList.get(position);
        holder.projectAssignItemName.setText(projectModule.getName());

    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return projectAssignList.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView projectAssignItemImage;
        TextView projectAssignItemName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectAssignItemName = itemView.findViewById(R.id.projectAssignItemName);
            projectAssignItemImage = itemView.findViewById(R.id.projectAssignItemImage);
            // Item Click Listener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    adapterProjectAssignListener.OnItemClick(position);
                }
            });
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface AdapterProjectAssignListener{
        void OnItemClick(int position);
    }

}
