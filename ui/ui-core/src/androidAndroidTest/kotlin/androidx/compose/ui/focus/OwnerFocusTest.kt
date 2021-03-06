/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.ui.focus

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Box
import androidx.test.filters.SmallTest
import androidx.ui.core.Modifier
import androidx.ui.core.ViewAmbient
import androidx.ui.core.focus.ExperimentalFocus
import androidx.ui.core.focus.FocusRequester
import androidx.ui.core.focus.FocusState2.Active
import androidx.ui.core.focus.FocusState2.Inactive
import androidx.ui.core.focus.focus
import androidx.ui.core.focus.focusObserver
import androidx.ui.core.focus.focusRequester
import androidx.ui.test.createComposeRule
import androidx.ui.test.runOnIdle
import com.google.common.truth.Truth.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@OptIn(ExperimentalFocus::class)
@RunWith(JUnit4::class)
class OwnerFocusTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun requestFocus_bringsViewInFocus() {
        // Arrange.
        lateinit var ownerView: View
        val focusRequester = FocusRequester()
        composeTestRule.setFocusableContent {
            ownerView = getOwner()
            Box(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focus()
            )
        }

        // Act.
        runOnIdle {
            focusRequester.requestFocus()
        }

        // Assert.
        runOnIdle {
            assertThat(ownerView.isFocused).isTrue()
        }
    }

    @Ignore("Enable this test after the owner propagates focus to the hierarchy (b/152535715)")
    @Test
    fun whenOwnerGainsFocus_focusModifiersAreUpdated() {
        // Arrange.
        lateinit var ownerView: View
        var focusState = Inactive
        val focusRequester = FocusRequester()
        composeTestRule.setFocusableContent {
            ownerView = getOwner()
            Box(
                modifier = Modifier
                    .focusObserver { focusState = it }
                    .focusRequester(focusRequester)
                    .focus()
            )
        }

        // Act.
        runOnIdle {
            ownerView.requestFocus()
        }

        // Assert.
        runOnIdle {
            assertThat(focusState).isEqualTo(Active)
        }
    }

    @Ignore("Enable this test after the owner propagates focus to the hierarchy (b/152535715)")
    @Test
    fun whenWindowGainsFocus_focusModifiersAreUpdated() {
        // Arrange.
        lateinit var ownerView: View
        var focusState = Inactive
        val focusRequester = FocusRequester()
        composeTestRule.setFocusableContent {
            ownerView = getOwner()
            Box(
                modifier = Modifier
                    .focusObserver { focusState = it }
                    .focusRequester(focusRequester)
                    .focus()
            )
        }

        // Act.
        runOnIdle {
            ownerView.dispatchWindowFocusChanged(true)
        }

        // Assert.
        runOnIdle {
            assertThat(focusState).isEqualTo(Active)
        }
    }

    @Test
    fun whenOwnerLosesFocus_focusModifiersAreUpdated() {
        // Arrange.
        lateinit var ownerView: View
        var focusState = Inactive
        val focusRequester = FocusRequester()
        composeTestRule.setFocusableContent {
            ownerView = getOwner()
            Box(
                modifier = Modifier
                    .focusObserver { focusState = it }
                    .focusRequester(focusRequester)
                    .focus()
            )
        }
        runOnIdle {
            focusRequester.requestFocus()
        }

        // Act.
        runOnIdle {
            ownerView.clearFocus()
        }

        // Assert.
        runOnIdle {
            assertThat(focusState).isEqualTo(Inactive)
        }
    }

    @Test
    fun whenWindowLosesFocus_focusStateIsUnchanged() {
        // Arrange.
        lateinit var ownerView: View
        var focusState = Inactive
        val focusRequester = FocusRequester()
        composeTestRule.setFocusableContent {
            ownerView = getOwner()
            Box(
                modifier = Modifier
                    .focusObserver { focusState = it }
                    .focusRequester(focusRequester)
                    .focus()
            )
        }
        runOnIdle {
            focusRequester.requestFocus()
        }

        // Act.
        runOnIdle {
            ownerView.dispatchWindowFocusChanged(false)
        }

        // Assert.
        runOnIdle {
            assertThat(focusState).isEqualTo(Active)
        }
    }

    @Composable
    private fun getOwner() = ViewAmbient.current
}
