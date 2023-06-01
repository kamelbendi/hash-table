import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LinearProbingHashTable {
    private Integer[] table;
    private int size;

    public LinearProbingHashTable(int capacity) {
        table = new Integer[capacity];
        size = 0;
    }

    public int hash(int key) {
        return key % table.length;
    }

    public void insert(int key) {
        int index = hash(key);
        while (table[index] != null) {
            index = (index + 1) % table.length;
        }
        table[index] = key;
        size++;
    }

    public int get(int key) {
        int index = hash(key);
        while (table[index] != null) {
            if (table[index] == key) {
                return index;
            }
            index = (index + 1) % table.length;
        }
        return -1;
    }

    public static void main(String[] args) throws IOException {
        LinearProbingHashTable linearProbingHashTable = new LinearProbingHashTable(1000000);
        double[] loadFactors = { 0.9 };
        File file = new File("set_of_1050000_random_numbers.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        for (double loadFactor : loadFactors) {
            int numEle = (int) Math.round(1050000 / loadFactor);
            double[] results = linearProbingHashTable.testHashTable(loadFactor, numEle, br);
            System.out.printf(
                    "Load factor: %.1f, Hits: %d, Misses: %d\n",
                    loadFactor, (int) results[0], (int) results[1]);
        }

        br.close();
        fr.close();
    }

    public double[] testHashTable(double loadFactor, int numElements, BufferedReader br) throws IOException {
        int numProbes = 0;
        int numHits = 0;
        int numMisses = 0;
        LinearProbingHashTable hashTable = new LinearProbingHashTable(numElements);

        String line;
        while ((line = br.readLine()) != null) {
            String[] numbers = line.split(" ");
            for (String number : numbers) {
                hashTable.insert(parseLargeInt(number));
            }
        }

        String line2;
        BufferedReader br2 = new BufferedReader(new FileReader(new File("set_of_1050000_random_numbers.txt")));
        while ((line2 = br2.readLine()) != null) {
            String[] numbers = line2.split(" ");
            for (String number : numbers) {
                int n = parseLargeInt(number);

                // Compute the hash value
                int hashValue = hash(n);

                // Probe the hash table
                int i = 0;
                while (hashTable.table[(hashValue + i) % hashTable.table.length] != null) {
                    numProbes++;
                    if (hashTable.table[(hashValue + i) % hashTable.table.length] == n) {
                        numHits++;
                        break;
                    }
                    i++;
                }
                if (hashTable.table[(hashValue + i) % hashTable.table.length] == null) {
                    numProbes++;
                    numMisses++;
                }
            }
        }
        br2.close();

        double avgProbes = (double) numProbes / (numHits + numMisses);
        return new double[] { numHits, numMisses };
    }

    public int parseLargeInt(String number) {
        long num = Long.parseLong(number);
        return (int) (num % Integer.MAX_VALUE);
    }
}