package com.vinaymaneti.audiobookapp

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AudioBooksModel(
    val coverPhoto: Bitmap,
    val bookTitle: String,
    val authorName: String,
    val audioPath: String
) : Parcelable