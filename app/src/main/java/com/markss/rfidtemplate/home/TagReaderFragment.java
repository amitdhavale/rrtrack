package com.markss.rfidtemplate.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.markss.rfidtemplate.R;
import com.markss.rfidtemplate.application.Application;
import com.markss.rfidtemplate.common.ResponseHandlerInterfaces;
import com.markss.rfidtemplate.inventory.InventoryListItem;
import com.markss.rfidtemplate.rfid.RFIDController;
import com.zebra.rfid.api3.RFIDResults;

public class TagReaderFragment extends Fragment implements View.OnClickListener, ResponseHandlerInterfaces.ResponseTagHandler, ResponseHandlerInterfaces.TriggerEventHandler, ResponseHandlerInterfaces.ResponseStatusHandler {

    EditText edttag;
    Button btnClear;
    TextView tvtags;

    public TagReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tag_reader, container, false);

        initView(rootView);

        return rootView;
    }

    public void initView(View rootView){
        edttag = rootView.findViewById(R.id.edtrfidtag);
        btnClear = rootView.findViewById(R.id.btnClear);
        tvtags = rootView.findViewById(R.id.tvScannedTags);

        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        tvtags.setText("Scanned Tags:");
        edttag.setText("");
    }

    @Override
    public void handleTagResponse(InventoryListItem inventoryListItem, boolean isAddedToList) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAddedToList) {
                        if (!Application.TAG_LIST_MATCH_MODE) {
                            edttag.setText("");
                            edttag.setText(inventoryListItem.getTagID());
                            tvtags.append("\n"+inventoryListItem.getTagID());
                        }
                    }
                }
            });
    }

    @Override
    public void handleStatusResponse(RFIDResults results) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (results.equals(RFIDResults.RFID_BATCHMODE_IN_PROGRESS)) {
//                    if (uniqueTags != null) {
//                        invtoryData.setVisibility(View.GONE);
//                        batchModeRR.setVisibility(View.VISIBLE);
//                    }
//                } else if (!results.equals(RFIDResults.RFID_API_SUCCESS)) {
//                    RFIDController.mIsInventoryRunning = false;
//                    if (inventoryButton != null){
//                        inventoryButton.setImageResource( android.R.drawable.ic_media_play );
//                    }
//                    RFIDController.isBatchModeInventoryRunning = false;
//                }
//            }
//        });
    }

    @Override
    public void triggerPressEventRecieved() {
        if (!RFIDController.mIsInventoryRunning && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.inventoryStartOrStop();
                    }
                }
            });
        }
    }

    @Override
    public void triggerReleaseEventRecieved() {
        if ((RFIDController.mIsInventoryRunning == true) && getActivity() != null) {
            //RFIDController.mInventoryStartPending = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.inventoryStartOrStop();
                    }
                }
            });
        }
    }
}