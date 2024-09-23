/*
 * Copyright 2020 Nicolas Maltais
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

package com.errortek.notes.ui

import com.errortek.notes.model.entity.Note
import com.errortek.notes.model.entity.NoteStatus

/**
 * A class representing a change of status for one or many notes.
 * Status change from [oldStatus] to [newStatus].
 */
data class StatusChange(
    val oldNotes: List<Note>,
    val oldStatus: NoteStatus,
    val newStatus: NoteStatus
)
