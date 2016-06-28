package com.example.camera.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.camera.CameraAty;
import com.example.camera.R;

/**
 * Created by zhao on 2016/6/28.
 */
public class PhotoPreviewFragment extends Fragment implements View.OnClickListener{

    private View mParent;
    private CameraAty mActivity;
    private Context mContext;
    private ImageView mPhotoPreview;
    private ImageButton mConfirmButton;
    private ImageButton mRePictureBuuton;

    public static PhotoPreviewFragment newInstance(Bundle args) {
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParent = inflater.inflate(R.layout.preview,container,false);
        initView();
        return mParent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (CameraAty) activity;
    }

    private void initView(){
        mConfirmButton = (ImageButton) mParent.findViewById(R.id.btn_confirm);
        mRePictureBuuton = (ImageButton) mParent.findViewById(R.id.btn_re_picture);
        mPhotoPreview = (ImageView) mParent.findViewById(R.id.iv_preview);
        mConfirmButton.setOnClickListener(this);
        mRePictureBuuton.setOnClickListener(this);
        String pictureUri = getArguments().getString("url");
        mPhotoPreview.setImageURI(Uri.parse(pictureUri));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
//                Intent mIntent = new Intent(mContext,FeedActivity.class);
                break;
            case R.id.btn_re_picture:
                mActivity.rePicture();
                break;
        }
    }
}
