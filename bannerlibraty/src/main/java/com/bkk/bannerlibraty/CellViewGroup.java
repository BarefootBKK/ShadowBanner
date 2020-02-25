package com.bkk.bannerlibraty;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

public class CellViewGroup {

    private final static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    /**
     * 保存当前Cell的信息
     */
    private static CellInfo cellInfo = new CellInfo();
    /**
     * 是否创建了Cell
     */
    private static boolean cellCreated = false;

    /**
     * 获取MODE为TITLE_CONTENT的CELL
     * @param context Context
     * @return View
     */
    public static synchronized View titleContentCellView(Context context) {
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        ConstraintSet c = new ConstraintSet();
        // 创建图片View
        ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        // 创建内容Text
        TextView cellTextContent = getTextView(context, 22, Color.WHITE);
        c.constrainWidth(cellTextContent.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.constrainHeight(cellTextContent.getId(), ConstraintSet.WRAP_CONTENT);
        c.connect(cellTextContent.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        c.connect(cellTextContent.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(cellTextContent.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        // 创建标题Text
        TextView cellTextTitle = getTextView(context, 18, Color.parseColor("#EBE3E3"));
        c.constrainWidth(cellTextTitle.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.constrainHeight(cellTextTitle.getId(), ConstraintSet.WRAP_CONTENT);
        c.connect(cellTextTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(cellTextTitle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        c.connect(cellTextTitle.getId(), ConstraintSet.BOTTOM, cellTextContent.getId(), ConstraintSet.TOP);
        // 创建覆盖层
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(View.generateViewId());
        linearLayout.setBackgroundColor(Color.BLACK);
        linearLayout.getBackground().setAlpha((int) (0.6 * 255));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        c.constrainWidth(linearLayout.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.constrainHeight(linearLayout.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.connect(linearLayout.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(linearLayout.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        c.connect(linearLayout.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        c.connect(linearLayout.getId(), ConstraintSet.TOP, cellTextTitle.getId(), ConstraintSet.TOP);
        // 将控件加入父控件
        constraintLayout.setLayoutParams(new Constraints.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        constraintLayout.addView(imageView);
        constraintLayout.addView(linearLayout);
        constraintLayout.addView(cellTextTitle);
        constraintLayout.addView(cellTextContent);
        c.applyTo(constraintLayout);
        // 保存当前必要控件id
        cellInfo.setImageId(imageView.getId());
        cellInfo.setTitleId(cellTextTitle.getId());
        cellInfo.setContentId(cellTextContent.getId());
        cellCreated = true;

        return constraintLayout;
    }

    /**
     * 构建Text View
     * @param context context
     * @param size 文字大小
     * @param color 文字颜色
     * @return TextView
     */
    private static TextView getTextView(Context context, float size, int color) {
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(size);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(color);
        return textView;
    }

    /**
     * 获取当前Cell的信息
     * @return 当前的CellInfo
     */
    public static CellInfo getCurrentCellInfo() {
        return cellCreated ? cellInfo : null;
    }
}
