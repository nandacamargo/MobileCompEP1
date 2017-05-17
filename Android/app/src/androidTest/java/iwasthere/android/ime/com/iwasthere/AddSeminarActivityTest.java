package iwasthere.android.ime.com.iwasthere;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddSeminarActivityTest {

    @Rule
    public ActivityTestRule<AddSeminarActivity> mActivityRule = new ActivityTestRule<>(
            AddSeminarActivity.class);

    @Test
    public void noName() {
        onView(withId(R.id.btAddSeminar)).perform(click());
        String expectedError = InstrumentationRegistry.getTargetContext()
                .getString(R.string.error_field_required);
        onView(ViewMatchers.withId((R.id.name)))
                .check(ViewAssertions.matches(ErrorTextMatcher.withError(expectedError)));
    }
}
