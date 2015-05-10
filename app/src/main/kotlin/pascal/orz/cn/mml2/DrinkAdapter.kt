package pascal.orz.cn.mml2

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by koduki on 2015/05/11.
 */
public class DrinkAdapter(private val mContext: Context) : ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1) {

    private val mInflater: LayoutInflater
    {
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: mInflater.inflate(R.layout.list_item_drink, null)

        val item = getItem(position)

        val name = view.findViewById(R.id.drink_name) as TextView
        name.setText(item)

        return view
    }
}
