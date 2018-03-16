package com.zk.lpw.config

/**
 * Created by mukun on 2017/8/20.
 */
class ActionURL {
    companion object {
        val REFRESH: Int = 0
        val LOAD: Int = 1
        val PAGE_COUNT: Int = 15

        val UserLogin: Int = 520//登录

        val GETSVV: Int = 499//自动更新

        val LOAD_MAIN_DATA: Int = 521//主界面数据

        val PostGiftDetails: Int = 522//今日礼品消耗详情

        val PostProfitDetail: Int = 523//今日收益详情

        val PostTodayMachineInfo: Int = 524//今日启动详情

        val PostGiftBusiness: Int = 525//礼品统计一级页面

        val PostGiftBusinessDetail: Int = 526//礼品统计二级页面

        val PostBusinessDetail:Int = 527//按机台统计

        val PostUsageByPayType:Int = 528//机台统计详情

        val PostGetBusiness:Int = 529//时间统计

        val PostGetBusiness_1:Int = 530//时间统计

        val PostGetBusiness_2:Int = 531//时间统计

        val PostBusinessDetail_2:Int = 532//类型统计

        val PostUsage:Int = 533//时间统计详情

        val GETSVVF:Int = 534//获取版本号
    }
}