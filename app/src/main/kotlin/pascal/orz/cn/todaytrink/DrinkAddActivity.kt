package pascal.orz.cn.todaytrink

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
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
import java.io.ByteArrayOutputStream

/**
 * Created by koduki on 2015/05/24.
 */
public class DrinkAddActivity :ImageUtils, AppCompatActivity() {
    val CHOSE_FILE_CODE = 123
    val RESULT_PICK_IMAGEFILE = 1001
    var viewWidth = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
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

            val drawable =  drink_image.getDrawable() as BitmapDrawable
            val bitmap = drawable.getBitmap();

            val bos =  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            val data = bos.toByteArray();
            val image = Base64.encodeToString(data, Base64.NO_WRAP);

            firebase.child("data").child("drinks").push().setValue(Drink(drink_name.getText().toString(), image))
            Toast.makeText(getApplication(), "登録しました", Toast.LENGTH_LONG)?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        val data = intent!!.getData()

        val bitmap = readImage(data, getContentResolver())
        drink_image.setScaleType(ImageView.ScaleType.MATRIX);

        val ajuestedImage = ajustImage(bitmap, getOrientation(data, getContentResolver()), viewWidth, drink_image)


        drink_image.setImageBitmap(ajuestedImage)

    }


}