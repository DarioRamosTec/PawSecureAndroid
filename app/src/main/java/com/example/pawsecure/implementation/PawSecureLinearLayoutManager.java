package com.example.pawsecure.implementation;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class PawSecureLinearLayoutManager extends LinearLayoutManager {

    private int smoothScrollDuration;

    public PawSecureLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return smoothScrollDuration;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public int getSmoothScrollDuration() {
        return smoothScrollDuration;
    }

    public void setSmoothScrollDuration(int smoothScrollDuration) {
        this.smoothScrollDuration = smoothScrollDuration;
    }
}
