package com.markss.rfidtemplate.locate_tag;


import static com.markss.rfidtemplate.application.Application.roomDatabaseBuilder;
import static com.markss.rfidtemplate.home.MainActivity.filter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.markss.rfidtemplate.R;
import com.markss.rfidtemplate.application.Application;
import com.markss.rfidtemplate.common.ResponseHandlerInterfaces;
import com.markss.rfidtemplate.common.asciitohex;
import com.markss.rfidtemplate.common.hextoascii;
import com.markss.rfidtemplate.home.MainActivity;
import com.markss.rfidtemplate.rfid.RFIDController;
import com.zebra.rfid.api3.RFIDResults;

import java.util.ArrayList;
import java.util.List;

import asset.trak.database.daoModel.BookAndAssetData;
import asset.trak.database.entity.CategoryMaster;
import asset.trak.database.entity.LocationMaster;
import asset.trak.modelsrrtrack.AssetMain;

/**
 * <p/>
 * Use the {@link com.markss.rfidtemplate.locate_tag.SingleTagLocateFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p/>
 * Fragment to handle locationing
 */
public class SingleTagLocateFragment extends Fragment implements ResponseHandlerInterfaces.TriggerEventHandler, ResponseHandlerInterfaces.ResponseStatusHandler,
        LocateOperationsFragment.OnRefreshListener {
    private RangeGraph locationBar;
    //private TextView distance;
    private Button btn_locate;
    private AutoCompleteTextView et_locateTag;
    private ArrayAdapter<String> adapter;
    View constLay;

    public SingleTagLocateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationingFragment.
     */
    public static com.markss.rfidtemplate.locate_tag.SingleTagLocateFragment newInstance() {
        return new com.markss.rfidtemplate.locate_tag.SingleTagLocateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_tag_locate, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        locationBar = (RangeGraph) getActivity().findViewById(R.id.locationBar);
        // distance=(TextView)getActivity().findViewById(R.id.distance);
        btn_locate = getActivity().findViewById(R.id.btn_locate);
        TextView tvTitle = getActivity().findViewById(R.id.btn_locate);
        TextView tvSearch = getActivity().findViewById(R.id.tvSearch);
        tvSearch.setVisibility(View.GONE);
        constLay= getActivity().findViewById(R.id.lt_book_info);

        et_locateTag = (AutoCompleteTextView) getActivity().findViewById(R.id.lt_et_epc);
        if (RFIDController.asciiMode == true)
            et_locateTag.setFilters(new InputFilter[]{filter});
        else
            et_locateTag.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        RFIDController.getInstance().updateTagIDs();
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Application.tagIDs);
        et_locateTag.setAdapter(adapter);
        locationBar.setValue(0);

        if (RFIDController.isLocatingTag) {
            if (et_locateTag != null && Application.locateTag != null) {
                if (RFIDController.asciiMode == true)
                    et_locateTag.setText(hextoascii.convert(RFIDController.currentLocatingTag));
                else
                    et_locateTag.setText(asciitohex.convert(RFIDController.currentLocatingTag));
            }
            et_locateTag.setFocusable(false);
            if (btn_locate != null) {
                // btn_locate.setImageResource(R.drawable.ic_play_stop);
            }
            showTagLocationingDetails();
        } else {
            if (et_locateTag != null && Application.locateTag != null) {
                if (RFIDController.asciiMode == true)
                    et_locateTag.setText(hextoascii.convert(Application.locateTag));
                else
                    et_locateTag.setText(asciitohex.convert(Application.locateTag));
            }
            if (btn_locate != null) {
                //btn_locate.setImageResource(android.R.drawable.ic_media_play);
            }
        }
        if (RFIDController.asciiMode == true) {
            SpannableStringBuilder print_tag = new SpannableStringBuilder(et_locateTag.getText());
            for (int i = 0; i < print_tag.length(); i++) {
                if (print_tag.charAt(i) == ' ') {
                    BackgroundColorSpan bcs = new BackgroundColorSpan(Color.YELLOW);
                    print_tag.setSpan(bcs, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            Log.d("serachList", print_tag.toString());
            et_locateTag.setText(print_tag);
        }


//        if(getArguments().containsKey("hide"))
        if(Application.comefrom.contains("hide"))
        {

            et_locateTag.setVisibility(View.VISIBLE);
            et_locateTag.setText(Application.locateTag);
            constLay.setVisibility(View.GONE);
        }
        else{
            getBookDetails();
        }


    }

    private void getBookDetails() {
        String RFIDTag = Application.locateTag;
        List<AssetMain> list = new ArrayList<>();
        List<LocationMaster> itemslocation = new ArrayList<>();
        TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
        TextView tvAuthor = getActivity().findViewById(R.id.tvAuthor);
        ImageView ivBook = getActivity().findViewById(R.id.ivBook);
        TextView tvTag = getActivity().findViewById(R.id.tvTag);
        TextView tvCategory = getActivity().findViewById(R.id.tvCategory);
        TextView tv = getActivity().findViewById(R.id.tv);
        if (roomDatabaseBuilder.getBookDao().getBookForRFID(RFIDTag) != null) {


            list.addAll(roomDatabaseBuilder.getBookDao().getBookForRFID(RFIDTag));
            if(list.isEmpty())
            {
                constLay.setVisibility(View.GONE);
            }
            else {
                constLay.setVisibility(View.VISIBLE);
                if (list.get(0).getSupplier() == null || TextUtils.isEmpty(list.get(0).getSupplier())) {
                    tvTitle.setText("-");
                } else {
                    tvTitle.setText(list.get(0).getSupplier());
                }

                if (list.get(0).getSampleType() == null || list.get(0).getSampleType() ==  null) {
                    tvAuthor.setText("-");
                } else {
                    tvAuthor.setText(list.get(0).getSampleType());
                }
                if (list.get(0).getSampleNature() != null) {
                    tvTag.setText(list.get(0).getSampleNature()+" | "+list.get(0).getSeason());
                }

                if (list.get(0).getSeason() != null) {
                    tvCategory.setText(list.get(0).getLocation()+" - "+list.get(0).getClass());
                }




                //          LocationMaster locationMaster = roomDatabaseBuilder.getBookDao().getLocationName(list.get(0).getAssetCatalogue().getLocationId());
                // CategoryMaster categoryMaster = roomDatabaseBuilder.getBookDao().getCatgeoryName(list.get(0).getAssetCatalogue().getCategoryId());

//            tvTag.setText(locationMaster.getLocationName());
                //     tvCategory.setText(categoryMaster.getCategoryName());

            }
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
        Application.locateTag = et_locateTag.getText().toString();
    }

    public void showTagLocationingDetails() {
        if (RFIDController.TagProximityPercent != -1) {
           /* if (distance != null)
                distance.setText(RFIDController.tagProximityPercent.Proximitypercent + "%");*/
            if (locationBar != null && RFIDController.TagProximityPercent != -1) {
                locationBar.setValue((short) RFIDController.TagProximityPercent);
                locationBar.invalidate();
                locationBar.requestLayout();
            }
        }
    }

    public void handleLocateTagResponse() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showTagLocationingDetails();
            }
        });
    }

    @Override
    public void triggerPressEventRecieved() {
        if (!RFIDController.isLocatingTag)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.locationingButtonClicked(btn_locate);
                    }
                }
            });
    }

    @Override
    public void triggerReleaseEventRecieved() {
        if (RFIDController.isLocatingTag) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.locationingButtonClicked(btn_locate);
                    }
                }
            });
        }
    }

    public void resetLocationingDetails(final boolean isDeviceDisconnected) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btn_locate != null) {
                    //btn_locate.setImageResource(android.R.drawable.ic_media_play);
                }
                if (isDeviceDisconnected && locationBar != null) {
                    locationBar.setValue(0);
                    locationBar.invalidate();
                    locationBar.requestLayout();
                }
                if (et_locateTag != null) {
                    et_locateTag.setFocusableInTouchMode(true);
                    et_locateTag.setFocusable(true);
                }
            }
        });
    }

    public void handleStatusResponse(final RFIDResults results) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!results.equals(RFIDResults.RFID_API_SUCCESS)) {
//                    String command = statusData.command.trim();
//                    if (command.equalsIgnoreCase("lt") || command.equalsIgnoreCase("locatetag"))
//                    {
                    RFIDController.isLocatingTag = false;
                    if (btn_locate != null) {
                        // btn_locate.setImageResource(android.R.drawable.ic_media_play);
                    }
                    if (et_locateTag != null) {
                        et_locateTag.setFocusableInTouchMode(true);
                        et_locateTag.setFocusable(true);
                    }
                }

            }
            // }
        });
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onRefresh() {
    }
}
