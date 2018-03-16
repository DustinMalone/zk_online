package com.zk.zk_online.HomeModel.Model;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class BannerBean {
    private String banner;
    private String pic;
    private String banner_content;
    private String is_skip;

    public String getBanner_content() {
        return banner_content;
    }

    public void setBanner_content(String banner_content) {
        this.banner_content = banner_content;
    }

    public String getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(String is_skip) {
        this.is_skip = is_skip;
    }


    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }




}
