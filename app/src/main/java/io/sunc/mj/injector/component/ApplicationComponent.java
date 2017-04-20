package io.sunc.mj.injector.component;

import android.app.Application;

import dagger.Component;
import io.sunc.mj.ListenerApp;
import io.sunc.mj.injector.module.ApplicationModule;
import io.sunc.mj.injector.module.NetworkModule;
import io.sunc.mj.injector.scope.PerApplication;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/3.
 */
@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    Application application();

    ListenerApp listenerApplication();

    Repository repository();
}
