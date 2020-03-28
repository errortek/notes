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

package com.maltaisn.notes.di

import android.content.Context
import com.maltaisn.notes.ui.edit.EditFragment
import com.maltaisn.notes.ui.main.MainActivity
import com.maltaisn.notes.ui.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    DatabaseModule::class,
    FirebaseModule::class
])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: MainFragment)
    fun inject(fragment: EditFragment)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

}