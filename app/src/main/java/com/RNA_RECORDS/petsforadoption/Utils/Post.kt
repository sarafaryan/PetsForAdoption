package com.RNA_RECORDS.petsforadoption.Utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(var name: String = "",
                var phone: String = "",
                var email: String = "",
                var details: String = "",
                var breed: String = "",
                var type: String = "",
                var latitude: Double = 10.0,
                var longitude: Double = 10.0,
                var age: String= "",
                var city: String = ""
):Parcelable