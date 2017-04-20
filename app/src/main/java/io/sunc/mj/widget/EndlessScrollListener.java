package io.sunc.mj.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 3;
    private RecyclerView.OnScrollListener mOnScrollListener;

    private IMore mMore;

    public EndlessScrollListener(IMore more) {
        this(3, more);
    }

    public EndlessScrollListener(int visibleThreshold, IMore more) {
        this.visibleThreshold = visibleThreshold;
        this.mMore = more;
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(recyclerView, newState);
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE && (mMore.hasMore()
                && !mMore.isLoading()
                && mMore.canLoad()
                && isReachLast(recyclerView))) {
            mMore.loadMore();
        }
    }

    private boolean isReachLast(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int itemCount = recyclerView.getAdapter().getItemCount();
        if(layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() >= itemCount - visibleThreshold;
        } else if(layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager)layoutManager).findLastVisibleItemPosition() >= itemCount - visibleThreshold;
        }
        return false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrolled(recyclerView, dx, dy);
        }
    }

    public interface IMore {
        /**
         * @return 是否允许加载更多
         */
        boolean canLoad();

        /**
         * @return 是否允许显示加载更多界面
         */
        boolean canShow();

        /**
         * @return 是否有更多
         */
        boolean hasMore();

        /**
         * @return 正在加载
         */
        boolean isLoading();

        /**
         * 加载更多
         */
        void loadMore();
    }
}