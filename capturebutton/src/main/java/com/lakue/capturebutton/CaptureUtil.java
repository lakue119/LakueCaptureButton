package com.lakue.capturebutton;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.LruCache;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureUtil {

    /**
     * 특정 뷰만 캡쳐
     * @param View
     */
    public static void captureView(View View, String folderName) {
        View.buildDrawingCache();
        Bitmap captureView = View.getDrawingCache();
        FileOutputStream fos;

        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + folderName;
        File folder = new File(strFolderPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png";
        File fileCacheItem = new File(strFilePath);

        try {
            fos = new FileOutputStream(fileCacheItem);
            captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 액티비티 전체 캡쳐
     * @param context
     */

    public static String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LovelyMarket6";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void captureActivity(Activity context, String folderName) {
        if(context == null) return;
        File file;
        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + folderName;

        if( Build.VERSION.SDK_INT < 29) file = new File(strFolderPath);
        else file = context.getExternalFilesDir(folderName);

//        File file = new File(download_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.fromFile(file),"r",null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileInputStream inputStream  = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

        SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
//        ll_screen_shot.buildDrawingCache();
//        Bitmap captureview = ll_screen_shot.getDrawingCache();
        View root = context.getWindow().getDecorView().getRootView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache();
// 루트뷰의 캐시를 가져옴
        Bitmap captureview = root.getDrawingCache();

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(strFolderPath + "/Capture" + day.format(date) + ".jpeg");
            captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + strFolderPath + "/Capture" + day.format(date) + ".JPEG")));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 리사이클러뷰 전체스크롤 캡쳐
     * @param view
     * @param bgColor
     */
    public static void captureRecyclerView(RecyclerView view, int bgColor, String folderName) {
        if(view == null) return;
        captureMyRecyclerView(view, bgColor, 0, view.getAdapter().getItemCount() - 1,folderName);
    }

    /**
     * 리사이클러뷰 범위 캡쳐
     * @param view
     * @param bgColor
     * @param startPosition
     * @param endPosition
     */
    public static void captureRecyclerView(RecyclerView view, int bgColor, int startPosition, int endPosition, String folderName) {
        if(view == null) return;
        captureMyRecyclerView(view, bgColor, startPosition, endPosition,folderName);
    }


    private static void captureMyRecyclerView(RecyclerView view, int bgColor, int startPosition, int endPosition, String folderName) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {

            if ( startPosition > endPosition ){
                int tmp = endPosition;
                endPosition = startPosition;
                startPosition = tmp;
            }

            int size = endPosition - startPosition;
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = startPosition; i < endPosition + 1; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                if(bgColor != 0)
                    holder.itemView.setBackgroundColor(bgColor);
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size + 1; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }

        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + folderName;
        File folder = new File(strFolderPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png";
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bigBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
