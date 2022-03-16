package asset.trak.views.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import asset.trak.views.baseclasses.BaseActivity
import asset.trak.scannercode.DWInterface
import asset.trak.scannercode.DWReceiver
import asset.trak.scannercode.Scan
//import asset.trak.scannercode.datawedgekotlin.Scan
//import com.darryncampbell.datawedgekotlin.ScanAdapter
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.home.MainActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashAssetTrak() : BaseActivity(), CoroutineScope {


    private var scans: ArrayList<Scan> = arrayListOf();
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_assettrak)
    }

    override fun onStart() {
        super.onStart()
        launch {
            getNavigateToNext()
        }
    }

    override fun onRestart() {
        super.onRestart()
        launch {
            getNavigateToNext()
        }
    }

    private suspend fun getNavigateToNext() {
        delay(2000)
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //  DataWedge intents received here
        if (intent.hasExtra(DWInterface.DATAWEDGE_SCAN_EXTRA_DATA_STRING)) {
            //  Handle scan intent received from DataWedge, add it to the list of scans
            var scanData = intent.getStringExtra(DWInterface.DATAWEDGE_SCAN_EXTRA_DATA_STRING)
            var symbology = intent.getStringExtra(DWInterface.DATAWEDGE_SCAN_EXTRA_LABEL_TYPE)
            var date = Calendar.getInstance().getTime()
            var df = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            var dateTimeString = df.format(date)
            var currentScan = Scan(scanData.toString(), symbology.toString(), dateTimeString);
            scans.add(0, currentScan)
        }
    }

}