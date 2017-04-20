package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ArtistInfoModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.adapter.ArtistAdapter;
import io.sunc.mj.ui.fragment.ArtistDetailFragment;

/**
 * Created by hefuyi on 2016/11/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ArtistInfoModule.class)
public interface ArtistInfoComponent {

    void injectForAdapter(ArtistAdapter artistAdapter);

    void injectForFragment(ArtistDetailFragment fragment);
}
