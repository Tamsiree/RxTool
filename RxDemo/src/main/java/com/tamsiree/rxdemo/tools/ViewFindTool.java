package com.tamsiree.rxdemo.tools;

import android.util.SparseArray;
import android.view.View;

@SuppressWarnings({"unchecked"})
public class ViewFindTool {
    /**
     * ViewHolder简洁写法,避免适配器中重复定义ViewHolder,减少代码量 用法:
     *
     * <pre>
     * if (convertView == null)
     * {
     * 	convertView = View.inflate(context, R.layout.ad_demo, null);
     * }
     * TextView tv_demo = ViewHolderUtils.get(convertView, R.id.tv_demo);
     * ImageView iv_demo = ViewHolderUtils.get(convertView, R.id.iv_demo);
     * </pre>
     */
    public static <T extends View> T hold(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);

        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    /**
     * 替代findviewById方法
     */
    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }
}
