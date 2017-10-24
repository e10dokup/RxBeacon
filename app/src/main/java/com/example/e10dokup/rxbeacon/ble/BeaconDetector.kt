package com.example.e10dokup.rxbeacon.ble

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import io.reactivex.Observable

/**
 * Created by e10dokup on 2017/10/22.
 */
class BeaconDetector(context: Context) {

    private var scanning = false

    private val scanner: BluetoothLeScanner
    private var scanCallback: ScanCallback? = null

    init {
        scanner = context.run {
            val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            return@run manager.adapter.bluetoothLeScanner
        }
    }

    fun start(): Observable<ScanResult> {
        return Observable.create { subscriber ->
            if (scanning) return@create
            scanning = true
            scanCallback = object: ScanCallback() {
                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                }

                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    result ?: return
                    subscriber.onNext(result)
                }

                override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                    super.onBatchScanResults(results)
                }
            }
            scanner.startScan(scanCallback)
        }


    }

    fun stop() {
        if (!scanning) return
        scanning = false

        scanner.stopScan(scanCallback)
    }

}