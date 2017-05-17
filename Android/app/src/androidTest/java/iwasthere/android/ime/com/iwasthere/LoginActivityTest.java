package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
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

import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
        VolleyIdlingResource volleyResources;
        RequestQueueSingleton.getInstance(InstrumentationRegistry.getTargetContext());
        try {
            volleyResources = new VolleyIdlingResource();
            registerIdlingResources(volleyResources);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void successfulStudentLogin() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("bin"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button)).perform(click());
        intended(hasComponent(SeminarListActivity.class.getName()));
    }

    @Test
    public void successfulTeacherLogin() {
        onView(withId(R.id.nusp))
                .perform(typeText("123456789"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.nusp_sign_in_button)).perform(click());
        intended(hasComponent(SeminarListActivity.class.getName()));
    }

    @Test
    public void wrongPassword() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_incorrect_password);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void userDoesNotExist() {
        onView(withId(R.id.nusp))
                .perform(typeText("31415926559"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_incorrect_password);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void emptyNusp() throws InterruptedException {
        onView(withId(R.id.password))
                .perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_field_required);
        onView(ViewMatchers.withId((R.id.nusp)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void emptyPassword() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_field_required);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void cantLoginAsStudentUsingTeacherCredentials() {
        onView(withId(R.id.nusp))
                .perform(typeText("123456789"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_incorrect_password);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void cantLoginAsTeacherUsingStudentCredentials() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("bin"), closeSoftKeyboard());
        onView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.nusp_sign_in_button))
                .perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_incorrect_password);
        onView(ViewMatchers.withId((R.id.password)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }

    @Test
    public void registerButtonWorks() {
        onView(withId(R.id.nusp_register_button))
                .perform(click());
        intended(hasComponent(SignUpActivity.class.getName()));
    }

    @Test
    public void typedNuspAndPasswordGoToRegisterActivity() {
        onView(withId(R.id.nusp))
                .perform(typeText("0101"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("bin"), closeSoftKeyboard());
        onView(withId(R.id.nusp_register_button))
                .perform(click());
        intended(allOf(
                hasComponent(SignUpActivity.class.getName()),
                hasExtras(allOf(
                        hasEntry(equalTo("nusp"), equalTo("0101")),
                        hasEntry(equalTo("password"), equalTo("bin"))
                ))));
    }
}
