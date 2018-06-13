package zip5001.my.com.zip.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import zip5001.my.com.zip.ChatScreenView.ChoosingAdapter;
import zip5001.my.com.zip.R;

public class ViewPagerCreation extends Fragment {

    private static final String PROFILE_ID = "profile_id";
    public static final int USERS = 0;
    public static final int MESSAGES = 1;
    public static final int ROOM = 2;
    public int id;
    RecyclerView recyclerView;
    ChoosingAdapter adapter = new ChoosingAdapter();
    TabsArrayClass tab = new TabsArrayClass();
    SwipeRefreshLayout swipe;

    public int getIdOfPager() {
        return id;
    }

    public static ViewPagerCreation createInstance(int id) {
        ViewPagerCreation creation = new ViewPagerCreation();
        Bundle b = new Bundle();
        b.putInt(PROFILE_ID, id);
        creation.setArguments(b);
        return creation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            id = b.getInt(PROFILE_ID);
        } else {
            throw new RuntimeException("(Pass the id man");
        }

//        swipe = this.getActivity().findViewById(R.id.swiperefresh);
//        tab.initializeHUD(this.getActivity());

//        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if(id==USERS){
//                    tab.refreshUsersArray(adapter,id);
//                }else if(id==MESSAGES){
//                    tab.refreshMessageArray(adapter,id);
//                }else {
//                    tab.refreshRoomArray(adapter,id);
//                }
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_choosemate_viewpager, container, false);
        recyclerView = view.findViewById(R.id.choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);
        Log.d("my", "meow meow");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (id) {
            case USERS:
                tab.refreshUsersArray(adapter, id);
                break;
            case MESSAGES:
                tab.refreshMessageArray(adapter, id);
                break;
            case ROOM:
                tab.refreshRoomArray(adapter, id);
                break;
        }
    }


}