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

package androidx.ui.androidview.demos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.ui.androidview.adapters.setOnClick
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.gesture.tapGestureFilter
import androidx.ui.demos.common.ComposableDemo
import androidx.ui.demos.common.DemoCategory
import androidx.compose.foundation.Box
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.unit.dp
import androidx.ui.viewinterop.AndroidView

val AndroidInComposeDemos = DemoCategory("Android In Compose Interop", listOf(
    ComposableDemo("4 Android tap in Compose") { FourAndroidTapInCompose() },
    ComposableDemo("Android tap in Compose tap") { AndroidTapInComposeTap() },
    ComposableDemo("Android tap in Compose scroll") { AndroidTapInComposeScroll() },
    ComposableDemo("Android scroll in Compose scroll (different orientation)") {
        AndroidScrollInComposeScrollDifferentOrientation()
    },
    ComposableDemo("Android scroll in Compose scroll (same orientation)") {
        AndroidScrollInComposeScrollSameOrientation()
    },
    ComposableDemo("2 ScrollViews as separate children of Compose") {
        TwoAndroidScrollViewsInCompose()
    }
))

@Composable
private fun FourAndroidTapInCompose() {
    Column {
        Text("Demonstrates that pointer locations are dispatched to Android correctly.")
        Text(
            "Below is a ViewGroup with 4 Android buttons in it.  When each button is tapped, the" +
                    " background of the ViewGroup is updated."
        )
        Box(
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .preferredSize(240.dp)
        ) {
            AndroidView({ context ->
                LayoutInflater.from(context)
                    .inflate(R.layout.android_4_buttons_in_compose, null).let { view ->
                        view as ViewGroup
                        view.findViewById<View>(R.id.buttonBlue).setOnClick {
                            view.setBackgroundColor(Color.BLUE)
                        }
                        view.findViewById<View>(R.id.buttonRed).setOnClick {
                            view.setBackgroundColor(Color.RED)
                        }
                        view.findViewById<View>(R.id.buttonGreen).setOnClick {
                            view.setBackgroundColor(Color.GREEN)
                        }
                        view.findViewById<View>(R.id.buttonYellow).setOnClick {
                            view.setBackgroundColor(Color.YELLOW)
                        }
                        view
                    }
            })
        }
    }
}

@Composable
private fun AndroidTapInComposeTap() {
    var theView: View? = null

    val onTap: (Offset) -> Unit = {
        theView?.setBackgroundColor(Color.BLUE)
    }

    Column {
        Text(
            "Demonstrates that pointer input interop is working correctly in the simple case of " +
                    "tapping."
        )
        Text(
            "Below there is an Android ViewGroup with a button in it.  The whole thing is wrapped" +
                    " in a Box with a tapGestureFilter modifier on it.  When you click the " +
                    "button, the ViewGroup's background turns red.  When you click anywhere else " +
                    "in the ViewGroup, the tapGestureFilter \"fires\" and the background turns " +
                    "Blue."
        )
        Box(
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .preferredSize(240.dp)
                .tapGestureFilter(onTap)
        ) {
            AndroidView({ context ->
                LayoutInflater.from(context)
                    .inflate(R.layout.android_tap_in_compose_tap, null).let { view ->
                        theView = view
                        theView?.setBackgroundColor(Color.GREEN)
                        view.findViewById<View>(R.id.buttonRed).setOnClick {
                            theView?.setBackgroundColor(Color.RED)
                        }
                        view
                    }
            })
        }
    }
}

@Composable
private fun AndroidTapInComposeScroll() {
    Column {
        Text(
            "Demonstrates that pointer input interop is working correctly when tappable things in" +
                    " Android are put inside of something scrollable in Compose."
        )
        Text(
            "Below is a Compose HorizontalScroller with a wide horizontal LinearLayout in it, " +
                    "that is comprised of 4 buttons.  Clicking buttons changes the LinearLayout's" +
                    " background color.  When you drag horizontally, the HorizontalScroller drags" +
                    ". If a pointer starts on a button and then drags horizontally, the button " +
                    "will not be clicked when released."
        )
        ScrollableRow {
            AndroidView({ context ->
                LayoutInflater.from(context)
                    .inflate(R.layout.android_tap_in_compose_scroll, null).let { view ->
                        view.setBackgroundColor(Color.YELLOW)
                        view.findViewById<View>(R.id.buttonRed).apply {
                            isClickable = false
                            setOnClick {
                                view.setBackgroundColor(Color.RED)
                            }
                        }
                        view.findViewById<View>(R.id.buttonGreen).apply {
                            isClickable = false
                            setOnClick {
                                view.setBackgroundColor(Color.GREEN)
                            }
                        }
                        view.findViewById<View>(R.id.buttonBlue).apply {
                            isClickable = false
                            setOnClick {
                                view.setBackgroundColor(Color.BLUE)
                            }
                        }
                        view.findViewById<View>(R.id.buttonYellow).apply {
                            isClickable = false
                            setOnClick {
                                view.setBackgroundColor(Color.YELLOW)
                            }
                        }
                        view
                    }
            })
        }
    }
}

@Composable
private fun AndroidScrollInComposeScrollDifferentOrientation() {
    Column {
        Text(
            "Demonstrates correct \"scroll orientation\" locking when something scrollable in " +
                    "Android is nested inside something scrollable in Compose."
        )
        Text("You should only be able to scroll in one orientation at a time.")
        ScrollableRow(modifier = Modifier.background(androidx.compose.ui.graphics.Color.Blue)) {
            Box(modifier = Modifier.padding(96.dp)
                .background(androidx.compose.ui.graphics.Color.Red)) {
                AndroidView({ context ->
                    LayoutInflater.from(context).inflate(
                        R.layout.android_scroll_in_compose_scroll_different_orientation,
                        null
                    )
                })
            }
        }
    }
}

@Composable
private fun AndroidScrollInComposeScrollSameOrientation() {
    Column {
        Text(
            "Supposed to demonstrate correct nested scrolling when something scrollable in " +
                    "Android is inside something scrollable in Compose."
        )
        Text(
            "This doesn't actually work because nested scrolling isn't implemented between " +
                    "Compose and Android.  Normally, this lack of implementation would mean the " +
                    "parent would always intercept first and thus block the child from ever " +
                    "scrolling. However, currently, the touch slop for Android is smaller than " +
                    "that for Compose, and thus the child scrolls and prevents the parent from " +
                    "intercepting. "
        )
        ScrollableColumn(modifier = Modifier.background(androidx.compose.ui.graphics.Color.Blue)) {
            Box(
                modifier = Modifier
                    .padding(96.dp)
                    .background(color = androidx.compose.ui.graphics.Color.Red)
                    .preferredHeight(750.dp)
            ) {
                AndroidView({ context ->
                    LayoutInflater.from(context)
                        .inflate(R.layout.android_scroll_in_compose_scroll_same_orientation, null)
                })
            }
        }
    }
}

@Composable
private fun TwoAndroidScrollViewsInCompose() {
    Column {
        Text(
            "Below are two Android Scrollviews that are nested in two different children of " +
                    "Compose. The user should be able to scroll each independently at the same " +
                    "time, but given that we currently don't split motion, this is not work."
        )
        Row {
            AndroidView(
                { context ->
                    LayoutInflater.from(context)
                        .inflate(R.layout.android_scrollview, null)
                },
                Modifier.weight(2f)
            )
            AndroidView(
                { context ->
                    LayoutInflater.from(context)
                        .inflate(R.layout.android_scrollview, null)
                },
                Modifier.weight(1f)
            )
        }
    }
}