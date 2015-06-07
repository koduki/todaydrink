package pascal.orz.cn.todaytrink

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import java.io.File

/**
 * Created by koduki on 2015/06/07.
 */
trait ImageUtils {

    fun getOrientation(selectedUri: Uri, contentResolver:ContentResolver): Int {
        val filePath = getFilePath(selectedUri, contentResolver)
        val exifInterface = ExifInterface(filePath)
        val orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
        Log.d("debug-hoge", "orientation = " + orientation)

        return orientation
    }

    fun getOrientation(file: File): Int {
        val filePath = file.getAbsolutePath()
        val exifInterface = ExifInterface(filePath)
        val orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
        Log.d("debug-hoge", "orientation = " + orientation)

        return orientation
    }

    fun ajustImage(bitmap: Bitmap, orientation: Int, viewWidth: Int, drink_image: ImageView):Bitmap {


        // 画像の幅、高さを取得
        val originalWidth = bitmap.getWidth()
        val originalHeight = bitmap.getHeight()


        when (orientation) {
            1 -> {
                //only scaling
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalWidth),
                        PreScale(1, 1),
                        PostTranslate(0, 0),
                        PostRotate(0f, 0f, 0f))
            }
            2 -> {
                //flip vertical
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalWidth),
                        PreScale(1, -1),
                        PostTranslate(0, originalHeight),
                        PostRotate(0f, 0f, 0f))
            }
            3 -> {
                //rotate 180
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalWidth),
                        PreScale(1, 1),
                        PostTranslate(0, 0),
                        PostRotate(180f, originalWidth / 2f, originalHeight / 2f))
            }
            4 -> {
                //flip horizontal
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalWidth),
                        PreScale(-1, 1),
                        PostTranslate(originalWidth, 0),
                        PostRotate(0f, 0f, 0f))
            }
            5 -> {
                //flip vertical rotate270
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalHeight),
                        PreScale(1, -1),
                        PostTranslate(0, 0),
                        PostRotate(0f, 0f, 0f))
            }
            6 -> {
                //rotate 90
                return rotateImage(bitmap,
                            (1.0 * viewWidth / originalHeight),
                            PreScale(1, 1),
                            PostTranslate(originalHeight, 0),
                            PostRotate(90f, 0f, 0f))
            }
            7 -> {
                //flip vertical, rotate 90
                return rotateImage(bitmap,
                        (1.0 * viewWidth / originalHeight),
                        PreScale(1, -1),
                        PostTranslate(originalHeight, originalWidth),
                        PostRotate(90f, 0f, 0f))
            }
            8 -> {
                //rotate 270
               return  rotateImage(bitmap,
                       (1.0 * viewWidth / originalHeight),
                        PreScale(1, 1),
                        PostTranslate(0, originalWidth),
                        PostRotate(270f, 0f, 0f))
            }else ->{
                return bitmap
            }
        }

    }

    class PreScale(val signX: Int, val signY: Int)
    class PostTranslate(val dx: Int, val dy: Int)
    class PostRotate(val degree: Float, val px: Float, val py: Float)
    class BaseSize(val width: Int, val height: Int)

    fun rotateImage(imageFactor: Double, base: BaseSize, scale: PreScale, translate: PostTranslate, rotate: PostRotate, mat: Matrix, lp: ViewGroup.LayoutParams) {
        val factor = imageFactor.toFloat()
        mat.postRotate(rotate.degree, rotate.px, rotate.py);
        mat.preScale(factor * scale.signX, factor * scale.signY)
        mat.postTranslate(factor * translate.dx, factor * translate.dy)
        lp.width = (base.width * factor).toInt()
        lp.height = (base.height * factor).toInt()

    }

    fun rotateImage(baseImage:Bitmap, imageFactor: Double, scale: PreScale, translate: PostTranslate, rotate: PostRotate):Bitmap {
        val matrix = Matrix()
        matrix.reset()

        val factor = imageFactor.toFloat()
        matrix.postRotate(rotate.degree, rotate.px, rotate.py);
        matrix.preScale(factor * scale.signX, factor * scale.signY)
        matrix.postTranslate(factor * translate.dx, factor * translate.dy)
//        lp.width = (base.width * factor).toInt()
//        lp.height = (base.height * factor).toInt()

        val ajustedImage = Bitmap.createBitmap(baseImage, 0, 0, baseImage.getWidth(), baseImage.getHeight(), matrix, false);
        return ajustedImage

    }

    fun readImage(selectedImageURI: Uri, contentResolver: ContentResolver): Bitmap {
        val input = contentResolver.openInputStream(selectedImageURI);
        val options = BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        val bitmap = BitmapFactory.decodeStream(input, null, options);
        input.close()

        return bitmap
    }

    fun getFilePath(selectedImageURI: Uri, contentResolver: ContentResolver): String? {
        // MediaStore.Images.Media.DATA is "_data"
        val columns = array("_data")
        val cursor = contentResolver.query(selectedImageURI, columns, null, null, null)
        cursor.moveToFirst()
        val filePath = cursor.getString(0)

        cursor.close()

        return filePath
    }
}