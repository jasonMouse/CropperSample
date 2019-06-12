package com.example.croppersample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.croppersample.constants.ConstantValues;
import com.example.croppersample.utils.Glide4Engine;
import com.example.croppersample.utils.NoGifFilter;
import com.example.croppersample.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.io.File;
import java.util.List;

/**
 * 校验图片大小的页面
 */
public class PictureCheckActivity extends AppCompatActivity {
    private ImageView ivSelectPictureTitleBack;             // 左上角回退按钮
    private LinearLayout llSelectPicture;                   // 图片选择器
    private TextView tvContent;

    private View.OnClickListener onListener;                // 点击事件监听器
    private Context context;                                // 上下文对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PictureCheckActivity.this;
        setContentView(R.layout.activity_picture_check);
        // 调整头顶状态栏
        ImmersionBar.with(this).statusBarColor(R.color.color_ffffff).statusBarDarkFont(true).statusBarView(R.id.vStatus).init();
        // 绑定所有视图
        initView();
        // 初始化监听器
        initListener();
    }

    // 绑定所有视图
    private void initView() {
        ivSelectPictureTitleBack = (ImageView) this.findViewById(R.id.ivSelectPictureTitleBack);
        llSelectPicture = (LinearLayout) this.findViewById(R.id.llSelectPicture);
        tvContent = (TextView) this.findViewById(R.id.tvContent);
    }

    // 初始化监听器
    private void initListener() {
        onListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // 回退事件
                    case R.id.ivSelectPictureTitleBack:
                        finish();
                        break;
                    // 选择图片
                    case R.id.llSelectPicture:
                        Matisse.from((Activity) context)
                                .choose(MimeType.ofImage(), true)//默认为true:表示不能同时选择2种类型文件,false表示可选择2种类型文件
                                .showSingleMediaType(true)
                                .countable(false)//是否显示选中图片时右上角数组
                                .capture(true)//是否显示拍一张功能
                                .captureStrategy(new CaptureStrategy(true, "com.example.croppersample.fileprovider", "test"))//拍照后图片存储地址
                                .maxSelectable(1)//图片可选择张数
                                .addFilter(new NoGifFilter())
                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.s_120dp))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .thumbnailScale(1f)//缩略图的比例
                                .imageEngine(new Glide4Engine())    // for glide-V4
                                .setOnSelectedListener(new OnSelectedListener() {
                                    @Override
                                    public void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                        // DO SOMETHING IMMEDIATELY HERE
                                    }
                                })
                                .originalEnable(true)
                                .maxOriginalSize(10)
                                .autoHideToolbarOnSingleTap(true)
                                .forResult(ConstantValues.REQUEST_PICTURE_CODE);
                        break;
                }
            }
        };
        ivSelectPictureTitleBack.setOnClickListener(onListener);
        llSelectPicture.setOnClickListener(onListener);
    }

    /**
     * 主要是添加图片请求的ActivityResult事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 跳转到其他页面获取图片请求code
                case ConstantValues.REQUEST_PICTURE_CODE:
                    List<String> imgPaths = Matisse.obtainPathResult(data);
                    if (imgPaths != null && imgPaths.size() > 0) {
                        String path = imgPaths.get(0);
                        File file = new File(path);
                        if (file.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            if (bitmap != null) {
                                if (bitmap.getWidth() >= 80 && bitmap.getHeight() >= 80) {
                                    Intent intent = new Intent(context, PictureEditActivity.class);
                                    intent.putExtra("data", path);
                                    context.startActivity(intent);
                                } else {
                                    ToastUtil.shortMsg(context, context.getResources().getString(R.string.str_picture_size_too_small));
                                }
                                // 这个bitmap也就用来测算一下高度
                                bitmap.recycle();
                            } else {
                                ToastUtil.shortMsg(context, context.getResources().getString(R.string.str_picture_not_exist));
                            }
                        } else {
                            ToastUtil.shortMsg(context, context.getResources().getString(R.string.str_picture_not_exist));
                        }
                    }
                    break;
            }
        }
    }
}
