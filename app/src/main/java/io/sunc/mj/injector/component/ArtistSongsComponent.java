package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ArtistSongModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.ArtistMusicFragment;

/**
 * Created by hefuyi on 2016/11/25.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ArtistSongModule.class)
public interface ArtistSongsComponent {

    void inject(ArtistMusicFragment artistMusicFragment);
}
