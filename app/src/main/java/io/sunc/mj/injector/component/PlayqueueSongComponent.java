package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.PlayqueueSongModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.dialogs.PlayqueueDialog;

/**
 * Created by hefuyi on 2016/12/27.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PlayqueueSongModule.class)
public interface PlayqueueSongComponent {

    void inject(PlayqueueDialog playqueueDialog);
}
