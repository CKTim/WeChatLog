package com.cxk.wechatlog;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * Created by cxk on 2017/2/4.
 * email:471497226@qq.com
 * <p>
 * 获取即时微信聊天记录服务类
 */

public class WeChatLogService extends AccessibilityService {

    /**
     * 聊天对象
     */
    private String ChatName;
    /**
     * 聊天最新一条记录
     */
    private String ChatRecord = "test";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            //每次在聊天界面中有新消息到来时都出触发该事件
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                //获取当前聊天页面的根布局
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                //获取聊天信息
                getWeChatLog(rootNode);
                break;
        }

    }

    /**
     * 遍历
     *
     * @param rootNode
     */

    private void getWeChatLog(AccessibilityNodeInfo rootNode) {
        if (rootNode != null) {
            //获取所有聊天的线性布局
            List<AccessibilityNodeInfo> listChatRecord = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/o");
            if(listChatRecord.size()==0){
                return;
            }
            //获取最后一行聊天的线性布局（即是最新的那条消息）
            AccessibilityNodeInfo finalNode = listChatRecord.get(listChatRecord.size() - 1);
            //获取聊天对象list（其实只有size为1）
            List<AccessibilityNodeInfo> imageName = finalNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/i_");
            //获取聊天信息list（其实只有size为1）
            List<AccessibilityNodeInfo> record = finalNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ib");
            if (imageName.size() != 0) {
                if (record.size() == 0) {
                    //判断当前这条消息是不是和上一条一样，防止重复
                    if (!ChatRecord.equals("对方发的是图片或者表情")) {
                        //获取聊天对象
                        ChatName = imageName.get(0).getContentDescription().toString().replace("头像", "");
                        //获取聊天信息
                        ChatRecord = "对方发的是图片或者表情";

                        Log.e("AAAA", ChatName + "：" + "对方发的是图片或者表情");
                        Toast.makeText(this, ChatName + "：" + ChatRecord, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //判断当前这条消息是不是和上一条一样，防止重复
                    if (!ChatRecord.equals(record.get(0).getText().toString())) {
                        //获取聊天对象
                        ChatName = imageName.get(0).getContentDescription().toString().replace("头像", "");
                        //获取聊天信息
                        ChatRecord = record.get(0).getText().toString();

                        Log.e("AAAA", ChatName + "：" + ChatRecord);
                        Toast.makeText(this, ChatName + "：" + ChatRecord, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "我快被终结了啊-----", Toast.LENGTH_SHORT).show();
    }

    /**
     * 服务开始连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        super.onServiceConnected();
    }

    /**
     * 服务断开
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "服务已被关闭", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}
