package ru.android.hikanumaruapp.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.android.hikanumaruapp.api.token.TokenManager
import ru.android.hikanumaruapp.local.storage.UserDataConst.Companion.USER_PREFERENCES_NAME
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@Module
@InstallIn(SingletonComponent::class)
class UserDataSingletonModule {

    @Singleton
    @Provides
    fun provideUserData(@ApplicationContext context: Context): UserDataStore = UserDataStore(context)

    @Singleton
    @Provides
    fun provideUserDataPreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext,USER_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
        )
    }
}
