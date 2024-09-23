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

import android.content.Intent
import androidx.fragment.app.Fragment

data class ShareData(val title: String, val content: String)

/**
 * Use an intent to share some [data] from this fragment.
 */
fun Fragment.startSharingData(data: ShareData) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TITLE, data.title)
    intent.putExtra(Intent.EXTRA_SUBJECT, data.title)
    intent.putExtra(Intent.EXTRA_TEXT, data.content)
    startActivity(Intent.createChooser(intent, null))
}
