package com.vondear.tools.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.tools.adapter.AdapterPopupDish;
import com.vondear.tools.model.ModelShopCart;
import com.vondear.tools.interfaces.ShopCartInterface;


/**
 * @author vondear
 * @date 16-12-22
 */
public class RxDialogShopCart extends Dialog implements View.OnClickListener, ShopCartInterface {

    private LinearLayout linearLayout, bottomLayout, clearLayout;
    private FrameLayout shopingcartLayout;
    private ModelShopCart mModelShopCart;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RecyclerView recyclerView;
    private AdapterPopupDish dishAdapter;
    private ShopCartDialogImp shopCartDialogImp;

    public RxDialogShopCart(Context context, ModelShopCart modelShopCart, int themeResId) {
        super(context, themeResId);
        this.mModelShopCart = modelShopCart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_popupview);
        linearLayout = findViewById(R.id.linearlayout);
        clearLayout = findViewById(R.id.clear_layout);
        shopingcartLayout = findViewById(R.id.shopping_cart_layout);
        bottomLayout = findViewById(R.id.shopping_cart_bottom);
        totalPriceTextView = findViewById(R.id.shopping_cart_total_tv);
        totalPriceNumTextView = findViewById(R.id.shopping_cart_total_num);
        recyclerView = findViewById(R.id.recycleview);
        shopingcartLayout.setOnClickListener(this);
        bottomLayout.setOnClickListener(this);
        clearLayout.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dishAdapter = new AdapterPopupDish(getContext(), mModelShopCart);
        recyclerView.setAdapter(dishAdapter);
        dishAdapter.setShopCartInterface(this);
        showTotalPrice();
    }

    @Override
    public void show() {
        super.show();
        animationShow(500);
    }

    @Override
    public void dismiss() {
        animationHide(500);
    }

    private void showTotalPrice() {
        if (mModelShopCart != null && mModelShopCart.getShoppingTotalPrice() > 0) {
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText(getContext().getResources().getString(R.string.rmb) + " " + mModelShopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText("" + mModelShopCart.getShoppingAccount());

        } else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
        }
    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 0, 1000).setDuration(mDuration)
        );
        animatorSet.start();

        if (shopCartDialogImp != null) {
            shopCartDialogImp.dialogDismiss();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                RxDialogShopCart.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shopping_cart_bottom:
            case R.id.shopping_cart_layout:
                this.dismiss();
                break;
            case R.id.clear_layout:
                clear();
                break;
        }
    }

    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
        if (mModelShopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }

    public ShopCartDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }

    public interface ShopCartDialogImp {
        public void dialogDismiss();
    }

    public void clear() {
        mModelShopCart.clear();
        showTotalPrice();
        if (mModelShopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }
}
