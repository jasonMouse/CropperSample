package com.example.croppersample.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.croppersample.R;
import com.example.croppersample.constants.ConstantValues;
import com.example.croppersample.utils.DeviceUtil;
import com.example.croppersample.utils.ToastUtil;

public class EditPictureSizeDialog {
    private Activity context;                                       // 当前依附的界面
    private int savedHeight;                                        // 临时存储的高度
    private int savedWidth;                                         // 临时存储的宽度
    private Dialog editPictureSizeDialog;                           // 对话框
    private View.OnClickListener onClickListener;                   // 点击事件监听器
    private OnSizeChangeListener sizeChangeListener;                // 和外界交互的监听器
    private TextWatcher widthWatcher;                               // 宽度文本输入监听器
    private TextWatcher heightWatcher;                              // 高度文本输入监听器

    public EditPictureSizeDialog(Activity context, int sourceWidth, int sourceHeight, OnSizeChangeListener listener) {
        this.context = context;
        this.savedHeight = sourceHeight;
        this.savedWidth = sourceWidth;
        this.sizeChangeListener = listener;
        initDialog(sourceWidth, sourceHeight);
    }

    private void initDialog(int sourceWidth, int sourceHeight) {
        if (editPictureSizeDialog == null) {
            this.editPictureSizeDialog = new Dialog(context, R.style.bottom_dialog);
            editPictureSizeDialog.setContentView(R.layout.layout_edit_picture_size_bottom_dialog);
            editPictureSizeDialog.setCanceledOnTouchOutside(false);
            editPictureSizeDialog.setCancelable(false);
            Window window = editPictureSizeDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        final EditText etWidth = editPictureSizeDialog.findViewById(R.id.etWidth);
        final EditText etHeight = editPictureSizeDialog.findViewById(R.id.etHeight);
        TextView tvCancel = editPictureSizeDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = editPictureSizeDialog.findViewById(R.id.tvConfirm);

        // 删除文本输入监听器
        if (widthWatcher != null) {
            etWidth.removeTextChangedListener(widthWatcher);
        }
        if (heightWatcher != null) {
            etHeight.removeTextChangedListener(heightWatcher);
        }
        if (sourceWidth <= 0) {
            etWidth.setText("");
        } else {
            etWidth.setText(String.valueOf(sourceWidth));
        }
        if (sourceHeight <= 0) {
            etHeight.setText("");
        } else {
            etHeight.setText(String.valueOf(sourceHeight));
        }

        // 获取到屏幕宽度和高度的最小值，作为可输入最大值
        int screenWidth = DeviceUtil.getScreenWidth(context);
        int screenHeight = DeviceUtil.getScreenHeight(context);
        final int maxValue = screenHeight > screenWidth ? screenWidth : screenHeight;
        widthWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable sResult) {
                String strResult = sResult.toString();
                if (!TextUtils.isEmpty(strResult)) {
                    int intResult = Integer.parseInt(strResult);
                    if (intResult > maxValue) {
                        ToastUtil.shortMsg(context, "输入的宽度不得超过" + maxValue);
                    }
                }
            }
        };
        heightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable sResult) {
                String strResult = sResult.toString();
                if (!TextUtils.isEmpty(strResult)) {
                    int intResult = Integer.parseInt(strResult);
                    if (intResult > maxValue) {
                        ToastUtil.shortMsg(context, "输入的高度不得超过" + maxValue);
                    }
                }
            }
        };
        etWidth.addTextChangedListener(widthWatcher);
        etHeight.addTextChangedListener(heightWatcher);

        // 添加点击监听器
        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tvCancel:
                            if (sizeChangeListener != null) {
                                sizeChangeListener.onCancel();
                            }
                            DeviceUtil.hideSoftKeyboard(context);
                            dismissDialog();
                            break;
                        case R.id.tvConfirm:
                            String strWidth = etWidth.getText().toString();
                            String strHeight = etHeight.getText().toString();
                            int tempWidth = 0;
                            int tempHeight = 0;
                            if (!TextUtils.isEmpty(strWidth)) {
                                tempWidth = Integer.parseInt(strWidth);
                            }
                            if (!TextUtils.isEmpty(strHeight)) {
                                tempHeight = Integer.parseInt(strHeight);
                            }
                            // 如果宽度和高度符合要求
                            if (tempWidth <= maxValue && tempHeight <= maxValue) {
                                if (sizeChangeListener != null) {
                                    if (tempWidth <= 0 || tempHeight <= 0) {
                                        sizeChangeListener.onCancel();
                                    } else {
                                        sizeChangeListener.onConfirm(tempWidth, tempHeight);
                                    }
                                }
                                // 临时保存上次输入的值
                                savedHeight = tempHeight;
                                savedWidth = tempWidth;
                                // 隐藏软键盘和对话框
                                DeviceUtil.hideSoftKeyboard(context);
                                dismissDialog();
                            } else if (tempWidth > maxValue) {
                                ToastUtil.shortMsg(context, "输入的宽度不得超过" + maxValue);
                            } else {
                                ToastUtil.shortMsg(context, "输入的高度不得超过" + maxValue);
                            }
                            break;

                    }
                }
            };
        }
        tvCancel.setOnClickListener(onClickListener);
        tvConfirm.setOnClickListener(onClickListener);
    }

    public static interface OnSizeChangeListener {
        /**
         * 确定之后的回调
         *
         * @param width
         * @param height
         */
        public void onConfirm(int width, int height);

        /**
         * 取消之后的回调
         */
        public void onCancel();
    }

    public void setOnSizeChangeListener(OnSizeChangeListener listener) {
        this.sizeChangeListener = listener;
    }

    // 启动之后的首次展示软键盘编辑
    private void showEditKeyBoard(final EditText view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        // 申请焦点
        view.requestFocus();
        //设置显示光标
        view.setCursorVisible(true);
        // 展示软键盘
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceUtil.showSoftKeyboard(view);
                view.removeCallbacks(this);
            }
        }, ConstantValues.SoftKeyBoardShowDuration);
    }

    // 展示对话框
    public void showDialog() {
        if (editPictureSizeDialog != null) {
            if (!editPictureSizeDialog.isShowing()) {
                editPictureSizeDialog.show();
                final EditText etWidth = editPictureSizeDialog.findViewById(R.id.etWidth);
                showEditKeyBoard(etWidth);
            }
        } else {
            initDialog(savedWidth, savedHeight);
            final EditText etWidth = editPictureSizeDialog.findViewById(R.id.etWidth);
            showEditKeyBoard(etWidth);
        }
    }

    private void dismissDialog() {
        if (editPictureSizeDialog != null && editPictureSizeDialog.isShowing()) {
            editPictureSizeDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return this.editPictureSizeDialog != null && editPictureSizeDialog.isShowing();
    }
}
