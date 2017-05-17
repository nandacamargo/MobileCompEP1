package iwasthere.android.ime.com.iwasthere;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by dududcbier on 5/17/17.
 */

public class SeminarListActivityTest {

    private VolleyIdlingResource volleyResources;

    public SeminarListActivityTest() {
        RequestQueueSingleton.getInstance(InstrumentationRegistry.getTargetContext());
        try {
            volleyResources = new VolleyIdlingResource();
            registerIdlingResources(volleyResources);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public ActivityTestRule<SeminarListActivity> mActivityRule = new ActivityTestRule<>(
            SeminarListActivity.class, false, false);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
        UserSingleton.deleteInstance();
    }

    @Test
    public void studentCantAccessFab() {
        UserSingleton.getInstance("name", "123", false);
        Intent i = new Intent();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.fab))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void teacherCanAccessFab() {
        UserSingleton.getInstance("name", "123", true);
        Intent i = new Intent();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()));
    }
}
