package pascal.orz.cn.todaytrink

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.firebase.client.Firebase
import kotlinx.android.synthetic.activity_drink_add.*
import org.joda.time.DateTime
import java.io.BufferedInputStream
import java.net.URLDecoder
import java.util.Date

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
//            val intent = Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//            startActivityForResult(intent, CHOSE_FILE_CODE);

            val intent =  Intent(Intent.ACTION_PICK,
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

        // 向きを取得
        val filePath = getFilePath(data)
        val exifInterface = ExifInterface(filePath)
        val orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));

        Log.i("debug-hoge", "orientation = " + orientation)

        drink_image.setScaleType(ImageView.ScaleType.MATRIX);
        drink_image.setImageBitmap(bitmap)

        // 画像の幅、高さを取得
        val wOrg = bitmap.getWidth()
        val hOrg = bitmap.getHeight()
        drink_image.getLayoutParams()
        val lp = drink_image.getLayoutParams()

        var factor = 0.0f
        val mat = Matrix()
        mat.reset()

        when (orientation) {
            1 -> {
                //only scaling
                factor =   (1.0f * viewWidth / wOrg).toFloat()
                mat.preScale(factor, factor)
                lp.width = (wOrg * factor).toInt()
                lp.height = (hOrg * factor).toInt()
            }
            2 -> {
                //flip vertical
                factor = (1.0f * viewWidth / wOrg) as Float

                mat.postScale(factor, -factor)
                mat.postTranslate(0.0f, hOrg * factor)
                lp.width = (wOrg * factor) as Int
                lp.height = (hOrg * factor) as Int
            }
            3 -> {
                //rotate 180
                mat.postRotate(180f, wOrg/2f, hOrg/2f);
                factor = (1.0f * viewWidth / wOrg) as Float

                mat.postScale(factor, factor);
                lp.width = (wOrg * factor) as Int
                lp.height = (hOrg * factor) as Int
            }
            4 -> {
                //flip horizontal
                factor = (1.0f * viewWidth / wOrg) as Float

                mat.postScale(-factor, factor);
                mat.postTranslate(wOrg*factor, 0f)
                lp.width = (wOrg * factor) as Int
                lp.height = (hOrg * factor) as Int
            }
            5 -> {
                //flip vertical rotate270
                 factor = (1.0f * viewWidth / hOrg) as Float

                mat.postScale(factor, -factor);
                lp.width = (hOrg * factor) as Int
                lp.height = (wOrg * factor) as Int
            }
            6 -> {
                //rotate 90
                mat.postRotate(90f, 0f, 0f);
                factor = (1.0f * viewWidth / hOrg).toFloat()

                mat.postScale(factor, factor);
                mat.postTranslate(hOrg*factor, 0f)
                lp.width = (hOrg * factor).toInt()
                lp.height = (wOrg * factor).toInt()
            }
            7 -> {
                //flip vertical, rotate 90
                mat.postRotate(90f, 0f, 0f);
                factor = (1.0f * viewWidth / hOrg) as Float

                mat.postScale(factor, -factor);
                mat.postTranslate(hOrg*factor, wOrg*factor);
                lp.width = (hOrg * factor) as Int
                lp.height = (wOrg * factor) as Int
            }
            8 -> {
                //rotate 270
                mat.postRotate(270f, 0f, 0f);
                factor = (1.0f * viewWidth / hOrg) as Float

                mat.postScale(factor, factor);
                mat.postTranslate(0f, wOrg*factor);
                lp.width = (hOrg * factor) as Int
                lp.height = (wOrg * factor) as Int
            }
        }


        drink_image.setLayoutParams(lp);
        drink_image.setImageMatrix(mat);
        drink_image.invalidate();
    }

    private fun readImage(selectedImageURI: Uri):Bitmap{
        val input = getContentResolver().openInputStream(selectedImageURI);
        val options =  BitmapFactory.Options();
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