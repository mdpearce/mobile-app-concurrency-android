package com.neaniesoft.concurrency.converter;

import com.neaniesoft.concurrency.BasePresenter;
import com.neaniesoft.concurrency.BaseView;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface ConverterContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
