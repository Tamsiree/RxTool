package com.vondear.vontools;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vonde on 2016/11/13.
 */

public class VonRecyclerViewUtils {

    public static  class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        private boolean isTop = false;

        public SpaceItemDecoration(int space, boolean isTop) {
            this.space = space;
            this.isTop = isTop;
        }

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
            if (isTop) {
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.top = 0;
                }
                outRect.left = 0;
                outRect.right = 0;
            }
        }
    }

}
