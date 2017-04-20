package io.sunc.mj.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.MusicPlayer;
import io.sunc.mj.R;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.ListenerUtil;

/**
 * Created by hefuyi on 2017/1/3.
 */

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.ItemHolder>{

    private Activity mContext;
    private List<RecommendDetailList.Track> searchResults = Collections.emptyList();

    public SearchAdapter(Activity context) {
        this.mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_linear_layout_item, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        RecommendDetailList.Track localItem = searchResults.get(position);
        itemHolder.title.setText(localItem.getName());
        itemHolder.artist.setText(localItem.getAr().get(0).getName());
        itemHolder.album.setText(localItem.getName());

        Glide.with(mContext)
                .load(localItem.getAl().getPicUrl())
                .placeholder(ATEUtil.getDefaultAlbumDrawable(mContext))
                .centerCrop()
                .into(itemHolder.albumArt);

        itemHolder.title.setTextColor(ATEUtil.getThemeTextColorPrimary(mContext));

        //setOnPopupMenuListener(itemHolder, position);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private void setSongPopupMenu(ItemHolder itemHolder, final int position) {
        final RecommendDetailList.Track song = searchResults.get(position);
//        itemHolder.popupMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final PopupMenu menu = new PopupMenu(mContext, v);
//                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.popup_song_play_next:
//                                MusicPlayer.playNext(mContext, new long[]{song.id}, -1, ListenerUtil.IdType.NA);
//                                break;
//                            case R.id.popup_song_goto_album:
//                                NavigationUtil.goToAlbum(mContext, song.albumId, song.title);
//                                break;
//                            case R.id.popup_song_goto_artist:
//                                NavigationUtil.goToArtist(mContext, song.artistId, song.artistName);
//                                break;
//                            case R.id.popup_song_addto_queue:
//                                MusicPlayer.addToQueue(mContext, new long[]{song.id}, -1, ListenerUtil.IdType.NA);
//                                break;
//                            case R.id.popup_song_addto_playlist:
//                                ListenerUtil.showAddPlaylistDialog(mContext, new long[]{song.id});
//                                break;
//                            case R.id.popup_song_delete:
//                                ListenerUtil.showDeleteDialog(mContext, song.title, new long[]{song.id},
//                                        new MaterialDialog.SingleButtonCallback() {
//                                            @Override
//                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                                searchResults.remove(position);
//                                                notifyItemRemoved(position);
//                                            }
//                                        });
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                menu.inflate(R.menu.popup_song);
//                menu.show();
//            }
//        });
    }






    public void updateSearchResults(List searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
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
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    long[] ret = new long[1];
                    ret[0] = searchResults.get(getAdapterPosition()).getId();
                    MusicPlayer.playAll(mContext, ret, 0, -1, ListenerUtil.IdType.NA, false);
                }
            }, 100);
        }

    }
}
