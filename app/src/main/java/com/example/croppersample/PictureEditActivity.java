package com.example.croppersample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edmodo.cropper.CropImageView;
import com.example.croppersample.dialog.EditPictureSizeDialog;
import com.example.croppersample.utils.BitmapUtils;
import com.example.croppersample.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

/**
 * 编辑图片的页面
 */
public class PictureEditActivity extends AppCompatActivity {

    private ImageView ivEditPictureTitle;                   // 主标题上的关闭按钮

    private LinearLayout llEditPictureRoot;                 // 裁剪视图的根布局
    private CropImageView civEditPicture;                   // 主要裁剪和展示内容（onLayout操作会导致整个选择框发生变动）
    private TextView tvEditPictureSize;                     // 裁剪结果的尺寸
    private FrameLayout flCancel;                           // 取消
    private TextView tvCancel;
    private FrameLayout flNationalExam;                     // 国考
    private TextView tvNationalExam;
    private FrameLayout flOneInch;                          // 一寸
    private TextView tvOneInch;
    private FrameLayout flTwoInch;                          // 两寸
    private TextView tvTwoInch;
    private FrameLayout flCustomize;                        // 自定义
    private TextView tvCustomize;
    private FrameLayout flFinish;                           // 完成
    private TextView tvFinish;

    private LinearLayout llCheckPictureRoot;                // 校对视图的根布局
    private FrameLayout flCheckPicture;                     // 校对布局中图片展示的父布局
    private ImageView ivCheckPicture;                       // 校对的图片展示器
    private TextView tvReCut;                               // 重新裁剪
    private TextView tvSaveLocal;                           // 保存到本地

    private Context context;                                // 上下文对象
    private View.OnClickListener onListener;                // 点击事件监听器
    private String originalUrl;                             // 原始图片的存放路径
    private TextView lastSelectTextView;                    // 上一次选中的文本
    private int targetWidth;                                // 最终结果的宽度
    private int targetHeight;                               // 最终结果的高度
    private Bitmap cutResult;                               // 图片裁剪结果

    private int textSelectedBgRes;                          // 处于选中状态下的文本背景
    private int textUnSelectedBgColor;                      // 处于未选中状态下的文本背景颜色
    private int textSelectedTextColor;                      // 处于选中状态下的文本颜色
    private int textUnSelectedTextColor;                    // 处于未选中状态下的文本颜色

    private EditPictureSizeDialog editDialog;               // 底部对话框
    private String strPictureSize;                          // 图片尺寸的格式化器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PictureEditActivity.this;
        setContentView(R.layout.activity_picture_edit);
        // 调整头顶状态栏
        ImmersionBar.with(this).statusBarColor(R.color.color_1c1c1c).fitsSystemWindows(true).statusBarDarkFont(false).statusBarView(R.id.vStatus).init();
        // 绑定所有视图
        bindViews();
        // 初始化数据
        initData();
        // 初始化监听器
        initListener();
        // 初始化传入数据内容
        initBaseData();
    }

    // 绑定所有视图
    private void bindViews() {
        ivEditPictureTitle = (ImageView) findViewById(R.id.ivEditPictureTitle);
        llEditPictureRoot = (LinearLayout) findViewById(R.id.llEditPictureRoot);
        civEditPicture = (CropImageView) findViewById(R.id.civEditPicture);
        tvEditPictureSize = (TextView) findViewById(R.id.tvEditPictureSize);
        flCancel = (FrameLayout) findViewById(R.id.flCancel);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        flNationalExam = (FrameLayout) findViewById(R.id.flNationalExam);
        tvNationalExam = (TextView) findViewById(R.id.tvNationalExam);
        flOneInch = (FrameLayout) findViewById(R.id.flOneInch);
        tvOneInch = (TextView) findViewById(R.id.tvOneInch);
        flTwoInch = (FrameLayout) findViewById(R.id.flTwoInch);
        tvTwoInch = (TextView) findViewById(R.id.tvTwoInch);
        flCustomize = (FrameLayout) findViewById(R.id.flCustomize);
        tvCustomize = (TextView) findViewById(R.id.tvCustomize);
        flFinish = (FrameLayout) findViewById(R.id.flFinish);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        llCheckPictureRoot = (LinearLayout) findViewById(R.id.llCheckPictureRoot);
        flCheckPicture = (FrameLayout) findViewById(R.id.flCheckPicture);
        tvReCut = (TextView) findViewById(R.id.tvReCut);
        tvSaveLocal = (TextView) findViewById(R.id.tvSaveLocal);
    }

    // 初始化数据
    private void initData() {
        textUnSelectedBgColor = Color.TRANSPARENT;
        textUnSelectedTextColor = getResources().getColor(R.color.color_ffffff);
        textSelectedBgRes = R.drawable.shape_bg_white_corner_4dp;
        textSelectedTextColor = getResources().getColor(R.color.color_1c1c1c);

        strPictureSize = context.getResources().getString(R.string.str_picture_size);
    }

    // 初始化监听器
    private void initListener() {
        onListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // 回退事件
                    case R.id.ivEditPictureTitle:
                    case R.id.flCancel:
                        finish();
                        break;
                    // 国考
                    case R.id.flNationalExam:
                        tvEditPictureSize.setText(String.format(strPictureSize, 130, 160));
                        targetWidth = 130;
                        targetHeight = 160;
                        civEditPicture.setAspectRatio(13, 16);
                        civEditPicture.setFixedAspectRatio(true);
                        selectTextView(tvNationalExam);
                        break;
                    // 一寸
                    case R.id.flOneInch:
                        tvEditPictureSize.setText(String.format(strPictureSize, 413, 295));
                        targetWidth = 413;
                        targetHeight = 295;
                        civEditPicture.setAspectRatio(413, 295);
                        civEditPicture.setFixedAspectRatio(true);
                        selectTextView(tvOneInch);
                        break;
                    // 两寸
                    case R.id.flTwoInch:
                        tvEditPictureSize.setText(String.format(strPictureSize, 626, 413));
                        targetWidth = 626;
                        targetHeight = 413;
                        civEditPicture.setAspectRatio(626, 413);
                        civEditPicture.setFixedAspectRatio(true);
                        selectTextView(tvTwoInch);
                        break;
                    // 自定义
                    case R.id.flCustomize:
                        if (editDialog == null) {
                            // 注：此处由于键盘的展出和隐藏会导致整个Activity中所有的View执行onLayout操作，防止这个操作的发生，需要在Manifest.xml中添加 android:windowSoftInputMode="adjustNothing"
                            editDialog = new EditPictureSizeDialog((Activity) context, 0, 0, new EditPictureSizeDialog.OnSizeChangeListener() {
                                @Override
                                public void onConfirm(int width, int height) {
                                    tvEditPictureSize.setText(String.format(strPictureSize, width, height));
                                    targetWidth = width;
                                    targetHeight = height;
                                    civEditPicture.setAspectRatio(width, height);
                                    civEditPicture.setFixedAspectRatio(true);
                                    selectTextView(tvCustomize);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                        editDialog.showDialog();
                        break;
                    // 完成
                    case R.id.flFinish:
                        // 隐藏裁剪布局
                        llEditPictureRoot.setVisibility(View.GONE);
                        // 展示校对布局
                        llCheckPictureRoot.setVisibility(View.VISIBLE);
                        // 获取到裁剪结果并展示
                        Bitmap tempBitmap = civEditPicture.getCroppedResult();
                        cutResult = BitmapUtils.imageScale(tempBitmap, targetWidth, targetHeight);
                        // 删除上次的展示器
                        if (ivCheckPicture != null) {
                            flCheckPicture.removeView(ivCheckPicture);
                            ivCheckPicture = null;
                        }
                        ivCheckPicture = (ImageView) View.inflate(context, R.layout.layout_edit_img, null);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;
                        flCheckPicture.addView(ivCheckPicture, params);
                        Glide.with(context).load(cutResult).into(ivCheckPicture);
                        // 调整头顶状态栏
                        ivEditPictureTitle.setVisibility(View.VISIBLE);
                        break;
                    // 重新裁剪
                    case R.id.tvReCut:
                        // 隐藏校对布局
                        llCheckPictureRoot.setVisibility(View.GONE);
                        // 展示裁剪布局
                        llEditPictureRoot.setVisibility(View.VISIBLE);
                        // 调整头顶状态栏
                        ivEditPictureTitle.setVisibility(View.GONE);
                        break;
                    // 保存到本地
                    case R.id.tvSaveLocal:
                        String result = BitmapUtils.saveImageToGallery(context, cutResult, "cropper_sample");
                        String msg = context.getResources().getString(R.string.str_picture_save_success);
                        if (TextUtils.isEmpty(result)) {
                            msg = context.getResources().getString(R.string.str_picture_save_fail);
                        }
                        ToastUtil.shortMsg(context, msg);
                        break;
                }
            }
        };
        // 右上角回退按钮
        ivEditPictureTitle.setOnClickListener(onListener);
        // 底部栏中六个操作按钮
        flCancel.setOnClickListener(onListener);
        flNationalExam.setOnClickListener(onListener);
        flOneInch.setOnClickListener(onListener);
        flTwoInch.setOnClickListener(onListener);
        flCustomize.setOnClickListener(onListener);
        flFinish.setOnClickListener(onListener);
        // 重新裁剪和保存到本地
        tvReCut.setOnClickListener(onListener);
        tvSaveLocal.setOnClickListener(onListener);
    }

    // 初始化传入数据内容
    private void initBaseData() {
        originalUrl = getIntent().getStringExtra("data");
        if (TextUtils.isEmpty(originalUrl)) {
            ToastUtil.shortMsg(context, context.getResources().getString(R.string.str_parameter_passing_error));
            finish();
            return;
        }
        // 加载图片
        Glide.with(context).load(originalUrl).into(civEditPicture);
        flNationalExam.callOnClick();
    }

    // 修改选中的文本背景和前景颜色
    private void selectTextView(TextView textView) {
        // 修改上次选中文本的背景
        if (lastSelectTextView != null) {
            lastSelectTextView.setBackgroundColor(textUnSelectedBgColor);
            lastSelectTextView.setTextColor(textUnSelectedTextColor);
        }
        // 设置本次选中的文本
        lastSelectTextView = textView;
        lastSelectTextView.setBackgroundResource(textSelectedBgRes);
        lastSelectTextView.setTextColor(textSelectedTextColor);
    }

    @Override
    public void onBackPressed() {
        if (llCheckPictureRoot.getVisibility() == View.VISIBLE) {
            tvReCut.callOnClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        Glide.get(context).clearMemory();
        super.onDestroy();
    }
}
