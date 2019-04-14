package android.mf.application.script;

import android.content.Context;
import android.content.Intent;
import android.mf.application.util.AppArguments;
import android.mf.application.util.HandlerMessage;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WeChatScript {

    private String TAG = "WeChatScript";
    private Context context = null;
    private XC_LoadPackage.LoadPackageParam lpparam = null;
    private double versions = 0.0;
    private ArrayList<Object> arguments = null;
    private Handler MessageHandler = null;
    private boolean isPictureDow = false;
    private int PictureNumber = 0;
    private int PictureDowNumber = 0;

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

    public void startApp(Context context) {
        if (lpparam.packageName.equals(AppArguments.PfPackage)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            context.startActivity(intent);
        }
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(AppArguments.WeChat));
    }

    public void executeTask(int type) {
        switch (type) {
            case 0: //看新闻
                break;
            case 1: //朋友圈
                PictureNumber = arguments.size();
                HandlerMessage handlerMessage = new HandlerMessage(context);
               /* String[] FileName = new String[PictureNumber];
                for (int i = 0; i < PictureNumber; i++) {
                    FileName[i] = ((ArrayList<Object>) arguments.get(i)).get(1).toString();
                    File fileDir = new File(((ArrayList<Object>) arguments.get(i)).get(2).toString());
                    if (!fileDir.exists()) {
                        fileDir.mkdir();
                    }
                }
                //this.setPictureDow(FileName);
                for (int i = 0; i < PictureNumber; i++) {

                }*/
                handlerMessage.downloadPictureTask(context,(ArrayList<String>) arguments.get(0));
                //handlerMessage.downloadPictureTask(context,(ArrayList<String>) arguments.get(0));
                String s = "[{\"FileHttpUrl\":\"\",\"SavePath\":\"\",\"FileNmae\":\"\"}] ";
//                        "[{\"FileHttpUrl\":\""+arguments.get(0)+"\",\"SavePath\":\""+arguments.get(1)+"\",\"FileNmae\":\""+arguments.get(2)+"\"}]";
                Toast.makeText(context,String.valueOf(context+"\n"+(ArrayList<String>) arguments.get(0)+"\n"+s), Toast.LENGTH_SHORT).show();
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
    }



    private void setPictureDow(final String[] name) {
        if (lpparam.packageName.equals(AppArguments.PfPackage)) {
            XposedHelpers.findAndHookMethod(AppArguments.PfHttpFileService,
                    lpparam.classLoader,
                    "XposedDownload",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            if (isPictureDow) {
                                String result = (String) param.getResult();
                                if (result.equals("下载失败！")) {
                                    Toast.makeText(context,result, Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < name.length; i++) {
                                        if (result == name[i]) {
                                            PictureDowNumber = PictureDowNumber + 1;
                                        }
                                        if (PictureDowNumber == name.length) {
                                            isPictureDow = false;
                                            Sns();
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void Sns() {
        Toast.makeText(context,String.valueOf("图片下载完成"), Toast.LENGTH_SHORT).show();
    }

    private Runnable runnable = new Runnable() {
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
