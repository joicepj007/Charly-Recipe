package com.android.charly.di

import com.android.charly.ui.recipe.recipedetail.fragment.AddFragment
import com.android.charly.ui.recipe.recipelist.fragment.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


// declare all the fragments here , dependency of fragments are provided by this module

@Module
abstract class FragmentBuildersModule {

    // Method #1
    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    // Method #2
    @ContributesAndroidInjector
    abstract fun contributeAddFragment(): AddFragment

}