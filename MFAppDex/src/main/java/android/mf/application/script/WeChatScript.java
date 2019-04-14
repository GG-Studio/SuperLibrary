package android.mf.application.script;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WeChatScript {

    private String TAG = "WeChatScript";
    private Context context = null;
    private XC_LoadPackage.LoadPackageParam lpparam = null;
    private double versions = 0.0;
    private ArrayList<Object> arguments = null;
    private Handler MessageHandler = null;

    public WeChatScript(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        this.context = context;
        this.lpparam = lpparam;
    }

    public void setScriptVersions(double versions) {
        this.versions = versions;
    }

    public void setFileArguments(ArrayList<Object> arguments) {
        this.arguments = arguments;
    }

    public void setMessageHandler(Handler handel) {
        this.MessageHandler = handel;
    }

    public void executeTask(int type) {
        Toast.makeText(context, "执行任务类型: "+type, Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.arg1 = 1;
        MessageHandler.sendMessage(msg);
    }

    private Handler DownloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
        }
    };
}
