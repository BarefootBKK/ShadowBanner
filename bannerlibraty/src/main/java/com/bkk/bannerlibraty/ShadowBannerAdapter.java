package com.bkk.bannerlibraty;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ShadowBannerAdapter<T> extends RecyclerView.Adapter<ShadowBannerAdapter.ViewHolder> {
    /**
     * cell的布局文件
     */
    private int layout;
    /**
     * 是否设置了布局文件，否则使用默认的布局
     */
    private boolean isSetLayoutRes;
    /**
     * cell list
     */
    private List<T> list;
    /**
     * item点击listener
     */
    private OnItemClickListener<T> onItemClickListener;

    /**
     * 最大循环Cell基数
     */
    public final static int MAX_LOOPER_COUNT = 1000;

    public ShadowBannerAdapter(List<T> list) {
        this.list = list;
        this.isSetLayoutRes = false;
    }

    public ShadowBannerAdapter(List<T> list, int layout) {
        this.list = list;
        this.layout = layout;
        this.isSetLayoutRes = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.getInstance(parent, layout, isSetLayoutRes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (getLogicItemCount() > 0) {
            int logicPos = position % getLogicItemCount();
            final T item = list.get(logicPos);
            onItemCreate(holder.itemView.getContext(), holder, item, logicPos);
            /**
             * 设置点击监听器
             */
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition() % getLogicItemCount();
                        onItemClickListener.onItemClick(item, position);
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getLogicItemCount() * MAX_LOOPER_COUNT;
    }

    public int getLogicItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 计算开始Item的位置
     * @return the position of start item.
     */
    public int getStartItem() {
        int size = getLogicItemCount();
        if (getItemCount() <= size) {
            return 0;
        }
        int currentItem = size * MAX_LOOPER_COUNT / 2;
        while (currentItem % size != 0) {
            currentItem++;
        }
        return currentItem;
    }

    public abstract void onItemCreate(Context context, ViewHolder holder, T item, int position);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static ViewHolder getInstance(ViewGroup parent, int layout, boolean isSetLayoutRes) {
            View view;
            if (isSetLayoutRes) {
                view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            } else {
                view = CellViewGroup.titleContentCellView(parent.getContext());
            }
            return new ViewHolder(view);
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }

    public interface OnItemClickListener<E> {
        void onItemClick(E item, int position);
    }
}
