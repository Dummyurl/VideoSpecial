package com.matrix.yukun.matrix.video_module.play;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SettingAboutActivity extends BaseActivity {


    private ImageView mIvBack;
    private ListView mListView;
    private Handler mHandler=new Handler();

    @Override
    public int getLayout() {
        return R.layout.activity_setting2;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mListView = findViewById(R.id.lv_list);
        mListView.setAdapter(new ListAdapter());
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        String imageDisplayname=System.currentTimeMillis()+".png";
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_erweima );;
                        FileUtil.loadImage(bitmap,imageDisplayname);
                        shareImage(bitmap,imageDisplayname );
                        break;
                    case 1:
                        Uri uri = Uri.parse(AppConstants.APP_STORE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
//                        try {
//                            Intent i = new Intent(Intent.ACTION_VIEW);
//                            i.setData(Uri.parse("market://search?q="+getResources().getString(R.string.app_name)));
//                            startActivity(i);
//                        } catch (Exception e) {
//                            ToastUtils.showToast("您的手机没有安装Android应用市场");
//                            e.printStackTrace();
//                        }
                        break;
                }
            }
        });
    }

    private void shareImage(final Bitmap bitmap, final String photoName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File destDir= FileUtil.createFile();
                FileUtil.loadImage(bitmap,photoName);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = FileUtil.shareMsg(destDir+"/"+photoName);
                        startActivity(Intent.createChooser(intent, "分享图片"));
                    }
                });
            }
        }).start();

    }

    class ListAdapter extends BaseAdapter{
        List<String> mStrings;
        public ListAdapter() {
            mStrings= Arrays.asList(getResources().getStringArray(R.array.setting_adout));
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(SettingAboutActivity.this).inflate(R.layout.setting_item_layout, null);
            TextView textView=convertView.findViewById(R.id.setting_con);
            textView.setText(mStrings.get(position));
            return convertView;
        }
    }
}
