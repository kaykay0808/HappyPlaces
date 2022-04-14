package com.kay.happyplaces.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.kay.happyplaces.R
import com.kay.happyplaces.databinding.FragmentAddHappyPlaceBinding
import com.kay.happyplaces.models.HappyPlaceModelEntity
import com.kay.happyplaces.models.HappyPlaceViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddHappyPlaceFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddHappyPlaceBinding? = null
    private val binding get() = _binding!!

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private val happyPlaceViewModel: HappyPlaceViewModel by viewModels()

    // We may not variable below
    private var uriFilePath: Uri? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    // ---------------------------- //

    companion object {
        // variable for Gallery selection which will be used in the onActivityResult
        private const val GALLERY = 1

        // variable for CAMERA selection which will be used in the onActivityResult
        private const val CAMERA = 2

        // this is going to be the name of the folder on the phone where it's going to store the image.
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddHappyPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Hide actionBar on this fragment
        // (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // toolbar setup for fragments
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAddPlace)
        // Back button setup on toolbar for fragments
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // navigate back with navHost
        binding.toolbarAddPlace.setNavigationOnClickListener {
            findNavController().popBackStack()
            // customDialogForBackButton()
        }

        // initiate dateSetListener
        setDateInitiate()

        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    override fun onClick(theViewWeClicked: View?) {
        theViewWeClicked?.let {
            when (it.id) {
                R.id.et_date -> {
                    context?.let {
                        DatePickerDialog(
                            it,
                            dateSetListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
                R.id.tv_add_image -> {
                    val pictureDialog = AlertDialog.Builder(context)
                    pictureDialog.setTitle("Select Action")
                    val pictureDialogItems =
                        arrayOf("Select from gallery", "Capture photo from camera")
                    pictureDialog.setItems(pictureDialogItems) { _, which ->
                        when (which) {
                            0 -> choosePhotoFromGallery()
                            1 -> takePhotoFromCamera()
                        }
                    }
                    pictureDialog.show()
                }
                R.id.btn_save ->{
                    addHappyPlaceRecord()
                }
                // we need this else block to make the compiler happy when using more kotlin like when statement.
                else -> Unit
            }
        }
        /*when (theViewWeClicked!!.id) {
            R.id.et_date -> {
                context?.let {
                    DatePickerDialog(
                        it,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(context)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select from gallery", "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems) { _, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
        }*/
    }

    private fun setDateInitiate() {
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        // update the date field when closing the date picker
        updateDateInView()
    }

    // 0 ->
    private fun choosePhotoFromGallery() {
        // permissions with third library Dexter.
        Dexter.withContext(context).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                // After all the permission are granted launch the gallery to select and image.
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }

            // when the user choose no
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    // 1 ->
    private fun takePhotoFromCamera() {
        // permissions with third library Dexter.
        Dexter.withContext(context).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                // After all the permission are granted launch the gallery to select and image.
                if (report!!.areAllPermissionsGranted()) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA)
                }
            }

            // when the user choose no
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Some gallery result checking
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectImageBitmap =
                            MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                        // todo: call the new method here
                        uriFilePath = saveImageToInternalStorage(selectImageBitmap)
                        Log.e("Saved image", "Path :: $uriFilePath")
                        binding.ivPlaceImage.setImageBitmap(selectImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            context,
                            "Failed to load the image from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (requestCode == CAMERA) {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                uriFilePath = saveImageToInternalStorage(thumbnail)
                Log.e("Saved image", "Path :: $uriFilePath")
                binding.ivPlaceImage.setImageBitmap(thumbnail)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(context)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under the Application Settings")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", "com.kay.happyplaces", null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(cal.time).toString())
    }

    private fun saveImageToInternalStorage(/*it should store a bitmap*/bitmap: Bitmap) /*(it should return URI)*/: Uri {
        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // The Mode private is the file creation mode which is the default mode where the created file can only be accessed by the calling application (or the application sharing the same id)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    /** ======================== DATABASE/ROOM ============================================== */
    private fun addHappyPlaceRecord() {
        // Title
        val title = binding.etTitle.text.toString()

        // Description
        val description = binding.etDescription.text.toString()

        // Date
        val date = binding.etDate.text.toString()

        // Location
        val location = binding.etLocation.text.toString()

        // Image
        val image = uriFilePath.toString()

        if(title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && image.isNotEmpty()) {
            lifecycleScope.launch {
                val newData = HappyPlaceModelEntity(
                    id = 0,
                    title = title,
                    image = image,
                    description = description,
                    date = date,
                    location = location,
                    latitude = latitude,
                    longitude = longitude
                )
                Toast.makeText(
                    context,
                    "Record Saved",
                    Toast.LENGTH_LONG
                ).show()
                happyPlaceViewModel.insertDataVM(newData)
                // clear the field after we added som new data
                binding.etTitle.text?.clear()
                binding.etDescription.text?.clear()
                binding.etLocation.text?.clear()
                findNavController().navigate(R.id.action_addHappyPlaceFragment_to_happyPlacesFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
