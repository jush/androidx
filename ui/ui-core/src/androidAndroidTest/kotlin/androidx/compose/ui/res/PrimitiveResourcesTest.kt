/*
 * Copyright 2019 The Android Open Source Project
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

package androidx.compose.ui.res

import androidx.compose.runtime.Providers
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.ui.core.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.core.test.R
import androidx.ui.res.booleanResource
import androidx.ui.res.dimensionResource
import androidx.ui.res.integerArrayResource
import androidx.ui.res.integerResource
import androidx.ui.test.createComposeRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@SmallTest
class PrimitiveResourcesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun integerResourceTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Providers(ContextAmbient provides context) {
                assertThat(integerResource(R.integer.integer_value)).isEqualTo(123)
            }
        }
    }

    @Test
    fun integerArrayResourceTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Providers(ContextAmbient provides context) {
                assertThat(integerArrayResource(R.array.integer_array))
                    .isEqualTo(intArrayOf(234, 345))
            }
        }
    }

    @Test
    fun boolArrayResourceTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Providers(ContextAmbient provides context) {
                assertThat(booleanResource(R.bool.boolean_value)).isTrue()
            }
        }
    }

    @Test
    fun dimensionResourceTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Providers(ContextAmbient provides context) {
                assertThat(dimensionResource(R.dimen.dimension_value)).isEqualTo(32.dp)
            }
        }
    }
}