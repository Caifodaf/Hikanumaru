package ru.android.hikanumaruapp.utilits

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.ReaderViewModel
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@SuppressLint("UseCompatLoadingForDrawables", "ResourceType", "ShowToast",
    "UseCompatLoadingForColorStateLists")
class SaverImagePager(
    val url: String,
    private val currentItem: Int,
    private val loadChapter: MutableLiveData<ReaderChapter>,
    val vm: ReaderViewModel, ) {

    fun saveImage():Boolean {
        val urlBitmap: URL = URL(url)
        val bitmap = BitmapFactory.decodeStream(urlBitmap.openStream())

        try {
            val chapter = loadChapter as ReaderChapter
            val imageFileName =
                chapter.title + "_" + chapter.tom + "_" + chapter.number + "_" + currentItem + 1 + ".jpg"
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/" + "Hikanumaru screenshots/"
            )

            val file = File(storageDir, imageFileName)
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            //vm.lastDirSaveImage = file.absolutePath
            return true
        } catch (e: Exception) {
            return false
        }
    }
}














//
//class SaverImagePager {
//
//    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType", "ShowToast",
//        "UseCompatLoadingForColorStateLists")
//    fun saveImage(
//        image: Bitmap,
//        currentItem: Int,
//        loadChapter: MutableLiveData<ReaderChapter>,
//        context: FragmentActivity,
//        view: ImageView,
//        handlerImageSaver: Handler?
//    ): String? {
//        val chapter = loadChapter as ReaderChapter
//        var savedImagePath: String? = null
//
//        val imageFileName = chapter.title + "_" + chapter.tom + "_" + chapter.number + "_" + currentItem+1 + ".jpg"
//        val storageDir = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                .toString() + "/" + "Hikanumaru screenshots/"
//        )
//        var success = true
//        if (!storageDir.exists()) {
//            success = storageDir.mkdirs()
//        }
//        if (success) {
//            val imageFile = File(storageDir, imageFileName)
//            savedImagePath = imageFile.absolutePath
//            try {
//                val fOut: OutputStream = FileOutputStream(imageFile)
//                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
//                fOut.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//
//                Toast.makeText(context, "Страница не сохранена.", Toast.LENGTH_LONG).show()
//            }
//
//            // Add the image to the system gallery
//            galleryAddPic(savedImagePath,context)
//
//            Toast.makeText(context, "Страница сохранена.\n$savedImagePath", 1000).show()
//
//            view.setImageDrawable(context.resources.getDrawable(R.drawable.ic_check_circle_on))
//            view.imageTintList = context.resources.getColorStateList(R.color.white)
//            view.setBackgroundResource(R.drawable.gradient_green)
//            view.backgroundTintList = context.resources.getColorStateList(R.color.green)
//
//            handlerImageSaver!!.postDelayed(Runnable {
//                view.setImageDrawable(context.resources.getDrawable(R.drawable.ic_reader_image_download))
//                view.imageTintList = context.resources.getColorStateList(R.color.black_back_text)
//                view.setBackgroundResource(R.drawable.rectangle_rounded_all)
//                view.backgroundTintList = context.resources.getColorStateList(R.color.black_back)
//            }, 2500)
//
//            //Log.d("dadadadada","do - $savedImagePath")
//            // to make this working, need to manage coroutine, as this execution is something off the main thread
//        } else {
//            Toast.makeText(context, "Страница не сохранена.", Toast.LENGTH_LONG).show()
//        }
//        return savedImagePath
//    }
//
//    private fun galleryAddPic(imagePath: String?, context: FragmentActivity) {
//        imagePath?.let { path ->
//            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//            val f = File(path)
//            val contentUri: Uri = Uri.fromFile(f)
//            mediaScanIntent.data = contentUri
//            context.sendBroadcast(mediaScanIntent)
//        }
//    }
//}