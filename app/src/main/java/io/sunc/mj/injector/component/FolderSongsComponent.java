package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.FolderSongsModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.FolderSongsFragment;

/**
 * Created by hefuyi on 2016/12/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = FolderSongsModule.class)
public interface FolderSongsComponent {

    void inject(FolderSongsFragment folderSongsFragment);
}
