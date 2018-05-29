package zip5001.my.com.zip.ChatScreenView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import zip5001.my.com.zip.R;
import zip5001.my.com.zip.activities.ChooseMate;

public class ChoosingAdapter extends RecyclerView.Adapter<ChoosingViewholder>{

    public ArrayList<String> listNames = new ArrayList<>();
    int id;

    public void setArray(ArrayList<String> arr,int id){
        listNames = arr;
        this.id=id;
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
}
