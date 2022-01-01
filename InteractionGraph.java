package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InteractionGraph {
    public static final int SRC = 0;
    public static final int DST = 1;
    public static final int TIMESTAMP = 2;
    public static final int RECEIVER = 0;
    public static final int WEIGHT = 1;

    /**
     * Read a file containing email transaction information and organize the data into a list.
     * @param fileName Name of the file to be read. Must not contain empty lines and must have at least one space
     *                 separating each of the three values in each line. The first value represents the user ID of
     *                 the sender, the second value the user ID of the receiver, and the third value the timestamp
     *                 of when the email was sent.
     * @param interactions Empty list within which the email information will be stored.
     * @return A list of email information where each element represents a unique transaction and contains three
     * elements: sender ID, receiver ID, and timestamp.
     */
    public List<ArrayList<Integer>> readFile(String fileName, List<ArrayList<Integer>> interactions) {
        List<String> allLinesInFile = new ArrayList<>();
        ArrayList<Integer> interaction = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                allLinesInFile.add(fileLine);
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        for (String s : allLinesInFile) {
            String[] line = s.split("\\s+");
            int num;

            for (String value : line) {
                num = Integer.parseInt(value);
                interaction.add(num);
            }

            interactions.add(new ArrayList<>(interaction));
            interaction.clear();
        }

        return new ArrayList<>(interactions);
    }

    /**
     * @param interactions list containing all the email transactions to consider
     * @param vertices the number of unique vertices (unique sender and receiver IDs) in interactions
     * @param adjacencyList an empty list within which will be stored the adjacency list interaction graph
     * @param UserIDToIndex Map that takes userID and returns its corresponding vertex number in the interaction graph
     * @param IndexToUserID Map that takes vertex number and returns the userID represented by that vertex number in the
     *                      interaction graph
     *
     * @return A directed weighted adjacency list with information from interactions
     */
    public static ArrayList<ArrayList<int[]>> createDirectedList(List<ArrayList<Integer>> interactions, int vertices,
                                                         ArrayList<ArrayList<int[]>> adjacencyList, Map<Integer, Integer> UserIDToIndex,
                                                         Map<Integer, Integer> IndexToUserID) {
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        int vertex = 0;
        for (int i = 0; i < interactions.size(); i++) {
            if (vertex > vertices) {
                break;
            }

            int sender = interactions.get(i).get(SRC);
            int receiver = interactions.get(i).get(DST);
            int weight = 0;

            for (ArrayList<Integer> interaction : interactions) {
                if (sender == interaction.get(SRC) && receiver == interaction.get(DST)) {
                    weight++;
                }
            }

            if (!UserIDToIndex.containsKey(sender)) {
                UserIDToIndex.put(sender, vertex);
                IndexToUserID.put(vertex, sender);
                int[] receiverAndWeight = {receiver, weight};
                adjacencyList.get(vertex).add(receiverAndWeight);
                vertex++;
            }

            else {
                int[] receiverAndWeight = {receiver, weight};
                boolean repeatInteraction = false;
                for (int[] array : adjacencyList.get(UserIDToIndex.get(sender))) {
                    if (array[RECEIVER] == receiver) {
                        repeatInteraction = true;
                        break;
                    }
                }

                if (!repeatInteraction) {
                    adjacencyList.get(UserIDToIndex.get(sender)).add(receiverAndWeight);
                }
            }
        }
        return new ArrayList<>(adjacencyList);
    }

    /**
     * @param interactions list containing all the email transactions to consider
     * @param vertices the number of unique vertices (unique sender and receiver IDs) in interactions
     * @param adjacencyList an empty list within which will be stored the adjacency list interaction graph
     * @param UserIDToIndex Map that takes userID and returns its corresponding vertex number in the interaction graph
     * @param IndexToUserID Map that takes vertex number and returns the userID represented by that vertex number in the
     *                      interaction graph
     *
     * @return An undirected weighted adjacency list with information from interactions
     */
    public static ArrayList<ArrayList<int[]>> createUndirectedList(List<ArrayList<Integer>> interactions, int vertices,
                                                                 ArrayList<ArrayList<int[]>> adjacencyList, Map<Integer, Integer> UserIDToIndex,
                                                                 Map<Integer, Integer> IndexToUserID) {
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        int vertex = 0;
        for (int i = 0; i < interactions.size(); i++) {
            if (vertex > vertices) {
                break;
            }

            int sender = interactions.get(i).get(SRC);
            int receiver = interactions.get(i).get(DST);
            int weight = 0;

            for (ArrayList<Integer> interaction : interactions) {
                if ((sender == interaction.get(SRC) && receiver == interaction.get(DST)) || (receiver == interaction.get(SRC) && sender == interaction.get(DST))) {
                    weight++;
                }
            }

            if(!UserIDToIndex.containsKey(sender) && !UserIDToIndex.containsKey(receiver) && sender != receiver) {
                UserIDToIndex.put(sender, vertex);
                IndexToUserID.put(vertex, sender);
                int[] receiverAndWeight = {receiver, weight};
                adjacencyList.get(vertex).add(receiverAndWeight);
                vertex++;

                UserIDToIndex.put(receiver, vertex);
                IndexToUserID.put(vertex, receiver);
                int[] senderAndWeight = {sender, weight};
                adjacencyList.get(vertex).add(senderAndWeight);
                vertex++;
            }

            else if (!UserIDToIndex.containsKey(sender)) {
                UserIDToIndex.put(sender, vertex);
                IndexToUserID.put(vertex, sender);
                int[] receiverAndWeight = {receiver, weight};
                adjacencyList.get(vertex).add(receiverAndWeight);
                vertex++;

                int[] senderAndWeight = {sender, weight};
                adjacencyList.get(UserIDToIndex.get(receiver)).add(senderAndWeight);
            }

            else if (!UserIDToIndex.containsKey(receiver)) {
                UserIDToIndex.put(receiver, vertex);
                IndexToUserID.put(vertex, receiver);
                int[] senderAndWeight = {sender, weight};
                adjacencyList.get(vertex).add(senderAndWeight);
                vertex++;

                int[] receiverAndWeight = {receiver, weight};
                adjacencyList.get(UserIDToIndex.get(sender)).add(receiverAndWeight);
            }

            else {
                int[] receiverAndWeight = {receiver, weight};
                int[] senderAndWeight = {sender, weight};
                boolean repeatInteraction = false;
                for (int[] array : adjacencyList.get(UserIDToIndex.get(sender))) {
                    if (array[RECEIVER] == receiver) {
                        repeatInteraction = true;
                        break;
                    }
                }

                if (!repeatInteraction) {
                    adjacencyList.get(UserIDToIndex.get(sender)).add(receiverAndWeight);
                    if (sender != receiver) {
                        adjacencyList.get(UserIDToIndex.get(receiver)).add(senderAndWeight);
                    }
                }
            }
        }
        return new ArrayList<>(adjacencyList);
    }
}

