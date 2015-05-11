package pascal.orz.cn.mml2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView


public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drinkAdapter = DrinkAdapter(this)
        val drinkList = findViewById(R.id.drinkListView) as (ListView)
        drinkList.setAdapter(drinkAdapter)
        drinkAdapter.add("日本酒")
        drinkAdapter.add("ビール")
        drinkAdapter.add("ワイン")

        drinkList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                //def list = (ListView) findViewById(R.id.part_list)
                //def part = (Part) list.getItemAtPosition(position)
                val name = adapterView.getItemAtPosition(i) as String
                        Log.i("hogehoge", name)
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

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
