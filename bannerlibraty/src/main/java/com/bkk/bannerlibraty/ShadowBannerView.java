package com.bkk.bannerlibraty;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author BarefootBKK
 */
public final class ShadowBannerView extends ConstraintLayout {

    /**
     * Context
     */
    private Context mContext;

    /**
     * ViewPager2控件
     */
    private ViewPager2 pager;

    /**
     * 底部容器
     */
    private ConstraintLayout bottomPointsLayout;

    /**
     * 上一个被选中的小圆点
     */
    private ImageView lastImagePoint;

    /**
     * 用来容纳小圆点的控件
     */
    private LinearLayout pointsContainer;

    /**
     * Banner适配器
     */
    private ShadowBannerAdapter bannerAdapter;

    /**
     * 小圆点集合
     */
    private List<ImageView> pointViews;

    /**
     * 自动轮播间隔时间
     */
    private long scrollTime;

    /*
    当前累计计时的时间（用于自动切换）
     */
    private long currentTimeCount = 0;

    /**
     * 是否自动轮播
     */
    private boolean isAutoScroll;

    /**
     * 是否在拖动
     */
    private boolean isOnDrag;
    /**
     * 轮播的cell个数
     */
    private int itemCount;

    /**
     * 选中的小圆点背景
     */
    private int pointBackgroundSelected;

    /**
     * 未选中的小圆点背景
     */
    private int pointBackgroundUnSelected;

    /**
     * 是否设置了图片背景
     */
    private boolean isResSet;

    /**
     * 圆点大小
     */
    private int pointSize;

    /**
     * 圆点间距
     */
    private int pointMargin;

    /**
     * 点击的滑动duration
     */
    private static long clickScrollDuration = 300;

    /**
     * 自动切换的duration
     */
    private static long autoScrollDuration = 400;

    /**
     * 保存上一次item位置（用于小圆点切换动画）
     */
    private int previousPos = 0;

    /**
     * 默认自动轮播间隔时间
     */
    public final static long DEFAULT_SCROLL_TIME = 3000;

    /**
     * 最小轮播间隔时间
     */
    public final static long MIN_SCROLL_TIME = 300;

    /**
     * 默认小圆点左右间隔
     */
    public final static int DEFAULT_POINT_MARGIN = 10;

    /**
     * 默认小圆点大小
     */
    public final static int DEFAULT_POINT_SIZE = 30;

    /**
     * 构造函数1
     *
     * @param context
     */
    public ShadowBannerView(Context context) {
        this(context, null);
    }

    /**
     * 构造函数2
     *
     * @param context
     * @param attrs
     */
    public ShadowBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数3
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ShadowBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 设置Banner Cell的点击监听器
     *
     * @param itemClickListener
     */
    public void setOnCellClickListener(ShadowBannerAdapter.OnItemClickListener itemClickListener) {
        if (bannerAdapter != null) {
            bannerAdapter.setOnItemClickListener(itemClickListener);
        }
    }

    /**
     * 设置Banner适配器
     *
     * @param bannerAdapter
     */
    public void setBannerAdapter(ShadowBannerAdapter bannerAdapter) {
        this.bannerAdapter = bannerAdapter;
        this.itemCount = bannerAdapter.getLogicItemCount();
    }

    /**
     * 设置小圆点大小
     *
     * @param pointSize
     */
    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    /**
     * 设置小圆点间隔
     *
     * @param pointMargin
     */
    public void setPointMargin(int pointMargin) {
        this.pointMargin = pointMargin;
    }

    /**
     * 设置自动轮播间隔时间
     *
     * @param scrollTime
     */
    public void setAutoScrollTime(long scrollTime) {
        this.scrollTime = scrollTime > MIN_SCROLL_TIME ? scrollTime : MIN_SCROLL_TIME;
    }

    /**
     * 设置是否自动轮播
     *
     * @param scroll 是否自动轮播
     */
    public void setAutoScroll(boolean scroll) {
        this.isAutoScroll = scroll;
    }

    /**
     * 设置选中的圆点颜色
     *
     * @param pointColorSelected
     */
    public void setPointColorSelected(int pointColorSelected) {
        this.pointBackgroundSelected = pointColorSelected;
    }

    /**
     * 设置未选中的圆点颜色
     *
     * @param pointColorUnSelected
     */
    public void setPointColorUnSelected(int pointColorUnSelected) {
        this.pointBackgroundUnSelected = pointColorUnSelected;
    }

    /**
     * 设置圆点颜色
     *
     * @param pointColorSelected
     * @param pointColorUnSelected
     */
    public void setPointsColor(int pointColorSelected, int pointColorUnSelected) {
        setPointColorSelected(pointColorSelected);
        setPointColorUnSelected(pointColorUnSelected);
    }

    /**
     * 设置圆点图片样式
     *
     * @param pointResSelected
     * @param pointColorUnSelected
     */
    public void setPointRes(int pointResSelected, int pointColorUnSelected) {
        this.pointBackgroundSelected = pointResSelected;
        this.pointBackgroundUnSelected = pointColorUnSelected;
        this.isResSet = true;
    }

    /**
     * 移除圆点图片样式
     */
    public void removePointRes() {
        this.isResSet = false;
    }

    /**
     * 获取Banner Cell个数
     *
     * @return
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * 设置自动切换时的duration
     *
     * @param duration 动画时长
     */
    public void setAutoScrollDuration(long duration) {
        autoScrollDuration = duration > 50 ? duration : 50;
    }

    /**
     * 设置点击切换时的duration
     *
     * @param duration 动画时长
     */
    public void setClickScrollDuration(int duration) {
        clickScrollDuration = duration > 50 ? duration : 50;
    }

    /**
     * 设置圆点容器背景颜色
     * @param color 颜色
     */
    public void setPointsContainerBackgroundColor(int color) {
        bottomPointsLayout.setBackgroundColor(color);
    }

    /**
     * 初始化
     */
    private void init() {
        constructViews();
        this.pointViews = new ArrayList<>();
        this.scrollTime = DEFAULT_SCROLL_TIME;
        this.isAutoScroll = false;
        this.isOnDrag = false;
        this.isResSet = false;
        this.pointSize = DEFAULT_POINT_SIZE;
        this.pointMargin = DEFAULT_POINT_MARGIN;
        this.pointBackgroundSelected = Color.WHITE;
        this.pointBackgroundUnSelected = Color.GRAY;
    }

    /**
     * 开始构建
     */
    public void build() {
        constructPoints();
        attachPagerAdapter();
        startScroll();
    }

    /**
     * 构建Views
     */
    private void constructViews() {
        ConstraintSet c = new ConstraintSet();

        // 构建小圆点内容器1
        pointsContainer = new LinearLayout(mContext);
        pointsContainer.setId(View.generateViewId());
        pointsContainer.setOrientation(LinearLayout.HORIZONTAL);
        c.constrainHeight(pointsContainer.getId(), ConstraintSet.WRAP_CONTENT);
        c.constrainWidth(pointsContainer.getId(), ConstraintSet.WRAP_CONTENT);
        c.connect(pointsContainer.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 4);
        c.connect(pointsContainer.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 4);
        c.connect(pointsContainer.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(pointsContainer.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        // 构建小圆点外容器2
        bottomPointsLayout = new ConstraintLayout(mContext);
        bottomPointsLayout.setId(View.generateViewId());
        bottomPointsLayout.setBackgroundColor(Color.WHITE);
        bottomPointsLayout.addView(pointsContainer);
        c.applyTo(bottomPointsLayout);
        c.constrainHeight(bottomPointsLayout.getId(), ConstraintSet.WRAP_CONTENT);
        c.constrainWidth(bottomPointsLayout.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.connect(bottomPointsLayout.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        c.connect(bottomPointsLayout.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(bottomPointsLayout.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        // 构建ViewPager
        pager = new ViewPager2(mContext);
        pager.setId(View.generateViewId());
        c.constrainHeight(pager.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.constrainWidth(pager.getId(), ConstraintSet.MATCH_CONSTRAINT);
        c.connect(pager.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        c.connect(pager.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        c.connect(pager.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        c.connect(pager.getId(), ConstraintSet.BOTTOM, bottomPointsLayout.getId(), ConstraintSet.TOP);

        // 将view添加到布局中
        this.addView(pager);
        this.addView(bottomPointsLayout);
        c.applyTo(this);
    }

    /**
     * 链接ViewPager和BannerAdapter
     */
    private void attachPagerAdapter() {
        // 注册滑动监听器
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    currentTimeCount = 0;
                    isOnDrag = true;
                } else {
                    isOnDrag = false;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // previousPos != position ：节约系统性能，防止多次执行浪费系统资源
                if (previousPos != position && pointViews.size() == itemCount) {
                    setPointBackground(lastImagePoint, pointBackgroundUnSelected);
                    lastImagePoint = pointViews.get(position % itemCount);
                    setPointBackground(lastImagePoint, pointBackgroundSelected);
                    previousPos = position;
                }
            }
        });
        pager.setAdapter(bannerAdapter);
        pager.setCurrentItem(bannerAdapter.getStartItem(), false);
    }

    /**
     * 生成小圆点及其样式
     */
    private void constructPoints() {
        for (int i = 0; i < itemCount; i++) {
            final ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pointSize, pointSize);
            layoutParams.leftMargin = pointMargin;
            imageView.setLayoutParams(layoutParams);
            setPointBackground(imageView, pointBackgroundUnSelected);
            final int imagePosition = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentItem = pager.getCurrentItem();
                    int offsetItem = imagePosition - currentItem % itemCount;
                    MyPagerHelper.setCurrentItem(pager, currentItem + offsetItem, clickScrollDuration);
                    currentTimeCount = 0;
                }
            });
            pointsContainer.addView(imageView);
            pointViews.add(imageView);
            if (i == 0) {
                lastImagePoint = imageView;
                setPointBackground(imageView, pointBackgroundSelected);
            }
        }
    }

    /**
     * 设置小圆点背景
     *
     * @param point
     * @param value
     * @return
     */
    private ImageView setPointBackground(ImageView point, int value) {
        if (isResSet) {
            point.setBackgroundResource(value);
        } else {
            point.setBackgroundColor(value);
        }
        return point;
    }

    /**
     * 开始自动轮播
     */
    private void startScroll() {
        if (isAutoScroll) {
            final MyHandler handler = new MyHandler(pager);
            final long sleepTime = 10;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isAutoScroll) {
                            Thread.sleep(sleepTime);
                            if (!isOnDrag) {
                                currentTimeCount += sleepTime;
                                if (currentTimeCount >= scrollTime) {
                                    currentTimeCount = 0;
                                    int currentItem = pager.getCurrentItem();
                                    if (currentItem < bannerAdapter.getItemCount() - 1) {
                                        currentItem++;
                                    } else {
                                        currentItem = bannerAdapter.getStartItem();
                                    }
                                    handler.sendEmptyMessage(currentItem);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.d("MyLog", "run: " + e.toString());
                    }
                }
            }).start();
        }
    }

    /**
     * 自定义Handler，防止内存泄漏
     */
    static class MyHandler extends Handler {
        private ViewPager2 viewPager;

        MyHandler(ViewPager2 viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyPagerHelper.setCurrentItem(viewPager, msg.what, autoScrollDuration);
        }
    }

    /**
     * Viewpager2辅助工具
     */
    private static class MyPagerHelper {
        /**
         * 保存前一个animatedValue
         */
        private static int previousValue;

        /**
         * 设置当前Item
         * @param pager    viewpager2
         * @param item     下一个跳转的item
         * @param duration scroll时长
         */
        static void setCurrentItem(final ViewPager2 pager, int item, long duration) {
            previousValue = 0;
            int currentItem = pager.getCurrentItem();
            int pagePxWidth = pager.getWidth();
            int pxToDrag = pagePxWidth * (item - currentItem);
            final ValueAnimator animator = ValueAnimator.ofInt(0, pxToDrag);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    float currentPxToDrag = (float) (currentValue - previousValue);
                    pager.fakeDragBy(-currentPxToDrag);
                    previousValue = currentValue;
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    pager.beginFakeDrag();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    pager.endFakeDrag();
                }

                @Override
                public void onAnimationCancel(Animator animation) { }

                @Override
                public void onAnimationRepeat(Animator animation) { }
            });
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(duration);
            animator.start();
        }
    }
}
