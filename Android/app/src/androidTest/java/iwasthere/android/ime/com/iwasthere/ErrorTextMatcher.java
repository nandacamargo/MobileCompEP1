/*
 * Copyright 2015 Steven Mulder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iwasthere.android.ime.com.iwasthere;

import android.support.annotation.NonNull;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ErrorTextMatcher {

    @NonNull
    public static Matcher<View> withError(final String expectedErrorText) {
        Checks.checkNotNull(expectedErrorText);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(final Description description) {
                description.appendText("error text: ");
                Matchers.is(expectedErrorText).describeTo(description);
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                return expectedErrorText.equals(textView.getError().toString());
            }
        };
    }
}
