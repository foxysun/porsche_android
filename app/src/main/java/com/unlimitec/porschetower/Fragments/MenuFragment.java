package com.unlimitec.porschetower.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.unlimitec.porschetower.HomeActivity;
import com.unlimitec.porschetower.R;
import com.unlimitec.porschetower.adapters.PorscheListAdapter;
import com.unlimitec.porschetower.utils.Utils;

public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;

    String[] mTitlesArray;


    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageView img_memuback = (ImageView) rootView.findViewById(R.id.img_memuback);
        String mMenuType = getArguments().getString("menu_type");
        if (mMenuType.equals("MainMenu"))
            img_memuback.setAlpha(0.85f);
        else if (mMenuType.equals("SubMenu"))
            img_memuback.setAlpha(1.0f);

        final ListView listView ;
        listView = (ListView) rootView.findViewById(R.id.menu_list_view);

        //  Get Titles Arguments from PickerFragment
        mTitlesArray = getArguments().getStringArray("titles");
        PorscheListAdapter adapter = new PorscheListAdapter(mTitlesArray);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert


//                ShowroomFragment showFragment = new ShowroomFragment();
//                Utils.addFragmentToBackstack(showFragment, (HomeActivity)getActivity(), true);
                String mType = getArguments().getString("type");
                int type = Integer.parseInt(mType);

                Fragment fragment;
                Bundle mTypeBundle;

                if (type  == 1)
                {
                    switch(position){
                        case 0:
                            Toast.makeText(getActivity().getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("elevator_request_type", "request");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 1:
                            fragment = new ShowroomFragment();
                            mTypeBundle = new Bundle();
                            mTypeBundle.putString("elevator_request_type", "schedule");
                            fragment.setArguments(mTypeBundle);
                            break;
                        case 2:
                            fragment = new ShowroomFragment();
                            break;
                        default:
                            fragment = new ShowroomFragment();
                            break;
                    }
                }
                else {
                    fragment = new MenuFragment();
                    String[] mTitlesString = {"abc", "def", "wer"};
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("titles", mTitlesString);
                    bundle.putString("menu_type", "SubMenu");
                    fragment.setArguments(bundle);
                }
                Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), true);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}