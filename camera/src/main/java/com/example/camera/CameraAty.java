package com.example.camera;

import java.io.File;
import java.util.List;

import com.example.camera.fragment.PhotoPreviewFragment;
import com.linj.FileOperateUtil;
import com.linj.album.view.FilterImageView;
import com.linj.camera.view.CameraContainer;
import com.linj.camera.view.CameraContainer.TakePictureListener;
import com.linj.camera.view.CameraView.FlashMode;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * @ClassName: CameraAty
 * @Description:  �Զ����������
 * @author LinJ
 * @date 2014-12-31 ����9:44:25 
 *
 */
public class CameraAty extends Activity implements View.OnClickListener,TakePictureListener{
	public final static String TAG="CameraAty";
	private boolean mIsRecordMode=false;
	private String mSaveRoot;
	private CameraContainer mContainer;
	private FilterImageView mThumbView;
	private ImageButton mCameraShutterButton;
	private ImageButton mRecordShutterButton;
	private ImageView mFlashView;
	private ImageButton mSwitchModeButton;
	private ImageView mSwitchCameraView;
	private TextView mCancelView;
	private ImageView mVideoIconView;
	private View mHeaderBar;
	private PhotoPreviewFragment mPhotoPreviewFragment;
	private boolean isRecording=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera);

		mHeaderBar=findViewById(R.id.camera_header_bar);
		mContainer=(CameraContainer)findViewById(R.id.container);
		mThumbView=(FilterImageView)findViewById(R.id.btn_thumbnail);
		mVideoIconView=(ImageView)findViewById(R.id.videoicon);
		mCameraShutterButton=(ImageButton)findViewById(R.id.btn_shutter_camera);
		mRecordShutterButton=(ImageButton)findViewById(R.id.btn_shutter_record);
		mSwitchCameraView=(ImageView)findViewById(R.id.btn_switch_camera);
		mFlashView=(ImageView)findViewById(R.id.btn_flash_mode);
		mSwitchModeButton=(ImageButton)findViewById(R.id.btn_switch_mode);
		mCancelView=(TextView) findViewById(R.id.btn_cancel);


		mThumbView.setOnClickListener(this);
		mCameraShutterButton.setOnClickListener(this);
		mRecordShutterButton.setOnClickListener(this);
		mFlashView.setOnClickListener(this);
		mSwitchModeButton.setOnClickListener(this);
		mSwitchCameraView.setOnClickListener(this);
		mCancelView.setOnClickListener(this);

		mSaveRoot="test";
		mContainer.setRootPath(mSaveRoot);
		initThumbnail();
	}


	/**
	 * ��������ͼ
	 */
	private void initThumbnail() {
		String thumbFolder=FileOperateUtil.getFolderPath(this, FileOperateUtil.TYPE_THUMBNAIL, mSaveRoot);
		List<File> files=FileOperateUtil.listFiles(thumbFolder, ".jpg");
		if(files!=null&&files.size()>0){
			Bitmap thumbBitmap=BitmapFactory.decodeFile(files.get(0).getAbsolutePath());
			if(thumbBitmap!=null){
				mThumbView.setImageBitmap(thumbBitmap);
				//��Ƶ����ͼ��ʾ����ͼ��
				if(files.get(0).getAbsolutePath().contains("video")){
					mVideoIconView.setVisibility(View.VISIBLE);
				}else {
					mVideoIconView.setVisibility(View.GONE);
				}
			}
		}else {
			mThumbView.setImageBitmap(null);
			mVideoIconView.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_shutter_camera:
			mCameraShutterButton.setClickable(false);
			mContainer.takePicture(this);
			break;
		case R.id.btn_thumbnail:
			startActivity(new Intent(this,AlbumAty.class));
			break;
		case R.id.btn_flash_mode:
			if(mContainer.getFlashMode()==FlashMode.ON){
				mContainer.setFlashMode(FlashMode.OFF);
				mFlashView.setImageResource(R.drawable.btn_flash_off);
			}else if (mContainer.getFlashMode()==FlashMode.OFF) {
				mContainer.setFlashMode(FlashMode.AUTO);
				mFlashView.setImageResource(R.drawable.btn_flash_auto);
			}
			else if (mContainer.getFlashMode()==FlashMode.AUTO) {
				mContainer.setFlashMode(FlashMode.TORCH);
				mFlashView.setImageResource(R.drawable.btn_flash_torch);
			}
			else if (mContainer.getFlashMode()==FlashMode.TORCH) {
				mContainer.setFlashMode(FlashMode.ON);
				mFlashView.setImageResource(R.drawable.btn_flash_on);
			}
			break;
		case R.id.btn_switch_mode:
			if(mIsRecordMode){
				mSwitchModeButton.setImageResource(R.drawable.ic_switch_camera);
				mCameraShutterButton.setVisibility(View.VISIBLE);
				mRecordShutterButton.setVisibility(View.GONE);
				//����ģʽ����ʾ�����˵�
				mHeaderBar.setVisibility(View.VISIBLE);
				mIsRecordMode=false;
				mContainer.switchMode(0);
				stopRecord();
			}
			else {
				mSwitchModeButton.setImageResource(R.drawable.ic_switch_video);
				mCameraShutterButton.setVisibility(View.GONE);
				mRecordShutterButton.setVisibility(View.VISIBLE);
				//¼��ģʽ�����ض����˵�
				mHeaderBar.setVisibility(View.GONE);
				mIsRecordMode=true;
				mContainer.switchMode(5);
			}
			break;
		case R.id.btn_shutter_record:
			if(!isRecording){
				isRecording=mContainer.startRecord();
				if (isRecording) {
					mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_recording);
				}
			}else {
				stopRecord();
			}
			break;
		case R.id.btn_switch_camera:
			mContainer.switchCamera();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}


	private void stopRecord() {
		mContainer.stopRecord(this);
		isRecording=false;
		mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
	}

	@Override
	public void onTakePictureEnd(Bitmap thumBitmap) {
//		mCameraShutterButton.setClickable(true);
		Bundle args = new Bundle();
		args.putString("url",mContainer.getCurrentImagePath());
		mPhotoPreviewFragment = PhotoPreviewFragment.newInstance(args);
		getFragmentManager().beginTransaction().replace(R.id.camera_main_layout,mPhotoPreviewFragment).commit();
	}

	@Override
	public void onAnimtionEnd(Bitmap bm,boolean isVideo) {
		if(bm!=null){
			//��������ͼ
			Bitmap thumbnail=ThumbnailUtils.extractThumbnail(bm, 213, 213);
			mThumbView.setImageBitmap(thumbnail);
			if(isVideo)
				mVideoIconView.setVisibility(View.VISIBLE);
			else {
				mVideoIconView.setVisibility(View.GONE);
			}
			mCameraShutterButton.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void rePicture(){
		if (mContainer.getCurrentImagePath() != null) FileOperateUtil.deleteSourceFile(mContainer.getCurrentImagePath(),this);
		if (mPhotoPreviewFragment != null) getFragmentManager().beginTransaction().remove(mPhotoPreviewFragment).commit();
		mCameraShutterButton.setClickable(true);
	}
}