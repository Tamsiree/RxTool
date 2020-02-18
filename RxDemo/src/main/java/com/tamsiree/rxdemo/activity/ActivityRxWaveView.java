package com.tamsiree.rxdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxtool.RxDeviceTool;
import com.tamsiree.rxtool.RxImageTool;
import com.tamsiree.rxui.view.colorpicker.ColorPickerView;
import com.tamsiree.rxui.view.colorpicker.OnColorChangedListener;
import com.tamsiree.rxui.view.colorpicker.OnColorSelectedListener;
import com.tamsiree.rxui.view.colorpicker.slider.AlphaSlider;
import com.tamsiree.rxui.view.colorpicker.slider.LightnessSlider;
import com.tamsiree.rxui.view.waveview.RxWaveHelper;
import com.tamsiree.rxui.view.waveview.RxWaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tamsiree
 */
public class ActivityRxWaveView extends AppCompatActivity {


    @BindView(R.id.wave)
    RxWaveView mWave;
    @BindView(R.id.border)
    TextView mBorder;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    @BindView(R.id.shape)
    TextView mShape;
    @BindView(R.id.shapeCircle)
    RadioButton mShapeCircle;
    @BindView(R.id.shapeSquare)
    RadioButton mShapeSquare;
    @BindView(R.id.shapeChoice)
    RadioGroup mShapeChoice;
    @BindView(R.id.color_picker_view)
    ColorPickerView mColorPickerView;
    @BindView(R.id.v_lightness_slider)
    LightnessSlider mVLightnessSlider;
    @BindView(R.id.v_alpha_slider)
    AlphaSlider mVAlphaSlider;
    private RxWaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#4489CFF0");
    private int mBorderWidth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_wave_view);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        mWave.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new RxWaveHelper(mWave);

        ((RadioGroup) findViewById(R.id.shapeChoice))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i) {
                            case R.id.shapeCircle:
                                mWave.setShapeType(RxWaveView.ShapeType.CIRCLE);
                                break;
                            case R.id.shapeSquare:
                                mWave.setShapeType(RxWaveView.ShapeType.SQUARE);
                                break;
                            default:
                                break;
                        }
                    }
                });

        ((SeekBar) findViewById(R.id.seekBar))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mBorderWidth = i;
                        mWave.setBorder(mBorderWidth, mBorderColor);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        mColorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                // Handle on color change
                Log.d("selectedColor", "selectedColor: " + selectedColor);

                mWave.setWaveColor(RxImageTool.changeColorAlpha(selectedColor, 40),
                        RxImageTool.changeColorAlpha(selectedColor, 60));
                mBorderColor = RxImageTool.changeColorAlpha(selectedColor, 68);
                mWave.setBorder(mBorderWidth, mBorderColor);

//                mCobwebView.setSpiderColor(selectedColor);
            }
        });
        mColorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                //mCobwebView.setSpiderColor(selectedColor);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }
}
