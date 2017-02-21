package com.vondear.tools.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.tools.view.FakeAddImageView;
import com.vondear.tools.view.PointFTypeEvaluator;
import com.vondear.tools.view.ShopCartDialog;
import com.vondear.tools.adapter.AdapterLeftMenu;
import com.vondear.tools.adapter.AdapterRightDish;
import com.vondear.tools.bean.Dish;
import com.vondear.tools.bean.DishMenu;
import com.vondear.tools.bean.ShopCart;
import com.vondear.tools.interfaces.ShopCartInterface;

import java.util.ArrayList;

public class ActivityELMe extends AppCompatActivity implements AdapterLeftMenu.onItemSelectedListener,ShopCartInterface,ShopCartDialog.ShopCartDialogImp{
    private final static String TAG = "MainActivity";
    private RecyclerView leftMenu;//左侧菜单栏
    private RecyclerView rightMenu;//右侧菜单栏
    private TextView headerView;
    private LinearLayout headerLayout;//右侧菜单栏最上面的菜单
    private LinearLayout bottomLayout;
    private DishMenu headMenu;
    private AdapterLeftMenu leftAdapter;
    private AdapterRightDish rightAdapter;
    private ArrayList<DishMenu> dishMenuList;//数据源
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动
    private ShopCart shopCart;
    //    private FakeAddImageView fakeAddImageView;
    private ImageView shoppingCartView;
    private FrameLayout shopingCartLayout;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elme);

        initData();
        initView();
        initAdapter();
    }

    private void initView(){
        mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        leftMenu = (RecyclerView)findViewById(R.id.left_menu);
        rightMenu = (RecyclerView)findViewById(R.id.right_menu);
        headerView = (TextView)findViewById(R.id.right_menu_tv);
        headerLayout = (LinearLayout)findViewById(R.id.right_menu_item);
//        fakeAddImageView = (FakeAddImageView)findViewById(R.id.right_dish_fake_add);
        bottomLayout = (LinearLayout)findViewById(R.id.shopping_cart_bottom);
        shoppingCartView = (ImageView) findViewById(R.id.shopping_cart);
        shopingCartLayout = (FrameLayout) findViewById(R.id.shopping_cart_layout);
        totalPriceTextView = (TextView)findViewById(R.id.shopping_cart_total_tv);
        totalPriceNumTextView = (TextView)findViewById(R.id.shopping_cart_total_num);

        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(new LinearLayoutManager(this));

        rightMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if( recyclerView.canScrollVertically(1)==false) {//无法下滑
                    showHeadView();
                    return;
                }
                View underView = null;
                if(dy>0)
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(),headerLayout.getMeasuredHeight()+1);
                else
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(),0);
                if(underView!=null && underView.getContentDescription()!=null ){
                    int position = Integer.parseInt(underView.getContentDescription().toString());
                    DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position);

                    if(leftClickType || !menu.getMenuName().equals(headMenu.getMenuName())) {
                        if (dy> 0 && headerLayout.getTranslationY()<=1 && headerLayout.getTranslationY()>= -1 * headerLayout.getMeasuredHeight()*4/5 && !leftClickType) {// underView.getTop()>9
                            int dealtY = underView.getTop() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else if(dy<0 && headerLayout.getTranslationY()<=0 && !leftClickType) {
                            headerView.setText(menu.getMenuName());
                            int dealtY = underView.getBottom() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        }
                        else{
                            headerLayout.setTranslationY(0);
                            headMenu = menu;
                            headerView.setText(headMenu.getMenuName());
                            for (int i = 0; i < dishMenuList.size(); i++) {
                                if (dishMenuList.get(i) == headMenu) {
                                    leftAdapter.setSelectedNum(i);
                                    break;
                                }
                            }
                            if(leftClickType)leftClickType=false;
                            Log.e(TAG, "onScrolled: "+menu.getMenuName() );
                        }
                    }
                }
            }
        });

        shopingCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCart(view);
            }
        });
    }

    private void initData(){
        shopCart = new ShopCart();
        dishMenuList = new ArrayList<>();
        ArrayList<Dish> dishs1 = new ArrayList<>();
        dishs1.add(new Dish("面包",1.0,10));
        dishs1.add(new Dish("蛋挞",1.0,10));
        dishs1.add(new Dish("牛奶",1.0,10));
        dishs1.add(new Dish("肠粉",1.0,10));
        dishs1.add(new Dish("绿茶饼",1.0,10));
        dishs1.add(new Dish("花卷",1.0,10));
        dishs1.add(new Dish("包子",1.0,10));
        DishMenu breakfast = new DishMenu("早点",dishs1);

        ArrayList<Dish> dishs2 = new ArrayList<>();
        dishs2.add(new Dish("粥",1.0,10));
        dishs2.add(new Dish("炒饭",1.0,10));
        dishs2.add(new Dish("炒米粉",1.0,10));
        dishs2.add(new Dish("炒粿条",1.0,10));
        dishs2.add(new Dish("炒牛河",1.0,10));
        dishs2.add(new Dish("炒菜",1.0,10));
        DishMenu launch = new DishMenu("午餐",dishs2);

        ArrayList<Dish> dishs3 = new ArrayList<>();
        dishs3.add(new Dish("淋菜",1.0,10));
        dishs3.add(new Dish("川菜",1.0,10));
        dishs3.add(new Dish("湘菜",1.0,10));
        dishs3.add(new Dish("粤菜",1.0,10));
        dishs3.add(new Dish("赣菜",1.0,10));
        dishs3.add(new Dish("东北菜",1.0,10));
        DishMenu evening = new DishMenu("晚餐",dishs3);

        ArrayList<Dish> dishs4 = new ArrayList<>();
        dishs4.add(new Dish("淋菜",1.0,10));
        dishs4.add(new Dish("川菜",1.0,10));
        dishs4.add(new Dish("湘菜",1.0,10));
        dishs4.add(new Dish("湘菜",1.0,10));
        dishs4.add(new Dish("湘菜1",1.0,10));
        dishs4.add(new Dish("湘菜2",1.0,10));
        dishs4.add(new Dish("湘菜3",1.0,10));
        dishs4.add(new Dish("湘菜4",1.0,10));
        dishs4.add(new Dish("湘菜5",1.0,10));
        dishs4.add(new Dish("湘菜6",1.0,10));
        dishs4.add(new Dish("湘菜7",1.0,10));
        dishs4.add(new Dish("湘菜8",1.0,10));
        dishs4.add(new Dish("粤菜",1.0,10));
        dishs4.add(new Dish("赣菜",1.0,10));
        dishs4.add(new Dish("东北菜",1.0,10));
        DishMenu menu1 = new DishMenu("夜宵",dishs4);

        dishMenuList.add(breakfast);
        dishMenuList.add(launch);
        dishMenuList.add(evening);
        dishMenuList.add(menu1);
    }

    private void initAdapter(){
        leftAdapter = new AdapterLeftMenu(this,dishMenuList);
        rightAdapter = new AdapterRightDish(this,dishMenuList,shopCart);
        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartInterface(this);
        initHeadView();
    }

    private void initHeadView(){
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        headerLayout.setContentDescription("0");
        headerView.setText(headMenu.getMenuName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leftAdapter.removeItemSelectedListener(this);
    }

    private void showHeadView(){
        headerLayout.setTranslationY(0);
        View underView = rightMenu.findChildViewUnder(headerView.getX(),0);
        if(underView!=null && underView.getContentDescription()!=null){
            int position = Integer.parseInt(underView.getContentDescription().toString());
            DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position+1);
            headMenu = menu;
            headerView.setText(headMenu.getMenuName());
            for (int i = 0; i < dishMenuList.size(); i++) {
                if (dishMenuList.get(i) == headMenu) {
                    leftAdapter.setSelectedNum(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onLeftItemSelected(int position, DishMenu menu) {
        int sum=0;
        for(int i = 0;i<position;i++){
            sum+=dishMenuList.get(i).getDishList().size()+1;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(sum,0);
        leftClickType = true;
    }

    @Override
    public void add(View view,int position) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCartView.getLocationInWindow(cartLocation);
        rightMenu.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1]-recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1]-recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(this);
        mainLayout.addView(fakeAddImageView);
        fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
        fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fakeAddImageView.setVisibility(View.GONE);
                mainLayout.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCartView,"scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCartView,"scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();

        showTotalPrice();
    }

    @Override
    public void remove(View view,int position) {
        showTotalPrice();
    }

    private void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>0){
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("¥ "+shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText(""+shopCart.getShoppingAccount());

        }else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
        }
    }

    private void showCart(View view) {
        if(shopCart!=null && shopCart.getShoppingAccount()>0){
            ShopCartDialog dialog = new ShopCartDialog(this,shopCart,R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount =0.5f;
            window.setAttributes(params);
        }
    }

    @Override
    public void dialogDismiss() {
        showTotalPrice();
        rightAdapter.notifyDataSetChanged();
    }
}
