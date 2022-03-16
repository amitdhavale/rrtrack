package asset.trak

import android.content.Context
import androidx.room.Room
import asset.trak.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object  AppModule {
    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationRfidCompenent (i.e. everywhere in the ApplicationRfid)
    @Provides
    fun provideBookDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        BookDatabase::class.java,
        "sqlite.db"
    ).
    fallbackToDestructiveMigration()
        .build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideBookDao(db: BookDatabase) = db.getBookDao() // The reason we can implement a Dao for the database

}