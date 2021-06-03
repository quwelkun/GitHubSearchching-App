package com.example.githubsearching

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSource(
    var name: String = "",
    var type: String = "",
    var avatar: String = ""
): Parcelable