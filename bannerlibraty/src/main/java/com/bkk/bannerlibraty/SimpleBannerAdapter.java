package com.bkk.bannerlibraty;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SimpleBannerAdapter extends ShadowBannerAdapter<ShadowBannerCell> {

    private CellInfo cellInfo;

    public SimpleBannerAdapter(List<ShadowBannerCell> list) {
        super(list);
    }

    @Override
    public void onItemCreate(ViewHolder holder, ShadowBannerCell item, int position) {
        cellInfo = CellViewGroup.getCurrentCellInfo();
        if (cellInfo != null) {
            TextView textTitle = holder.getView(cellInfo.titleId);
            TextView textContent = holder.getView(cellInfo.contentId);
            ImageView imageView = holder.getView(cellInfo.imageId);
            if (textTitle != null && textContent != null && imageView != null) {
                textTitle.setText(item.getTitle());
                textContent.setText(item.getContent());
                Glide.with(holder.getContext())
                        .load(item.getImageUrl())
                        .into(imageView);
            }
        }
    }
}
