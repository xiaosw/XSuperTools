package com.xiaosw.tool.activity

import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.xiaosw.library.activity.BaseAppCompatActivity
import com.xiaosw.library.manager.CoverFlowLayoutManager
import com.xiaosw.library.manager.SwipeCardLayoutManager
import com.xiaosw.library.precenter.BasePercenter
import com.xiaosw.library.utils.CompatToast
import com.xiaosw.library.widget.adapter.UniversalRecyclerAdaper
import com.xiaosw.tool.R
import com.xiaosw.tool.bean.SwipeCardBean
import kotlinx.android.synthetic.main.activity_cover_flow.*
import java.util.*
import kotlin.properties.Delegates

/**
 * @ClassName {@link MainActivity}
 * @Description
 *
 * @Date 2016-10-10 19:17.
 * @Author xiaoshiwang.
 */
class CoverFlowActivity : BaseAppCompatActivity<BasePercenter<*, *>>() {

    private var mDatas: List<SwipeCardBean> by Delegates.notNull()
    private var mUniversalRecyclerAdapter: UniversalRecyclerAdaper<SwipeCardBean> by Delegates.notNull()
    private var mRequestManager: RequestManager by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useCustomActionBar()
        setDisplayHomeAsUpEnabled(true)
        title = TAG
        setContentView(R.layout.activity_cover_flow)

        mRequestManager = Glide.with(this)
        mDatas = generateData()

        mUniversalRecyclerAdapter = object : UniversalRecyclerAdaper<SwipeCardBean>(this, mDatas, R.layout.item_swipe_card) {
            override fun bindData(viewHolder: UniversalRecyclerAdaper.ViewHolder, swipeCardBean: SwipeCardBean, position: Int) {
                viewHolder.bindText(R.id.tv_page_indicator, (swipeCardBean.position + 1).toString() + "/" + mDatas.size)
                viewHolder.bindText(R.id.tv_description, swipeCardBean.description)
                mRequestManager.load(swipeCardBean.url).placeholder(R.mipmap.ic_swipe_default).into(viewHolder.findViewById<ImageView>(R.id.iv_icon))
            }
        }

        with(recycler_view) {
            layoutManager = CoverFlowLayoutManager()
            adapter = mUniversalRecyclerAdapter
        }
        mUniversalRecyclerAdapter.setOnItemClickListener { _, position ->
            CompatToast.makeText(this, mUniversalRecyclerAdapter.getData(position).description, Toast.LENGTH_SHORT).show()
        }

        ItemTouchHelper(SwipeCardLayoutManager.SwipeCardCallback(mUniversalRecyclerAdapter)).attachToRecyclerView(recycler_view)
    }

    ///////////////////////////////////////////////////////////////////////////
    // test data
    ///////////////////////////////////////////////////////////////////////////
    private fun generateData(): List<SwipeCardBean> {
        val datas = ArrayList<SwipeCardBean>()
        var i = 0
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201702/05/20170205220116_ihCHT.thumb.700_0.jpeg", "到此结束"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142242_jUZNk.jpeg", "小清新"))
        datas.add(SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_03/2016_03_25/201603259771458878793312_origin.jpg", "瘦富美"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142239_fSwjY.thumb.700_0.jpeg", "绿绿你懂的"))
        datas.add(SwipeCardBean(i++, "http://p14.go007.com/2014_11_02_05/a03541088cce31b8_1.jpg", "description"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143602_u5PeF.thumb.700_0.jpeg", "萌妹子"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143412_KfiTX.jpeg", "xiaolb"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316143418_ivE8W.thumb.700_0.jpeg", "妹子"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142211_iAcsM.thumb.700_0.jpeg", "白妹子"))
        datas.add(SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_04/2016_04_12/201604124411460430531500.jpg", "清纯"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316142230_VciA2.thumb.700_0.jpeg", "小青春"))
        datas.add(SwipeCardBean(i++, "https://a-ssl.duitang.com/uploads/item/201703/16/20170316165211_tYvhR.thumb.700_0.jpeg", "白富美"))
        return datas
    }

    companion object {
        const val TAG = "CoverFlowActivity"
    }
}
