package io.sunc.mj.injector.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.injector.scope.PerActivity;

/**
 * Created by hefuyi on 2016/11/1.
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Context provideContext(){
        return mActivity;
    }
}
