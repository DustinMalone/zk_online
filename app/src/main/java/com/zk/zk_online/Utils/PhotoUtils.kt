package com.zk.zk_online.Utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import java.io.*








/**
 * Created by LCB on 2017/11/27.
 */
class PhotoUtils {
    companion object {
     /**
     * 4.4以下系统处理图片的方法
     */
      fun handleImageBeforeKitKat( context: Context,data: Intent):String? {

        val uri = data.data
        val imagePath = getImagePath(context,uri, null)

        return imagePath

        }


    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
     fun handleImgeOnKitKat(context: Context,data: Intent):String? {
        var imagePath: String? = null
        val uri = data.data
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                //解析出数字格式的id
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            }  else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId)!!)
                imagePath = getImagePath(context,contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(context,uri, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.path
            }
            //根据图片路径显示图片
            return imagePath

//        return null
    }




    /**
     * 通过uri和selection来获取真实的图片路径
     */
     fun getImagePath( context: Context,uri: Uri, selection: String?): String? {

        var path: String? = null
        val cursor =  context.getContentResolver().query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                path = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor!!.close()
        }
        return path
    }

        //打开相册
    fun openAlbum(activity: Activity,what:Int){
            var intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(intent, what)
    }

        /**
         * 将图片转换成Base64编码的字符串
         * @param path
         * @return base64编码的字符串
         */
        fun imageToBase64(path: String): String? {
            if (TextUtils.isEmpty(path)) {
                return null
            }
            var `is`: InputStream? = null
            var data: ByteArray? = null
            var result: String? = null
            try {
                `is` = FileInputStream(path)
                //创建一个字符流大小的数组。
                data = ByteArray(`is`!!.available())
                //写入数组
                `is`!!.read(data)
                //用默认的编码格式进行编码
                result = Base64.encodeToString(data, Base64.DEFAULT)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (null != `is`) {
                    try {
                        `is`!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

            }
            return result
        }


        /**
         * base64编码字符集转化成图片文件。
         * @param base64Str
         * @param path 文件存储路径
         * @return 是否成功
         */
        fun base64ToFile(base64Str: String, path: String): Boolean {
            val data = Base64.decode(base64Str, Base64.DEFAULT)
            for (i in data.indices) {
                if (data[i] < 0) {
                    //调整异常数据
                    data[i]= (data[i]+256).toByte()
                }
            }
            var os: OutputStream? = null
            try {
                os = FileOutputStream(path)
                os!!.write(data)
                os!!.flush()
                os!!.close()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        /**
         * bitmap转为base64
         * @param bitmap
         * @return
         */
        fun bitmapToBase64(bitmap: Bitmap?): String? {

            var result: String? = null
            var baos: ByteArrayOutputStream? = null
            try {
                if (bitmap != null) {
                    baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)

                    baos.flush()
                    baos.close()

                    val bitmapBytes = baos.toByteArray()
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return result
        }

        /**
         * base64转为bitmap
         * @param base64Data
         * @return
         */
        fun base64ToBitmap(base64Data: String): Bitmap {
            val bytes = Base64.decode(base64Data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }


        /**
         * 质量压缩方法
         *
         * @param image
         * @return
         */
        fun compressImage(image: Bitmap): Bitmap {

            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            var options = 90

            while (baos.toByteArray().size / 1024 > 300) { // 循环判断如果压缩后图片是否大于1M,大于继续压缩
                baos.reset() // 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos)// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10// 每次都减少10
            }
            val isBm = ByteArrayInputStream(baos.toByteArray())// 把压缩后的数据baos存放到ByteArrayInputStream中
            return BitmapFactory.decodeStream(isBm)
        }

    }
}