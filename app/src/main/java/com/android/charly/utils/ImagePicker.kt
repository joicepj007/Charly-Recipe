package com.android.charly.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import com.kbeanie.imagechooser.api.ChooserType
import com.kbeanie.imagechooser.api.ChosenImage
import com.kbeanie.imagechooser.api.ImageChooserListener
import com.kbeanie.imagechooser.api.ImageChooserManager
import com.soundcloud.android.crop.Crop
import java.io.File


class ImagePicker private constructor(builder: Builder) : ImageChooserListener {
    private val FOLDER_NAME = "CultureTruckImages"
    private var filePath: String? = null
    private var chooserType = 0
    private var imageReq = -1
    private var imageChooserManager: ImageChooserManager? = null
    private val mActivity: Activity?
    private val imageChooserListener: ImageListener?
    private val mFragment: Fragment?
    private val mSupportFragment: Fragment?
    private var outputUri: Uri? = null
    private val isCrop: Boolean
    private val x: Int
    private val y: Int
    private var inputUri: Uri? = null

    private val activity: Activity?
        get() {
            if (mActivity != null) {
                return mActivity
            } else if (mFragment != null) {
                return mFragment.activity
            } else if (mSupportFragment != null) {
                return mSupportFragment.getActivity()
            }
            return null
        }

    fun performImgPicAction(reqCode: Int, which: Int) {
        imageReq = reqCode
        chooserType = if (which == 1) {
            ChooserType.REQUEST_PICK_PICTURE
        } else {
            ChooserType.REQUEST_CAPTURE_PICTURE
        }
        initializeImageChooser(chooserType)
        try {
            filePath = imageChooserManager?.choose()
        } catch (e: Exception) {
            imageChooserListener?.onError(e.message)
            e.printStackTrace()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.i(TAG, "OnActivityResult")
        Log.i(TAG, "File Path : $filePath")
        Log.i(TAG, "Chooser Type: $chooserType")
        Log.d(TAG, "onActivityResult() called with: "
                + "requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]")
        if (resultCode == Activity.RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE
                        || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageReq == -1) {
                if (imageChooserListener != null) {
                    imageChooserListener.onError("Request code is diff")
                }
                return
            }
            if (imageChooserManager == null) {
                initializeImageChooser(chooserType)
                imageChooserManager?.reinitialize(filePath)
            }
            imageChooserManager?.submit(requestCode, data)
        }
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val file = File(inputUri?.path)
            file.delete()
            returnImage(imageReq, outputUri?.path)
            imageReq = -1
        }
    }

    private fun returnImage(imageReq: Int, path: String?) {
        val handler = Handler()
        handler.post {
            if (imageChooserListener != null) {
                imageChooserListener.onImagePick(imageReq, path)
            }
        }
    }

    private fun initializeImageChooser(type: Int) {
        if (mActivity != null) {
            imageChooserManager = ImageChooserManager(mActivity, type, FOLDER_NAME, false)
        } else if (mFragment != null) {
            imageChooserManager = ImageChooserManager(mFragment, type, FOLDER_NAME, false)
        } else {
            imageChooserManager = ImageChooserManager(mSupportFragment, type, FOLDER_NAME, false)
        }
        imageChooserManager?.setImageChooserListener(this)
    }

    override fun onImageChosen(chosenImage: ChosenImage) {
        inputUri = Uri.fromFile(File(chosenImage.getFilePathOriginal()))
        val imageDir = File(Environment.getExternalStorageDirectory(), "/$FOLDER_NAME")
        Log.d(TAG, "onImageChosen: $imageDir")
        if (!imageDir.exists()) {
            imageDir.mkdir()
        }
        if (isCrop) {
            val croppedOutputFile = File(imageDir, System.currentTimeMillis().toString() + ".jpeg")
            outputUri = Uri.fromFile(croppedOutputFile)
            var crop: Crop = Crop.of(inputUri, outputUri)
            if (x != y) {
                crop = crop.withAspect(x, y)
            } else if (x != -1) {
                crop = crop.asSquare()
            }
            if (mActivity != null) {
                crop.start(mActivity)
            } else if (mFragment != null) {
                crop.start(activity, mFragment)
            } else {
                crop.start(activity, mSupportFragment)
            }
        } else {
            val activity = activity
            activity?.runOnUiThread { returnImage(imageReq, chosenImage.getFilePathOriginal()) }
        }
    }

    override fun onError(s: String?) {
        imageReq = -1
        if (imageChooserListener != null) {
            imageChooserListener.onError(s)
        }
    }

    class Builder {
        var activity: Activity? = null
        var fragment: Fragment? = null
        var supportFragment: Fragment? = null
        var listener: ImageListener? = null

        private constructor(activity: Activity) {
            this.activity = activity
        }

        constructor(fragment: Fragment?) {
            this.fragment = fragment
        }

        fun setListener(imageChooserListener: ImageListener?): Builder {
            listener = imageChooserListener
            return this
        }

        fun build(): ImagePicker {
            return ImagePicker(this)
        }
    }

    companion object {
        private const val TAG = "ImagePicker"
    }

    init {
        mActivity = builder.activity
        mFragment = builder.fragment
        mSupportFragment = builder.supportFragment
        imageChooserListener = builder.listener
        x = -1
        y = -1
        isCrop = false
    }
}