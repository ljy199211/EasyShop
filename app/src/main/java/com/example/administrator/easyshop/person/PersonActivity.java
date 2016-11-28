package com.example.administrator.easyshop.person;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.easyshop.MainActivity;
import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.components.AvatarLoadOptions;
import com.example.administrator.easyshop.components.PicWindow;
import com.example.administrator.easyshop.components.ProgressDialogFragment;
import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.model.ItemShow;
import com.example.administrator.easyshop.model.User;
import com.example.administrator.easyshop.network.EasyShopApi;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends MvpActivity<PersonView,PersonPresenter> implements PersonView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_user_head)
    CircularImageView ivUserHead;
    @BindView(R.id.listView)
    ListView listView;
    private List<ItemShow> list = new ArrayList<>();
    private ActivityUtils activityUtils;
    private PersonAdapter adapter;
    private ProgressDialogFragment progressDialogFragment;
    private PicWindow picWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new PersonAdapter(list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(itemClickListener);
        //获取头像
        updataAvatar(CachePreferences.getUser().getHead_Image());
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow(getResources().getString(R.string.username),user.getName()));
        list.add(new ItemShow(getResources().getString(R.string.nickname),user.getNick_name()));
        list.add(new ItemShow(getResources().getString(R.string.hx_id),user.getHx_Id()));
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    activityUtils.showToast(getResources().getString(R.string.username_update));
                    break;
                case 1:
                    activityUtils.startActivity(NickNameActivity.class);
                    break;
                case 2:
                    activityUtils.showToast(getResources().getString(R.string.id_update));
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPrb() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(),"progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void updataAvatar(String url) {
        //设置头像
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL+url,ivUserHead, AvatarLoadOptions.build());
    }
    @OnClick({R.id.btn_login_out,R.id.iv_user_head})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_user_head:
                if (picWindow == null){
                    picWindow = new PicWindow(PersonActivity.this,listener);
                }
                if (picWindow.isShowing()){
                    picWindow.dismiss();
                    return;
                }
                picWindow.show();
                break;
            case R.id.btn_login_out:
                // TODO: 2016/11/23 0023 环信的退出登录
                //清空本地配置
                CachePreferences.clearAllData();
                Intent intent = new Intent(this, MainActivity.class);
                //清除所有旧的activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
    }
    private PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent,CropHelper.REQUEST_CROP);

        }

        @Override
        public void toCamera() {
            //从相机中选择
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent,CropHelper.REQUEST_CAMERA);

        }
    };

    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            //业务类上传头像
            presenter.updataAvatar(file);
        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {

        }

        @Override
        public CropParams getCropParams() {
            //自定义裁剪大小参数
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return PersonActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //帮助我们去处理结果（裁剪完的图像）
        CropHelper.handleResult(cropHandler,requestCode, resultCode, data);
    }
}
