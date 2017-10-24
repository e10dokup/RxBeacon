package com.example.e10dokup.rxbeacon.transformer

import android.bluetooth.le.ScanResult
import com.example.e10dokup.rxbeacon.model.IBeaconValue

/**
 * Created by e10dokup on 2017/10/23.
 */
class IBeaconTransformer {
    companion object {
        fun check(result: ScanResult): Boolean {
            val packet = result.scanRecord.bytes
            return packet[4] == 0x02.toByte() && packet[5] == 0x15.toByte()
        }

        fun transform(result: ScanResult): IBeaconValue {
            val packet = result.scanRecord.bytes
            val major = byteArrayOf(packet[22], packet[23])
            val minor = byteArrayOf(packet[24], packet[25])
            val power = packet[26]
            return IBeaconValue(major, minor, power)
        }
    }
}