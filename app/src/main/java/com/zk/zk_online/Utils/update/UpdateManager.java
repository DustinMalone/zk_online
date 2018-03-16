package com.zk.zk_online.Utils.update;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


import com.zk.lpw.config.Constant;
import com.zk.zk_online.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by lcb on 2017/1/7 0007.
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    public AppUpdateVO appUpdate = new AppUpdateVO();
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context, AppUpdateVO appUpdate) {
        this.mContext = context;
        this.appUpdate = appUpdate;
    }

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public boolean checkUpdate() {
        if (appUpdate.getError_code() == 0) {
            // 显示提示对话框
            showNoticeDialog();
            return true;
        } else {
            return false;
            //Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }




    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("更新提示");
        builder.setMessage("更新内容:\n"+appUpdate.getUpdate_info());
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                } catch (Exception e) {

                } finally {
                    dialog.dismiss();
                }
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        if (mDownloadDialog == null) {
            // 构造软件下载对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("正在更新");
            builder.setMessage(appUpdate.getUpdate_info());
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.update_soft_progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
            builder.setView(v);
            // 取消更新
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 设置取消状态
                    cancelUpdate = true;
                }
            });
            mDownloadDialog = builder.create();
            mDownloadDialog.setCancelable(false);
            mDownloadDialog.show();
        }

        // 现在文件
        downloadApk();
    }

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        //    new downloadApkThread().start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);


        } else {
            // 启动新线程下载软件
            new downloadApkThread().start();
        }


    }

    /**
     * 下载文件线程
     */
    public class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(appUpdate.getUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "pmp.apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */

    private void installApk() {
        File apkfile = new File(mSavePath, "pmp.apk");
        if (!apkfile.exists()) {
            return;
        }


        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
        //   AppManager.getAppManager().AppExit(mContext);

    }

    public class AppUpdateVO implements Serializable {

        /**
         * "apk_url":"http://112.74.100.18/HappyShare/upload/apk/huijutianxia.apk",
         * "errmsg":"版本过旧，请下载更新版本！",
         * "success":true,
         * "errcode":0,
         * "version":1
         */

        private String url;
        private String message;
        private int error_code;
        private int version;
        private String update_info;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getUpdate_info() {
            return update_info;
        }

        public void setUpdate_info(String update_info) {
            this.update_info = update_info;
        }
    }

}
