package com.example.bookshelf.ui.gallery

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.bookshelf.R
import com.example.bookshelf.mlkit.BitmapUtils
import com.example.bookshelf.mlkit.GraphicOverlay
import com.example.bookshelf.mlkit.VisionImageProcessor
import com.example.bookshelf.ui.create.CreateActivity
import com.example.bookshelf.mlkit.barcodescanner.BarcodeScannerProcessor
import com.example.bookshelf.mlkit.preference.SettingsActivity
import java.io.IOException
import kotlin.math.max


class GalleryFragment : Fragment() {

    private lateinit var GalleryFragment: GalleryViewModel

    private var preview: ImageView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var isLandScape = false
    private var imageUri: Uri? = null
    private var selectedSize: String? = SIZE_SCREEN
    // Max width (portrait mode)
    private var imageMaxWidth = 0
    // Max height (portrait mode)
    private var imageMaxHeight = 0
    private var imageProcessor: VisionImageProcessor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        GalleryFragment =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_top, container, false)
        //GalleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //})

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toastButton: Button = view.findViewById(R.id.button_self)
        toastButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateActivity::class.java)
            startActivity(intent)
        }

        val barcodeButton: Button = view.findViewById(R.id.button4)
        barcodeButton.setOnClickListener { view: View ->
            // Menu for selecting either: a) take new photo b) select from existing
            val popup =
                PopupMenu(activity, view)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                val itemId =
                    menuItem.itemId
                if (itemId == R.id.select_images_from_local) {
                    startChooseImageIntentForResult()
                    return@setOnMenuItemClickListener true
                } else if (itemId == R.id.take_photo_using_camera) {
                    startCameraIntentForResult()
                    return@setOnMenuItemClickListener true
                }
                false
            }
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.camera_button_menu, popup.menu)
            popup.show()
        }
        preview = view.findViewById(R.id.preview)
        graphicOverlay = view.findViewById(R.id.graphic_overlay)

        isLandScape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if(savedInstanceState != null){
            imageUri =
                savedInstanceState.getParcelable(KEY_IMAGE_URI)
            imageMaxWidth =
                savedInstanceState.getInt(KEY_IMAGE_MAX_WIDTH)
            imageMaxHeight =
                savedInstanceState.getInt(KEY_IMAGE_MAX_HEIGHT)
            selectedSize =
                savedInstanceState.getString(KEY_SELECTED_SIZE)
        }

        val rootView = view.findViewById<View>(R.id.root)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    imageMaxWidth = rootView.width
                    imageMaxHeight = rootView.height
                    if (SIZE_SCREEN == selectedSize) {
                        tryReloadAndDetectInImage()
                    }
                }
            })

        val settingsButton = view.findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent =
                Intent(
                    activity,
                    SettingsActivity::class.java
                )
            intent.putExtra(
                SettingsActivity.EXTRA_LAUNCH_SOURCE,
                SettingsActivity.LaunchSource.STILL_IMAGE
            )
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createImageProcessor()
        tryReloadAndDetectInImage()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            KEY_IMAGE_URI,
            imageUri
        )
        outState.putInt(
            KEY_IMAGE_MAX_WIDTH,
            imageMaxWidth
        )
        outState.putInt(
            KEY_IMAGE_MAX_HEIGHT,
            imageMaxHeight
        )
        outState.putString(
            KEY_SELECTED_SIZE,
            selectedSize
        )
    }

    private fun startCameraIntentForResult() { // Clean up last time's image
        imageUri = null
        //preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity?.packageManager!!) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(
                takePictureIntent,
                REQUEST_IMAGE_CAPTURE
            )
        }
    }

    private fun startChooseImageIntentForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CHOOSE_IMAGE
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage()
        } else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // In this case, imageUri is returned by the chooser, save it.
            imageUri = data!!.data
            tryReloadAndDetectInImage()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun tryReloadAndDetectInImage() {
        Log.d(
            com.example.bookshelf.ui.gallery.GalleryFragment.TAG,
            "Try reload and detect image"
        )
        try {
            if (imageUri == null) {
                return
            }

            if (com.example.bookshelf.ui.gallery.GalleryFragment.SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
                // UI layout has not finished yet, will reload once it's ready.
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(
                activity?.contentResolver,
                imageUri
            )
                ?: return
            // Clear the overlay first
            graphicOverlay!!.clear()
            // Get the dimensions of the image view
            val targetedSize = targetedWidthHeight
            // Determine how much to scale down the image
            val scaleFactor = max(
                imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                imageBitmap.height.toFloat() / targetedSize.second.toFloat()
            )
            val resizedBitmap = Bitmap.createScaledBitmap(
                imageBitmap,
                (imageBitmap.width / scaleFactor).toInt(),
                (imageBitmap.height / scaleFactor).toInt(),
                true
            )
            preview!!.setImageBitmap(resizedBitmap)
            if (imageProcessor != null) {
                graphicOverlay!!.setImageSourceInfo(
                    resizedBitmap.width, resizedBitmap.height, /* isFlipped= */false
                )
                imageProcessor!!.processBitmap(resizedBitmap, graphicOverlay)
            } else {
                Log.e(
                    com.example.bookshelf.ui.gallery.GalleryFragment.TAG,
                    "Null imageProcessor, please check adb logs for imageProcessor creation error"
                )
            }
        } catch (e: IOException) {
            Log.e(
                com.example.bookshelf.ui.gallery.GalleryFragment.TAG,
                "Error retrieving saved image"
            )
            imageUri = null
        }
    }

    private val targetedWidthHeight: Pair<Int, Int>
        get() {
            val targetWidth: Int
            val targetHeight: Int
            when (selectedSize) {
                com.example.bookshelf.ui.gallery.GalleryFragment.SIZE_SCREEN -> {
                    targetWidth = imageMaxWidth
                    targetHeight = imageMaxHeight
                }
                com.example.bookshelf.ui.gallery.GalleryFragment.SIZE_640_480 -> {
                    targetWidth = if (isLandScape) 640 else 480
                    targetHeight = if (isLandScape) 480 else 640
                }
                com.example.bookshelf.ui.gallery.GalleryFragment.SIZE_1024_768 -> {
                    targetWidth = if (isLandScape) 1024 else 768
                    targetHeight = if (isLandScape) 768 else 1024
                }
                else -> throw IllegalStateException("Unknown size")
            }
            return Pair(targetWidth, targetHeight)
        }

    private fun createImageProcessor() {
        try {
            imageProcessor = activity?.applicationContext?.let { BarcodeScannerProcessor(it) }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Can not create image processor: ",
                e
            )
            Toast.makeText(
                activity?.applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    companion object {
        private const val TAG = "StillImageActivity"
        private const val OBJECT_DETECTION = "Object Detection"
        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection (Birds)"
        private const val FACE_DETECTION = "Face Detection"
        private const val BARCODE_SCANNING = "Barcode Scanning"
        private const val TEXT_RECOGNITION = "Text Recognition"
        private const val IMAGE_LABELING = "Image Labeling"
        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
        private const val AUTOML_LABELING = "AutoML Labeling"
        private const val POSE_DETECTION = "Pose Detection"
        private const val SIZE_SCREEN = "w:screen" // Match screen width
        private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
        private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio
        private const val KEY_IMAGE_URI = "com.google.mlkit.vision.demo.KEY_IMAGE_URI"
        private const val KEY_IMAGE_MAX_WIDTH = "com.google.mlkit.vision.demo.KEY_IMAGE_MAX_WIDTH"
        private const val KEY_IMAGE_MAX_HEIGHT = "com.google.mlkit.vision.demo.KEY_IMAGE_MAX_HEIGHT"
        private const val KEY_SELECTED_SIZE = "com.google.mlkit.vision.demo.KEY_SELECTED_SIZE"
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CHOOSE_IMAGE = 1002
    }
}
