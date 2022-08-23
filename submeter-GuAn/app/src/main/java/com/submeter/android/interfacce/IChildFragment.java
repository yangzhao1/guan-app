package com.submeter.android.interfacce;

import android.support.v4.app.Fragment;

public interface IChildFragment {
        public Fragment getFragment();

        public void pageShow();

        public void pageHide();

        public boolean getStatusbarDardMode();
    }