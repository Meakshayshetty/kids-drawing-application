package com.example.kidsdrawingapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mCurrentImagePaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val linearLayoutPaint = findViewById<LinearLayout>(R.id.ll_paint_color)
        mCurrentImagePaint = linearLayoutPaint[1] as ImageButton

        /**val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        it ->
        it.entries.forEach{
        val permissionName =it.key
        val isGranted =it.value
        if(isGranted){
        Toast.makeText(this@MainActivity,"permisson granted and now you can read the file"
        ,Toast.LENGTH_SHORT).show()
        val pickintent =Intent(Intent.ACTION_PICK
        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        openGalleryLauncher.launch(pickintent)
        }else{
        if(permissionName ==Manifest)
        }**/


        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setsizeforBrush(20.toFloat())

        val brush: ImageButton = findViewById(R.id.ib_brush)
        brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

        val undo: ImageButton = findViewById(R.id.ib_undo)
        undo.setOnClickListener {
            drawingView?.onClickUndo()
        }
        val redu: ImageButton = findViewById(R.id.ib_redo)
        redu.setOnClickListener {
            drawingView?.onclickRedo()
        }
        /**val save: ImageButton = findViewById(R.id.ib_save)
        save.setOnClickListener {

        val myBitmap :Bitmap = getBitmapFromView(drawingView)

        }
        }
        fun requestStoragePermsisson() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
        this, Manifest.
        )
        )
        }**/
    }
    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("brush size: ")

        val smallBtn = brushDialog.findViewById(R.id.small_brush) as ImageButton
        smallBtn.setOnClickListener {
            drawingView?.setsizeforBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val btnMedium = brushDialog.findViewById(R.id.medium_brush) as ImageButton
        btnMedium.setOnClickListener {
            drawingView?.setsizeforBrush(20.toFloat())
            brushDialog.dismiss()
        }
        val btnLarge = brushDialog.findViewById(R.id.large_brush) as ImageButton
        btnLarge.setOnClickListener {
            drawingView?.setsizeforBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view : View) {
        if (view != mCurrentImagePaint){
            val imagebutton = view as ImageButton
            val colorTag = imagebutton.tag.toString()
            drawingView?.setColor(colorTag)

            imagebutton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )
            mCurrentImagePaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            mCurrentImagePaint = view
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable == null) {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    /**private suspend fun saveBitmap(mBitmap: Bitmap): String {
        var result = ""
        withContext(Dispatchers.IO){
            if(mBitmap != null){
                try{
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)

                    val f = File(externalCacheDir?.absoluteFile.toString() + File.separator
                            + "KidsaDrawingApp_"+System.currentTimeMillis()/1000 + ".png" )

                    val fo =FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    result = f.absolutePath

                    runOnUiThread{
                        if(result.isNotEmpty()){
                            Toast.makeText(this@MainActivity,"file saved successfully :$result"
                                ,Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@MainActivity,"file saved successfully :$result"
                                ,Toast.LENGTH_SHORT).show()

                        }
                    }
                }
                catch(e :Exception){
                    result =""
                    e.printStackTrace()
                }
            }
        }
        return result
    }**/
}
