package com.neaniesoft.concurrency;

import android.support.annotation.NonNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);

}
