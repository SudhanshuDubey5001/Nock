//package nock.my.com.nock.ChatScreenView;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//
//import java.util.ArrayList;
//
//import nock.my.com.nock.R;
//import nock.my.com.nock.activities.LoginActivity;
//import nock.my.com.nock.activities.MainActivity;
//
//
//public class AdapterClass extends RecyclerView.Adapter<ViewholderClassForUser> {
//
//    public ArrayList<String> msg = new ArrayList<>();
//    public ArrayList<String> key = new ArrayList<>();
//    private MainActivity ob;
//    private int times = 1;
//
//    public AdapterClass(MainActivity ob) {
//        this.ob = ob;
//    }
//
//    //  adjusting the array for old messages----->
//    public void adjustMsgArray(ArrayList<String> keyArray, ArrayList<String> msgArray) {
//        ArrayList<String> tempMsg = new ArrayList<>(msg);
//        ArrayList<String> tempKey = new ArrayList<>(key);
//        msg.clear();
//        key.clear();
//        Log.d("my", "Msg Size: " + tempMsg.size());
//        Log.d("my", "MsgArray Size: " + msgArray.size());
//        for (int i = 0; i < msgArray.size() + tempMsg.size(); i++) {
//            if (i >= msgArray.size()) {
//                msg.add(tempMsg.get(i - msgArray.size()));
//                key.add(tempKey.get(i - msgArray.size()));
//            } else {
//                msg.add(msgArray.get(i));
//                key.add(keyArray.get(i));
//            }
//        }
//        Log.d("my", "New size: " + msg.size());
//    }
//
//    public void passMsgUSER(String key, String msg) {
//        Log.d("no", msg);
//        this.msg.add(msg);
//        this.key.add(key);
//        if (key.contains(MainActivity.friendUserName)) {
//            MainActivity.mpRecieve.start();
//        }
//    }
//
//    @Override
//    public ViewholderClassForUser onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_recycle, parent, false);
//        return new ViewholderClassForUser(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewholderClassForUser holder, int position) {
//        Log.d("my", "Position>>>>>>>>>" + position);
//        RelativeLayout.LayoutParams paramstext = (RelativeLayout.LayoutParams) holder.text.getLayoutParams();
//        RelativeLayout.LayoutParams paramstextl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams paramsdate = (RelativeLayout.LayoutParams) holder.date.getLayoutParams();
//        RelativeLayout.LayoutParams paramsdatel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        int pos = holder.getAdapterPosition();
//        int index = key.get(pos).indexOf("Time");
//        String time = key.get(pos).substring(index + 4, index + 12);
//        if (key.get(pos).contains(LoginActivity.UserName)) {
//            holder.text.setBackgroundResource(R.drawable.chat_bubble_sender);
//
////            holder.text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            paramstext.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            paramsdate.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            paramstext.addRule(RelativeLayout.TEXT_ALIGNMENT_VIEW_END);
//            paramsdate.addRule(RelativeLayout.TEXT_ALIGNMENT_VIEW_END);
//
//            holder.text.setLayoutParams(paramstextl);
//            holder.text.setLayoutParams(paramstext);
//            holder.date.setLayoutParams(paramsdatel);
//            holder.date.setLayoutParams(paramsdate);
//
//            holder.text.setText(msg.get(pos));
////            holder.date.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            holder.date.setText(time);
//        } else {
//            holder.text.setBackgroundResource(R.drawable.chat_bubble_reciever);
////            holder.text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//
//            paramstext.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            paramsdate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            holder.text.setLayoutParams(paramstext);
//            holder.date.setLayoutParams(paramsdate);
//
//            if (msg.get(pos).contains("new:")) {
//                int i = msg.get(pos).indexOf("new:");
//                String newToOldMsg = msg.get(pos).substring(i + 4);
//                holder.text.setText(newToOldMsg);
//            } else {
//                holder.text.setText(msg.get(pos));
//            }
////            holder.date.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            holder.date.setText(time);
//        }
//
////      to load more messages---->
//        if (pos == 1) {
//            if (times > 0) {    //to skip first time
//                ob.loadMore(times);
//            }
//            times++;
//        }
//
//        Log.d("my", "Message size: " + msg.size());
//    }
//
//    @Override
//    public int getItemCount() {
//        return msg.size();
//    }
//}


package nock.my.com.nock.ChatScreenView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nock.my.com.nock.R;
import nock.my.com.nock.activities.LoginActivity;
import nock.my.com.nock.activities.MainActivity;


public class AdapterClass extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<String> msg = new ArrayList<>();
    public ArrayList<String> key = new ArrayList<>();
    private MainActivity ob;
    private int times = 1;
    private static final int SENDER = 0;
    private static final int RECIEVER = 1;

    public AdapterClass(MainActivity ob) {
        this.ob = ob;
    }

    //  adjusting the array for old messages----->
    public void adjustMsgArray(ArrayList<String> keyArray, ArrayList<String> msgArray) {
        ArrayList<String> tempMsg = new ArrayList<>(msg);
        ArrayList<String> tempKey = new ArrayList<>(key);
        msg.clear();
        key.clear();
        Log.d("my", "Msg Size: " + tempMsg.size());
        Log.d("my", "MsgArray Size: " + msgArray.size());
        for (int i = 0; i < msgArray.size() + tempMsg.size(); i++) {
            if (i >= msgArray.size()) {
                msg.add(tempMsg.get(i - msgArray.size()));
                key.add(tempKey.get(i - msgArray.size()));
            } else {
                msg.add(msgArray.get(i));
                key.add(keyArray.get(i));
            }
        }
        Log.d("my", "New size: " + msg.size());
    }

    //  and for setting the message to the screen---------->
    public void passMsgUSER(String key, String msg) {
        Log.d("no", msg);
        this.msg.add(msg);
        this.key.add(key);
        if (key.contains(MainActivity.friendUserName)) {
            MainActivity.mpRecieve.start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (key.get(position).contains(LoginActivity.UserName)) return SENDER;
        else return RECIEVER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=null;
        switch (viewType) {
            case SENDER:
                view = inflater.inflate(R.layout.activity_recycle, parent, false);
                return new ViewholderClassForSender(view);
            case RECIEVER:
                view = inflater.inflate(R.layout.activity_recycle_reciever, parent, false);
                return new ViewholderClassForReceiver(view);
        }
        return new ViewholderClassForReceiver(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("my", "Position>>>>>>>>>" + position);

        int pos = holder.getAdapterPosition();
        int index = key.get(pos).indexOf("Time");
        String time = key.get(pos).substring(index + 4, index + 12);
        switch (holder.getItemViewType()) {
            case SENDER:
                ViewholderClassForSender holderS = (ViewholderClassForSender) holder;
                holderS.text.setText(msg.get(pos));
                holderS.date.setText(time);
                break;
            case RECIEVER:
                ViewholderClassForReceiver holderR = (ViewholderClassForReceiver) holder;
                if (msg.get(pos).contains("new:")) {
                    int i = msg.get(pos).indexOf("new:");
                    String newToOldMsg = msg.get(pos).substring(i + 4);
                    holderR.textR.setText(newToOldMsg);
                } else {
                    holderR.textR.setText(msg.get(pos));
                }
                holderR.dateR.setText(time);
                break;
        }

////      to load more messages---->
//        if (pos == 1) {
//            if (times > 0) {    //to skip first time
//                ob.loadMore(times);
//            }
//            times++;
//        }

        Log.d("my", "Message size: " + msg.size());
    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    class ViewholderClassForSender extends RecyclerView.ViewHolder {

        TextView text;
        TextView date;

        public ViewholderClassForSender(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.msgtext);
            date = itemView.findViewById(R.id.date);
        }
    }

    class ViewholderClassForReceiver extends RecyclerView.ViewHolder {

        TextView textR;
        TextView dateR;

        public ViewholderClassForReceiver(View itemView) {
            super(itemView);
            textR = itemView.findViewById(R.id.msgReceiver);
            dateR = itemView.findViewById(R.id.dateReceiver);
        }
    }
}
