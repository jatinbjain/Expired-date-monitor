package com.example.expireddatemonitor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    var MY_PERMISSIONS_REQUEST_CAMERA = 0
    var scannerView: ZXingScannerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun handleResult(result: Result) {
        additemActivity.resulttextview!!.text = result.text
        onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        scannerView!!.stopCamera()
    }


    override fun onPostResume() {
        super.onPostResume()
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA)
        }
        scannerView!!.setResultHandler(this)
        scannerView!!.startCamera()
    }
}