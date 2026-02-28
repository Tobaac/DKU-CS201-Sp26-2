import java.util.ArrayList;

class MyHeap {

    private ArrayList<PeopleRecord> heap;

    public MyHeap() {
        this.heap = new ArrayList<>();
    }

    public int size() { return heap.size(); }
    public boolean isEmpty() { return heap.isEmpty(); }

    public PeopleRecord peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    private void swap(int i, int j) {
        PeopleRecord temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // define ordering for the heap (min-heap)
    private int compare(PeopleRecord a, PeopleRecord b) {
        int c = a.getGivenName().compareToIgnoreCase(b.getGivenName());
        if (c != 0) return c;
        return a.getFamilyName().compareToIgnoreCase(b.getFamilyName());
    }

    //heapify
    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            // if child >= parent, heap property is satisfied
            if (compare(heap.get(index), heap.get(parent)) >= 0) {
                break;
            }

            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        int n = heap.size();

        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < n && compare(heap.get(left), heap.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < n && compare(heap.get(right), heap.get(smallest)) < 0) {
                smallest = right;
            }

            if (smallest == index) break;

            swap(index, smallest);
            index = smallest;
        }
    }

    public void insert(PeopleRecord node) {
        heap.add(node);
        bubbleUp(heap.size() - 1);
    }

    // remove and return the root (min element)
    public PeopleRecord remove() {
        if (heap.isEmpty()) return null;

        PeopleRecord root = heap.get(0);
        PeopleRecord last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubbleDown(0);
        }
        return root;
    }
}
