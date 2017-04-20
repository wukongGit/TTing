package io.sunc.mj.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.appthemeengine.ATE;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sunc.mj.Constants;
import io.sunc.mj.ListenerApp;
import io.sunc.mj.R;
import io.sunc.mj.RxBus;
import io.sunc.mj.event.FavourateSongEvent;
import io.sunc.mj.event.MediaUpdateEvent;
import io.sunc.mj.event.MetaChangedEvent;
import io.sunc.mj.event.RecentlyPlayEvent;
import io.sunc.mj.injector.component.ApplicationComponent;
import io.sunc.mj.injector.component.DaggerSongsComponent;
import io.sunc.mj.injector.component.SongsComponent;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.SongsModule;
import io.sunc.mj.mvp.contract.SongsContract;
import io.sunc.mj.mvp.model.Song;
import io.sunc.mj.ui.activity.MainActivity;
import io.sunc.mj.ui.adapter.SongsListAdapter;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.PreferencesUtility;
import io.sunc.mj.util.SortOrder;
import io.sunc.mj.widget.DividerItemDecoration;
import io.sunc.mj.widget.fastscroller.FastScrollRecyclerView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongsFragment extends Fragment implements SongsContract.View {

    @Inject
    SongsContract.Presenter mPresenter;
    @BindView(R.id.recyclerview)
    FastScrollRecyclerView recyclerView;
    @BindView(R.id.view_empty)
    View emptyView;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    private SongsListAdapter mAdapter;
    private PreferencesUtility mPreferences;
    private String action;

    public static SongsFragment newInstance(String action) {

        Bundle args = new Bundle();
        switch (action) {
            case Constants.NAVIGATE_ALLSONG:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_FAVOURATE:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            default:
                throw new RuntimeException("wrong action type");
        }
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
        mPresenter.attachView(this);
        mPreferences = PreferencesUtility.getInstance(getActivity());

        if (getArguments() != null) {
            action = getArguments().getString(Constants.PLAYLIST_TYPE);
        }

        mAdapter = new SongsListAdapter((AppCompatActivity) getActivity(), null, action, false);
    }

    public void injectDependences() {
        ApplicationComponent applicationComponent = ((ListenerApp) getActivity().getApplication()).getApplicationComponent();
        SongsComponent songsComponent = DaggerSongsComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .songsModule(new SongsModule())
                .build();
        songsComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ATE.apply(this, ATEUtil.getATEKey(getActivity()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, true));

        mPresenter.loadSongs(action);

        if (Constants.NAVIGATE_PLAYLIST_FAVOURATE.equals(action)) {
            subscribeFavourateSongEvent();
        } else if (Constants.NAVIGATE_PLAYLIST_RECENTPLAY.equals(action)) {
            subscribeRecentlyPlayEvent();
        } else {
            subscribeMediaUpdateEvent();
        }
        subscribeMetaChangedEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
        RxBus.getInstance().unSubscribe(this);
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

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.song_sort_by, menu);
        if (!Constants.NAVIGATE_ALLSONG.equals(action)) {
            menu.findItem(R.id.menu_sort_by).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_az:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_A_Z);
                mPresenter.loadSongs(action);
                return true;
            case R.id.menu_sort_by_za:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_Z_A);
                mPresenter.loadSongs(action);
                return true;
            case R.id.menu_sort_by_add:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_DATE);
                mPresenter.loadSongs(action);
                return true;
            case R.id.menu_sort_by_artist:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ARTIST);
                mPresenter.loadSongs(action);
                return true;
            case R.id.menu_sort_by_album:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ALBUM);
                mPresenter.loadSongs(action);
                return true;
            case R.id.menu_sort_by_duration:
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_DURATION);
                mPresenter.loadSongs(action);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSongs(List<Song> songList) {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter.setSongList(songList);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void subscribeFavourateSongEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(FavourateSongEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FavourateSongEvent>() {
                    @Override
                    public void call(FavourateSongEvent event) {
                        mPresenter.loadSongs(action);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    private void subscribeRecentlyPlayEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(RecentlyPlayEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RecentlyPlayEvent>() {
                    @Override
                    public void call(RecentlyPlayEvent event) {
                        mPresenter.loadSongs(action);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    private void subscribeMediaUpdateEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(MediaUpdateEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Action1<MediaUpdateEvent>() {
                    @Override
                    public void call(MediaUpdateEvent event) {
                        mPresenter.loadSongs(action);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    @OnClick(R.id.ll_search)
    public void onClick() {
        Activity activity = getActivity();
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).search();
        }
    }
}
