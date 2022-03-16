package asset.trak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import asset.trak.database.entity.*
import asset.trak.model.MasterClass
import asset.trak.model.SamplingArticles
import asset.trak.modelsrrtrack.AssetMain
import asset.trak.modelsrrtrack.InventoryScan
import asset.trak.modelsrrtrack.MasterLocation
import asset.trak.modelsrrtrack.MasterVendor

@Database(
    entities = [AssetMain::class, InventoryScan::class, MasterLocation::class, MasterVendor::class,MasterClass::class,SamplingArticles::class,BookAttributes::class, CategoryMaster::class, AssetClassification::class, AssetClassCatMap::class,
        LocationMaster::class, AssetCatalogue::class, CatSubCatMap::class, SubCategoryMaster::class,Inventorymaster::class,
        ScanTag::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun getBookDao(): BookDao
}