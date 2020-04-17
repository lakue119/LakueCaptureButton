package com.lakue.capturebutton;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.lakue.capturebutton.Permission.ModulePermission;
import com.lakue.capturebutton.Permission.PermissionListener;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class LakueCaptureButton extends FrameLayout {
    private static final int PERMISSION_REQUEST_CODE = 200;
    CaptureType selType = CaptureType.CAPTURE_TYPE_ACTIVITY;
    String folderName = "/ScreenShot";
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};//, Manifest.permission.ACCESS_COARSE_LOCATION
    public LakueCaptureButton(Context context) {
        super(context);
        init();
    }

    public LakueCaptureButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LakueCaptureButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnCapture = new Button(getContext());
        btnCapture.setLayoutParams(params);

        this.addView(btnCapture);

        btnCapture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraCapture();
            }
        });

    }

    public void setFolderName(String folder_name){
        this.folderName = folder_name;
    }
//
//    public void setType(CaptureType type){
//        selType = type;
//    }
//
//    public void captureRange(View View){
//        selType = CaptureType.CAPTURE_TYPE_VIEW;
//
//    }
//
//    public void capture(){
//        switch (selType){
//            case CAPTURE_TYPE_VIEW:
//                CaptureUtil.captureView(View View);
//                break;
//            case CAPTURE_TYPE_ACTIVITY:
//                CaptureUtil.captureActivity(Activity context);
//                break;
//            case CAPTURE_TYPE_RECYCLERVIEW_ALL:
//                CaptureUtil.captureRecyclerView(RecyclerView view, int bgColor);
//                break;
//            case CAPTURE_TYPE_RECYCLERVIEW_CERTAIN_INDEX:
//                CaptureUtil.captureMyRecyclerView(RecyclerView view, int bgColor, int startPosition, int endPosition);
//                break;
//        }
//    }

    public void openActivity(){
        CaptureUtil.captureActivity(((Activity)getContext()),folderName);
    }

    //사진 캡처
    private void cameraCapture(){
//        if (!checkPermission()) {
//            openActivity();
//        } else {
//            if (checkPermission()) {
//                requestPermissionAndContinue();
//            } else {
//                openActivity();
//            }
//        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openActivity();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        ModulePermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("위치 권한을 허용하지 않으면 입장권발급을 받을 수 없습니다.\n[설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(REQUIRED_PERMISSIONS)
                .check();
    }

//    private boolean checkPermission() {
//        return ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissionAndContinue() {
//        if (ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            if (shouldShowRequestPermissionRationale((Activity) getContext(), WRITE_EXTERNAL_STORAGE)
//                    && shouldShowRequestPermissionRationale((Activity) getContext(), READ_EXTERNAL_STORAGE)) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                alertBuilder.setCancelable(true);
//                alertBuilder.setTitle("dd");
//                alertBuilder.setMessage("dddddd");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        requestPermissions((Activity) getContext(), new String[]{WRITE_EXTERNAL_STORAGE
//                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//                    }
//                });
//                AlertDialog alert = alertBuilder.create();
//                alert.show();
//                Log.e("", "permission denied, show dialog");
//            } else {
//                requestPermissions((Activity) getContext(), new String[]{WRITE_EXTERNAL_STORAGE,
//                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//        } else {
//            openActivity();
//        }
//    }
}

