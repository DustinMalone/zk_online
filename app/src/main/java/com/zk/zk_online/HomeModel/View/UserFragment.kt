package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.zk.lpw.base.BaseFragment
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.lpw.utils.VersionUtil
import com.zk.zk_online.HomeModel.Model.UserInfo
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.weight.circleImageView


/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class UserFragment : BaseFragment() {

    var linear_my_gif: LinearLayout? = null;
    var linear_my_bill: LinearLayout? = null;
    var linear_my_code: LinearLayout? = null;
    var linear_input_code: LinearLayout? = null;
    var linear_help: LinearLayout? = null;
    var linear_address_manager: LinearLayout? = null;
    var linear_advice: LinearLayout? = null;
    var linear_message:LinearLayout?=null;
    var linear_setting:LinearLayout?=null


    var img_user: circleImageView?=null;
    var tv_user_name:TextView?=null
    var tv_user_coins:TextView?=null

    var iv_user_fragment_sex: ImageView?=null
    var iv_userFragment_img_back: ImageView?=null
    var tv_user_fragment_version:TextView?=null
    var iv_red_point:ImageView?=null


    var sex=""

    //获取已返回申诉条数接口
    val GETFEEDBACKNUMBER=1001


    override fun getLayoutId(): Int {
        return R.layout.fragment_user;

    }

    override fun init() {
        tv_user_fragment_version=baseView!!.findViewById(R.id.tv_user_fragment_version)
        linear_my_bill = baseView!!.findViewById(R.id.linear_my_bill);
        linear_my_gif = baseView!!.findViewById(R.id.linear_my_gif);
        linear_my_code = baseView!!.findViewById(R.id.linear_my_code);
        linear_input_code = baseView!!.findViewById(R.id.linear_input_code);
        linear_help = baseView!!.findViewById(R.id.linear_help);
        linear_address_manager = baseView!!.findViewById(R.id.linear_address_manager);
        linear_message=baseView!!.findViewById(R.id.linear_message);
        linear_setting=baseView!!.findViewById(R.id.linear_setting)
        tv_user_coins=baseView!!.findViewById(R.id.tv_user_coins)
        iv_user_fragment_sex=baseView!!.findViewById(R.id.iv_user_fragment_sex)
        iv_userFragment_img_back=baseView!!.findViewById(R.id.iv_userFragment_img_back)
        linear_advice= baseView!!.findViewById(R.id.linear_advice);
        iv_red_point=baseView!!.findViewById(R.id.iv_red_point)

        linear_advice!!.setOnClickListener(this)
        linear_my_bill!!.setOnClickListener(this)
        linear_my_gif!!.setOnClickListener(this)
        linear_my_code!!.setOnClickListener(this)
        linear_input_code!!.setOnClickListener(this)
        linear_help!!.setOnClickListener(this)
        linear_message!!.setOnClickListener(this)
        linear_address_manager!!.setOnClickListener(this)
        iv_userFragment_img_back!!.setOnClickListener(this)
        linear_setting!!.setOnClickListener(this)

        img_user=baseView!!.findViewById(R.id.img_user);
        tv_user_name=baseView!!.findViewById(R.id.tv_user_name)
        sex=getUsersex();
        tv_user_name!!.text=getUsername();
        Glide.with(activity).load(getUserimg())
                .into(img_user)
        if (sex.equals("1"))
        {

            if (isAdded)
            {
                iv_user_fragment_sex!!.setImageResource(R.drawable.man)
//                tv_user_name!!.setCompoundDrawablesWithIntrinsicBounds(null,null,resources.getDrawable(R.drawable.boy),null)
            }
        }
        else
        {
            if (isAdded) {
                iv_user_fragment_sex!!.setImageResource(R.drawable.lady)
//                tv_user_name!!.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.girl), null)
            }
        }

        tv_user_fragment_version!!.text=VersionUtil.getVersionName(context)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.linear_my_gif ->
                startActivity(Intent(activity, UserGifActivity::class.java));
            R.id.linear_address_manager ->{
                startActivity(Intent(activity, AddressManagerActivity::class.java));
            }
            R.id.linear_my_bill ->
                startActivity(Intent(activity, UserConsumeActivity::class.java));
            R.id.img_user -> {

            }
            R.id.linear_my_code -> {
                startActivity(Intent(activity, UserInviteCodeActivity::class.java));
            }
            R.id.linear_input_code -> {
                startActivity(Intent(activity, InputInviteCodeActivity::class.java));
            }
            R.id.linear_help -> {
                startActivity(Intent(activity, HelpActivity::class.java));
            }
            R.id.iv_userFragment_img_back -> {
                (activity as MainActivity).showTitleBar(true)
                var homefragment=HomeFragment();
                (activity as MainActivity).supportFragmentManager!!.beginTransaction().replace(R.id.fragment_content, homefragment).commit()

            }

            R.id.linear_advice->{
                startActivity(Intent(activity, UserAdviceActivity::class.java));
            }
            R.id.linear_setting->{
                startActivity(Intent(activity,SettingActivity::class.java))
            }

            R.id.linear_message ->{
                startActivity(Intent(activity, SystemMessageActivity::class.java));
            }



        }
    }

    override fun getMessage(bundle: Bundle) {
        var data=bundle.getString("msg");
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)

        when(bundle.getInt("what")){
            0->{
                var userinfo: UserInfo =GsonUtil.jsonToObject(data,UserInfo::class.java) as UserInfo
                tv_user_coins!!.text=userinfo.coins.toString()

                if (isAdded)
                {
                    SharedPreferencesUtil.putString(activity,Constant.USERCOINS,userinfo.coins.toString());
                    SharedPreferencesUtil.putString(activity,Constant.SHARE_CODE,userinfo.sharecode.toString());

                }
            }

            GETFEEDBACKNUMBER->{
                if(null!=rjson!!.get("number")&&rjson!!.get("number").asInt>0){
                    iv_red_point!!.visibility=View.VISIBLE
                }else{
                    iv_red_point!!.visibility=View.GONE
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
        getFeedBackNumber()
    }

    //获得用户信息
    fun getUserInfo()
    {
        var sign="loginid="+getUsercode()+"&sn="+Constant.SN+"&"+Constant.KEY;
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN);
        param.put("loginid",getUsercode());
        param.put("sign",MD5Utils.getPwd(sign).toLowerCase())
        setDialogShow(false)
        httpPost(Constant.SERVERIP+Constant.getuserinfo,param,0)
    }


    //获取已返回申诉条数接口
    fun getFeedBackNumber(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        setDialogShow(false)
        httpPost(Constant.SERVERIP + Constant.getfeedbacknumber, param, GETFEEDBACKNUMBER);
    }




}