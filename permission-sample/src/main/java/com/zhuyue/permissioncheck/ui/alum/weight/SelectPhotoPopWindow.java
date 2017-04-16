package com.zhuyue.permissioncheck.ui.alum.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhuyue.permissioncheck.R;

/**
 * Created by win7 on 2016/12/27.
 */

public class SelectPhotoPopWindow extends PopupWindow implements View.OnClickListener {

    private View contentView;
    private Button btCameraSelect, btAlbumSelector, btCancle;
    private PopupWindow popupWindow;
    private onSelectPhotoInterface onSelectPhotoInterface;

    public SelectPhotoPopWindow(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_picture_selector, null);
        btCameraSelect = (Button) contentView.findViewById(R.id.picture_selector_take_photo_btn);
        btAlbumSelector = (Button) contentView.findViewById(R.id.picture_selector_pick_picture_btn);
        btCancle = (Button) contentView.findViewById(R.id.picture_selector_cancel_btn);

        btCameraSelect.setOnClickListener(this);
        btAlbumSelector.setOnClickListener(this);
        btCancle.setOnClickListener(this);
    }

    /**
     * 显示Popwindow
     *
     * @param activity
     */
    public void showPopSelectPhotoWindow(Activity activity) {
        popupWindow = new PopupWindow(contentView,    // 添加到popupWindow
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /**
         * 必须要设置背景，播放动画有一个前提 就是窗体必须有背景
         */
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        /**
         * 设置窗口显示的动画效果
         */
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        /**
         * 点击其他地方隐藏键盘 popupWindow
         */
        popupWindow.setFocusable(false);
        popupWindow.update();
    }

    /**
     * 隐藏PopWindow
     */
    public void dismissPopSelectPhotoWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_selector_take_photo_btn:
                if (onSelectPhotoInterface != null) {
                    onSelectPhotoInterface.OnSelected(v, 0);
                }
                break;
            case R.id.picture_selector_pick_picture_btn:
                if (onSelectPhotoInterface != null) {
                    onSelectPhotoInterface.OnSelected(v, 1);
                }
                break;
            case R.id.picture_selector_cancel_btn:
                if (onSelectPhotoInterface != null) {
                    onSelectPhotoInterface.OnSelected(v, 2);
                }
                break;
        }
    }

    public void setOnSelectPhotoInterface(SelectPhotoPopWindow.onSelectPhotoInterface onSelectPhotoInterface) {
        this.onSelectPhotoInterface = onSelectPhotoInterface;
    }

    public interface onSelectPhotoInterface {

        void OnSelected(View v, int position);

    }
}
