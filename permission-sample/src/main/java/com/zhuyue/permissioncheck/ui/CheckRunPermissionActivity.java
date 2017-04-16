package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kevin.crop.UCrop;
import com.zhuyue.annotation.PermissionDenied;
import com.zhuyue.annotation.PermissionGrant;
import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BaseActivity;
import com.zhuyue.permissioncheck.ui.alum.dialog.SelectPhotoDialog;
import com.zhuyue.permissioncheck.ui.alum.ui.CropImageActivity;
import com.zhuyue.permissioncheck.ui.record.RecordActivity;
import com.zhuyue.permissionlib.PermissionCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhuyue on 2017/3/26
 */

public class CheckRunPermissionActivity extends BaseActivity implements SelectPhotoDialog.Builder.onSelectPhotoInterface {
    private static final String TAG = "CheckRunPermissionActiv";
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;
    /**
     * 拍照权限集合
     */
    private String[] mCameraPermissions = new String[]{Manifest.permission.CAMERA};
    /**
     * 拍照权限Code
     */
    private static final int CAMREA_QUEST_CODE = 100;
    /**
     * 相册权限集合
     */
    private String[] mAlumPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    /**
     * 相册权限Code
     */
    private static final int ALUM_QUEST_CODE = 101;
    private String mTempPhotoPath;
    private Uri mDestinationUri;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDestinationUri = Uri.fromFile(new File(this.getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        imageView = (ImageView) findViewById(R.id.img_photo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_runpermission_check;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.onRequestPermissionsResultByCompiler(CheckRunPermissionActivity.this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;
            }
        }
    }

    public void OnRecord(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    public void OnAlum(View view) {
        showSelectPhotoDialog();
    }

    /**
     * 显示选择
     */
    public void showSelectPhotoDialog() {
        SelectPhotoDialog.Builder builder = new SelectPhotoDialog.Builder(CheckRunPermissionActivity.this);
        builder.setOnSelectPhotoInterface(this);
        SelectPhotoDialog dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 高度
        lp.alpha = 0.7f; // 透明度
        lp.windowAnimations = android.R.style.Animation_InputMethod;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void OnSelected(View v, int position, SelectPhotoDialog selectPhotoDialog) {
        switch (position) {
            case 0:
                PermissionCheck.requestPermissionByCompiler(CheckRunPermissionActivity.this, CAMREA_QUEST_CODE, mCameraPermissions);
                selectPhotoDialog.dismiss();
                break;
            case 1:
                PermissionCheck.requestPermissionByCompiler(CheckRunPermissionActivity.this, ALUM_QUEST_CODE, mAlumPermissions);
                selectPhotoDialog.dismiss();
                break;
            case 2:
                selectPhotoDialog.dismiss();
                break;
        }
    }

    @PermissionGrant(CAMREA_QUEST_CODE)
    public void requestCameraSuccess() {
        startCamera();
    }

    @PermissionDenied(CAMREA_QUEST_CODE)
    public void requestCameraFailed() {
        showRequestPermissionFailDialog();
    }

    @PermissionGrant(ALUM_QUEST_CODE)
    public void requestAlumSuccess() {
        startAlum();
    }

    @PermissionDenied(ALUM_QUEST_CODE)
    public void requestAlumFailed() {
        showRequestPermissionFailDialog();
    }

    /**
     * 开启相机
     */
    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 开启相册
     */
    public void startAlum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropImageActivity.class)
                .start(CheckRunPermissionActivity.this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(CheckRunPermissionActivity.this.getContentResolver(), resultUri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CheckRunPermissionActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(CheckRunPermissionActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CheckRunPermissionActivity.this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }
}
