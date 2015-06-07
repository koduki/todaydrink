package pascal.orz.cn.todaytrink

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.firebase.client.Firebase
import kotlinx.android.synthetic.activity_drink_add.drink_image
import kotlinx.android.synthetic.activity_drink_add.drink_name
import kotlinx.android.synthetic.activity_drink_add.save_button
import kotlinx.android.synthetic.activity_drink_add.select_image_button

/**
 * Created by koduki on 2015/05/24.
 */
public class DrinkAddActivity : AppCompatActivity() {
    val CHOSE_FILE_CODE = 123
    val RESULT_PICK_IMAGEFILE = 1001
    var viewWidth = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_add)
        Firebase.setAndroidContext(this);

        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        val disp = wm.getDefaultDisplay();
        viewWidth = disp.getWidth()

        select_image_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_PICK_IMAGEFILE);

        }

        save_button.setOnClickListener {
            val firebase = Firebase("https://shining-heat-6127.firebaseio.com/")
            firebase.child("data").child("drinks").push().setValue(Drink(drink_name.getText().toString()))
            Toast.makeText(getApplication(), "登録しました", Toast.LENGTH_LONG)?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        val data = intent!!.getData()

        val bitmap = readImage(data)
        drink_image.setScaleType(ImageView.ScaleType.MATRIX);
        drink_image.setImageBitmap(bitmap)

        ajustImage(bitmap, getOrientation(data))
    }

    private fun getOrientation(selectedUri:Uri):Int{
        val filePath = getFilePath(selectedUri)
        val exifInterface = ExifInterface(filePath)
        val orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
        Log.d("debug-hoge", "orientation = " + orientation)

        return orientation
    }
    private fun ajustImage(bitmap:Bitmap, orientation:Int){


        // 画像の幅、高さを取得
        val originalWidth = bitmap.getWidth()
        val originalHeight = bitmap.getHeight()
        val layoutParams = drink_image.getLayoutParams()

        val matrix = Matrix()
        matrix.reset()

        when (orientation) {
            1 -> {
                //only scaling
                rotateImage((1.0 * viewWidth / originalWidth),
                        BaseSize(originalWidth, originalHeight),
                        PreScale(1, 1),
                        PostTranslate(0, 0),
                        PostRotate(0f, 0f, 0f),
                        matrix, layoutParams)
            }
            2 -> {
                //flip vertical
                rotateImage((1.0 * viewWidth / originalWidth),
                        BaseSize(originalWidth, originalHeight),
                        PreScale(1, -1),
                        PostTranslate(0, originalHeight),
                        PostRotate(0f, 0f, 0f),
                        matrix, layoutParams)
            }
            3 -> {
                //rotate 180
                rotateImage((1.0 * viewWidth / originalWidth),
                        BaseSize(originalWidth, originalHeight),
                        PreScale(1, 1),
                        PostTranslate(0, 0),
                        PostRotate(180f, originalWidth / 2f, originalHeight / 2f),
                        matrix, layoutParams)
            }
            4 -> {
                //flip horizontal
                rotateImage((1.0 * viewWidth / originalWidth),
                        BaseSize(originalWidth, originalHeight),
                        PreScale(-1, 1),
                        PostTranslate(originalWidth, 0),
                        PostRotate(0f, 0f, 0f),
                        matrix, layoutParams)
            }
            5 -> {
                //flip vertical rotate270
                rotateImage((1.0 * viewWidth / originalHeight),
                        BaseSize(originalHeight, originalWidth),
                        PreScale(1, -1),
                        PostTranslate(0, 0),
                        PostRotate(0f, 0f, 0f),
                        matrix, layoutParams)
            }
            6 -> {
                //rotate 90
                rotateImage((1.0 * viewWidth / originalHeight),
                        BaseSize(originalHeight,originalWidth),
                        PreScale(1, 1),
                        PostTranslate(originalHeight, 0),
                        PostRotate(90f, 0f, 0f),
                        matrix, layoutParams)
            }
            7 -> {
                //flip vertical, rotate 90
                rotateImage((1.0 * viewWidth / originalHeight),
                        BaseSize(originalHeight,originalWidth),
                        PreScale(1, -1),
                        PostTranslate(originalHeight, originalWidth),
                        PostRotate(90f, 0f, 0f),
                        matrix, layoutParams)
            }
            8 -> {
                //rotate 270
                rotateImage((1.0 * viewWidth / originalHeight),
                        BaseSize(originalHeight,originalWidth),
                        PreScale(1, 1),
                        PostTranslate(0, originalWidth),
                        PostRotate(270f, 0f, 0f),
                        matrix, layoutParams)
            }
        }

        drink_image.setLayoutParams(layoutParams);
        drink_image.setImageMatrix(matrix);
        drink_image.invalidate();
    }
    class PreScale(val signX:Int, val signY:Int)
    class PostTranslate(val dx:Int, val dy:Int)
    class PostRotate(val degree:Float, val px:Float, val py:Float)
    class BaseSize(val width:Int, val height:Int)
    private fun rotateImage(imageFactor: Double, base:BaseSize, scale:PreScale, translate:PostTranslate, rotate:PostRotate, mat: Matrix, lp: ViewGroup.LayoutParams) {
        val factor = imageFactor.toFloat()
        mat.postRotate(rotate.degree, rotate.px, rotate.py);
        mat.preScale(factor * scale.signX, factor * scale.signY)
        mat.postTranslate(factor * translate.dx, factor * translate.dy)
        lp.width = (base.width * factor).toInt()
        lp.height = (base.height * factor).toInt()
    }

    private fun readImage(selectedImageURI: Uri): Bitmap {
        val input = getContentResolver().openInputStream(selectedImageURI);
        val options = BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        val bitmap = BitmapFactory.decodeStream(input, null, options);
        input.close()

        return bitmap
    }

    private fun getFilePath(selectedImageURI: Uri): String? {
        // MediaStore.Images.Media.DATA is "_data"
        val columns = array("_data")
        val cursor = getContentResolver().query(selectedImageURI, columns, null, null, null)
        cursor.moveToFirst()
        val filePath = cursor.getString(0)

        cursor.close()

        return filePath
    }
}