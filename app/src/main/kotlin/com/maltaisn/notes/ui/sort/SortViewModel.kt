/*
 * Copyright 2022 Nicolas Maltais
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

package com.errortek.notes.ui.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.errortek.notes.model.PrefsManager
import com.errortek.notes.model.SortDirection
import com.errortek.notes.model.SortField
import com.errortek.notes.ui.Event
import com.errortek.notes.ui.send
import javax.inject.Inject

class SortViewModel @Inject constructor(
    private val prefs: PrefsManager,
) : ViewModel() {

    private val _sortField = MutableLiveData<Event<SortField>>()
    val sortField: LiveData<Event<SortField>>
        get() = _sortField

    private val _sortDirection = MutableLiveData<Event<SortDirection>>()
    val sortDirection: LiveData<Event<SortDirection>>
        get() = _sortDirection

    fun start() {
        _sortField.send(prefs.sortField)
        _sortDirection.send(prefs.sortDirection)
    }
}
