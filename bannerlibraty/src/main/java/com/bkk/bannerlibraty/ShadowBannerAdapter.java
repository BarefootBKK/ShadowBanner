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

    private int layout;

    private List<T> list;

    private OnItemClickListener onItemClickListener;
    /**
     * 最大循环Cell基数
     */
    public final static int MAX_LOOPER_COUNT = 1000;

    public ShadowBannerAdapter(List<T> list, int layout) {
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.getInstance(parent, layout);
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getLogicItemCount() * MAX_LOOPER_COUNT;
    }

    public int getLogicItemCount() {
        return list == null ? 0 : list.size();
    }

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

    abstract void onItemCreate(Context context, ViewHolder holder, T item, int position);

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static ViewHolder getInstance(ViewGroup parent, int layout) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
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

    interface OnItemClickListener<E> {
        void onItemClick(E item, int position);
    }
}
