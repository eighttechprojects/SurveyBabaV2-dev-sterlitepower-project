package com.surveybaba.ADAPTER;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.R;
import java.util.ArrayList;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    // Context
    Context context;
    // List
    ArrayList<BluetoothDevice> bluetoothDeviceList;
    // Interface
    BluetoothDeviceListener bluetoothDeviceListener;


//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------------------------------------------

    public BluetoothDeviceAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDeviceList, BluetoothDeviceListener bluetoothDeviceListener){
        this.context = context;
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.bluetoothDeviceListener = bluetoothDeviceListener;
    }

//------------------------------------------------------- on CreateViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public BluetoothDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bluetooth_device_adapter_layout, parent, false);
        return new ViewHolder(view);
    }


//-------------------------------------------------------  on BindViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceAdapter.ViewHolder holder, int position) {

        BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
        // Set Device Name
        holder.bluetoothDeviceName.setText(bluetoothDevice.getName());
        // Click on Device
        holder.itemView.setOnClickListener(view -> {
                bluetoothDeviceListener.onBluetoothDeviceClick(bluetoothDevice);

        });
    }

//------------------------------------------------------- ItemCount -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return bluetoothDeviceList.size();
    }

//------------------------------------------------------- ViewHolder -------------------------------------------------------------------------------------------------------------------------------------------------

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bluetoothDeviceName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bluetoothDeviceName = itemView.findViewById(R.id.bluetoothDeviceName);
        }
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface BluetoothDeviceListener{
        void onBluetoothDeviceClick(BluetoothDevice bluetoothDevice) ;
    }
}
