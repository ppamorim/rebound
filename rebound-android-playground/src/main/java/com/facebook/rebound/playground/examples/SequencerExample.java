package com.facebook.rebound.playground.examples;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSequencer;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.facebook.rebound.playground.R;

public class SequencerExample extends FrameLayout {

  private final BaseSpringSystem mSpringSystem = SpringSystem.create();
  private final ZoomSpringListener mZoomSpringListener = new ZoomSpringListener();
  private final RotateSpringListener mRotateSpringListener = new RotateSpringListener();
  private final FrameLayout mRootView;
  private final SpringSequencer springSequencer;
  private final Spring mScaleSpring;
  private final Spring mRotateSpring;
  private final View mImageView;

  public SequencerExample(Context context) {
    this(context, null);
  }

  public SequencerExample(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SequencerExample(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    springSequencer = new SpringSequencer();

    mScaleSpring = mSpringSystem.createSpring();
    mRotateSpring = mSpringSystem.createSpring();
    springSequencer.add(0, mScaleSpring);
    springSequencer.add(1, mRotateSpring);
    LayoutInflater inflater = LayoutInflater.from(context);
    mRootView = (FrameLayout) inflater.inflate(R.layout.photo_scale_example, this, false);
    mImageView = mRootView.findViewById(R.id.image_view);
    mRootView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            springSequencer.setEndValue(1);
            break;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_CANCEL:
            springSequencer.setEndValue(0);
            break;
        }
        return true;
      }
    });
    addView(mRootView);
  }

  @Override
  protected void onAttachedToWindow() {
    mScaleSpring.addListener(mZoomSpringListener);
    mRotateSpring.addListener(mRotateSpringListener);
  }

  @Override
  protected void onDetachedFromWindow() {
    mScaleSpring.removeListener(mZoomSpringListener);
    mRotateSpring.removeListener(mRotateSpringListener);
  }

  private class ZoomSpringListener extends SimpleSpringListener {
    @Override
    public void onSpringUpdate(Spring spring) {
      float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0,
          1, 1, 0.5);
      mImageView.setScaleX(mappedValue);
      mImageView.setScaleY(mappedValue);
    }
  }

  private class RotateSpringListener extends SimpleSpringListener {
    @Override
    public void onSpringUpdate(Spring spring) {
      float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0,
          1, 0, 90);
      mImageView.setRotation(mappedValue);
    }
  }

}
