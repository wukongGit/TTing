package io.sunc.mj.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.appthemeengine.ATE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sunc.mj.ListenerApp;
import io.sunc.mj.R;
import io.sunc.mj.RxBus;
import io.sunc.mj.api.LoadType;
import io.sunc.mj.api.model.RecommendList;
import io.sunc.mj.event.MetaChangedEvent;
import io.sunc.mj.injector.component.ApplicationComponent;
import io.sunc.mj.injector.component.DaggerRecommendComponent;
import io.sunc.mj.injector.component.RecommendComponent;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.RecommendModule;
import io.sunc.mj.mvp.contract.RecommendContract;
import io.sunc.mj.ui.activity.MainActivity;
import io.sunc.mj.ui.adapter.RecommendListAdapter;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.widget.DividerItemDecoration;
import io.sunc.mj.widget.EndlessScrollListener;
import io.sunc.mj.widget.fastscroller.FastScrollRecyclerView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements RecommendContract.View, EndlessScrollListener.IMore {

    @Inject
    RecommendContract.Presenter mPresenter;
    @BindView(R.id.recyclerview)
    FastScrollRecyclerView recyclerView;
    @BindView(R.id.view_empty)
    View emptyView;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    private RecommendListAdapter mAdapter;
    private boolean mHasMore, mIsLoading;
    private LoadType mLoadType;
    List<RecommendList> mSongList = new ArrayList<>();

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
        mPresenter.attachView(this);
        mAdapter = new RecommendListAdapter((AppCompatActivity) getActivity(), null, this);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollListener(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, true));

        mIsLoading = true;
        mLoadType = LoadType.New;
        mPresenter.loadSongs();
        subscribeMetaChangedEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
        RxBus.getInstance().unSubscribe(this);
    }

    public void injectDependences() {
        ApplicationComponent applicationComponent = ((ListenerApp) getActivity().getApplication()).getApplicationComponent();
        RecommendComponent recommendComponent = DaggerRecommendComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .recommendModule(new RecommendModule())
                .build();
        recommendComponent.inject(this);
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
    public void showSongs(List<RecommendList> songList) {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mSongList.addAll(songList);
        mAdapter.setSongList(mSongList);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
        mIsLoading = false;
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
        mPresenter.loadSongs();
    }

    @OnClick(R.id.ll_search)
    public void onClick() {
        Activity activity = getActivity();
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).search();
        }
    }
}
