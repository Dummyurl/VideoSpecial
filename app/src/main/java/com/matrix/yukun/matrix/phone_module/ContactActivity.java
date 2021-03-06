package com.matrix.yukun.matrix.phone_module;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.MainActivity;
import com.matrix.yukun.matrix.main_module.filters.Image;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.entity.SortModel;
import com.matrix.yukun.matrix.video_module.utils.Cn2Spell;
import com.matrix.yukun.matrix.video_module.utils.ISideBarSelectCallBack;
import com.matrix.yukun.matrix.video_module.utils.PinyinComparator;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.matrix.yukun.matrix.video_module.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactActivity extends BaseActivity implements View.OnClickListener, ISideBarSelectCallBack {

    private ImageView mIvBack;
    private TextView mTvContact;
    private ListView mRvList;
    private List<PhoneNumberBean> mPhoneNumberBeans;
    private List<PhoneBean> mPhoneBeans=new ArrayList<>();
    private RVContactAdapter mRvContactAdapter;
    private SideBar mSideBar;

    @Override
    public int getLayout() {
        return R.layout.activity_contact;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mTvContact = findViewById(R.id.tv_contact);
        mRvList = findViewById(R.id.rv_phone_list);
        mSideBar = findViewById(R.id.sidebar);
        mRvContactAdapter = new RVContactAdapter(this,mPhoneBeans);
        mRvList.setAdapter(mRvContactAdapter);
    }

    @Override
    public void initDate() {
        getPermission();
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvContact.setOnClickListener(this);
        mSideBar.setOnStrSelectCallBack(this);
        //dialog
        mRvContactAdapter.setMoreCallBack(new RVContactAdapter.MoreCallBack() {
            @Override
            public void onMoreCallBack(int pos, PhoneNumberBean phoneNumberBean) {
                PhoneDialog phoneDialog=PhoneDialog.getInstance(phoneNumberBean.getPhoneNum(),phoneNumberBean.contactName);
                phoneDialog.show(getSupportFragmentManager(),"");
            }
        });
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(ContactActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }else{
            mPhoneNumberBeans = queryContactPhoneNumber();
            mPhoneBeans.addAll(getSortModule());
            mRvContactAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //6.0权限访问
                    mPhoneNumberBeans = queryContactPhoneNumber();
                    mPhoneBeans.addAll(getSortModule());
                    mRvContactAdapter.notifyDataSetChanged();
                } else { //权限被拒绝
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onSelectStr(int index, String selectStr) {
        if(mPhoneBeans==null){
            return;
        }
        for (int i = 0; i < mPhoneBeans.size() ; i++){
            if(mPhoneBeans.get(i).getSortLetters().equals(selectStr)){
                mRvList.setSelection(i);
                return;
            }
        }
    }

    @Override
    public void onSelectEnd() {

    }

    @Override
    public void onSelectStart() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_contact:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Contacts.People.CONTENT_URI);
                startActivity(intent);
                break;
        }
    }

    private List<PhoneNumberBean> queryContactPhoneNumber() {
        List<PhoneNumberBean> list=new ArrayList<>();
        String[] cols = {
                ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String number = cursor.getString(numberFieldColumnIndex);
            PhoneNumberBean phoneNumberBean=new PhoneNumberBean(name,number);
            list.add(phoneNumberBean);
        }
        return list;
    }

    private List<PhoneBean> getSortModule() {
        List<PhoneBean> filterDateList = new ArrayList<>();
        for (int i = 0; i < mPhoneNumberBeans.size(); i++) {
            String pinYinFirstLetter = Cn2Spell.getPinYinFirstLetter(mPhoneNumberBeans.get(i).contactName);
            PhoneBean sortModel = new PhoneBean(mPhoneNumberBeans.get(i),pinYinFirstLetter.toUpperCase().charAt(0) + "");
            filterDateList.add(sortModel);
        }
        Collections.sort(filterDateList, new PinyinPhoneComparator());
        return filterDateList;
    }

}
