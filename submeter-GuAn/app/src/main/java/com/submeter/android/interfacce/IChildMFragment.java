package com.submeter.android.interfacce;

import android.support.v4.app.Fragment;

public interface IChildMFragment {
        public Fragment getFragment();

        public void pageShow();

        public void pageHide();

        public boolean getStatusbarDardMode();

        void setNum(String all, String zc, String wg);
    }