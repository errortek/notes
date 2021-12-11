/*
 * Copyright 2021 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maltaisn.notes.ui.navigation.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.maltaisn.notes.model.entity.Label.Companion.NO_COLOR
import com.maltaisn.notes.ui.navigation.NavigationDestination
import com.maltaisn.notes.ui.navigation.adapter.NavigationAdapter.ViewType

sealed class NavigationItem {
    abstract val id: Long
    abstract val type: ViewType
}

data class NavigationTopItem(
    override val id: Long,
) : NavigationItem() {
    override val type: ViewType
        get() = ViewType.TOP
}

data class NavigationDestinationItem(
    override val id: Long,
    val destination: NavigationDestination,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int = 0,   // can be 0 to fallback on title text
    val title: String = "",             // can be empty to fallback on title res
    val checked: Boolean = false,
    val color: Int = NO_COLOR
) : NavigationItem() {

    override val type: ViewType
        get() = ViewType.ITEM
}

data class NavigationHeaderItem(
    override val id: Long,
    val actionDestination: NavigationDestination.NavGraph?,
    @StringRes val titleRes: Int,
    @StringRes val actionBtnTextRes: Int,  // can be 0 for no action button
) : NavigationItem() {

    override val type: ViewType
        get() = ViewType.HEADER
}

data class NavigationDividerItem(
    override val id: Long,
) : NavigationItem() {

    override val type: ViewType
        get() = ViewType.DIVIDER
}
