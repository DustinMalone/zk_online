package com.zk.lpw.config

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import org.xutils.x


/**
 * Created by mukun on 2017/5/30.
 */
class MyApplication : Application() {

    init {
        PlatformConfig.setWeixin(Constant.WXAPPID,Constant.AppSecret);
    }

    override fun onCreate() {
        super.onCreate()
        //配置数据库
//        setupDatabase()
        // 初始化
        x.Ext.init(this)
        // 设置是否输出debug
        x.Ext.setDebug(true)
        UMShareAPI.get(this);
        Logger.addLogAdapter(AndroidLogAdapter())

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }

    /**
     * 配置数据库
     */
   /* fun setupDatabase() {
        //创建数据库shop.db
        val helper = DaoMaster.DevOpenHelper(this, "user.db", null)
        //获取可写数据库
        val db = helper.writableDatabase
        //获取数据库对象
        val daoMaster = DaoMaster(db)
        //获取dao对象管理者
        daoSession = daoMaster.newSession()
    }

    companion object {
        var daoSession: DaoSession? = null
        fun getDaoInstant(): DaoSession {
            return daoSession!!
        }
    }*/


}