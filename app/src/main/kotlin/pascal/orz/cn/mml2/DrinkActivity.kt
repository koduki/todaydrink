package pascal.orz.cn.mml2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.activity_drink.*

public class DrinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val intent = getIntent()
        val name = intent.getSerializableExtra("drink_name") as String
        drink_name.setText(name)

        drunk_button.setOnClickListener {
            Toast.makeText(getApplication(), "ドリンクヒストリーに追加しました", Toast.LENGTH_LONG)?.show()
        }
    }
}