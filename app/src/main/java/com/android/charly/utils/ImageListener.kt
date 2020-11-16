package com.android.charly.utils

interface ImageListener {
    fun onImagePick(reqCode: Int, path: String?)
    fun onError(s: String?)
}