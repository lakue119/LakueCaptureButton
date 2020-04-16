package com.lakue.capturebutton.Permission;

import java.util.List;

public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(List<String> deniedPermissions);

}
