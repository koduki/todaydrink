package pascal.orz.cn.todaytrink

import java.io.Serializable

/**
 * Created by koduki on 2015/05/18.
 */
data class Drink(var name: String, var image:String) : Serializable{
    constructor() : this("", ""){}
}
