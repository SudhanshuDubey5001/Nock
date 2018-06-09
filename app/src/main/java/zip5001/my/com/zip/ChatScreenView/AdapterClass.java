package zip5001.my.com.zip.ChatScreenView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import zip5001.my.com.zip.R;
import zip5001.my.com.zip.activities.LoginActivity;
import zip5001.my.com.zip.activities.MainActivity;


public class AdapterClass extends RecyclerView.Adapter<ViewholderClassForUser> {

    public ArrayList<String> msg = new ArrayList<>();
    public ArrayList<String> key = new ArrayList<>();
    private MainActivity ob;
    private int times = 1;

    public AdapterClass(MainActivity ob) {
        this.ob = ob;
    }

    //  adjusting the array for old messages----->
    public void adjustMsgArray(ArrayList<String> keyArray,ArrayList<String> msgArray) {
        ArrayList<String> tempMsg=new ArrayList<>(msg);
        ArrayList<String> tempKey=new ArrayList<>(key);
        msg.clear();
        key.clear();
        Log.d("my","Msg Size: "+tempMsg.size());
        Log.d("my","MsgArray Size: "+msgArray.size());
        for(int i=0;i<msgArray.size()+tempMsg.size();i++){
            if(i>=msgArray.size()){
                msg.add(tempMsg.get(i-msgArray.size()));
                key.add(tempKey.get(i-msgArray.size()));
            }else {
                msg.add(msgArray.get(i));
                key.add(keyArray.get(i));
            }
        }
    }

    public void passMsgUSER(String key, String msg) {
        Log.d("no", msg);
        this.msg.add(msg);
        this.key.add(key);
        if (key.contains(MainActivity.friendUserName)) {
            MainActivity.mpRecieve.start();
        }
    }

    @Override
    public ViewholderClassForUser onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_recycle, parent, false);
        return new ViewholderClassForUser(view);
    }

    @Override
    public void onBindViewHolder(ViewholderClassForUser holder, int position) {

        int index = key.get(position).indexOf("Time");
        String time = key.get(position).substring(index + 4, index + 12);
        if (key.get(position).contains(LoginActivity.UserName)) {
            holder.text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.text.setText(msg.get(position));
            holder.date.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.date.setText(time);
        } else {
            holder.text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            if (msg.get(position).contains("new:")) {
                int i = msg.get(position).indexOf("new:");
                String newToOldMsg = msg.get(position).substring(i + 4);
                holder.text.setText(newToOldMsg);
            } else {
                holder.text.setText(msg.get(position));
            }
            holder.date.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.date.setText(time);
        }

//      to load more messages---->
        if (position == 1) {
            if (times > 1) {    //to skip first time
                ob.loadMore(times);
            }
            times++;
        }
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }
}
