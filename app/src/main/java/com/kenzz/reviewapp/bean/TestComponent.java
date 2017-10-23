package com.kenzz.reviewapp.bean;

import com.kenzz.reviewapp.MainActivity;
import com.kenzz.reviewapp.annotation.PerApp;

import dagger.Component;

/**
 * Created by ken.huang on 10/23/2017.
 *
 */

@PerApp
@Component(modules = TestModule.class,dependencies = BaseComponent.class)
public interface TestComponent {
    void inject(MainActivity mainActivity);
}
