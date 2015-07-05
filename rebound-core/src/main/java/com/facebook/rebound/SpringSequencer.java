package com.facebook.rebound;

import java.util.ArrayList;

/**
 * SpringSequencer class provides a sequencer animation for your collection of Spring,
 * this automatically runs the next spring when the current spring ends.
 */
public class SpringSequencer {

  private int position = 0;
  private double mEndValue;
  private ArrayList<Spring> springs = new ArrayList<>();

  /**
   *
   * @param springId represent the position of spring to be animated.
   * @param spring the instance of spring.
   * @return Actual instance of SpringSequencer.
   */
  public SpringSequencer add(int springId, Spring spring) {
    springs.add(springId, spring);
    return this;
  }

  /**
   * Erase all springs added on springs arrayList
   * @return Actual instance of SpringSequencer.
   */
  public SpringSequencer clear() {
    springs.clear();
    return this;
  }

  /**
   * Control the state of animation, this do a recursivity to jump to the next
   * animation that contains at springs arraylist.
   * @param endValue the endValue for the spring.
   */
  public void setEndValue(double endValue) {

    if(this.mEndValue != endValue) {
      this.mEndValue = endValue;
      position = 0;
    }

    if(springs.size() > 0 && springs.size() > position) {
      Spring spring = springs.get(position);
      if(spring != null) {
        spring.addListener(new SpringListener() {
          @Override
          public void onSpringUpdate(Spring spring) {
          }

          @Override
          public void onSpringAtRest(Spring spring) {
            position++;
            setEndValue(mEndValue);
          }

          @Override
          public void onSpringActivate(Spring spring) {
          }

          @Override
          public void onSpringEndStateChange(Spring spring) {
          }
        }).setEndValue(mEndValue);
      }
    }
  }

}
