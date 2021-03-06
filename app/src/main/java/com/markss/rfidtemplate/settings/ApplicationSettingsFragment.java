package com.markss.rfidtemplate.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.markss.rfidtemplate.R;
import com.markss.rfidtemplate.application.Application;
import com.markss.rfidtemplate.common.Constants;
import com.markss.rfidtemplate.rfid.RFIDController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

public class ApplicationSettingsFragment extends Fragment {

//    public static Hashtable<String, Boolean> applicationSettings;
//
//    static {
//        applicationSettings = new Hashtable<>();
//        applicationSettings.put(Constants.AUTO_RECONNECT_READERS, false);
//        applicationSettings.put(Constants.NOTIFY_READER_AVAILABLE, false);
//        applicationSettings.put(Constants.NOTIFY_READER_CONNECTION, false);
//        applicationSettings.put(Constants.NOTIFY_BATTERY_STATUS, false);
//        applicationSettings.put(Constants.EXPORT_DATA, false);
//    }
    private static final int TAGLIST_MATCH_MODE_IMPORT = 0;
    private CheckBox autoReconnectReaders;
    private CheckBox readerAvailable;
    private CheckBox readerConnection;
    private CheckBox readerBattery;
    private CheckBox exportData;
    public CheckBox tagListMatchMode;
    private CheckBox tagListMatchTagNames;
    private CheckBox asciiMode;
    private SharedPreferences settings;
    File cacheMatchModeTagFile = null;

    public ApplicationSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConnectionSettingsFragment.
     */
    public static com.markss.rfidtemplate.settings.ApplicationSettingsFragment newInstance() {
        return new com.markss.rfidtemplate.settings.ApplicationSettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheMatchModeTagFile = new File(getActivity().getCacheDir().getAbsolutePath(), Application.CACHE_TAGLIST_MATCH_MODE_FILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_settings, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadCheckBoxStates();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
    }

    private void initializeViews() {
        autoReconnectReaders = ((CheckBox) getActivity().findViewById(R.id.autoReconnectReaders));
        readerAvailable = (CheckBox) getActivity().findViewById(R.id.readerAvailable);
        readerConnection = ((CheckBox) getActivity().findViewById(R.id.readerConnection));
        readerBattery = ((CheckBox) getActivity().findViewById(R.id.readerBattery));
        exportData = ((CheckBox) getActivity().findViewById(R.id.exportData));
        tagListMatchMode = ((CheckBox) getActivity().findViewById(R.id.tagListMatchMode));
        tagListMatchTagNames = ((CheckBox) getActivity().findViewById(R.id.tagListMatchTagNames));
        asciiMode = ((CheckBox) getActivity().findViewById(R.id.asciiMode));

        if (RFIDController.mIsInventoryRunning || RFIDController.isLocatingTag) {
            tagListMatchMode.setEnabled(false);
            tagListMatchTagNames.setEnabled(false);
        }
        loadCheckBoxStates();

        tagListMatchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/*");
                    startActivityForResult(Intent.createChooser(intent, "ChooseFile to upload"), TAGLIST_MATCH_MODE_IMPORT);

                } else {
                    // clear friendly names
//                    tagListMatchTagNames.setChecked(false);
                    tagListMatchTagNames.setEnabled(false);
                }
                RFIDController.getInstance().clearInventoryData();
            }
        });
        tagListMatchTagNames.setOnCheckedChangeListener((buttonView, isChecked) -> {

            //if(isChecked){
//                Toast.makeText(getActivity(), getString(R.string.REQUIRES_TAGLIST_CSV), Toast.LENGTH_SHORT).show();
            tagListMatchTagNames.setChecked(isChecked);
            //}
        });

        asciiMode.setOnCheckedChangeListener((buttonView, isChecked) -> RFIDController.asciiMode = isChecked);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        storeCheckBoxesStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (cacheMatchModeTagFile.exists()) {
            cacheMatchModeTagFile.delete();
        }
        if (resultCode == RESULT_OK && requestCode == TAGLIST_MATCH_MODE_IMPORT) {
            Uri uri = data.getData();
            if (data == null) {
                return;
            }
            try {
                InputStream in = getActivity().getContentResolver().openInputStream(uri);
                OutputStream out = new FileOutputStream(cacheMatchModeTagFile);
                Log.d("size", in.toString());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!cacheMatchModeTagFile.exists()) {
                Toast.makeText(getActivity(), getString(R.string.REQUIRES_TAGLIST_CSV), Toast.LENGTH_SHORT).show();
                tagListMatchMode.setChecked(false);
            } else {
                tagListMatchTagNames.setEnabled(true);
				Toast.makeText(getActivity(), R.string.status_success_message, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Method to load the checkbox states
     */
    private void loadCheckBoxStates() {
        settings = getActivity().getSharedPreferences(Constants.APP_SETTINGS_STATUS, 0);
        autoReconnectReaders.setChecked(settings.getBoolean(Constants.AUTO_RECONNECT_READERS, true));
        readerAvailable.setChecked(settings.getBoolean(Constants.NOTIFY_READER_AVAILABLE, false));
        readerConnection.setChecked(settings.getBoolean(Constants.NOTIFY_READER_CONNECTION, false));
        readerBattery.setChecked(settings.getBoolean(Constants.NOTIFY_BATTERY_STATUS, true));
//        if (Build.MODEL.contains("MC33"))
//            readerBattery.setChecked(settings.getBoolean(Constants.NOTIFY_BATTERY_STATUS, false));
//        else
//            readerBattery.setChecked(settings.getBoolean(Constants.NOTIFY_BATTERY_STATUS, true));
        exportData.setChecked(settings.getBoolean(Constants.EXPORT_DATA, false));
        tagListMatchMode.setChecked(settings.getBoolean(Constants.TAG_LIST_MATCH_MODE, false));

        if (tagListMatchMode.isChecked()) {
            if (!cacheMatchModeTagFile.exists()) {
                tagListMatchMode.setChecked(false);
            }
        }

        if (!tagListMatchMode.isChecked()) {
            tagListMatchTagNames.setEnabled(false);
        } else
            tagListMatchTagNames.setChecked(settings.getBoolean(Constants.SHOW_CSV_TAG_NAMES, false));

        RFIDController.asciiMode = settings.getBoolean(Constants.ASCII_MODE, false);
        asciiMode.setChecked(RFIDController.asciiMode);
    }

    /**
     * Method to store the checkbox states
     */
    private void storeCheckBoxesStatus() {

        boolean AUTO_RECONNECT_READERS = ((CheckBox) getActivity().findViewById(R.id.autoReconnectReaders)).isChecked();
        boolean NOTIFY_READER_AVAILABLE = ((CheckBox) getActivity().findViewById(R.id.readerAvailable)).isChecked();
        boolean NOTIFY_READER_CONNECTION = ((CheckBox) getActivity().findViewById(R.id.readerConnection)).isChecked();
        boolean NOTIFY_BATTERY_STATUS = ((CheckBox) getActivity().findViewById(R.id.readerBattery)).isChecked();
        boolean EXPORT_DATA = ((CheckBox) getActivity().findViewById(R.id.exportData)).isChecked();
        boolean TAG_LIST_MATCH_MODE = ((CheckBox) getActivity().findViewById(R.id.tagListMatchMode)).isChecked();
        boolean SHOW_CSV_TAG_NAMES = ((CheckBox) getActivity().findViewById(R.id.tagListMatchTagNames)).isChecked();
        boolean ASCCI_MODE = asciiMode.isChecked();

        SharedPreferences settings = getActivity().getSharedPreferences(Constants.APP_SETTINGS_STATUS, 0);
        SharedPreferences.Editor editor = settings.edit();

        boolean isChanged = false;

        if (settings.getBoolean(Constants.AUTO_RECONNECT_READERS, true) != AUTO_RECONNECT_READERS) {
            editor.putBoolean(Constants.AUTO_RECONNECT_READERS, AUTO_RECONNECT_READERS);
            isChanged = true;
        }

        if (settings.getBoolean(Constants.NOTIFY_READER_AVAILABLE, false) != NOTIFY_READER_AVAILABLE) {
            editor.putBoolean(Constants.NOTIFY_READER_AVAILABLE, NOTIFY_READER_AVAILABLE);
            isChanged = true;
        }

        if (settings.getBoolean(Constants.NOTIFY_READER_CONNECTION, false) != NOTIFY_READER_CONNECTION) {
            editor.putBoolean(Constants.NOTIFY_READER_CONNECTION, NOTIFY_READER_CONNECTION);
            isChanged = true;
        }

        if (settings.getBoolean(Constants.NOTIFY_BATTERY_STATUS, true) != NOTIFY_BATTERY_STATUS) {
            editor.putBoolean(Constants.NOTIFY_BATTERY_STATUS, NOTIFY_BATTERY_STATUS);
            isChanged = true;
        }
        if (settings.getBoolean(Constants.EXPORT_DATA, false) != EXPORT_DATA) {
            editor.putBoolean(Constants.EXPORT_DATA, EXPORT_DATA);
            isChanged = true;
        }
        if (settings.getBoolean(Constants.TAG_LIST_MATCH_MODE, false) != TAG_LIST_MATCH_MODE) {
            editor.putBoolean(Constants.TAG_LIST_MATCH_MODE, TAG_LIST_MATCH_MODE);
            if (!TAG_LIST_MATCH_MODE) {
                isChanged = true;
            }
        }
        if (settings.getBoolean(Constants.SHOW_CSV_TAG_NAMES, false) != SHOW_CSV_TAG_NAMES) {
            editor.putBoolean(Constants.SHOW_CSV_TAG_NAMES, SHOW_CSV_TAG_NAMES);
            isChanged = true;
        }

        if (settings.getBoolean(Constants.ASCII_MODE, false) != ASCCI_MODE) {
            editor.putBoolean(Constants.ASCII_MODE, ASCCI_MODE);
            isChanged = true;
        }


        // Commit the edits!
        editor.commit();

        //Update the preferences in the RFIDController
        RFIDController.AUTO_RECONNECT_READERS = AUTO_RECONNECT_READERS;
        RFIDController.NOTIFY_READER_AVAILABLE = NOTIFY_READER_AVAILABLE;
        RFIDController.NOTIFY_READER_CONNECTION = NOTIFY_READER_CONNECTION;
        RFIDController.NOTIFY_BATTERY_STATUS = NOTIFY_BATTERY_STATUS;
        RFIDController.EXPORT_DATA = EXPORT_DATA;
        Application.TAG_LIST_MATCH_MODE = TAG_LIST_MATCH_MODE;
        RFIDController.SHOW_CSV_TAG_NAMES = SHOW_CSV_TAG_NAMES;
        RFIDController.asciiMode = ASCCI_MODE;

        if (isChanged)
            Toast.makeText(getActivity(), R.string.status_success_message, Toast.LENGTH_SHORT).show();
    }
}
