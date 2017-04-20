package io.sunc.mj.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.appthemeengine.ATE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.ListenerApp;
import io.sunc.mj.MusicPlayer;
import io.sunc.mj.R;
import io.sunc.mj.RxBus;
import io.sunc.mj.api.LoadType;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.event.MediaUpdateEvent;
import io.sunc.mj.injector.component.ApplicationComponent;
import io.sunc.mj.injector.component.DaggerSearchComponent;
import io.sunc.mj.injector.component.SearchComponent;
import io.sunc.mj.injector.module.SearchModule;
import io.sunc.mj.mvp.contract.SearchContract;
import io.sunc.mj.provider.SearchHistory;
import io.sunc.mj.ui.adapter.RecommendDetailListAdapter;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.DensityUtil;
import io.sunc.mj.util.ListenerUtil;
import io.sunc.mj.widget.EndlessScrollListener;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnTouchListener,SearchContract.View, EndlessScrollListener.IMore{
    @Inject
    SearchContract.Presenter mPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.status_bar)
    View statusBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.view_empty)
    View emptyView;

    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;
    private RecommendDetailListAdapter adapter;
    private List<RecommendDetailList.Track> mSongList = new ArrayList<>();
    private long[] mSongIDs;
    private int mCurrent;

    private boolean mHasMore, mIsLoading;
    private LoadType mLoadType;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
        mPresenter.attachView(this);

        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter = new RecommendDetailListAdapter(getActivity(), new RecommendDetailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int i) {
                mPresenter.loadMusic(mSongList.get(i).getId());
                mCurrent = i;
            }
        }, this);
    }

    private void injectDependences() {
        ApplicationComponent applicationComponent = ((ListenerApp) getActivity().getApplication()).getApplicationComponent();
        SearchComponent searchComponent = DaggerSearchComponent.builder()
                .applicationComponent(applicationComponent)
                .searchModule(new SearchModule())
                .build();
        searchComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ATE.apply(this, ATEUtil.getATEKey(getActivity()));

        if (Build.VERSION.SDK_INT < 21 && view.findViewById(R.id.status_bar) != null) {
            view.findViewById(R.id.status_bar).setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 19) {
                int statusBarHeight = DensityUtil.getStatusBarHeight(getContext());
                view.findViewById(R.id.toolbar).setPadding(0, statusBarHeight, 0, 0);
            }
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        recyclerView.setOnTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new EndlessScrollListener(this));
        recyclerView.setAdapter(adapter);

        subscribeMediaUpdateEvent();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideInputManager();
        mPresenter.unsubscribe();
        RxBus.getInstance().unSubscribe(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Activity activity = getActivity();
                activity.onBackPressed();
                return true;
            }
        });

        menu.findItem(R.id.menu_search).expandActionView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        hideInputManager();

        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        if (newText.equals(queryString)) {
            return true;
        }

        queryString = newText;

        if (queryString.trim().equals("")) {
            mHasMore = false;
            mSongList.clear();
            adapter.updateSearchResults(mSongList);
            adapter.notifyDataSetChanged();
        } else {
            mLoadType = LoadType.New;
            mIsLoading = true;
            mPresenter.search(newText, LoadType.New);
        }

        return true;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                mSearchView.clearFocus();
            }
        }
        return false;
    }

    private void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();

            SearchHistory.getInstance(getContext()).addSearchString(queryString);
        }
    }

    @Override
    public void showSearchResult(List<RecommendDetailList.Track> list) {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(LoadType.New == mLoadType) {
            mSongList.clear();
        }
        mSongList.addAll(list);
        mSongIDs = getSongIds();
        adapter.updateSearchResults(mSongList);
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
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startMusic(String url) {
        RecommendDetailList.Track music = mSongList.get(mCurrent);
        music.setMp3_url(url);
        List<RecommendDetailList.Track> tracks = new ArrayList<>();
        tracks.add(music);
        ListenerUtil.insertNetMusic(getContext(), tracks);
        final long[] ids = new long[1];
        ids[0] = mSongList.get(mCurrent).getId();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MusicPlayer.playAll(getContext(), ids, 0, -1, ListenerUtil.IdType.NA, false);
            }
        }, 100);
    }

    @Override
    public void setHasMore(boolean hasMore) {
        mIsLoading = false;
        mHasMore = hasMore;
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
                        mLoadType = LoadType.New;
                        mIsLoading = true;
                        mPresenter.search(queryString, LoadType.New);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        RxBus.getInstance().addSubscription(this, subscription);
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean canShow() {
        return hasMore() || (mLoadType == LoadType.More && isLoading());
    }

    @Override
    public boolean hasMore() {
        return mHasMore;
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void loadMore() {
        mIsLoading = true;
        mLoadType = LoadType.More;
        mPresenter.search(queryString, LoadType.More);
    }
}
