package com.adedom.mylocation

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun MapView.getGoogleMap(): GoogleMap = suspendCoroutine { continuation ->
    this.getMapAsync { googleMap ->
        continuation.resume(googleMap)
    }
}