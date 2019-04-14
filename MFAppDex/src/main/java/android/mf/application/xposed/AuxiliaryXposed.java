package android.mf.application.xposed;


import android.content.Context;
import android.mf.application.script.WeChatScript;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AuxiliaryXposed {

    private String TAG = "AuxiliaryXposed";
    private double DexVersions = 1.0;
    private Context context = null;
    private XC_LoadPackage.LoadPackageParam lpparam = null;
    private ArrayList<Object> TotalTask = null;
    private ArrayList<Object> Task = null;
    private int TaskNumber = 0;
    private boolean isExecuteTask = false;
    private boolean isUninstallDex = false;

    public void onCreate(Context context) {
        this.context = context;
    }

    public void onHookLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        this.lpparam = lpparam;
    }

    public void onTask(ArrayList<Object> task) throws Throwable {
        Toast.makeText(context, "666", Toast.LENGTH_SHORT).show();
        if (TotalTask == null) {
            TotalTask = new ArrayList<>();
        }
        if (task != null) {
            Task = task;
            TotalTask.add(Task);
            analysisTask();
        }
    }

    private void analysisTask() throws Throwable {
        if (isExecuteTask) {
            Toast.makeText(context, "第" + (TaskNumber + 1) + "个任务在执行，排队中！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "没有任务执行，立即执行新任务！", Toast.LENGTH_SHORT).show();
            isExecuteTask = true;
            ArrayList<Object> task = (ArrayList<Object>) TotalTask.get(0);
            if (isVersions((Double) task.get(0))) {
                task.remove(0);
                Toast.makeText(context, "版本匹配！", Toast.LENGTH_LONG).show();
                String appName = task.get(0).toString();
                task.remove(0);
                switch (appName) {
                    case "WeChat":
                        Toast.makeText(context, "运行微信脚本", Toast.LENGTH_LONG).show();
                        WeChatScript weChatScript = new WeChatScript(context, lpparam);
                        weChatScript.setScriptVersions(((Double) task.get(0)).doubleValue());
                        weChatScript.setFileArguments((ArrayList<Object>) task.get(1));
                        weChatScript.setMessageHandler(TaskHandler);
                        weChatScript.executeTask((Integer) task.get(2));
                        break;
                    case "Blued":
                        Toast.makeText(context, "运行Blued脚本", Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                isExecuteTask = false;
                Toast.makeText(context, "版本不匹配！", Toast.LENGTH_LONG).show();
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

    public boolean uninstallDex() {
        return isUninstallDex;
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
                Toast.makeText(context, "任务执行完成！剩余"+TotalTask.size()+"个任务", Toast.LENGTH_LONG).show();
                TaskNumber = TaskNumber + 1;
                TotalTask.remove(0);
                if (TotalTask.size() > 0) {
                    try {
                        Toast.makeText(context, "开始执行新任务任务", Toast.LENGTH_LONG).show();
                        analysisTask();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };
/*
    public class uninstallDexThread implements Runnable{
        @Override
        public void run() {
            isUninstallDex = true;
            while (isUninstallDex) {
                try {
                    Thread.sleep(1000);// 线程暂停1秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    uninstallDexhandler.sendMessage(message);// 发送消息
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    private Handler uninstallDexhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isUninstallDex = false;
            super.handleMessage(msg);
        }
    };*/
}
