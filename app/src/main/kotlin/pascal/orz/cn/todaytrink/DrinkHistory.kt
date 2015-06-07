package pascal.orz.cn.todaytrink

import org.joda.time.DateTime
import java.util.Date

/**
 * Created by koduki on 2015/05/18.
 */
data class DrinkHistory(var drinkName: String, var drinkKey:String, var createdAt:Date){
    constructor() : this("", "", Date()){}
}
