package com.matrix.yukun.matrix.video_module.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.fragment.BaseCardFragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard1Fragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard2Fragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard3Fragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard4Fragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard5Fragment;
import com.matrix.yukun.matrix.video_module.fragment.ShareCard6Fragment;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvBack;
    private TextView mTvSave;
    private TextView mTvShare;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private ShareViewPagerAdapter mShareViewPagerAdapter;
    private int pagePos;
    private RadioGroup mRadioGroup;

    @Override
    public int getLayout() {
        return R.layout.activity_setting2;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvSave = findViewById(R.id.tv_save);
        mTvShare = findViewById(R.id.tv_share);
        mViewPager = findViewById(R.id.vp_card);
        mRadioGroup = findViewById(R.id.rg_layout);
    }

    @Override
    public void initListener() {
        ((RadioButton)mRadioGroup.getChildAt(0)).setChecked(true);
        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                pagePos=position;
                ((RadioButton)mRadioGroup.getChildAt(position)).setChecked(true);
                super.onPageSelected(position);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
    }

    @Override
    public void initDate() {
        mFragmentList.add(ShareCard1Fragment.getInstance());
        mFragmentList.add(ShareCard2Fragment.getInstance());
        mFragmentList.add(ShareCard3Fragment.getInstance());
        mFragmentList.add(ShareCard4Fragment.getInstance());
        mFragmentList.add(ShareCard5Fragment.getInstance());
        mFragmentList.add(ShareCard6Fragment.getInstance());
        mShareViewPagerAdapter = new ShareViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mShareViewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                ((BaseCardFragment)mFragmentList.get(pagePos)).saveCardView();
                ToastUtils.showToast("卡片已经保存到存储卡");
                break;
            case R.id.tv_share:
                ToastUtils.showToast("分享正在后台启动");
                ((BaseCardFragment)mFragmentList.get(pagePos)).saveCardView();
                String imagePath = ((BaseCardFragment) mFragmentList.get(pagePos)).getImagePath();
                ShareDialog shareDialog = ShareDialog.getImageInstance(imagePath, AppConstants.APP_STORE);
                shareDialog.show(getSupportFragmentManager(),"");
                break;
        }
    }

    class ShareViewPagerAdapter extends FragmentPagerAdapter {
        public ShareViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
    }
}
