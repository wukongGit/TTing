package io.sunc.mj.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.MusicPlayer;
import io.sunc.mj.R;
import io.sunc.mj.RxBus;
import io.sunc.mj.listener.PanelSlideListener;
import io.sunc.mj.permission.PermissionCallback;
import io.sunc.mj.permission.PermissionManager;
import io.sunc.mj.ui.fragment.MainFragment;
import io.sunc.mj.ui.fragment.SearchFragment;
import io.sunc.mj.util.ATEUtil;
import io.sunc.mj.util.ListenerUtil;

public class MainActivity extends BaseActivity implements ATEActivityThemeCustomizer {

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
    private String action;
    private PanelSlideListener mPanelSlideListener;
    private boolean listenerSeted = false;
    private boolean isDarkTheme;

    private final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            loadEverything();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        action = getIntent().getAction();

        isDarkTheme = ATEUtil.getATEKey(this).equals("dark_theme");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_list_viewpager);
        ButterKnife.bind(this);

        if (ListenerUtil.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
            loadEverything();
        }

        addBackstackListener();

        if (Intent.ACTION_VIEW.equals(action)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.clearQueue();
                    MusicPlayer.openFile(getIntent().getData().getPath());
                    MusicPlayer.playOrPause();
                }
            }, 350);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
        RxBus.getInstance().unSubscribe(mPanelSlideListener);
    }

    private void loadEverything() {
        Fragment mainFragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment).commitAllowingStateLoss();
        new initQuickControls().execute("");
    }

    public void search() {
        Fragment searchFragment = new SearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, searchFragment);
        transaction.addToBackStack(null).commit();
    }

    private void checkPermissionAndThenLoad() {
        //check for permission
        if (PermissionManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadEverything();
        } else {
            if (PermissionManager.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(panelLayout, "Listener will need to read external storage to display songs on your device.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionManager.askForPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
                            }
                        }).show();
            } else {
                PermissionManager.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
            }
        }
    }

    private void addBackstackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                getSupportFragmentManager().findFragmentById(R.id.fragment_container).onResume();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                search();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!listenerSeted) {
            mPanelSlideListener = new PanelSlideListener(panelLayout);
            panelLayout.addPanelSlideListener(mPanelSlideListener);
            listenerSeted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getActivityTheme() {
        return isDarkTheme ? R.style.AppThemeDark : R.style.AppThemeLight;
    }

}
