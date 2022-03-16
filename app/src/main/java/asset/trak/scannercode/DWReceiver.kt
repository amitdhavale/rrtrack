package asset.trak.scannercode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson

class DWReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("scanData9743Create ", "" + Gson().toJson(intent.extras));
        //  This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //  Notify registered observers
        ObservableObject.instance.updateValue(intent)
    }
}
