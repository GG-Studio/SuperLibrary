package android.mf.application.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CommandManager {
    private Context context = null;
    private ArrayList<String> cmd = null;
    public CommandManager(Context context) {
        if(hasRootPerssion()) {
            this.context = context;
        }
    }

    public boolean isROOT() {
        return hasRootPerssion();
    }

    public boolean installApkCMD(String apkPath) {
        if(context != null) {
            cmd = new ArrayList<>();
            cmd.add("pm install -r "+apkPath);
            return executeCommand(cmd);
        } else return false;
    }

    public boolean uninstallApkCMD(String packageName){
        if(context != null) {
            cmd = new ArrayList<>();
            cmd.add("pm uninstall "+packageName);
            return executeCommand(cmd);
        } else return false;
    }

    /**
     * 判断手机是否有root权限
     */
    public boolean hasRootPerssion(){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    private static boolean returnResult(int value){
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    public boolean executeCommand(ArrayList<String> hashMap) {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            for (int i = 0; i < hashMap.size(); i++) {
                dataOutputStream.writeBytes(hashMap.get(i) + "\n");
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
            if (process.exitValue() == 0) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
