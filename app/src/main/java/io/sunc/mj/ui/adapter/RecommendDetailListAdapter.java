package io.sunc.mj.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.R;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.widget.EndlessScrollListener;

/**
 * Created by hefuyi on 2016/12/3.
 */

public class RecommendDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_MORE = 1;

    private List<RecommendDetailList.Track> arraylist;
    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    protected EndlessScrollListener.IMore mMore;

    public RecommendDetailListAdapter(Activity context, OnItemClickListener listener, EndlessScrollListener.IMore more) {
        this.mContext = context;
        this.mOnItemClickListener = listener;
        this.mMore = more;
    }

    public void updateSearchResults(List searchResults) {
        this.arraylist = searchResults;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_DATA: {
                holder = new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_linear_layout_item, viewGroup, false));
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
                RecommendDetailList.Track localItem = arraylist.get(position);
                itemHolder.title.setText(localItem.getName());
                itemHolder.artist.setText(localItem.getAr().get(0).getName());
                itemHolder.album.setText(localItem.getName());

                Glide.with(mContext)
                        .load(localItem.getAl().getPicUrl())
                        .placeholder(ATEUtil.getDefaultAlbumDrawable(mContext))
                        .centerCrop()
                        .into(itemHolder.albumArt);

                itemHolder.title.setTextColor(ATEUtil.getThemeTextColorPrimary(mContext));
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
        int more;
        if(mMore == null || !mMore.canShow()) {
            more = 0;
        } else {
            more = 1;
        }
        int count = arraylist == null ? 0 : arraylist.size();
        return more + count;
    }

    @Override
    public int getItemViewType(int position) {
        if(mMore == null) {
            return TYPE_DATA;
        }
        boolean hasMore = mMore.canShow();
        if (hasMore && position == getItemCount() - 1) {
            return TYPE_MORE;
        } else {
            return TYPE_DATA;
        }
    }

    public void setSongList(List<RecommendDetailList.Track> songList) {
        arraylist = songList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView albumArt;
        @BindView(R.id.text_item_title)
        TextView title;
        @BindView(R.id.text_item_subtitle)
        TextView artist;
        @BindView(R.id.text_item_subtitle_2)
        TextView album;

        public ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClicked(getAdapterPosition());
            }
        }
    }
}
