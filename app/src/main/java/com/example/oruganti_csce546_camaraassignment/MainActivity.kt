package com.example.oruganti_csce546_camaraassignment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.oruganti_csce546_camaraassignment.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import kotlin.properties.Delegates

private const val FILE_NAME = "testTest.jpg"
private lateinit var photoFile: File

class MainActivity : AppCompatActivity() {

    var storage = Firebase.storage
    var storageRef = storage.reference

    //    val database = Firebase.database
    lateinit var binding: ActivityMainBinding
    lateinit var database: DatabaseReference
    private var tookPic by Delegates.notNull<Boolean>()

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                val view: ImageView = findViewById(R.id.imageView)
                view.setImageBitmap(takenImage)
            }


        }

    // Watched https://www.youtube.com/watch?v=DPHkhamDoyc
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.btnTakePicture)
        val uploadButton: Button = findViewById(R.id.uploadButton)
        val fetchButton: Button = findViewById(R.id.fetchButton)

        binding = ActivityMainBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Files")
        storage = FirebaseStorage.getInstance()
        tookPic = false
        var dataList = FileNameDatabase(mutableListOf("a", "c"))
        val spinner: Spinner = findViewById(R.id.spinner)
        var nameImage: EditText = findViewById(R.id.fileEditText)
        // Asks permission
        val initialText = nameImage.text.toString()

        val cameraRequest = 1888
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
        button.setOnClickListener {

            tookPic = true
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            val fileProvider = FileProvider.getUriForFile(
                this,
                "com.example.oruganti_csce546_camaraassignment.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            resultLauncher.launch(takePictureIntent)
        }
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fileList = mutableListOf<String>()
                for (fileSnapshot in dataSnapshot.children) {
                    val fileNameList = fileSnapshot.value
                    fileNameList?.let {
                        for (fileName in fileNameList as ArrayList<*>) {
                            if (fileName is String) {
                                fileList.add(fileName)
                            }
                        }
                    }
                }
                dataList = FileNameDatabase(fileList)
                // Set the adapter for the spinner
                val adapter =
                    ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, fileList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur while retrieving the list of files
            }
        })

        uploadButton.setOnClickListener {
            if (tookPic) {
                if (nameImage.text.toString() == initialText) {
                    Toast.makeText(
                        this, "Please enter the name of the image to upload",
                        Toast.LENGTH_LONG
                    ).show();
                } else {
                    var nameOfTheFile = nameImage.text.toString() + ".jpg"
                    var file = Uri.fromFile(photoFile)
                    val riversRef = storageRef.child("images/${nameOfTheFile}")

                    riversRef.putFile(file)
                    dataList.files.add(nameOfTheFile)
                    database.setValue(dataList).addOnSuccessListener {
                        Log.d("Upload File Status: ", "File uploaded")
                    }
                    nameImage.setText(initialText)

                }
            } else {
                Toast.makeText(
                    this, "Please take a picture to upload",
                    Toast.LENGTH_LONG
                ).show();
            }
        }

        fetchButton.setOnClickListener {

            val imageFetch = spinner.selectedItem.toString()
            val islandRef = storageRef.child("images/$imageFetch")
            photoFile = File.createTempFile(FILE_NAME, "jpg")
            photoFile = getPhotoFile(FILE_NAME)
            islandRef.getFile(photoFile).addOnSuccessListener {
                // Local temp file has been created
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                val view: ImageView = findViewById(R.id.imageView)
                view.setImageBitmap(takenImage)

            }.addOnFailureListener {
                // Handle any errors
            }

        }


    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

}
