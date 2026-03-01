import java.io.*;
import java.util.*;

public class DatabaseProcessing {

  private MyBST bst;
  private ArrayList<PeopleRecord> allRecords;

  public DatabaseProcessing() {
    this.bst = new MyBST();
    this.allRecords = new ArrayList<>();
  }

  public void loadData(String fileName) {
    bst = new MyBST();
    allRecords = new ArrayList<>();

    int count = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;

      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty())
          continue;

        PeopleRecord r = PeopleRecord.fromLine(line);
        if (r != null) {
          bst.insert(r);
          allRecords.add(r);
          count++;
        }
      }

      System.out.println("Loaded records: " + count);

      int[] info = bst.getInfo();
      System.out.println("BST info: size=" + info[0] + ", height=" + info[1]);

    } catch (FileNotFoundException e) {
      System.out.println("ERROR: file not found -> " + fileName);
    } catch (IOException e) {
      System.out.println("ERROR: problem reading file -> " + e.getMessage());
    }
  }

  public ArrayList<PeopleRecord> search(String given, String family) {
    if (bst == null)
      bst = new MyBST();
    return bst.search(given, family);
  }

  // sort: heap sort by given name (then family name via PeopleRecord.compareTo)
  public ArrayList<PeopleRecord> sort() {
    if (allRecords == null || allRecords.isEmpty()) {
      System.out.println("WARNING: sort() called but no data loaded. Call loadData(\"people.txt\") first.");
      return new ArrayList<>();
    }

    MyHeap heap = new MyHeap();
    for (PeopleRecord r : allRecords) {
      heap.insert(r);
    }

    ArrayList<PeopleRecord> sorted = new ArrayList<>(allRecords.size());
    while (!heap.isEmpty()) {
      sorted.add(heap.remove());
    }

    return sorted;
  }

  // getMostFrequentWords this to get top count words length >= len, letters only,
  // from specified fields
  public ArrayList<String> getMostFrequentWords(int count, int len) throws ShortLengthException {
    if (len < 3) {
      System.out.println("ERROR: len must be at least 3.");
      throw new ShortLengthException("len must be at least 3");
    }

    if (allRecords == null || allRecords.isEmpty()) {
      System.out
          .println("WARNING: getMostFrequentWords() called but no data loaded. Call loadData(\"people.txt\") first.");
      return new ArrayList<>();
    }

    MyHashmap freq = new MyHashmap();
    ArrayList<String> uniqueWords = new ArrayList<>();

    for (PeopleRecord r : allRecords) {
      // Only these fields per PDF:
      addWordsFromField(r.getGivenName(), len, freq, uniqueWords);
      addWordsFromField(r.getFamilyName(), len, freq, uniqueWords);
      addWordsFromField(r.getCompanyName(), len, freq, uniqueWords);
      addWordsFromField(r.getAddress(), len, freq, uniqueWords);
      addWordsFromField(r.getCity(), len, freq, uniqueWords);
      addWordsFromField(r.getCounty(), len, freq, uniqueWords);
      addWordsFromField(r.getState(), len, freq, uniqueWords);
    }

    // sort uniqueWords by frequency descending, tie-break alphabetically
    Collections.sort(uniqueWords, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        int fa = safeInt(freq.get(a));
        int fb = safeInt(freq.get(b));
        if (fa != fb)
          return Integer.compare(fb, fa); // descending
        return a.compareToIgnoreCase(b);
      }
    });

    int limit = Math.min(count, uniqueWords.size());
    ArrayList<String> result = new ArrayList<>(limit);

    for (int i = 0; i < limit; i++) {
      String w = uniqueWords.get(i);
      result.add(w + " : " + safeInt(freq.get(w)));
    }

    return result;
  }

  // Helper: extract letter only words, count those with length >= len
  private static void addWordsFromField(String text, int len, MyHashmap freq, ArrayList<String> uniqueWords) {
    if (text == null || text.isEmpty())
      return;

    int n = text.length();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < n; i++) {
      char c = text.charAt(i);
      if (Character.isLetter(c)) {
        sb.append(Character.toLowerCase(c));
      } else {
        flushWord(sb, len, freq, uniqueWords);
      }
    }
    flushWord(sb, len, freq, uniqueWords);
  }

  private static void flushWord(StringBuilder sb, int len, MyHashmap freq, ArrayList<String> uniqueWords) {
    if (sb.length() == 0)
      return;

    String w = sb.toString();
    sb.setLength(0);

    // w is already letters-only because we only append letters
    if (w.length() < len)
      return;

    Integer cur = freq.get(w);
    if (cur == null) {
      uniqueWords.add(w);
      freq.put(w, 1);
    } else {
      freq.put(w, 1); // put adds delta in your MyHashmap implementation
    }
  }

  private static int safeInt(Integer x) {
    return (x == null) ? 0 : x.intValue();
  }

  public static void main(String[] args) {
    String fileName = "people.txt";
    if (args != null && args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
      fileName = args[0].trim();
    }

    DatabaseProcessing dp = new DatabaseProcessing();
    dp.loadData(fileName);

    // Test search (you can change these names to ones you know exist)
    System.out.println();
    System.out.println("SEARCH TEST:");
    if (dp.allRecords.size() > 0) {
      PeopleRecord first = dp.allRecords.get(0);
      ArrayList<PeopleRecord> hits = dp.search(first.getGivenName(), first.getFamilyName());
      System.out.println("Matches for " + first.getGivenName() + " " + first.getFamilyName() + ": " + hits.size());
      for (int i = 0; i < Math.min(5, hits.size()); i++) {
        System.out.println("  " + hits.get(i));
      }
    } else {
      System.out.println("No records loaded, cannot run search test.");
    }

    // Test sort
    System.out.println();
    System.out.println("SORT TEST:");
    ArrayList<PeopleRecord> sorted = dp.sort();
    System.out.println("Sorted records count: " + sorted.size());
    for (int i = 0; i < Math.min(10, sorted.size()); i++) {
      System.out.println("  " + sorted.get(i));
    }

    // Test most frequent words
    System.out.println();
    System.out.println("MOST FREQUENT WORDS TEST:");
    try {
      ArrayList<String> top = dp.getMostFrequentWords(10, 5);
      for (String s : top) {
        System.out.println("  " + s);
      }
    } catch (ShortLengthException e) {
      System.out.println("Caught ShortLengthException: " + e.getMessage());
    }

    // Prove exception works
    System.out.println();
    System.out.println("EXCEPTION TEST (len < 3):");
    try {
      dp.getMostFrequentWords(5, 2);
    } catch (ShortLengthException e) {
      System.out.println("Caught ShortLengthException as expected: " + e.getMessage());
    }
  }
}