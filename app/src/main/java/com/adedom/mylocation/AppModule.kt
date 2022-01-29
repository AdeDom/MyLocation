package com.adedom.mylocation

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideLocationRequest(): LocationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}