package com.base.hilt.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        if (Build.VERSION.SDK_INT >= 23) {
            EncryptedSharedPreferences.create(
                PrefKey.PREFERENCE_NAME,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            context.getSharedPreferences(
                PrefKey.PREFERENCE_NAME, Context.MODE_PRIVATE
            )
        }

    @Singleton
    @Provides
    fun provideMyPreference(mSharedPreferences: SharedPreferences) =
        MyPreference(mSharedPreferences)

}