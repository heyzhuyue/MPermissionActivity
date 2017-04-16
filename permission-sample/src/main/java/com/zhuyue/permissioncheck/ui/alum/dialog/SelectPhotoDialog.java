package com.zhuyue.permissioncheck.ui.alum.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.zhuyue.permissioncheck.R;

/**
 * Created by zhuyue on 2017/4/16
 */

public class SelectPhotoDialog extends Dialog {

    public SelectPhotoDialog(@NonNull Context context) {
        super(context);
    }

    public SelectPhotoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SelectPhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private onSelectPhotoInterface onSelectPhotoInterface;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setOnSelectPhotoInterface(onSelectPhotoInterface onSelectPhotoInterface) {
            this.onSelectPhotoInterface = onSelectPhotoInterface;
            return this;
        }

        public SelectPhotoDialog create() {
            final SelectPhotoDialog selectPhotoDialog = new SelectPhotoDialog(mContext, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.layout_picture_selector, null);
            contentView.findViewById(R.id.picture_selector_take_photo_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectPhotoInterface != null) {
                        onSelectPhotoInterface.OnSelected(v, 0, selectPhotoDialog);
                    }
                }
            });
            contentView.findViewById(R.id.picture_selector_pick_picture_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectPhotoInterface != null) {
                        onSelectPhotoInterface.OnSelected(v, 1, selectPhotoDialog);
                    }
                }
            });
            contentView.findViewById(R.id.picture_selector_cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectPhotoInterface != null) {
                        onSelectPhotoInterface.OnSelected(v, 2, selectPhotoDialog);
                    }
                }
            });
            selectPhotoDialog.setContentView(contentView);
            return selectPhotoDialog;
        }

        public interface onSelectPhotoInterface {

            /**
             * 选择方式
             *
             * @param v
             * @param position          0:拍照;1:相册;2:取消
             * @param selectPhotoDialog SelectPhotoDialog
             */
            void OnSelected(View v, int position, SelectPhotoDialog selectPhotoDialog);

        }

    }

}
