package com.example.e10dokup.rxbeacon

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.e10dokup.rxbeacon.ble.BeaconDetector
import com.example.e10dokup.rxbeacon.ble.BluetoothAdvertiseHelper
import com.example.e10dokup.rxbeacon.transformer.IBeaconTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var startAdvButton: Button
    private lateinit var stopAdvButton: Button
    private lateinit var startScanButton: Button
    private lateinit var stopScanButton: Button

    private val advertiser by lazy { BluetoothAdvertiseHelper(this) }
    private val detector by lazy { BeaconDetector(this) }

    private var scanDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startAdvButton = findViewById(R.id.start_adv_btn)
        stopAdvButton = findViewById(R.id.stop_adv_btn)
        startScanButton = findViewById(R.id.start_scan_btn)
        stopScanButton = findViewById(R.id.stop_scan_btn)
        startAdvButton.setOnClickListener(this)
        stopAdvButton.setOnClickListener(this)
        startScanButton.setOnClickListener(this)
        stopScanButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_adv_btn -> advertiser.startAdvertising(callback)
            R.id.stop_adv_btn -> advertiser.stopAdvertising(callback)
            R.id.start_scan_btn -> startScan()
            R.id.stop_scan_btn -> detector.stop()
        }
    }

    private fun startScan() {
        scanDisposable = detector.start()
                .filter { IBeaconTransformer.check(it) }
                .map { IBeaconTransformer.transform(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("MainActivity", String(it.major))
                }, {
                    Log.d("MainActivity", it.message)
                })
    }

    private fun stopScan() {
        detector.stop()
        scanDisposable?.dispose()
    }

    private val callback = object: AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Toast.makeText(this@MainActivity, "Start Advertise", Toast.LENGTH_SHORT).show()
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
        }
    }

}
