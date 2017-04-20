package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.FolderModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.FoldersFragment;

/**
 * Created by hefuyi on 2016/12/11.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = FolderModule.class)
public interface FoldersComponent {

    void inject(FoldersFragment foldersFragment);
}
