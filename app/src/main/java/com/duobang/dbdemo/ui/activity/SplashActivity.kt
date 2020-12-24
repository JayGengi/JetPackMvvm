package com.duobang.dbdemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.duobang.common.base.BaseActivity
import com.duobang.common.util.CacheUtil
import com.duobang.dbdemo.R
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.routerJump
import com.duobang.dbdemo.databinding.ActivityWelcomeBinding
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 * 作者　: JayGengi
 * 时间　: 2020/2/22
 * 描述　:
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SplashActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {

    private var alphaAnimation: AlphaAnimation? = null

    override fun layoutId() = R.layout.activity_welcome
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        StatusBarUtil.setDarkMode(this)
//    }
    override fun initView(savedInstanceState: Bundle?) {
        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
//        mDatabind.click = ProxyClick()


        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                val tokenStr = CacheUtil.getToken()
                if ("" == tokenStr) {
                    routerJump(RouterConstant.ACT.LOGIN)
                } else {
                    routerJump(RouterConstant.ACT.MAIN)
//                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {}

        })
        if (alphaAnimation != null) {
            welcome_image.startAnimation(alphaAnimation)
        }
    }

//    inner class ProxyClick {
//
////        fun toMain() {
////            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
////            finish()
////            //带点渐变动画
////            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
////        }
//    }

}