package com.example.cambox;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.cambox.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TCL1_login_test {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

    @Test
    public void gotologin(){
        onView(withId(R.id.btnSplashStart)).perform(click());
        onView(withId(R.id.linkLogin)).perform(click());
        onView(allOf(withId(R.id.txtLoginTitle), isDisplayed())).check(matches(withText("Login")));
    }

    @Test
    public void gotoregister(){
        onView(withId(R.id.btnSplashStart)).perform(click());
        onView(withId(R.id.txtRegisterTitle)).check(matches(withText("Create an Account")));
    }

    @Test
    public void rightlogin(){
        onView(withId(R.id.btnSplashStart)).perform(click());
        onView(withId(R.id.linkLogin)).perform(click());
        onView(allOf(withId(R.id.txtLoginTitle), isDisplayed())).check(matches(withText("Login")));
        onView(withId(R.id.mEmail)).perform(typeText("argadiaz@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.mPassword)).perform(typeText("arga")).perform(closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
    }

    @Test
    public void wronglogin(){
        onView(withId(R.id.btnSplashStart)).perform(click());
        onView(withId(R.id.linkLogin)).perform(click());
        onView(allOf(withId(R.id.txtLoginTitle), isDisplayed())).check(matches(withText("Login")));
        onView(withId(R.id.mEmail)).perform(typeText("arga@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.mPassword)).perform(typeText("arga")).perform(closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
    }

}