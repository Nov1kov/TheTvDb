package ru.novikov.novikovthetvdb.View.Animation;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * custom animation for views
 */
public class CustomAnimation {

    public static Animation createFabAnimation(Activity activity, final View fab){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        int[] pos = new int[2];
        fab.getLocationInWindow(pos);
        int fabWidth = pos[0];
        int fabHeight = pos[1];

        Animation animation = new TranslateAnimation(0, 0, 0, -fabHeight + 20);
        animation.setDuration(700);
        animation.setInterpolator(activity, android.R.anim.anticipate_overshoot_interpolator);
        Animation animation2 = new TranslateAnimation(0,-screenWidth-100,0,0);
        animation2.setStartOffset(700);
        animation2.setDuration(500);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation2);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animationSet;
    }

}
