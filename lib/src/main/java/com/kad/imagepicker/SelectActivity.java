package com.kad.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kad.imagepicker.adapter.ImgAdapter;
import com.kad.imagepicker.compress.CompressConfig;
import com.kad.imagepicker.compress.CompressImage;
import com.kad.imagepicker.compress.CompressImageImpl;
import com.kad.imagepicker.compress.CompressImageUtil;
import com.kad.imagepicker.model.KadImage;
import com.kad.imagepicker.uitl.AbsolutePathUtil;
import com.kad.imagepicker.uitl.ImagePickerUtils;

import java.io.File;
import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CAMERA_REQUEST_CODE = 3;
    // 拍照路径
    public static String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory().getPath()+ "/temp/";
    String cameraPath;

    private ArrayList<KadImage> imgLists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ImgAdapter mImgAdapter;
    private CompressConfig compressConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();
        setupData();
    }

    private void initView() {
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPath = ImagePickerUtils.getNewCameraPath();
                ImagePickerUtils.openCamera(SelectActivity.this, cameraPath);
            }
        });
        findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.openAlbum(SelectActivity.this);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_layout);

    }

    private void setupData() {

        GridLayoutManager manager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(manager);
        mImgAdapter = new ImgAdapter(this, imgLists);
        mRecyclerView.setAdapter(mImgAdapter);
        initImgData();
    }

    private void initImgData() {
        compressConfig = new CompressConfig.Builder().setMaxSize(100 * 1024).setMaxPixel(800).create();
        KadImage kadImage = KadImage.of("/storage/emulated/0/temp/1482734729032.jpg");
        CompressImageImpl.of(this, compressConfig, kadImage, new CompressImage.CompressListener() {
            @Override
            public void onCompressSuccess(ArrayList<KadImage> images) {

            }

            @Override
            public void onCompressFailed(ArrayList<KadImage> images, String msg) {

            }
        });
    }

    private void compressImg(String originPath){

        KadImage kadImage = KadImage.of(originPath);
        CompressImageImpl.of(this, compressConfig, kadImage, new CompressImage.CompressListener() {
            @Override
            public void onCompressSuccess(ArrayList<KadImage> images) {
                if(images != null && images.size()>0){
                    imgLists.add(images.get(0));
                    mImgAdapter.notifyDataSetChanged();
                    Toast.makeText(SelectActivity.this, ""+images.size(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCompressFailed(ArrayList<KadImage> images, String msg) {
                Toast.makeText(SelectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }
        }).compress();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                System.out.println(""+data);
                Log.d("imagepicker", "path=camora==" + cameraPath);
                compressImg(cameraPath);

            }else if (requestCode == ALBUM_REQUEST_CODE) {
                try {
                    Uri uri = data.getData();
                    final String absolutePath = AbsolutePathUtil.getAbsolutePath(this, uri);
                    compressImg(absolutePath);

                    Log.d("imagepicker", "path=album==" + absolutePath);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

    }

}
