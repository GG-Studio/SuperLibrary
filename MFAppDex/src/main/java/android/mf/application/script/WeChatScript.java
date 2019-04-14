package android.mf.application.script;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
        if (MessageHandler != handel) {
            this.MessageHandler = handel;
        }
    }

    public void executeTask(int type) {
        switch (type) {
            case 0: //看新闻
                break;
            case 1: //朋友圈
                break;
            case 2: //看看
                break;
            case 3: //搜搜
                break;
            case 4: //购物
                break;
            case 5: //游戏
                break;
            case 6: //小程序
                break;
        }
        MessageHandler.postDelayed(runnable, 15000);
    }


    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            msg.arg1 = 1;
            MessageHandler.sendMessage(msg);
        }
    };


    private Handler DownloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
        }
    };
}
