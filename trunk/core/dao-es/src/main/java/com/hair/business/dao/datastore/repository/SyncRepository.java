package com.hair.business.dao.datastore.repository;

import com.google.appengine.api.datastore.DatastoreAttributes;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import com.hair.business.dao.datastore.abstractRepository.AbstractSyncRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Datastore repository impl.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class SyncRepository implements AbstractSyncRepository {

    private DatastoreService datastoreService;

    public SyncRepository() {
        
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }


    public Entity get(Key key) throws EntityNotFoundException {

        return datastoreService.get(key);
    }

    public Entity get(Transaction transaction, Key key) throws EntityNotFoundException {
        return datastoreService.get(transaction, key);
    }

    public Map<Key, Entity> get(Iterable<Key> iterable) {
        return datastoreService.get(iterable);
    }

    public Map<Key, Entity> get(Transaction transaction, Iterable<Key> iterable) {
        return datastoreService.get(transaction, iterable);
    }

    public Key put(Entity entity) {
        return datastoreService.put(entity);
    }

    public Key put(Transaction transaction, Entity entity) {
        return datastoreService.put(transaction, entity);
    }

    public List<Key> put(Iterable<Entity> iterable) {
        return datastoreService.put(iterable);
    }

    public List<Key> put(Transaction transaction, Iterable<Entity> iterable) {
        return datastoreService.put(transaction, iterable);
    }

    public void delete(Key... keys) {
        datastoreService.delete(keys);
    }

    public void delete(Transaction transaction, Key... keys) {
        datastoreService.delete(transaction, keys);
    }

    public void delete(Iterable<Key> iterable) {
        datastoreService.delete(iterable);
    }

    public void delete(Transaction transaction, Iterable<Key> iterable) {
        datastoreService.delete(transaction, iterable);
    }

    public Transaction beginTransaction() {
        return datastoreService.beginTransaction();
    }

    public Transaction beginTransaction(TransactionOptions transactionOptions) {
        return datastoreService.beginTransaction(transactionOptions);
    }

    public KeyRange allocateIds(String s, long l) {
        return datastoreService.allocateIds(s, l);
    }

    public KeyRange allocateIds(Key key, String s, long l) {
        return datastoreService.allocateIds(key, s, l);
    }

    public KeyRangeState allocateIdRange(KeyRange keyRange) {
        return datastoreService.allocateIdRange(keyRange);
    }

    public DatastoreAttributes getDatastoreAttributes() {
        return datastoreService.getDatastoreAttributes();
    }

    public Map<Index, Index.IndexState> getIndexes() {
        return datastoreService.getIndexes();
    }

    public PreparedQuery prepare(Query query) {
        return datastoreService.prepare(query);
    }

    public PreparedQuery prepare(Transaction transaction, Query query) {
        return datastoreService.prepare(transaction, query);
    }

    public Transaction getCurrentTransaction() {
        return datastoreService.getCurrentTransaction();
    }

    public Transaction getCurrentTransaction(Transaction transaction) {
        return datastoreService.getCurrentTransaction(transaction);
    }

    public Collection<Transaction> getActiveTransactions() {
        return datastoreService.getActiveTransactions();
    }
}
