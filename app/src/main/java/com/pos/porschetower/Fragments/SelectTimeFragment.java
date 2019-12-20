package com.pos.porschetower.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pos.porschetower.HomeActivity;
import com.pos.porschetower.R;
import com.pos.porschetower.customview.PorscheTextView;
import com.pos.porschetower.datamodel.UserObject;
import com.pos.porschetower.network.APIClient;
import com.pos.porschetower.network.CustomCall;
import com.pos.porschetower.utils.UserUtils;
import com.pos.porschetower.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SelectTimeFragment extends Fragment {
    private View rootView;
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAYOFMONTH = "dayOfMonth";
    private static final String SCHEDULEDATA = "scheduleData";

    // TODO: Rename and change types of parameters
    private int myear, mmonth, mdayOfMonth;
    private String mScheduleData;


    public SelectTimeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SelectTimeFragment newInstance(int year, int month, int dayOfMonth, String scheduleData) {
        SelectTimeFragment fragment = new SelectTimeFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putInt(DAYOFMONTH, dayOfMonth);
        args.putString(SCHEDULEDATA, scheduleData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myear = getArguments().getInt(YEAR);
            mmonth = getArguments().getInt(MONTH);
            mdayOfMonth = getArguments().getInt(DAYOFMONTH);
            mScheduleData = getArguments().getString(SCHEDULEDATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_time, container, false);
        initializeControl();
        return rootView;
    }

    private void initializeControl() {
        final TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        PorscheTextView txt_date_selecttime = (PorscheTextView) rootView.findViewById(R.id.txt_date_selecttime);
        txt_date_selecttime.setText(String.valueOf(myear) + "-" + String.valueOf(mmonth) + "-" + String.valueOf(mdayOfMonth));
        Button btnSave = (Button) rootView.findViewById(R.id.btn_time_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR);
                int currentMinute = c.get(Calendar.MINUTE);
                int currentAMPM = c.get(Calendar.AM_PM);
                if (currentAMPM > 0)
                    currentHour = currentHour + 12;
                int hour = timePicker.getCurrentHour();
                boolean is24HourView = timePicker.is24HourView();

                int minute = timePicker.getCurrentMinute();

                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH) + 1;
                int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                if (myear > currentYear || (myear >= currentYear && mmonth > currentMonth) || (myear >= currentYear && mmonth >= currentMonth && mdayOfMonth >= currentDayOfMonth)) {

                }

                int intervalSinceNow = (minute - currentMinute) + (hour - currentHour) * 60 + (mdayOfMonth - currentDayOfMonth) * 24 * 60 +
                        (mmonth - currentMonth) * 30 * 24 * 60 + (myear - currentYear) * 365 * 30 * 24 * 60;

                if (intervalSinceNow < 5) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_cant_select_time), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mScheduleData.equals("showroom_booking")) {
                    MenuFragment fragment = new MenuFragment();
                    Bundle bd = new Bundle();
                    bd.putString("type", "1021"); // repeat schedule
                    String[] titles = getResources().getStringArray(R.array.repeat_schedule_titles);
                    bd.putStringArray("titles", titles);
                    bd.putString("menu_type", "SubMenu");
                    bd.putString("Datetime", myear + "-" + mmonth + "-" + mdayOfMonth + " " + hour + ":" + minute + ":" + "00");
                    fragment.setArguments(bd);
                    Utils.addFragmentToBackstack(fragment, (HomeActivity) getActivity(), false);
                } else if (mScheduleData.equals("pool_beach")) {
                    String location = getArguments().getString("Location");
                    Bundle bd = new Bundle();
                    bd.putString(SCHEDULEDATA, location);
                    bd.putString("Datetime", myear + "-" + mmonth + "-" + mdayOfMonth + " " + hour + ":" + minute + ":" + "00");
                    BeachRequestFragment beachRequestFragment = new BeachRequestFragment();
                    beachRequestFragment.setArguments(bd);
                    Utils.addFragmentToBackstack(beachRequestFragment, (HomeActivity) getActivity(), true);
                } else {
                    UserObject owner = UserUtils.getSession(getActivity());

                    String scheduleString = UserUtils.getScheduleData(getActivity());
//                    RequestParams params = new RequestParams();
                    Map<String, String> requestparam = new HashMap<>();
                    try {
                        JSONObject scheduleObject = new JSONObject(scheduleString);
                        requestparam.put("type", mScheduleData);
                        requestparam.put("index", scheduleObject.getString("index"));
                        requestparam.put("owner", owner.getIndex() + "");
                        requestparam.put("date_time", myear + "-" + mmonth + "-" + mdayOfMonth + " " + hour + ":" + minute + ":" + "00");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    APIClient.get().send_schedule_request(requestparam).enqueue(new CustomCall<ResponseBody>(SelectTimeFragment.this.getActivity()) {
                        @Override
                        public void handleResponse(Call<ResponseBody> call, Response<ResponseBody> responsebody) {

                            if (mScheduleData.equals("storage")) {
                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_car_scheduled_for_storage));
                            } else {
                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
                            }
                            UserUtils.storeSelectedCategory(getActivity(), "100");
                            HomeFragment fragment = new HomeFragment();
                            Utils.replaceFragmentToBackStack(fragment, (HomeActivity) getActivity(), false);

                        }
                    });

//                    AsyncHttpClient client = new AsyncHttpClient();
//                    String functName = "send_schedule_request";
//                    client.post(Utils.BASE_URL + functName, params, new PorscheTowerResponseHandler(getActivity()) {
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            super.onSuccess(statusCode, headers, response);
//                            if (mScheduleData.equals("storage"))
//                            {
//                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_car_scheduled_for_storage));
//                            }
//                            else
//                            {
//                                Utils.showAlert(getActivity(), getResources().getString(R.string.msg_request_sent));
//                            }
//                            UserUtils.storeSelectedCategory(getActivity(), "100");
//                            HomeFragment fragment = new HomeFragment();
//                            Utils.replaceFragmentToBackStack(fragment, (HomeActivity)getActivity(), false);
//                        }
//                    });
                }

            }
        });
        Button btnCancel = (Button) rootView.findViewById(R.id.btn_time_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
