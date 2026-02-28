class MyHashmap {

    private static class Entry {
        String key;
        int value;
        boolean isDeleted;

        Entry(String key, int value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }

    private Entry[] table;
    private int size;          // number of ACTIVE (not deleted) entries
    private int usedSlots;     // ACTIVE + DELETED (helps decide when to rehash)

    public MyHashmap() {
        this(101); // start prime-ish
    }

    public MyHashmap(int capacity) {
        table = new Entry[capacity];
        size = 0;
        usedSlots = 0;
    }

    public int size() {
        return size;
    }

    // core map operations

    public Integer get(String key) {
        int idx = findKeyIndex(key);
        if (idx == -1) return null;
        return table[idx].value;
    }

    public boolean containsKey(String key) {
        return findKeyIndex(key) != -1;
    }

    // For word counting: increment by 1 (or by delta)
    public void put(String key, int delta) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");

        // Resize if table getting too full (including tombstones)
        if ((double) usedSlots / table.length > 0.6) {
            rehash(nextCapacity(table.length * 2));
        }

        int h = baseIndex(key);
        int firstTombstone = -1;

        for (int i = 0; i < table.length; i++) {
            int idx = probeIndex(h, i);
            Entry e = table[idx];

            if (e == null) {
                // insert into first tombstone if we saw one, otherwise here
                int insertIdx = (firstTombstone != -1) ? firstTombstone : idx;
                table[insertIdx] = new Entry(key, delta);
                size++;
                if (firstTombstone == -1) usedSlots++; // new slot used
                return;
            }

            if (e.isDeleted) {
                if (firstTombstone == -1) firstTombstone = idx;
                continue;
            }

            if (e.key.equals(key)) {
                e.value += delta; // update existing
                return;
            }
        }

        // If we get here, table is in a bad state, force rehash then retry
        rehash(nextCapacity(table.length * 2));
        put(key, delta);
    }

    public boolean delete(String key) {
        int idx = findKeyIndex(key);
        if (idx == -1) return false;

        table[idx].isDeleted = true;
        size--;
        // usedSlots stays the same (slot still "used" due to tombstone)
        return true;
    }

    // helpers

    private int findKeyIndex(String key) {
        int h = baseIndex(key);

        for (int i = 0; i < table.length; i++) {
            int idx = probeIndex(h, i);
            Entry e = table[idx];

            if (e == null) {
                // Stop: EMPTY means key was never inserted in this probe chain
                return -1;
            }

            if (!e.isDeleted && e.key.equals(key)) {
                return idx;
            }
            // else keep probing
        }
        return -1;
    }

    private int baseIndex(String key) {
        int hc = key.hashCode();
        // avoid negative
        hc = hc & 0x7fffffff;
        return hc % table.length;
    }

    private int probeIndex(int base, int i) {
        return (base + i * i) % table.length;
    }

    private void rehash(int newCapacity) {
        Entry[] old = table;
        table = new Entry[newCapacity];
        size = 0;
        usedSlots = 0;

        for (Entry e : old) {
            if (e != null && !e.isDeleted) {
                // reinsert exact value (not delta)
                putExact(e.key, e.value);
            }
        }
    }

    private void putExact(String key, int value) {
        int h = baseIndexForCapacity(key, table.length);

        for (int i = 0; i < table.length; i++) {
            int idx = (h + i * i) % table.length;
            if (table[idx] == null) {
                table[idx] = new Entry(key, value);
                size++;
                usedSlots++;
                return;
            }
        }

        // should not happen if capacity is reasonable
        throw new IllegalStateException("Rehash failed");
    }

    private int baseIndexForCapacity(String key, int cap) {
        int hc = key.hashCode();
        hc = hc & 0x7fffffff;
        return hc % cap;
    }

    private int nextCapacity(int atLeast) {
        // simple: just return odd number; better: pick next prime
        if (atLeast % 2 == 0) return atLeast + 1;
        return atLeast;
    }
}