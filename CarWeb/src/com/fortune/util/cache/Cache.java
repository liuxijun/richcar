package com.fortune.util.cache;

import com.fortune.util.cache.LinkedList;
import com.fortune.util.cache.LinkedListNode;

import java.util.HashMap;

/**
 * General purpose cache implementation. It stores objects associated with
 * unique keys in memory for fast access. All objects added to the cache must
 * implement the Cacheable interface, which requires objects to know their
 * size in memory. This restrictions allows the cache to never grow larger
 * than a specified amount.<p>
 *
 * If the cache does grow too large, objects will be removed such that those
 * that are accessed least frequently are removed first. Because expiration
 * happens automatically, the cache makes <b>no</b> gaurantee as to how long
 * an object will remain in cache after it is put in. The cache will return
 * null if the requested object is not found.<p>
 *
 * Optionally, a maximum lifetime for all objects can be specified. In that
 * case, objects will be deleted from cache after that amount of time, even
 * if they are frequently accessed. This feature is useful if objects put in
 * cache represent data that should be periodically refreshed; for example,
 * information from a database.<p>
 *
 * Cache is optimized for fast data access. The getObject() method has 0(n)
 * performance regardless of cache size. The other cache operations also
 * perform quite fast.<p>
 *
 * Cache objects are thread safe.<p>
 *
 * The algorithm for cache is as follows: a HashMap is maintained for fast
 * object lookup. Two linked lists are maintained: one keeps objects in the
 * order they are accessed from cache, the other keeps objects in the order
 * they were originally added to cache. When objects are added to cache, they
 * are first wrapped by a CacheObject which maintains the following pieces
 * of information:<ul>
 *    <li> The size of the object (in bytes).
 *    <li> A pointer to the node in the linked list that maintains accessed
 *         order for the object. Keeping a reference to the node lets us avoid
 *         linear scans of the linked list.
 *    <li> A pointer to the node in the linked list that maintains the age
 *         of the object in cache. Keeping a reference to the node lets us avoid
 *         linear scans of the linked list.</ul>
 *
 * To get an object from cache, a hash lookup is performed to get a reference
 * to the CacheObject that wraps the real object we are looking for.
 * The object is subsequently moved to the front of the accessed linked list
 * and any necessary cache cleanups are performed. Cache deletion and expiration
 * is performed as needed.
 *
 * @see Cacheable
 */
public class Cache implements Cacheable {

    /**
     * One of the major potential bottlenecks of the cache is performing
     * System.currentTimeMillis() for every cache get operation. Instead,
     * we maintain a global timestamp that gets updated once a second. This
     * means that cache expirations can be no more than one second accurate.
     */
    //protected static long currentTime = CacheTimer.currentTime;

    /**
     * Maintains the hash of cached objects. Hashing provides the best
     * performance for fast lookups.
     */
    protected HashMap cachedObjectsHash;

    /**
     * Linked list to maintain order that cache objects are accessed
     * in, most used to least used.
     */
    protected LinkedList lastAccessedList;

    /**
     * Linked list to maintain time that cache objects were initially added
     * to the cache, most recently added to oldest added.
     */
    protected LinkedList ageList;

    /**
     * Maximum size in bytes that the cache can grow to. Default
     * maximum size is 128 K.
     */
    protected int maxSize = Integer.MAX_VALUE ;

    /**
     * Maintains the current size of the cache in bytes.
     */
    protected int size = 0;

    /**
     * Maximum length of time objects can exist in cache before expiring.
     * Default is that objects have no maximum lifetime.
     */
    protected long maxLifetime = -1;

    /**
     * Maintain the number of cache hits and misses. A cache hit occurs every
     * time the get method is called and the cache contains the requested
     * object. A cache miss represents the opposite occurence.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     */
    protected long cacheHits, cacheMisses = 0L;

    /**
     * Create a new cache with default values. Default cache size is 128K with
     * no maximum lifetime.
     */
    public Cache() {
        // Our primary data structure is a hash map. The default capacity of 11
        // is too small in almost all cases, so we set it bigger.
        cachedObjectsHash = new HashMap(103);

        lastAccessedList = new LinkedList();
        ageList = new LinkedList();
    }

    /**
     * Create a new cache and specify the maximum size for the cache in bytes.
     * Items added to the cache will have no maximum lifetime.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    public Cache(int maxSize) {
        this();
        this.maxSize = maxSize;
    }

    /**
     * Create a new cache and specify the maximum lifetime of objects. The
     * time should be specified in milleseconds. The minimum lifetime of any
     * cache object is 1000 milleseconds (1 second). Additionally, cache
     * expirations have a 1000 millesecond resolution, which means that all
     * objects are guaranteed to be expired within 1000 milliseconds of their
     * maximum lifetime.
     *
     * @param maxLifetime the maximum amount of time objects can exist in
     *    cache before being deleted.
     */
    public Cache(long maxLifetime) {
        this();
        this.maxLifetime = maxLifetime;
    }

    /**
     * Create a new cache and specify the maximum size of for the cache in
     * bytes, and the maximum lifetime of objects.
     *
     * @param maxSize the maximum size of the cache in bytes.
     * @param maxLifetime the maximum amount of time objects can exist in
     *    cache before being deleted.
     */
    public Cache(int maxSize, long maxLifetime) {
        this();
        this.maxSize = maxSize;
        this.maxLifetime = maxLifetime;
    }

    /**
     * Returns the current size of the cache in bytes.
     *
     * @return the size of the cache in bytes.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @return the maximum size of the cache in bytes.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Sets the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    public void setMaxSize(int maxSize) {
        //this.maxSize = maxSize;
        // It's possible that the new max size is smaller than our current cache
        // size. If so, we need to delete infrequently used items.
        cullCache();
    }

    /**
     * Returns the number of objects in the cache.
     *
     * @return the number of objects in the cache.
     */
    public synchronized int getNumElements() {
        return cachedObjectsHash.size();
    }

    /**
     * Adds a new Cacheable object to the cache. The key must be unique.
     *
     * @param key a unique key for the object being put into cache.
     * @param object the Cacheable object to put into cache.
     */
    public synchronized void add(Object key, Cacheable object) {
        // Delete an old entry if it exists.
        remove(key);

        int objectSize = object.getSize();
        // If the object is bigger than the entire cache, simply don't add it.
        if (objectSize > maxSize * .90) {
            return;
        }
        size += objectSize;
        CacheObject cacheObject = new CacheObject(object, objectSize);
        cachedObjectsHash.put(key, cacheObject);
        // Make an entry into the cache order list.
        LinkedListNode lastAccessedNode = lastAccessedList.addFirst(key);
        // Store the cache order list entry so that we can get back to it
        // during later lookups.
        cacheObject.lastAccessedListNode = lastAccessedNode;
        // Add the object to the age list
        LinkedListNode ageNode = ageList.addFirst(key);
        // We make an explicit call to currentTimeMillis() so that total accuracy
        // of lifetime calculations is better than one second.
        ageNode.timestamp = System.currentTimeMillis();
        cacheObject.ageListNode = ageNode;

        // If cache is too full, remove least used cache entries until it is
        // not too full.
        cullCache();
    }

    /**
     * Gets an object from cache. This method will return null under two
     * conditions:<ul>
     *    <li>The object referenced by the key was never added to cache.
     *    <li>The object referenced by the key has expired from cache.</ul>
     *
     * @param key the unique key of the object to get.
     * @return the Cacheable object corresponding to unique key.
     */
    public synchronized Object get(Object key) {
        // First, clear all entries that have been in cache longer than the
        // maximum defined age.

        //todo
        //deleteExpiredEntries();

        CacheObject cacheObject = (CacheObject) cachedObjectsHash.get(key);

        //todo
        if (maxLifetime > 0 && cacheObject!=null){
            //if ((currentTime - maxLifetime) > cacheObject.ageListNode.timestamp){
            if ((System.currentTimeMillis() - maxLifetime) > cacheObject.ageListNode.timestamp){
                remove(key);
                cacheObject = null;
            }
        }
        //todo

        if (cacheObject == null) {
            // The object didn't exist in cache, so increment cache misses.
            cacheMisses++;
            return null;
        }

        // The object exists in cache, so increment cache hits.
        cacheHits++;

        // Remove the object from it's current place in the cache order list,
        // and re-insert it at the front of the list.
        cacheObject.lastAccessedListNode.remove();
        lastAccessedList.addFirst(cacheObject.lastAccessedListNode);

        return cacheObject.object;
    }

    /**
     * Removes an object from cache.
     *
     * @param key the unique key of the object to remove.
     */
    public synchronized void remove(Object key) {
        CacheObject cacheObject = (CacheObject) cachedObjectsHash.get(key);
        // If the object is not in cache, stop trying to remove it.
        if (cacheObject == null) {
            return;
        }
        // remove from the hash map
        cachedObjectsHash.remove(key);
        // remove from the cache order list
        cacheObject.lastAccessedListNode.remove();
        cacheObject.ageListNode.remove();
        // remove references to linked list nodes
        cacheObject.ageListNode = null;
        cacheObject.lastAccessedListNode = null;
        // removed the object, so subtract its size from the total.
        size -= cacheObject.size;
    }

    /**
     * Clears the cache of all objects. The size of the cache is reset to 0.
     */
    public synchronized void clear() {
        Object[] keys = cachedObjectsHash.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            remove(keys[i]);
        }

        // Now, reset all containers.
        cachedObjectsHash.clear();
        cachedObjectsHash = new HashMap(103);
        lastAccessedList.clear();
        lastAccessedList = new LinkedList();
        ageList.clear();
        ageList = new LinkedList();

        size = 0;
        cacheHits = 0;
        cacheMisses = 0;
    }

    /**
     * Returns an array of the keys contained in the cache.
     *
     * @return an array of the keys present in the cache.
     */
    public Object[] keys() {
        return cachedObjectsHash.keySet().toArray();
    }

    /**
     * Returns an array of the values contained in the cache.
     *
     * @return a array of the cache entries.
     */
    public Object[] values() {
        Object[] cacheObjects = cachedObjectsHash.values().toArray();
        Object[] values = new Object[cacheObjects.length];
        for (int i = 0; i < cacheObjects.length; i++) {
            values[i] = ((CacheObject) cacheObjects[i]).object;
        }
        return values;
    }

    /**
     * Returns the number of cache hits. A cache hit occurs every
     * time the get method is called and the cache contains the requested
     * object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheHits() {
        return cacheHits;
    }

    /**
     * Returns the number of cache misses. A cache miss occurs every
     * time the get method is called and the cache does not contain the
     * requested object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheMisses() {
        return cacheMisses;
    }

    /**
     * Clears all entries out of cache where the entries are older than the
     * maximum defined age.
     */
    public final void deleteExpiredEntries() {
        //Check if expiration is turned on.
        if (maxLifetime <= 0) {
            return;
        }

        // Remove all old entries. To do this, we remove objects from the end
        // of the linked list until they are no longer too old. We get to avoid
        // any hash lookups or looking at any more objects than is strictly
        // neccessary.
        LinkedListNode node = ageList.getLast();
        //If there are no entries in the age list, return.
        if (node == null) {
            return;
        }

        // Determine the expireTime, which is the moment in time that elements
        // should expire from cache. Then, we can do an easy to check to see
        // if the expire time is greater than the expire time.
        long expireTime = System.currentTimeMillis() - maxLifetime;

        while (expireTime > node.timestamp) {
            // Remove the object
            remove(node.object);

            // Get the next node.
            node = ageList.getLast();
            // If there are no more entries in the age list, return.
            if (node == null) {
                return;
            }
        }
    }

    /**
     * Removes objects from cache if the cache is too full. "Too full" is
     * defined as within 3% of the maximum cache size. Whenever the cache is
     * is too big, the least frequently used elements are deleted until the
     * cache is at least 10% empty.
     */
    private final void cullCache() {
        // See if the cache size is within 3% of being too big. If so, clean out
        // cache until it's 10% free.
        if (size >= maxSize * .97) {
            // First, delete any old entries to see how much memory that frees.
            deleteExpiredEntries();
            int desiredSize = (int) (maxSize * .90);
            while (size > desiredSize) {
                // Get the key and invoke the remove method on it.
                remove(lastAccessedList.getLast().object);
            }
        }
    }
}