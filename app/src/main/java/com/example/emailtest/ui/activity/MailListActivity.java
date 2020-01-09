package com.example.emailtest.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;


import com.example.emailtest.R;
import com.example.emailtest.adapter.MailListAdapter;
import com.example.emailtest.db.MailService;
import com.example.emailtest.ui.activity.write.WriteEmailActivity;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.HtmlFormatUtil;
import com.example.emailtest.utils.MailReceiver;
import com.example.emailtest.utils.SPUtils;
import com.example.emailtest.utils.Stack;
import com.example.emailtest.email.xlistview.XListView;
import com.sun.mail.pop3.POP3Folder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 * 收件箱
 */
public class MailListActivity extends Activity
        implements XListView.IXListViewListener,
        OnClickListener {

    private XListView mailListView;
    private List<MailBrief> list = new ArrayList<MailBrief>();
    private MailListAdapter mailListAdapter;
    private User user;
    private ProgressDialog dialog;
    private ImageView imageBack;
    private ImageView imageEdit;

    private MailService mailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);

        Stack.addActivity(this);

        mailService = new MailService(this);

        user = (User) getIntent().getSerializableExtra("user");

        imageBack = (ImageView) findViewById(R.id.image_back);
        imageBack.setOnClickListener(this);
        imageEdit = (ImageView) findViewById(R.id.image_edit);
        imageEdit.setOnClickListener(this);
        mailListView = (XListView) findViewById(R.id.mail_list);
        mailListView.setPullRefreshEnable(true);
        mailListView.setPullLoadEnable(true);
        mailListView.setXListViewListener(this);

        list = mailService.findMailByUserId(user.getId() + "");
        mailListAdapter = new MailListAdapter(this, list, user);
        mailListView.setAdapter(mailListAdapter);
        mailListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MailListActivity.this, MailDetailActivity.class);
                intent.putExtra("user", user);
//				intent.putExtra("position", String.valueOf(position-1));
                intent.putExtra("position", list.get(position - 1).getMindex());
                intent.putExtra("mailsubject", list.get(position - 1).getSubject());
                intent.putExtra("mailfrom", list.get(position - 1).getFromPersonal());
                intent.putExtra("mailTime", list.get(position - 1).getSendTime());
                intent.putExtra("mailTo", list.get(position - 1).getTOO());
                intent.putExtra("mailCC", list.get(position - 1).getCC());
                intent.putExtra("mailBCC", list.get(position - 1).getBCC());
                intent.putExtra("id", list.get(position - 1).getId());
                startActivity(intent);
                SPUtils.putValue(MailListActivity.this, user.getUsername(),
                        list.get(position - 1).getSubject() + list.get(position - 1).getSendTime(), true);
                mailListAdapter.notifyDataSetChanged();
            }
        });

        if (list.size() == 0) {
            new GetMailListTask().execute(user.getUsername(), user.getPwd());
        }

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        list.clear();
        mailService.deleteMailByUserId(user.getId() + "");
        new GetMailListTask().execute(user.getUsername(), user.getPwd());
        onLoad();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        onLoad();
    }

    private void onLoad() {
        mailListView.stopRefresh();
        mailListView.stopLoadMore();
        mailListView.setRefreshTime("刚刚");
    }

    class GetMailListTask extends AsyncTask<String, Void, List<MailBrief>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MailListActivity.this);
            dialog.setMessage("加载中...");
            dialog.show();
        }

        @Override
        protected List<MailBrief> doInBackground(String... arg0) {
            String[] mailAddress = arg0[0].split("@");
            String host = "pop." + mailAddress[1];
            List<MailBrief> mmList = new ArrayList<MailBrief>();
            Properties prop = new Properties();
            Store store = null;
            Session session = null;
            Message[] message = {};
            POP3Folder folder = null;
            try {
                if (host.equals("pop.qq.com")) {
                    prop.setProperty("mail.store.protocol", "pop3s");
                    prop.setProperty("mail.pop3s.host", host);
                    session = Session.getInstance(prop);
                    store = session.getStore("pop3s");

                } else {
                    prop.setProperty("mail.store.protocol", "pop3");
                    prop.setProperty("mail.pop3.host", host);
                    session = Session.getInstance(prop);
                    session.setDebug(true);
                    store = session.getStore("pop3");
                }
                store.connect(arg0[0], arg0[1]);
                folder = (POP3Folder) store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                message = folder.getMessages();
            } catch (NoSuchProviderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int count = message.length - 1;

            for (int i = 0; i < message.length; i++) {
                MailBrief mb = new MailBrief();
                MimeMessage mimeMessage = (MimeMessage) message[i];
                MailReceiver mr = new MailReceiver(mimeMessage);

                try {
                    mb.setFromPersonal(mr.getFrom());
                    mb.setSubject(mr.getSubject());
                    mb.setSendTime(mr.getSentData());

                    /**
                     * 做一个摘要内容的截取、如果放在adapter中会导致卡顿问题 但是放在这里会导致 加载中..页面时间延长
                     */
                    String mailContent = mr.getMailContent();
                    String formatMailContent = null;
                    if (mailContent != null) {
                        formatMailContent = HtmlFormatUtil.getTextFromHtml(mailContent);
                        if (formatMailContent.length() >= 40) {
                            formatMailContent = formatMailContent.substring(0, 40);
                        }
                    } else {
                        formatMailContent = "";
                    }

                    mb.setMindex(count + "");
                    mb.setMailContent(formatMailContent);
                    mb.setMailType("7");
                    mb.setUserId(user.getId() + "");
                    mb.setTOO(mr.getMailAddress("TO"));
                    mb.setCC(mr.getMailAddress("CC"));
                    mb.setBCC(mr.getMailAddress("BCC"));
                    mb.setMalieId(mr.getMessageID());
                    count--;
                } catch (Exception e) {
                    mb.setFromPersonal("未获取到发件人");
                    mb.setSubject(mr.getSubject());
                    try {
                        mb.setSendTime(mr.getSentData());
                    } catch (MessagingException e1) {
                        mb.setSendTime("未获取到发送日期");
                        Log.e("zcj", e1.toString());
                        e1.printStackTrace();
                    }
                    Log.e("zcj", "" + i + e.toString());
                    e.printStackTrace();
                }
                mailService.addMail(mb);
                mmList.add(mb);
            }


            try {
                if (folder != null) {
                    folder.close(true);
                }
                if (store != null) {
                    store.close();
                }

            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return mmList;

        }

        @Override
        protected void onPostExecute(List<MailBrief> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.cancel();
            list.clear();
            list.addAll(result);
            Collections.reverse(list);
            mailListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back: {
                MailListActivity.this.finish();
            }
            break;
            case R.id.image_edit: {
                Intent intent = new Intent(MailListActivity.this, WriteEmailActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            break;
            default:
                break;
        }

    }

}
