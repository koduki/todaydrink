package pascal.orz.cn.todaytrink

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import kotlinx.android.synthetic.activity_main.drinkListView
import net.danlew.android.joda.JodaTimeAndroid
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder

public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(this);
        Firebase.setAndroidContext(this);

        val drinkAdapter = DrinkAdapter(this)
        drinkListView.setAdapter(drinkAdapter)

        val firebase = Firebase("https://shining-heat-6127.firebaseio.com/")
        firebase.addChildEventListener(object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot?, previousChildKey: String?) {
                val drinks = snapshot!!.child("drinks").getChildren()
                drinks.forEach { x ->
                    val drink = x.getValue(javaClass<Drink>())
                    drinkAdapter.add(drink)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onCancelled(p0: FirebaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

        })

        drinkListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                val drink = adapterView.getItemAtPosition(i) as Drink
                val imageData = Base64.decode(drink.image, Base64.NO_WRAP)
                val image = BitmapFactory.decodeByteArray(imageData, 0, imageData.size())
                val fileDir = getExternalCacheDir()
                val file =  File(fileDir, "cache.jpg");
                val fo = FileOutputStream(file)
                fo.write(imageData)
                fo.close()

                val intent = Intent(getApplicationContext(), javaClass<DrinkActivity>())
                intent.putExtra("drink_name", drink.name);
                intent.putExtra("drink_image_path", fileDir)

                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        return when(id) {
            R.id.action_home -> {
                val intent = Intent(getApplicationContext(), javaClass<MainActivity>())
                startActivity(intent)
                true
            }
            R.id.action_my_drink_history -> {
                val intent = Intent(getApplicationContext(), javaClass<MyHistoryActivity>())
                startActivity(intent)
                true
            }
            R.id.action_add_drink -> {
                val intent = Intent(getApplicationContext(), javaClass<DrinkAddActivity>())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
