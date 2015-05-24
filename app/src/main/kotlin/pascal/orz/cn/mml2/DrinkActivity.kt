package pascal.orz.cn.mml2

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
import java.util.Date

public class DrinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)
        Firebase.setAndroidContext(this);

        val intent = getIntent()
        val name = intent.getSerializableExtra("drink_name") as String
        drink_name.setText(name)

        drunk_button.setOnClickListener {
            val firebase = Firebase("https://shining-heat-6127.firebaseio.com/")
            firebase.child("data").child("drink_histories").push().setValue(DrinkHistory(name, "", DateTime.now().toDate()))
            Toast.makeText(getApplication(), "登録しました", Toast.LENGTH_LONG)?.show()
        }
    }
}