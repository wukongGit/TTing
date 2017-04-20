package io.sunc.mj.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sunc.mj.R;
import io.sunc.mj.util.ListenerUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Date: 2017-04-19 17:35
 * Author: suncheng
 */
public class WelcomeActivity extends Activity {
    @BindView(R.id.iv_welcome_bg)
    ImageView mIvWelcomeBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        showContent(getImgData());
        startCountDown();
    }

    private void startCountDown() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        jumpToMain();
                    }
                });
    }

    private void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showContent(List<String> list) {
        if (list != null) {
            int page = ListenerUtil.getRandomNumber(0, list.size() - 1);
            Glide.with(this).load(list.get(page)).into(mIvWelcomeBg);
            mIvWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        }

    }

    private List<String> getImgData() {
        List<String> imgs = new ArrayList<>();
        imgs.add("file:///android_asset/a.jpg");
        imgs.add("file:///android_asset/b.jpg");
        imgs.add("file:///android_asset/c.jpg");
        imgs.add("file:///android_asset/d.jpg");
        imgs.add("file:///android_asset/e.jpg");
        imgs.add("file:///android_asset/f.jpg");
        return imgs;
    }
}
