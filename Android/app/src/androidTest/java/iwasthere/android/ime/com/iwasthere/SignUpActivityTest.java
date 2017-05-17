package iwasthere.android.ime.com.iwasthere;

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

/**
 * Created by dududcbier on 5/17/17.
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    private VolleyIdlingResource volleyResources;

    public SignUpActivityTest() {
        RequestQueueSingleton.getInstance(InstrumentationRegistry.getTargetContext());
        try {
            volleyResources = new VolleyIdlingResource();
            registerIdlingResources(volleyResources);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(
            SignUpActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void noName() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_field_required);
        onView(ViewMatchers.withId((R.id.name)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void noNusp() {
        onView(withId(R.id.name))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_field_required);
        onView(ViewMatchers.withId((R.id.nusp)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void noPassword() {
        onView(withId(R.id.name))
                .perform(typeText("Nome"), closeSoftKeyboard());
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_invalid_password);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void userAlreadyExists() {
        onView(withId(R.id.name))
                .perform(typeText("Nome"), closeSoftKeyboard());
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_user_exists);
        onView(ViewMatchers.withId((R.id.nusp)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void passwordsDontMatch() {
        onView(withId(R.id.name))
                .perform(typeText("Nome"), closeSoftKeyboard());
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password))
                .perform(typeText("outro pass"), closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_password_mismatch);
        onView(ViewMatchers.withId((R.id.confirm_password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }
}
