package com.example.kotlin.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

internal open class BaseActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var activeFragment = Fragment()

    protected fun addFragment(fragment: Fragment, container: Int)  {

        if ( fragmentManager.findFragmentByTag(fragment.tag) == null ) {
            fragmentManager.beginTransaction()
                .add(container, fragment, fragment.tag)
                .hide(fragment)
                .commit()

            activeFragment = fragment
        }
    }

   protected fun showFragment(fragment: Fragment){

        fragmentManager
            .beginTransaction()
            .hide(activeFragment)
            .show(fragment).commit()

        activeFragment = fragment

    }

}