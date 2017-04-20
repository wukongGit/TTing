package io.sunc.mj.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.sunc.mj.R;
import io.sunc.mj.widget.EndlessScrollListener;


public class MoreHolder extends RecyclerView.ViewHolder {

    TextView mMoreInfo;
    RelativeLayout mMore;
    LinearLayout mLoading;
    RelativeLayout mRoot;

    public MoreHolder(View view, final EndlessScrollListener.IMore more) {
        super(view);
        mRoot = (RelativeLayout) view.findViewById(R.id.root);
        mMore = (RelativeLayout) view.findViewById(R.id.more);
        mMoreInfo = (TextView) view.findViewById(R.id.more_info);
        mLoading = (LinearLayout) view.findViewById(R.id.loading);
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.loadMore();
            }
        });

        mMore.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }

    public void updateStatus(EndlessScrollListener.IMore more) {
        if (more.isLoading()) {
            mMore.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
        } else if (more.hasMore()) {
            mMore.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            mMoreInfo.setText("点击加载更多");
            mMore.setClickable(true);
        } else {
            mMore.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            mMoreInfo.setText("下面没有了");
            mMore.setClickable(false);
        }
    }
}