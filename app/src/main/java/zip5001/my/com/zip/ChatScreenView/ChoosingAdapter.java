package zip5001.my.com.zip.ChatScreenView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import zip5001.my.com.zip.DatabaseOperations;
import zip5001.my.com.zip.R;
import zip5001.my.com.zip.activities.ChooseMate;

public class ChoosingAdapter extends RecyclerView.Adapter<ChoosingViewholder>{

    public ArrayList<String> listNames = new ArrayList<>();
    private int id;
    public static ChoosingAdapter context;

    public static int knowId(){
        return ChoosingAdapter.context.id;
    }

    public void setArray(ArrayList<String> arr,int id){
        listNames = arr;
        this.id=id;
        ChoosingAdapter.context=this;
    }

    @Override
    public ChoosingViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_recycle_choose, parent, false);
        return new ChoosingViewholder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(ChoosingViewholder holder, int position) {
        holder.userIDs.setText(listNames.get(position));
        holder.position=position;
        holder.id=id;
    }

    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public void cancel(View view){
        if(view==null){
            ChoosingViewholder.LongClickheld=false;
            for(View v:ChoosingViewholder.viewArray){
                v.setBackgroundColor(Color.WHITE);
            }

            DatabaseOperations.deleteUsers.clear();
            DatabaseOperations.deleteNew.clear();
            DatabaseOperations.deleteRoom.clear();
        }else {
            for(View v:ChoosingViewholder.viewArray){
                if(v==view) {
                    Log.d("my","Arey ab to yahan par bhi aa gaye!!");
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        }
    }
}
