package com.example.e10dokup.rxbeacon.ble

/**
 * Created by e10dokup on 2017/10/22.
 */
class BeaconParameter(
        var manufacturId: Array<Byte?> = arrayOfNulls(2),
        var manufactureData: Array<Byte?> = arrayOfNulls(23)
) {


    fun generatePacket() {

    }

}