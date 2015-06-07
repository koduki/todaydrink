package pascal.orz.cn.todaytrink

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.firebase.client.Firebase
import kotlinx.android.synthetic.activity_drink.*
import org.joda.time.DateTime
import java.io.File
import java.io.FileInputStream
import java.util.Date

public class DrinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)
        Firebase.setAndroidContext(this);

        val intent = getIntent()
        val name = intent.getSerializableExtra("drink_name") as String
        
        val imageDir = intent.getSerializableExtra("drink_image_path") as File
        val file = File(imageDir, "cache.jpg")
        val image = BitmapFactory.decodeStream(FileInputStream(file))
        
        drink_name.setText(name)
        drink_image.setImageBitmap(image)

        drunk_button.setOnClickListener {
            val firebase = Firebase("https://shining-heat-6127.firebaseio.com/")
            firebase.child("data").child("drink_histories").push().setValue(DrinkHistory(name, "", DateTime.now().toDate()))
            Toast.makeText(getApplication(), "登録しました", Toast.LENGTH_LONG)?.show()
        }
    }
}