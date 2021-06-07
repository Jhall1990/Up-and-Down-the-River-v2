package com.example.upanddowntheriver.Backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.R;


public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;

    @SuppressLint("UseCompatLoadingForDrawables")
    public SimpleDividerItemDecoration(Context context) {
        divider = context.getDrawable(R.drawable.recycler_horizontal_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = 0;
        int right = parent.getWidth();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // Add a line on top of each item
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // If it's the last index, add an item at the end.
            if (i == parent.getChildCount()) {
                top = child.getBottom() + params.bottomMargin;
            }

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}