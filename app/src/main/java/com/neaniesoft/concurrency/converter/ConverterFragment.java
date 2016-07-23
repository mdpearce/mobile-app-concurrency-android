package com.neaniesoft.concurrency.converter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neaniesoft.concurrency.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConverterFragment extends Fragment {

    public ConverterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }
}
