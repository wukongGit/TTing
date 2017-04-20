package io.sunc.mj.ui.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.appthemeengine.ATE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sunc.mj.Constants;
import io.sunc.mj.ListenerApp;
import io.sunc.mj.MusicPlayer;
import io.sunc.mj.R;
import io.sunc.mj.RxBus;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.event.MetaChangedEvent;
import io.sunc.mj.injector.component.ApplicationComponent;
import io.sunc.mj.injector.component.DaggerRecommendDetailComponent;
import io.sunc.mj.injector.component.RecommendDetailComponent;
import io.sunc.mj.injector.module.RecommendDetailModule;
import io.sunc.mj.mvp.contract.RecommendDetailContract;
import io.sunc.mj.ui.adapter.RecommendDetailListAdapter;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.ColorUtil;
import io.sunc.mj.util.DensityUtil;
import io.sunc.mj.util.ListenerUtil;
import io.sunc.mj.util.PreferencesUtility;
import io.sunc.mj.widget.DividerItemDecoration;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendDetailFragment extends Fragment implements RecommendDetailContract.View {

    @Inject
    RecommendDetailContract.Presenter mPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.fab_play)
    FloatingActionButton fabPlay;
    @BindView(R.id.album_art)
    ImageView albumArt;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private PreferencesUtility mPreferences;
    private Context context;
    private RecommendDetailListAdapter mAdapter;
    private long albumID = -1;
    private String albumName;
    private String coverImg;
    private int primaryColor = -1;
    private List<RecommendDetailList.Track> mSongList = new ArrayList<>();
    private long[] mSongIDs;
    private int mCurrentPosition;

    public static RecommendDetailFragment newInstance(long id, String name, boolean useTransition, String transitionName, String coverImg) {
        RecommendDetailFragment fragment = new RecommendDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ALBUM_ID, id);
        args.putString(Constants.ALBUM_NAME, name);
        args.putBoolean("transition", useTransition);
        if (useTransition)
            args.putString("transition_name", transitionName);
        args.putString("cover_img", coverImg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
        mPresenter.attachView(this);

        if (getArguments() != null) {
            albumID = getArguments().getLong(Constants.ALBUM_ID);
            albumName = getArguments().getString(Constants.ALBUM_NAME);
            coverImg = getArguments().getString("cover_img");
        }
        context = getActivity();
        mPreferences = PreferencesUtility.getInstance(context);
        mAdapter = new RecommendDetailListAdapter(getActivity(), new RecommendDetailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int i) {
                mCurrentPosition = i;
                mPresenter.loadMusic(mSongList.get(i).getId());
            }
        }, null);
    }

    private void injectDependences() {
        ApplicationComponent applicationComponent = ((ListenerApp) getActivity().getApplication()).getApplicationComponent();
        RecommendDetailComponent albumSongsComponent = DaggerRecommendDetailComponent.builder()
                .applicationComponent(applicationComponent)
                .recommendDetailModule(new RecommendDetailModule())
                .build();
        albumSongsComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommend_detail, container, false);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            root.findViewById(R.id.app_bar).setFitsSystemWindows(false);
            root.findViewById(R.id.album_art).setFitsSystemWindows(false);
            root.findViewById(R.id.gradient).setFitsSystemWindows(false);
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
            CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.height += DensityUtil.getStatusBarHeight(getContext());
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPadding(0, DensityUtil.getStatusBarHeight(getContext()), 0, 0);
        }
        return root;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ATE.apply(this, ATEUtil.getATEKey(context));

        if (getArguments().getBoolean("transition")) {
            albumArt.setTransitionName(getArguments().getString("transition_name"));
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, false));

        setupToolbar();

        mPresenter.subscribe(albumID);
        subscribeMetaChangedEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
        RxBus.getInstance().unSubscribe(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        if (primaryColor != -1 && getActivity() != null) {
            collapsingToolbarLayout.setContentScrimColor(primaryColor);
            collapsingToolbarLayout.setStatusBarScrimColor(ColorUtil.getStatusBarColor(primaryColor));
        }
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(albumName);
    }

    @Override
    public void showAlbumSongs(List<RecommendDetailList.Track> songList) {
        mSongList = songList;
        mSongIDs = getSongIds();
        mAdapter.setSongList(songList);
        //ListenerUtil.insertNetMusic(getContext(), mSongList);
    }

    public long[] getSongIds() {
        int songNum = mSongList.size();
        long[] ret = new long[songNum];
        for (int i = 0; i < songNum; i++) {
            ret[i] = mSongList.get(i).getId();
        }

        return ret;
    }

    @Override
    public void showAlbumArt(Bitmap bitmap) {
        albumArt.setImageBitmap(bitmap);
        if (ATEUtil.getATEKey(getActivity()).equals("dark_theme")) {
            return;
        }
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = ColorUtil.getMostPopulousSwatch(palette);
                if (swatch != null) {
                    int color = swatch.getRgb();
                    collapsingToolbarLayout.setContentScrimColor(color);
                    collapsingToolbarLayout.setStatusBarScrimColor(ColorUtil.getStatusBarColor(color));
                    primaryColor = color;
                }
            }
        });
    }

    @Override
    public void startMusic(String url) {
        RecommendDetailList.Track music = mSongList.get(mCurrentPosition);
        music.setMp3_url(url);
        //ListenerUtil.updateNetMusic(getContext(), music);
        List<RecommendDetailList.Track> tracks = new ArrayList<>();
        tracks.add(music);
        ListenerUtil.insertNetMusic(getContext(), tracks);
        final long[] ids = new long[1];
        ids[0] = mSongList.get(mCurrentPosition).getId();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MusicPlayer.playAll(getContext(), ids, 0, -1, ListenerUtil.IdType.NA, false);
            }
        }, 100);

    }

    @Override
    public String getCoverImg() {
        return coverImg;
    }

    @Override
    public void showAlbumArt(Drawable drawable) {
        albumArt.setImageDrawable(drawable);
        primaryColor = ATEUtil.getThemePrimaryColor(getContext());
        collapsingToolbarLayout.setContentScrimColor(primaryColor);
        collapsingToolbarLayout.setStatusBarScrimColor(ColorUtil.getStatusBarColor(primaryColor));
    }

    @OnClick(R.id.fab_play)
    public void onFabPlayClick() {
        if(mSongList == null || mSongList.size() < 1) {
            return;
        }
        mCurrentPosition = 0;
        mPresenter.loadMusic(mSongList.get(0).getId());
    }

    private void subscribeMetaChangedEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(MetaChangedEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe(new Action1<MetaChangedEvent>() {
                    @Override
                    public void call(MetaChangedEvent event) {
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
    }
}
