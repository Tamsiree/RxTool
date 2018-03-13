package com.vondear.tools.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.scaleimage.ImageSource;
import com.vondear.rxtools.view.scaleimage.RxScaleImageView;
import com.vondear.tools.R;
import com.vondear.tools.view.RxPinView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author vondear
 */
public class ActivityRxScaleImageView extends ActivityBase implements View.OnClickListener {

    private static final String BUNDLE_POSITION = "position";

    private int position;

    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_scale_image_view);

        RxTitle rxTitle = (RxTitle) findViewById(R.id.rx_title);
        rxTitle.setLeftFinish(mContext);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.previous).setOnClickListener(this);
        findViewById(R.id.play).setOnClickListener(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_POSITION)) {
            position = savedInstanceState.getInt(BUNDLE_POSITION);
        }
        notes = Arrays.asList(
                new Note("A demo", "点击播放按钮,将在图像上生成随机点缩放并变焦,显示标记。"),
                new Note("Limited pan", "如果目标点附近的边缘图像,它将尽可能靠近中心。"),
                new Note("Unlimited pan", "无限制的目标点总是可以动画中心。"),
                new Note("Customisation", "持续时间是可配置的,你也可以做动画不可中断。")
        );

        initialiseImage();
        updateNotes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_POSITION, position);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next) {
            position++;
            updateNotes();
        } else if (view.getId() == R.id.previous) {
            position--;
            updateNotes();
        } else if (view.getId() == R.id.play) {
            RxPinView rxPinView = (RxPinView) findViewById(R.id.imageView);
            Random random = new Random();
            if (rxPinView.isReady()) {
                float maxScale = rxPinView.getMaxScale();
                float minScale = rxPinView.getMinScale();
                float scale = (random.nextFloat() * (maxScale - minScale)) + minScale;
                PointF center = new PointF(random.nextInt(rxPinView.getSWidth()), random.nextInt(rxPinView.getSHeight()));
                rxPinView.setPin(center);
                RxScaleImageView.AnimationBuilder animationBuilder = rxPinView.animateScaleAndCenter(scale, center);
                if (position == 3) {
                    animationBuilder.withDuration(2000).withEasing(RxScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start();
                } else {
                    animationBuilder.withDuration(750).start();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void initialiseImage() {
        RxScaleImageView imageView = (RxScaleImageView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.asset("squirrel.jpg"));
    }

    private void updateNotes() {
        if (position > notes.size() - 1) {
            return;
        }
        ((TextView) findViewById(R.id.note)).setText(notes.get(position).text);
        findViewById(R.id.next).setVisibility(position >= notes.size() - 1 ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.previous).setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);

        RxScaleImageView imageView = (RxScaleImageView) findViewById(R.id.imageView);
        if (position == 2) {
            imageView.setPanLimit(RxScaleImageView.PAN_LIMIT_CENTER);
        } else {
            imageView.setPanLimit(RxScaleImageView.PAN_LIMIT_INSIDE);
        }

    }

    private static final class Note {
        private final String text;
        private final String subtitle;

        private Note(String subtitle, String text) {
            this.subtitle = subtitle;
            this.text = text;
        }
    }
}
