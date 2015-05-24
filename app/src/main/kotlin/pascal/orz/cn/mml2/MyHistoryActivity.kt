package pascal.orz.cn.mml2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.HashMap

public class MyHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_history)
        Firebase.setAndroidContext(this);

        val drinkAdapter = DrinkAdapter(this)
        drinkListView.setAdapter(drinkAdapter)

        val firebase = Firebase("https://shining-heat-6127.firebaseio.com/")
        firebase.addChildEventListener(object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot?, previousChildKey: String?) {
                val drinks = snapshot!!.child("drink_histories").getChildren()
                drinks.forEach { x ->
                    val drink = x.getValue(javaClass<DrinkHistory>())
                    drinkAdapter.add(drink.drinkName + " - " + DateTime(drink.createdAt).toString("yyyy/MM/dd"))
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        if (id == R.id.action_homes) {
            return true
        }

        return when(id) {
            R.id.action_homes -> {
                val intent = Intent(getApplicationContext(), javaClass<MainActivity>())
                startActivity(intent)
                true
            }
            R.id.action_my_drinks -> {
                val intent = Intent(getApplicationContext(), javaClass<MyHistoryActivity>())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
