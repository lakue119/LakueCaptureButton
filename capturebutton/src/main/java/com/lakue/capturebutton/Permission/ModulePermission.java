package com.lakue.capturebutton.Permission;

import android.content.Context;

public class ModulePermission{
    public static final String TAG= ModulePermission.class.getSimpleName();

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends PermissionBuilder<Builder> {

        private Builder(Context context) {
            super(context);
        }

        public void check() {
            checkPermissions();
        }

    }
}
