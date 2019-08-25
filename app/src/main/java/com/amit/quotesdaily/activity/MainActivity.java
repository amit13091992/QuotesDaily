package com.amit.quotesdaily.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.amit.quotesdaily.R;
import com.amit.quotesdaily.adapters.QuotesListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    //ActivityMainBinding activityMainBinding;
    List<String> quoteList = new ArrayList<>();
    private MenuItem mSearchItem;
    private Toolbar mToolbar;
    private CoordinatorLayout mDrawerLayout;

    /**
     * Method gets the theme color for the screen.
     *
     * @param context
     * @param id
     * @return
     */
    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        //activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.quantum_grey_600));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mDrawerLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //prefrenceManager = new PrefrenceManager(getApplicationContext());

        String title = getIntent().getStringExtra("quote_title");
        quoteList = getIntent().getStringArrayListExtra("quote_text");

        Collections.shuffle(quoteList);
        //getQuotesFromFile();
        RecyclerView quotesListRecyclerview = (RecyclerView) findViewById(R.id.quotesRecyclerview);
        quotesListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        quotesListRecyclerview.setAdapter(new QuotesListAdapter(quoteList, title, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview_menu, menu);
        mSearchItem = menu.findItem(R.id.m_search);
        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    animateSearchToolbar(1, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true);
                return true;
            }
        });
        return true;
    }

    /**
     * Method animates the searchview with toolbar.
     *
     * @param numberOfMenuIcon
     * @param containsOverflow
     * @param show
     */
    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.quantum_grey_600));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-mToolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                mToolbar.clearAnimation();
                mToolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
                createCircularReveal.setDuration(250);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mToolbar.setBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimary));
                        mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
                    }
                });
                createCircularReveal.start();
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-mToolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mToolbar.setBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                mToolbar.startAnimation(animationSet);
            }
            mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
        }
    }

    /**
     * Method Checks the layout direction.
     *
     * @param resources
     * @return
     */
    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Method starts a animation when starting the activity.
     */
    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);
    }


}
