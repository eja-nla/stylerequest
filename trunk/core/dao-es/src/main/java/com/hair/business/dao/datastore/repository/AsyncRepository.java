package com.hair.business.dao.datastore.repository;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreAttributes;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import com.hair.business.dao.datastore.abstractRepository.AbstractAsyncRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Datastore repository impl.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class AsyncRepository implements AbstractAsyncRepository {

    private AsyncDatastoreService asyncDatastoreService;

    public AsyncRepository() {
        
        this.asyncDatastoreService = DatastoreServiceFactory.getAsyncDatastoreService();
    }


    public Future<Entity> get(Key key) {

        return asyncDatastoreService.get(key);
    }

    public Future<Entity> get(Transaction transaction, Key key)  {
        return asyncDatastoreService.get(transaction, key);
    }

    public Future<Map<Key, Entity>> get(Iterable<Key> iterable) {
        return asyncDatastoreService.get(iterable);
    }

    public Future<Map<Key, Entity>> get(Transaction transaction, Iterable<Key> iterable) {
        return asyncDatastoreService.get(transaction, iterable);
    }

    public Future<Key> put(Entity entity) {
        return asyncDatastoreService.put(entity);
    }

    public Future<Key> put(Transaction transaction, Entity entity) {
        return asyncDatastoreService.put(transaction, entity);
    }

    public Future<List<Key>> put(Iterable<Entity> iterable) {
        return asyncDatastoreService.put(iterable);
    }

    public Future<List<Key>> put(Transaction transaction, Iterable<Entity> iterable) {
        return asyncDatastoreService.put(transaction, iterable);
    }

    public Future<Void> delete(Key... keys) {

        return asyncDatastoreService.delete(keys);
    }

    public Future<Void> delete(Transaction transaction, Key... keys) {
        return asyncDatastoreService.delete(transaction, keys);
    }

    public Future<Void> delete(Iterable<Key> iterable) {
        return asyncDatastoreService.delete(iterable);
    }

    public Future<Void> delete(Transaction transaction, Iterable<Key> iterable) {
        return asyncDatastoreService.delete(transaction, iterable);
    }

    public Future<Transaction> beginTransaction() {
        return asyncDatastoreService.beginTransaction();
    }

    public Future<Transaction> beginTransaction(TransactionOptions transactionOptions) {
        return asyncDatastoreService.beginTransaction(transactionOptions);
    }

    public Future<KeyRange> allocateIds(String s, long l) {
        return asyncDatastoreService.allocateIds(s, l);
    }

    public Future<KeyRange> allocateIds(Key key, String s, long l) {
        return asyncDatastoreService.allocateIds(key, s, l);
    }

    public Future<DatastoreAttributes> getDatastoreAttributes() {
        return asyncDatastoreService.getDatastoreAttributes();
    }

    public Future<Map<Index, Index.IndexState>> getIndexes() {
        return asyncDatastoreService.getIndexes();
    }

    public PreparedQuery prepare(Query query) {
        return asyncDatastoreService.prepare(query);
    }

    public PreparedQuery prepare(Transaction transaction, Query query) {
        return asyncDatastoreService.prepare(transaction, query);
    }

    public Transaction getCurrentTransaction() {
        return asyncDatastoreService.getCurrentTransaction();
    }

    public Transaction getCurrentTransaction(Transaction transaction) {
        return asyncDatastoreService.getCurrentTransaction(transaction);
    }

    public Collection<Transaction> getActiveTransactions() {
        return asyncDatastoreService.getActiveTransactions();
    }
}
