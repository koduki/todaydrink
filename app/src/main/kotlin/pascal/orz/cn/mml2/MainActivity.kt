package pascal.orz.cn.mml2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView


public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drinkAdapter = DrinkAdapter(this)
        val drinkList =  findViewById(R.id.drinkListView) as (ListView)
        drinkList.setAdapter(drinkAdapter)
        drinkAdapter.add("日本酒")
        drinkAdapter.add("ビール")
        drinkAdapter.add("ワイン")
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
