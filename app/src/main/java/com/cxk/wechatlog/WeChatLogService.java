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
    private String ChatRecord = "cxk";

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
     * 遍历所有控件获取聊天信息
     *
     * @param rootNode
     */

    private void getWeChatLog(AccessibilityNodeInfo rootNode) {
        if (rootNode != null) {
            //获取所有聊天的线性布局
            List<AccessibilityNodeInfo> listChatRecord = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/p");
            if (listChatRecord.size() == 0) {
                return;
            }
            //获取最后一行聊天的线性布局（即是最新的那条消息）
            AccessibilityNodeInfo finalNode = listChatRecord.get(listChatRecord.size() - 1);
            //获取聊天对象
            GetChatName(finalNode);
            //获取聊天内容
            GetChatRecord(finalNode);
        }
    }

    /**
     * 遍历所有控件，找到联系人，头像Imagview里面有
     */
    private void GetChatName(AccessibilityNodeInfo node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo node1 = node.getChild(i);
            if ("android.widget.ImageView".equals(node1.getClassName()) && node1.isClickable()) {
                //判断是否是重复消息
                ChatName = node1.getContentDescription().toString().replace("头像", "");
            }
            GetChatName(node1);
        }
    }

    /**
     * 遍历所有控件，找到TextView，因为里面只有一个Textview装载着聊天信息
     *
     * @param node
     */
    public void GetChatRecord(AccessibilityNodeInfo node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo node1 = node.getChild(i);
            if ("android.widget.TextView".equals(node1.getClassName())) {
                String chatRecord1 = node1.getText().toString();
                //检查下是不是重复消息，防止多次显示
                if (!TextUtils.isEmpty(chatRecord1)) {
                    if (!chatRecord1.equals(ChatRecord)) {
                        ChatRecord = chatRecord1;
                        Toast.makeText(this, ChatName + ":" + ChatRecord, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                GetChatRecord(node1);
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
