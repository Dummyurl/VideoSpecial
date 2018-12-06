package com.matrix.yukun.matrix.video_module.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.gesture_module.GestureActivity;
import com.matrix.yukun.matrix.setting_module.AgreeActivity;
import com.matrix.yukun.matrix.setting_module.FankuiDialog;
import com.matrix.yukun.matrix.setting_module.IntroduceActivity;
import com.matrix.yukun.matrix.setting_module.SettingActivity;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.play.ChatActivity;
import com.matrix.yukun.matrix.video_module.play.SettingAboutActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.matrix.yukun.matrix.video_module.views.NoScrolledListView;
import com.tencent.bugly.beta.Beta;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class AboutUsFragment extends BaseFragment {

    @BindView(R2.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R2.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R2.id.tv_version)
    TextView mTvVersion;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.lv_list)
    NoScrolledListView mLvList;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    List<String> mStringList=new ArrayList();

    public static AboutUsFragment getInstance(){
        return new AboutUsFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mTvVersion.setText("V " + getVersion());
        startAmim(mIvIcon);
        mStringList= Arrays.asList(getResources().getStringArray(R.array.about_us_list));
        mLvList.setAdapter(new LvAdapter());
        initListener();
    }

    private void initListener() {
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Class> classes = new ArrayList<>();
                Class aClass = null;
                switch (position) {
                    case 0:
                        aClass = ChatActivity.class;
                        break;
                    case 1:
                        aClass = GestureActivity.class;
                        break;
                    case 2:
                        aClass = AgreeActivity.class;
                        break;
                    case 3:
                        aClass = IntroduceActivity.class;
                        break;
                    case 4:
                        FankuiDialog noteCommentDialog = FankuiDialog.newInstance(0);
                        noteCommentDialog.show(getChildFragmentManager(), "NoteDetailActivity");
                        break;
                    case 5:
                        Beta.checkUpgrade();
                        break;
                    case 6:
                        ToastUtils.showToast("版本号：V"+getVersion());
                        break;
                }
                if (aClass != null) {
                    Intent intent = new Intent(getContext(), aClass);
                    startActivity(intent);
                }
            }
        });

        mIvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SettingAboutActivity.class);
                startActivity(intent);
            }
        });
        //share
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = ShareDialog.getInstance(getString(R.string.share_content), AppConstants.APP_STORE, AppConstants.APP_ICON_URl);
                shareDialog.show(getChildFragmentManager(),"");
            }
        });
    }

    private String getVersion() {
        String mVersionName=null;
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            mVersionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    private void startAmim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", 360, 0);
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    class LvAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mStringList.size();
        }

        @Override
        public Object getItem(int position) {
            return mStringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.setting_item_layout, null);
            TextView textView=inflate.findViewById(R.id.setting_con);
            TextView tvBottom=inflate.findViewById(R.id.tv_bottom);
            textView.setText(mStringList.get(position));
            if(position==1){
                tvBottom.setVisibility(View.VISIBLE);
            }
            return inflate;
        }
    }
}
