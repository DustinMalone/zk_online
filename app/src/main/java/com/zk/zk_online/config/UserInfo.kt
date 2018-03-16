package com.zk.lpw.config

/**
 * Created by mukun on 2017/8/20.
 */
open class UserInfo private constructor() {

    private object Holder {
        val INSTANCE = UserInfo()
    }

    companion object {
        val instance: UserInfo by lazy { Holder.INSTANCE }
    }

    var UserName: String? = null
    var UserPassword: String? = null
    var Ip: String? = null
}