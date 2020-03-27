package com.tamsiree.rxui.view.wavesidebar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tamsiree.rxkit.TLog;
import com.tamsiree.rxui.R;

import java.util.ArrayList;

/**
 * A subclass of RecyclerView.Adapter responsible for providing views that add header/footer/empty view
 */
abstract class AbsWaveSideAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected static final String TAG = "AbsWaveSideAdapter";

    public static final int TYPE_EMPTY_VIEW = 1 << 5;
    public static final int TYPE_LOADING_VIEW = 1 << 6;
    public static final int TYPE_FOOTER_VIEW = 1 << 7;
    public static final int TYPE_HEADER_VIEW = 1 << 8;

    private final ArrayList<OnItemClickListener> mOnItemClickListeners =
            new ArrayList<>();

    private final ArrayList<OnItemLongClickListener> mOnItemLongClickListeners =
            new ArrayList<>();

    protected View mHeaderView;
    protected View mFooterView;
    /**
     * View to show if there are no items to show.
     */
    protected View mEmptyView;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    /**
     * initialization
     *
     * @param context The context.
     */
    public AbsWaveSideAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    abstract public T getItem(int position);

    @Override
    abstract public int getItemCount();

    /**
     *
     * @return Whether there is data exists
     */
    abstract protected boolean isEmpty();


    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder vh;
        switch (viewType) {
            case TYPE_LOADING_VIEW:
                vh = onCreateLoadingViewHolder(parent);
                if (vh == null) {
                    vh = createBaseViewHolder(parent, R.layout.footer_item_default_loading);
                }
                break;
            case TYPE_EMPTY_VIEW:
                vh = new BaseViewHolder(mEmptyView);
                break;
            case TYPE_FOOTER_VIEW:
                vh = new BaseViewHolder(mFooterView);
                break;
            case TYPE_HEADER_VIEW:
                vh = new BaseViewHolder(mHeaderView);
                break;
            default:
                vh = onCreateDefViewHolder(parent, viewType);
                dispatchItemClickListener(vh);
                break;
        }
        return vh;

    }


    protected BaseViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        return null;
    }

    abstract protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType);

    private BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return new BaseViewHolder(inflateItemView(layoutResId, parent));
    }

    protected View inflateItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case TYPE_LOADING_VIEW:
                break;
            case TYPE_HEADER_VIEW:
                break;
            case TYPE_EMPTY_VIEW:
                break;
            case TYPE_FOOTER_VIEW:
                break;
            default:
                bindHolder((VH) holder, position);
                break;
        }

    }


    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param holder A fully initialized helper.
     * @param position The position of the item within the adapter's data set.
     */
    abstract protected void bindHolder(VH holder, int position);


    public int getHeaderViewCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewCount() {
        return mFooterView == null ? 0 : 1;
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    public void addHeaderView(View header) {
        if (header == null) {
            TLog.e(TAG, "header is null!!!");
            return;
        }
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            this.mHeaderView = null;
            this.notifyDataSetChanged();
        }
    }

    public void addFooterView(View footer) {
        if (footer == null) {
            TLog.e(TAG, "footer is null!!!");
            return;
        }
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View footer) {
        if (mFooterView != null) {
            this.mFooterView = null;
            this.notifyDataSetChanged();
        }
    }

    /**
     * @param emptyView Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (gridLayoutManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup)
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        BaseWaveSideAdapter adapter = (BaseWaveSideAdapter) recyclerView.getAdapter();
                        if (isFullSpanType(adapter.getItemViewType(position))) {
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
        }
    }


    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    private boolean isFullSpanType(int type) {
        return type == TYPE_HEADER_VIEW || type == TYPE_FOOTER_VIEW || type == TYPE_LOADING_VIEW || type == TYPE_EMPTY_VIEW;
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListeners.add(listener);
    }

    public void removeOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListeners.remove(listener);
    }

    public void addOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListeners.add(listener);
    }

    public void removeOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListeners.remove(listener);
    }

    private void dispatchItemClickListener(final BaseViewHolder vh) {
        if (mOnItemClickListeners != null && mOnItemClickListeners.size() > 0) {
            if (!(vh.itemView instanceof AdapterView)) {
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < mOnItemClickListeners.size(); i++) {
                            final OnItemClickListener listener = mOnItemClickListeners.get(i);
                            listener.onItemClick(vh, vh.getLayoutPosition() - getHeaderViewCount());
                        }
                    }
                });
            }
        }

        if (mOnItemLongClickListeners != null && mOnItemLongClickListeners.size() > 0) {
            if (!(vh.itemView instanceof AdapterView)) {
                vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        for (int i = 0; i < mOnItemLongClickListeners.size(); i++) {
                            final OnItemLongClickListener listener = mOnItemLongClickListeners.get(i);
                            listener.onItemLongClick(vh, vh.getLayoutPosition() - getHeaderViewCount());
                        }
                        return true;
                    }
                });
            }
        }
    }
}
