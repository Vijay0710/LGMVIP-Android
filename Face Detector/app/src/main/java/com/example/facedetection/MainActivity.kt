package com.example.facedetection

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.google.android.odml.image.BitmapMlImageBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.IOException
import android.content.ContextWrapper
import android.os.Environment
import android.widget.*
import androidx.core.content.FileProvider


class MainActivity : AppCompatActivity() {
    var selectedPhotoUri : Uri? = null
    var bitmap : Bitmap?= null
    var photo: Bitmap? = null
    var Progress: ProgressDialog? = null
    private var currentPhotoPath:String? = null
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val pic_id = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imgView)

        val cameraClick = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.cameraClick)
        btn.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,0)
        }

        cameraClick.setOnClickListener {
            val filename = (1..15)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
            Log.i("VIJFILENAME",filename)
            // storage/emulated/0/Android/data/com.example.facedetection/files/Pictures
            val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            Log.i("VIJSTORAGE",storageDirectory.toString())
            try{
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            File.createTempFile(filename,".jpg",storageDirectory)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val imageUri: Uri = FileProvider.getUriForFile(this,
                                "com.example.facedetection.fileprovider",it)
                            currentPhotoPath = photoFile.absolutePath
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(takePictureIntent, pic_id)
                        }
                    }
                }
            }
            catch (e: IOException){
                e.printStackTrace()
            }

        }


        findViewById<Button>(R.id.checkFace).setOnClickListener {
            if(bitmap == null){
                Toast.makeText(this,"Please upload a picture",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pic_id ){
            Log.i("VIJPHOT","BEFORE PHOTO")
            photo = BitmapFactory.decodeFile(currentPhotoPath)
            Log.i("VIJPHOT","AFTER PHOTO")
            findViewById<ImageView>(R.id.imgView).setImageBitmap(photo)
            Log.i("VIJPHOT","UPDATE PHOTO")

            findViewById<Button>(R.id.checkFace).setOnClickListener {
                Progress = ProgressDialog(this)
                Progress!!.isIndeterminate = true
                Progress!!.setMessage("Detecting the face...")
                Progress!!.show()
                detect_face(requestCode)
            }
        }

        if(requestCode == 0 && data!=null){
            Log.i("info","Photo Selected")
            selectedPhotoUri = data.data
            bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
            )
            findViewById<ImageView>(R.id.imgView).setImageBitmap(bitmap)

            findViewById<Button>(R.id.checkFace).setOnClickListener {
                Progress = ProgressDialog(this)
                Progress!!.isIndeterminate = true
                Progress!!.setMessage("Detecting the face...")
                Progress!!.show()
                detect_face(requestCode)
            }
        }
    }


    private fun detect_face(requestCode: Int) {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        val detector = FaceDetection.getClient(highAccuracyOpts)
        var image: InputImage? = null
        var tempBitmap: Bitmap? = null
        if(requestCode==pic_id){
            try{
                image = InputImage.fromBitmap(photo!!,0)
            }
            catch (e: IOException){
                e.printStackTrace()
            }

            val result = detector.process(image!!)
                .addOnSuccessListener { faces ->
                    if (faces.size == 0){
                        findViewById<TextView>(R.id.faceDetect).text = "No Face Detected"
                        Progress!!.dismiss()
                        return@addOnSuccessListener
                    }
                    for (face in faces) {
                        val bounds = face.boundingBox
                        tempBitmap = photo!!.copy(Bitmap.Config.RGB_565, true)
                        val canvas = Canvas(tempBitmap!!)
                        val paint = Paint()
                        paint.alpha = 0xA0
                        paint.color = Color.RED
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = 20F
                        canvas.drawRect(bounds, paint)
                        findViewById<ImageView>(R.id.imgView).setImageBitmap(tempBitmap)
                        Progress!!.dismiss()
                        findViewById<TextView>(R.id.faceDetect).text = "Face Detected: " + faces.size

                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                            Log.i("VIJ809213", "${smileProb}")
                        } else {
                            Log.i("VIJ809213", "${null}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Progress!!.dismiss()
                }


        }
        else{
            try {
                image = InputImage.fromFilePath(this, selectedPhotoUri!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val result = detector.process(image!!)
                .addOnSuccessListener { faces ->

                    if (faces.size == 0){
                        findViewById<TextView>(R.id.faceDetect).text = "No Face Detected"
                        Progress!!.dismiss()
                        return@addOnSuccessListener
                    }

                    for (face in faces) {
                        val bounds = face.boundingBox
                        tempBitmap = bitmap!!.copy(Bitmap.Config.RGB_565, true)
                        val canvas = Canvas(tempBitmap!!)
                        val paint = Paint()
                        paint.alpha = 0xA0
                        paint.color = Color.RED
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = 20F
                        canvas.drawRect(bounds, paint)
                        findViewById<ImageView>(R.id.imgView).setImageBitmap(tempBitmap)
                        Progress!!.dismiss()
                        findViewById<TextView>(R.id.faceDetect).text = "Face Detected: " + faces.size

                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                            Log.i("VIJ809213", "${smileProb}")
                        } else {
                            Log.i("VIJ809213", "${null}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Progress!!.dismiss()
                }


        }
    }
}