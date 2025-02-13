/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.data.db

import androidx.paging.PagingSource
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import tm.alashow.domain.models.Entity
import tm.alashow.domain.models.PaginatedEntity

abstract class BaseDao<E : Entity> {
    @Insert
    abstract suspend fun insert(entity: E)

    @Insert
    abstract suspend fun insertAll(vararg entity: E)

    @Insert
    abstract suspend fun insertAll(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Update
    suspend fun updateOrInsert(entity: E) {
        val entry = entry(entity.id).firstOrNull()
        if (entry == null) {
            insert(entity)
        } else update(entity)
    }

    @Delete
    abstract suspend fun delete(entity: E)

    @Delete
    abstract suspend fun delete(id: String)

    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    // abstract fun entriesObservable(): Flow<List<E>>
    abstract fun entriesObservable(count: Int, offset: Int): Flow<List<E>>

    abstract fun entriesPagingSource(): PagingSource<Int, E>

    abstract fun entry(id: String): Flow<E>
    abstract fun entryNullable(id: String): Flow<E?>

    abstract fun entriesById(ids: List<String>): Flow<List<E>>

    abstract suspend fun has(id: String): Int
}

abstract class EntityDao<Params : Any, E : Entity> : BaseDao<E>() {

    abstract fun entriesPagingSource(params: Params): PagingSource<Int, E>
    abstract fun entriesObservable(params: Params, page: Int): Flow<List<E>>
    abstract suspend fun count(params: Params): Int
    abstract suspend fun delete(params: Params)

    @Transaction
    open suspend fun update(params: Params, entity: E) {
        delete(params)
        insert(entity)
    }
}

abstract class PaginatedEntryDao<Params : Any, E : PaginatedEntity> : EntityDao<Params, E>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(vararg entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(entities: List<E>)

    abstract suspend fun delete(params: Params, page: Int)
    abstract suspend fun getLastPage(params: Params): Int?

    @Transaction
    open suspend fun update(id: String, entity: E) {
        delete(id)
        insert(entity)
    }

    @Transaction
    open suspend fun update(params: Params, page: Int, entities: List<E>) {
        delete(params, page)
        insertAll(entities)
    }

    /**
     * Inserts given entities if it doesn't exist already in database.
     * This is little dirty because entity ids are not actually primary keys.
     */
    @Transaction
    open suspend fun insertMissing(entities: List<E>) {
        val existingIds = entriesById(entities.map { it.id }).first().map { it.id }.toSet()
        insertAll(entities.filterNot { entity -> existingIds.contains(entity.id) })
    }
}
