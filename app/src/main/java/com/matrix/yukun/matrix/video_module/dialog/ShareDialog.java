package com.matrix.yukun.matrix.video_module.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.barrage_module.dialog.BaseBottomDialog;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * author: kun .
 * date:   On 2018/11/16
 */
public class ShareDialog  extends BaseBottomDialog implements View.OnClickListener {

    private static ShareDialog shareDialog;
    private String mTitle;
    private String mShareUrl;
    private ImageView mIvQQ;
    private ImageView mIvZone;
    private ImageView mIvMore;
    private String mImageUrl;

    public static ShareDialog getInstance(String title, String shareUrl,String imageUrl){

        if(shareDialog==null){
            shareDialog = new ShareDialog();
        }
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        bundle.putString("imageUrl",imageUrl);
        bundle.putString("shareUrl",shareUrl);
        shareDialog.setArguments(bundle);
        return shareDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitle = arguments.getString("title");
        mShareUrl = arguments.getString("shareUrl");
        mImageUrl = arguments.getString("imageUrl");
    }

    @Override
    public int setLayout() {
        return R.layout.share_dialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mIvQQ = inflate.findViewById(R.id.iv_share_qq);
        mIvZone = inflate.findViewById(R.id.iv_share_zone);
        mIvMore = inflate.findViewById(R.id.iv_share_more);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mIvQQ.setOnClickListener(this);
        mIvZone.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share_qq:
                shareToQQ(QQ.NAME);
                break;
            case R.id.iv_share_zone:
                shareToQQ(QZone.NAME);
                break;
            case R.id.iv_share_more:
                shareToMore();
                break;
        }
        getDialog().dismiss();
    }

    private void shareToMore() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, mShareUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(Intent.createChooser(intent, ""));
    }

    private void shareToQQ(String plat) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl(mImageUrl);
        oks.setUrl(mImageUrl);
        oks.setSiteUrl(mImageUrl);
        oks.setTitleUrl(mShareUrl);
        oks.setTitle("Matrix Photo");
        oks.setText(mTitle);
        oks.setPlatform(plat);
        oks.show(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0.6f;
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }
}
