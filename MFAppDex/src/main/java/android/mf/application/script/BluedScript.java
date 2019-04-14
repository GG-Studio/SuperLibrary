package android.mf.application.script;

import android.content.Context;
import android.widget.Toast;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BluedScript {

    private String TAG = "BluedScript";
    private Context context = null;
    private XC_LoadPackage.LoadPackageParam lpparam = null;
    private double versions = 0.0;

    public BluedScript(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        Toast.makeText(context,"BluedScript实例化！", Toast.LENGTH_SHORT).show();
        this.context = context;
        this.lpparam = lpparam;
    }

    public void setScriptVersions(double versions) {
        this.versions = versions;
        Toast.makeText(context,"执行任务！", Toast.LENGTH_SHORT).show();
    }

    public void executeTask() {
        Toast.makeText(context,"执行任务！", Toast.LENGTH_SHORT).show();
    }
}
