package com.example.myapplication.Helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

public class BottomNavigationBehaviour extends CoordinatorLayout.Behavior {

    public BottomNavigationBehaviour() {
    }

    public BottomNavigationBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        child.setTranslationY(Math.max(0f,
                Math.min(Float.parseFloat(String.valueOf(child.getHeight())),child.getTranslationY()+dyConsumed)));
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout)
            updateSnackbar(child,dependency);
        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackbar(View child, View dependency) {
        if (dependency.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();

            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            params.gravity = Gravity.TOP;
            dependency.setLayoutParams(params);
        }
    }
}
