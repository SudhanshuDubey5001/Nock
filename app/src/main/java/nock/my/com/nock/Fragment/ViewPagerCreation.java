package nock.my.com.nock.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nock.my.com.nock.ChatScreenView.ChoosingAdapter;
import nock.my.com.nock.R;
import nock.my.com.nock.activities.ChooseMate;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class ViewPagerCreation extends Fragment {

    private static final String PROFILE_ID = "profile_id";
    public static final int USERS_B = 0;
    public static final int USERS_G = 1;
    public static final int MESSAGES = 2;
    public static final int ROOM = 3;
    public int id;
    RecyclerView recyclerView;
    ChoosingAdapter adapter = new ChoosingAdapter();
    TabsArrayClass tab = new TabsArrayClass();
    SwipeRefreshLayout swipe;
    int currentID;

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

        Log.d("my", "Run ho gaya");

        switch (id) {
            case USERS_B:
                tab.refreshUsers_B_Array(adapter, id);
                break;
            case USERS_G:
                tab.refreshUsers_G_Array(adapter, id);
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