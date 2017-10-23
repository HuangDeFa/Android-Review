package com.kenzz.reviewapp.bean;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ken.huang on 10/23/2017.
 */

@Module()
public class TestModule {

    @Provides
    @Named("wnm")
    public User createPerson(){
        User user = new User("王尼玛",User.GENDER_MALE);
        return user;
    }
    @Provides
    @Named("wnmF")
    public User createOtherPerson(){
        User user = new User("王尼妹",User.GENDER_FElMALE);
        return user;
    }
}
