package com.myohset.realmdatabasetest.di

import com.myohset.realmdatabasetest.data.MongoRepository
import com.myohset.realmdatabasetest.data.MongoRepositoryImpl
import com.myohset.realmdatabasetest.model.Address
import com.myohset.realmdatabasetest.model.Person
import com.myohset.realmdatabasetest.model.Pet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Person::class, Address::class, Pet::class
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideMongoRepository( realm: Realm ): MongoRepository {
        return MongoRepositoryImpl(realm = realm)
    }
}