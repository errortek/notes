/*
 * Copyright 2023 Nicolas Maltais
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

package com.errortek.notes.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.errortek.notes.model.PrefsManager
import com.errortek.notes.model.ReminderAlarmManager
import com.errortek.notes.model.entity.Label
import com.errortek.notes.model.entity.LabelRef
import com.errortek.notes.model.entity.Note
import com.errortek.notes.model.entity.NoteStatus
import com.errortek.notes.model.entity.PinnedStatus
import com.errortek.notes.ui.MockAlarmCallback
import com.errortek.notes.ui.getOrAwaitValue
import com.errortek.notes.ui.navigation.HomeDestination
import com.errortek.notes.ui.note.NoteItemFactory
import com.errortek.notes.ui.note.NoteViewModel
import com.errortek.notes.ui.note.SwipeAction
import com.errortek.notes.ui.note.adapter.NoteAdapter
import com.errortek.notes.ui.note.adapter.NoteItem
import com.errortek.notes.ui.note.adapter.NoteListLayoutMode
import com.errortek.notesshared.MainCoroutineRule
import com.errortek.notesshared.model.MockLabelsRepository
import com.errortek.notesshared.model.MockNotesRepository
import com.errortek.notesshared.testNote
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeViewModelLabelsTest {

    private lateinit var viewModel: HomeViewModel

    private lateinit var labelsRepo: MockLabelsRepository
    private lateinit var notesRepo: MockNotesRepository
    private lateinit var prefs: PrefsManager
    private lateinit var itemFactory: NoteItemFactory

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        labelsRepo = MockLabelsRepository()
        labelsRepo.addLabel(Label(1, "label1"))
        labelsRepo.addLabel(Label(2, "label2"))
        labelsRepo.addLabel(Label(3, "label3"))
        labelsRepo.addLabelRefs(listOf(
            LabelRef(1, 1),
            LabelRef(2, 1),
            LabelRef(3, 1),
            LabelRef(4, 1),
            LabelRef(1, 2),
            LabelRef(3, 2),
            LabelRef(2, 3),
            LabelRef(3, 3),
        ))

        notesRepo = MockNotesRepository(labelsRepo)
        notesRepo.addNote(testNote(id = 1,
            status = NoteStatus.ACTIVE,
            pinned = PinnedStatus.PINNED))
        notesRepo.addNote(testNote(id = 2, status = NoteStatus.ACTIVE))
        notesRepo.addNote(testNote(id = 3, status = NoteStatus.ARCHIVED))

        // never shown
        notesRepo.addNote(testNote(id = 4, status = NoteStatus.DELETED))

        prefs = mock {
            on { listLayoutMode } doReturn NoteListLayoutMode.LIST
        }

        itemFactory = NoteItemFactory(prefs)

        viewModel = HomeViewModel(SavedStateHandle(), notesRepo, labelsRepo, prefs,
            ReminderAlarmManager(notesRepo, MockAlarmCallback()), itemFactory, mock())
    }

    @Test
    fun `should show all notes with headers (pinned, unpinned, archived)`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(1)))
        assertTrue(viewModel.fabShown.getOrAwaitValue())

        assertEquals(listOf(
            HomeViewModel.PINNED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(1), listOf(labelsRepo.requireLabelById(2))),
            HomeViewModel.NOT_PINNED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(2), listOf(labelsRepo.requireLabelById(3))),
            HomeViewModel.ARCHIVED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(3), listOf(
                labelsRepo.requireLabelById(2), labelsRepo.requireLabelById(3))),
        ), viewModel.noteItems.getOrAwaitValue())
    }

    @Test
    fun `should show all notes with headers (pinned, archived)`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(2)))
        assertEquals(listOf(
            HomeViewModel.PINNED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(1), listOf(labelsRepo.requireLabelById(1))),
            HomeViewModel.ARCHIVED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(3), listOf(
                labelsRepo.requireLabelById(1), labelsRepo.requireLabelById(3))),
        ), viewModel.noteItems.getOrAwaitValue())
    }

    @Test
    fun `should show all notes with headers (not pinned, archived)`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(3)))
        assertEquals(listOf(
            noteItem(notesRepo.requireNoteById(2), listOf(labelsRepo.requireLabelById(1))),
            HomeViewModel.ARCHIVED_HEADER_ITEM,
            noteItem(notesRepo.requireNoteById(3), listOf(
                labelsRepo.requireLabelById(1), labelsRepo.requireLabelById(2))),
        ), viewModel.noteItems.getOrAwaitValue())
    }

    @Test
    fun `should update list when data is changed`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(3)))
        labelsRepo.deleteLabelRefs(listOf(LabelRef(3, 3)))
        assertEquals(listOf(
            noteItem(notesRepo.requireNoteById(2), listOf(labelsRepo.requireLabelById(1))),
        ), viewModel.noteItems.getOrAwaitValue())
    }

    @Test
    fun `should consider selection as active and unpinned`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(1)))
        viewModel.onNoteItemLongClicked(getNoteItemAt(1), 1)
        viewModel.onNoteItemLongClicked(getNoteItemAt(3), 3)
        assertEquals(NoteViewModel.NoteSelection(2,
            NoteStatus.ACTIVE, PinnedStatus.UNPINNED, false),
            viewModel.currentSelection.getOrAwaitValue())
    }

    @Test
    fun `should consider selection as archived`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(3)))
        viewModel.onNoteItemLongClicked(getNoteItemAt(2), 2)
        assertEquals(NoteViewModel.NoteSelection(1,
            NoteStatus.ARCHIVED, PinnedStatus.CANT_PIN, false),
            viewModel.currentSelection.getOrAwaitValue())
    }

    @Test
    fun `should consider selection as active (active + archived selected)`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(3)))
        viewModel.onNoteItemLongClicked(getNoteItemAt(0), 0)
        viewModel.onNoteItemLongClicked(getNoteItemAt(2), 2)
        assertEquals(NoteViewModel.NoteSelection(2,
            NoteStatus.ACTIVE, PinnedStatus.UNPINNED, false),
            viewModel.currentSelection.getOrAwaitValue())
    }

    @Test
    fun `should not allow swipe actions`() = runTest {
        viewModel.setDestination(HomeDestination.Labels(labelsRepo.requireLabelById(3)))
        assertEquals(SwipeAction.NONE, viewModel.getNoteSwipeAction(NoteAdapter.SwipeDirection.LEFT))
        assertEquals(SwipeAction.NONE, viewModel.getNoteSwipeAction(NoteAdapter.SwipeDirection.RIGHT))
    }

    // the rest is already tested in NoteViewModelTest

    private fun getNoteItemAt(pos: Int) = viewModel.noteItems.getOrAwaitValue()[pos] as NoteItem

    private fun noteItem(note: Note, labels: List<Label>) = itemFactory.createItem(note, labels, false)
}
