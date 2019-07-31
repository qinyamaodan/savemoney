package com.example.savemoney.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 作者：zyy
 * 日期：2018/8/29
 * 作用：利用baseActivity抽象重复内容
 * 组件绑定、事件跳转、窗口管理（横竖屏。沉浸式，Theme）、生命周期这三个是每个Activity必有的
 * 注：可以继承Activity，或者FragmentActivty或者AppCompatActivity，但是现在都是Activity+Fragment模式开发 所以推荐用AppCompatActivity ，FragmentActivty不支持tooslBar….）
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private boolean isDebug;    //是否输出日志信息
    private boolean isSetStatusBar = false;    //是否沉浸状态栏
    private boolean isAllowFullScreen = false;    //是否允许全屏
    private boolean isAllowScreenRotate = true;    //是否禁止旋转屏幕
    protected BaseActivity BaseContext;    //Context
    private ScreenManager screenManager;
    private static Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Java的执行顺序有关！目前遇到的这个问题就是因为在系统运行开始的时候就已经调用父类的构造方法，接着调用setContentView方法展示视图界面。
         * R.layout.main是R.java资源类中的一个属性。当你在调用这个方法之后在声明Widget就会报：
         * android.util.AndroidRuntimeException: requestFeature() must be called before adding content
         * 解决办法：把screenManager移到initView()前面
         */
        BaseContext = this;

//        ActivityStackManager.getInstance().addActivity(this);//把当前Activity入站
        ActivityStackManager.getActivityStackManager().pushActivity(this);
        screenManager = ScreenManager.getInstance();
        screenManager.setStatusBar(isSetStatusBar,BaseContext);
        screenManager.setFullScreen(isAllowFullScreen,BaseContext);
        screenManager.setScreenRotate(isAllowScreenRotate,BaseContext);
        initView(savedInstanceState);
        initData();
        initEvent();
    }

    protected abstract void initView(Bundle savedInstanceState) ;    //初始化界面
    protected abstract void initData() ;    //初始化数据
    protected abstract void initEvent() ;    //控件监听

    //跳过当前Activity
    public static void skipAnotherActivity(Activity activity, Class<? extends Activity> cls){
        Intent intent = new Intent(activity,cls);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void toNextActivity(Bundle bundle,Activity activity, Class<? extends Activity> cls){
        if (null != bundle){
            intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(activity,cls);
        }else {
            intent = new Intent(activity,cls);
        }
            activity.startActivity(intent);
    }

    //退出应用
    public void exitLogic(){
        ActivityStackManager.getActivityStackManager().popAllActivity();//remove all activities
        System.exit(0); //system exit 0 表示正常退出程序，1表示非正常退出程序
    }

    //是否设置沉浸状态栏
    public void setStatusBar(boolean statusBar){
        this.isSetStatusBar = statusBar;
    }

    //是否设置全屏
    public void setFullScreen(boolean fullScreen){
        this.isAllowFullScreen = fullScreen;
    }

    //是否设置屏幕旋转
    public void setScreenRotate(boolean screenRotate){
        this.isAllowScreenRotate = screenRotate;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "---->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "---->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "---->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "---->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "---->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "---->onDestroy()");
//        ActivityStackManager.getInstance().removeActivity(this);
        ActivityStackManager.getActivityStackManager().popActivity(this);
    }

}
