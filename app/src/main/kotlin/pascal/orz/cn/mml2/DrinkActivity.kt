package pascal.orz.cn.mml2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView


public class DrinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val intent = getIntent()
        val drinkName = intent.getSerializableExtra("drink_name") as String
        val drinkNameView = findViewById(R.id.drink_name) as (TextView)
        drinkNameView.setText(drinkName)
    }
}