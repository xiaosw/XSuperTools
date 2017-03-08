package com.xiaosw.tool.activity

import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.Toast
import com.xiaosw.library.activity.BaseAppCompatActivity
import com.xiaosw.library.precenter.BasePercenter
import com.xiaosw.library.utils.LogUtil
import com.xiaosw.tool.R

/**
 * @ClassName {@link MainActivity}
 * @Description Kotlin练习
 *
 * @Date 2016-10-10 19:17.
 * @Author xiaoshiwang.
 */
class KotlinActivity : BaseAppCompatActivity<BasePercenter<*, *>>() {

    companion object {
        val TAG = "KotlinActivity"
    }

    var acbt_kotlin: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        useCustomActionBar()
        setDisplayHomeAsUpEnabled(true)
        title = TAG
        acbt_kotlin = findViewById(R.id.acbt_kotlin) as AppCompatButton
        acbt_kotlin?.setOnClickListener {
            Toast.makeText(this, "acbt_kotlin" + it.id, Toast.LENGTH_SHORT).show()
        }

        for (i in 10 downTo 0 step 3) {
            LogUtil.e(TAG, "i 1 = " + i)
        }

        for (i in 0 until 10) { //
            LogUtil.e(TAG, "i 2 = " + i)
        }

        sum(1, 2)
    }

    fun sum(x: Int, y: Int) : Unit {
        println(x + y)
        var userInfo: UserInfo = UserInfo("kotlin", 23)
        LogUtil.e(TAG, "UserInfo------> " + userInfo.toString())
    }

    inline fun tinlin(a: Int, b: Int) : Int = a + b

    data class UserInfo(var name: String, var age: Int)

}
