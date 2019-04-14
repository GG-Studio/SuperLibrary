package android.mf.application.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;

public class HandlerMessage {

    private Context context = null;
    private static String PfPackage = "android.mf.application";
    private static String PfAwakenService = ".service.AwakenService";
    public HandlerMessage(Context context) {
        this.context = context;
    }

    public void resultTask() {
        AwakenParasitifer(new Intent());
    }

    private boolean isServiceRunning(String ServiceName) {
        if (TextUtils.isEmpty(ServiceName))
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>)
                myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    private void AwakenParasitifer(Intent intentService) {
        ComponentName componentName = new ComponentName(PfPackage, PfPackage + PfAwakenService);
        intentService.setComponent(componentName);
        intentService.putExtra("Key", "AppTask");
        intentService.putExtra("Content", "1");
        context.startService(intentService);
    }
}
