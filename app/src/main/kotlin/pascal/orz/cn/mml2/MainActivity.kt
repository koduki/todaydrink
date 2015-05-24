package pascal.orz.cn.mml2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.activity_main.drinkListView
import net.danlew.android.joda.JodaTimeAndroid

public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(this);


        val drinkAdapter = DrinkAdapter(this)
        drinkListView.setAdapter(drinkAdapter)
        drinkAdapter.add("日本酒")
        drinkAdapter.add("ビール")
        drinkAdapter.add("ワイン")

        drinkListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                val name = adapterView.getItemAtPosition(i) as String
                val intent = Intent(getApplicationContext(), javaClass<DrinkActivity>())
                intent.putExtra("drink_name", name);

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
