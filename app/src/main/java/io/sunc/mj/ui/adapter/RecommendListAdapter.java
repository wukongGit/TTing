package io.sunc.mj.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.R;
import io.sunc.mj.api.model.RecommendList;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.ListenerUtil;
import io.sunc.mj.util.NavigationUtil;
import io.sunc.mj.widget.EndlessScrollListener;
import io.sunc.mj.widget.fastscroller.FastScrollRecyclerView;

public class RecommendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_MORE = 1;

    protected EndlessScrollListener.IMore mMore;
    private List<RecommendList> arraylist;
    private AppCompatActivity mContext;

    public RecommendListAdapter(AppCompatActivity context, List<RecommendList> arraylist, EndlessScrollListener.IMore more) {
        if (arraylist == null) {
            this.arraylist = new ArrayList<>();
        } else {
            this.arraylist = arraylist;

        }
        this.mContext = context;
        this.mMore = more;
    }

    public void setSongList(List<RecommendList> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_DATA: {
                holder = new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommend_list, viewGroup, false));
                break;
            }
            case TYPE_MORE: {
                holder = new MoreHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_more, viewGroup, false), mMore);
                break;
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DATA: {
                ItemHolder itemHolder = (ItemHolder) holder;
                RecommendList localItem = arraylist.get(position);

                itemHolder.mTvCount.setText(localItem.getPlayCount());
                itemHolder.mTvName.setText(localItem.getName());

                Glide.with(holder.itemView.getContext()).load(localItem.getCoverImgUrl())
                        .error(ATEUtil.getDefaultAlbumDrawable(mContext))
                        .placeholder(ATEUtil.getDefaultAlbumDrawable(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(itemHolder.mIvCover);

                if (ListenerUtil.isLollipop())
                    itemHolder.mIvCover.setTransitionName("transition_recommend_art" + position);
                break;
            }
            case TYPE_MORE: {
                MoreHolder moreHolder = (MoreHolder) holder;
                moreHolder.updateStatus(mMore);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        int more = mMore.canShow() ? 1 : 0;
        int count = arraylist == null ? 0 : arraylist.size();
        return more + count;
    }

    @Override
    public int getItemViewType(int position) {
        boolean hasMore = mMore.canShow();
        if (hasMore && position == getItemCount() - 1) {
            return TYPE_MORE;
        } else {
            return TYPE_DATA;
        }
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return "hhhhh";
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_cover)
        ImageView mIvCover;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.tv_name)
        TextView mTvName;

        public ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecommendList recommendList = arraylist.get(getAdapterPosition());
            NavigationUtil.navigateToRecommend(mContext, recommendList.getId(), recommendList.getName(),
                    new Pair<View, String>(mIvCover, "transition_recommend_art" + getAdapterPosition()), recommendList.getCoverImgUrl());

        }
    }

}


