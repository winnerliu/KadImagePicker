package com.kad.imagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kad.imagepicker.R;
import com.kad.imagepicker.model.KadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Winner on 2016/12/28.
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {

    private List<KadImage> datas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public ImgAdapter(Context context, List<KadImage> datas) {
        this.mContext = context;
        this.datas = datas;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(inflater.inflate(R.layout.layout_img_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {

        KadImage image = datas.get(position);
        if(image.isCompressed()){
            holder.simpleDraweeView.setImageURI(Uri.parse("file://"+image.getCompressPath()));
        }else{
            holder.simpleDraweeView.setImageURI(Uri.parse("file://"+image.getOriginalPath()));
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class ImgViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView simpleDraweeView;

        public ImgViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.img_sdv);
        }
    }
}
