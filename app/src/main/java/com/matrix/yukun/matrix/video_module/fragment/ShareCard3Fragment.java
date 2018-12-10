package com.matrix.yukun.matrix.video_module.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

/**
 * author: kun .
 * date:   On 2018/12/10
 */
public class ShareCard3Fragment extends BaseCardFragment {
    CardView mCardView;
    private String imageName="card_3.png";
    public static ShareCard3Fragment getInstance(){
        ShareCard3Fragment shareCard1Fragment=new ShareCard3Fragment();
        return shareCard1Fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.card_share_3_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mCardView = inflate.findViewById(R.id.cardview);
    }

    @Override
    public void saveCardView(){
        Bitmap bitmap = ImageUtils.createViewBitmap(mCardView, mCardView.getWidth(), mCardView.getHeight());
        FileUtil.loadImage(bitmap,imageName);
    }

    @Override
    public String getImagePath() {
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ AppConstants.PATH+"/"+imageName;
        return path;
    }
}
