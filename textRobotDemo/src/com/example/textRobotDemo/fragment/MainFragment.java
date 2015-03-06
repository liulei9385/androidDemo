package com.example.textRobotDemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.textRobotDemo.R;
import com.example.textRobotDemo.adapter.ChatMessageAdapter;
import com.example.textRobotDemo.bean.ChatMessage;
import com.example.textRobotDemo.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * USER: liulei
 * DATA: 2015/1/28
 * TIME: 11:27
 */
public class MainFragment extends BaseFragment {

    private Toolbar toolbar;

    private ListView mChatView;
    private EditText mMsg;
    private Button chat_send_btn;
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
    private ChatMessageAdapter mAdapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ChatMessage from = (ChatMessage) msg.obj;
            mDatas.add(from);
            mAdapter.notifyDataSetChanged();
            mChatView.setSelection(mDatas.size() - 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main, container, false);

        initView(root);
        mAdapter = new ChatMessageAdapter(activity, mDatas);
        mChatView.setAdapter(mAdapter);

        return root;
    }

    private void initView(View view) {

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        ((ActionBarActivity) activity).setSupportActionBar(toolbar);

        mChatView = (ListView) view.findViewById(R.id.id_chat_listView);

        chat_send_btn = (Button) view.findViewById(R.id.id_chat_send);
        chat_send_btn.setOnClickListener(clickListener);

        mMsg = (EditText) view.findViewById(R.id.id_chat_msg);
        mDatas.add(new ChatMessage(ChatMessage.Type.INPUT, "我是小貅貅，很高兴为您服务"));


    }

    public void sendMessage() {
        final String msg = mMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(activity, "您还没有填写信息呢...", Toast.LENGTH_SHORT).show();
            return;
        }
        ChatMessage to = new ChatMessage(ChatMessage.Type.OUTPUT, msg);
        to.setDate(new Date());
        mDatas.add(to);
        mAdapter.notifyDataSetChanged();
        mChatView.setSelection(mDatas.size() - 1);
        mMsg.setText("");
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
        new Thread() {
            public void run() {
                ChatMessage from;
                try {
                    from = HttpUtils.sendMsg(msg);
                } catch (Exception e) {
                    from = new ChatMessage(ChatMessage.Type.INPUT, "服务器挂了呢...");
                }
                Message message = Message.obtain();
                message.obj = from;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == chat_send_btn)
                sendMessage();
        }
    };
}
