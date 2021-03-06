package android.mf.application.xposed;


import android.content.Context;
import android.content.pm.PackageManager;
import android.mf.application.script.WeChatScript;
import android.mf.application.util.AppArguments;
import android.mf.application.util.CommandManager;
import android.mf.application.util.HandlerMessage;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.widget.Toast;

import java.util.ArrayList;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AuxiliaryXposed {

    private String TAG = "AuxiliaryXposed";
    private double DexVersions = 1.0;
    private Context context = null;
    private Context initContext = null;
    private XC_LoadPackage.LoadPackageParam lpparam = null;
    private ArrayList<Object> TotalTask = null;
    private ArrayList<Object> Task = null;
    private int TaskNumber = 0;
    private boolean isExecuteTask = false;
    private boolean isUninstallDex = false;
    private boolean isDexDow = false;

    public void onCreate(Context context) {
        this.context = context;
        TotalTask = new ArrayList<>();
    }

    public void onInit(Context context) {
        this.initContext = context;
    }

    public void onHookLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        this.lpparam = lpparam;
    }

    public void onTask(ArrayList<Object> task) throws Throwable {
        if (task != null) {
            Task = task;
            TotalTask.add(Task);
        }
        analysisTask();
    }

    private void analysisTask() throws Throwable {
        if (isExecuteTask) {
            Toast.makeText(context, "第" + (TaskNumber + 1) + "个任务在执行，排队中！", Toast.LENGTH_SHORT).show();
        } else {
            isExecuteTask = true;

            final ArrayList<Object> task = (ArrayList<Object>) TotalTask.get(0);
            if (isVersions((Double) task.get(0))) {
                task.remove(0);
                String appName = task.get(0).toString();
                task.remove(0);
                switch (appName) {
                    case "WeChat":
                        if (isAvilible(AppArguments.WeChat)) {
                            WeChatScript weChatScript = new WeChatScript(context, lpparam);
                            weChatScript.setScriptVersions(((Double) task.get(0)).doubleValue());
                            weChatScript.setFileArguments((ArrayList<Object>) task.get(1));
                            weChatScript.setMessageHandler(TaskHandler);
                            weChatScript.executeTask((Integer) task.get(2));
                            weChatScript.startApp(initContext);
                        }
                        break;
                    case "Blued":
                        Toast.makeText(context, "运行Blued脚本", Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                isExecuteTask = false;
                /*HandlerMessage handlerMessage = new HandlerMessage(context);
                handlerMessage.downloadDexTask((Double) task.get(0), context);
                isDexDow = true;
                XposedHelpers.findAndHookMethod(AppArguments.PfHttpFileService,
                        lpparam.classLoader,
                        "XposedDownload",
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                if (isDexDow) {
                                    String result = (String) param.getResult();
                                    String v ="MFAppDex_v"+(Double) task.get(0)+".jar";
                                    if (result.equals("下载失败！")) {

                                    } else if (result.equals("MFAppDex_v"+task.get(0).toString()+".jar下载完成！")) {
                                        analysisTask();
                                    }
                                }
                            }
                        });*/
            }
        }
    }

    private boolean isVersions(double dexVersions) throws Throwable {
        if (DexVersions == dexVersions) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAvilible(String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Handler TaskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isExecuteTask = false;
            if (msg.arg1 == 0) {
                Toast.makeText(context, "任务执行失败！", Toast.LENGTH_LONG).show();
                if (TaskNumber > 0) {
                    TaskNumber = TaskNumber - 1;
                }
            } else if (msg.arg1 == 1) {
                TaskNumber = TaskNumber + 1;
                TotalTask.remove(0);
                Toast.makeText(context, "任务执行完成！剩余" + TotalTask.size() + "个任务", Toast.LENGTH_LONG).show();
                if (TotalTask.size() > 0) {
                    try {
                        Toast.makeText(context, "开始执行新任务", Toast.LENGTH_LONG).show();
                        analysisTask();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else if (TotalTask.size() <= 0) {
                    Toast.makeText(context, "任务全部执行完成！", Toast.LENGTH_LONG).show();
                    CommandManager CMD = new CommandManager(context);
                    ArrayList<String> cmd = new ArrayList<>();
                    cmd.add("an force-stop " + SyncStateContract.Constants.ACCOUNT_NAME);
                    CMD.executeCommand(cmd);
                    HandlerMessage handlerMessage = new HandlerMessage(initContext);
                    handlerMessage.resultTask();
                }
            }
            super.handleMessage(msg);
        }
    };
}
