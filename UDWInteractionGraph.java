package cpen221.mp2;

import java.util.*;
/**
 * A UDWInteractionGraph represents a undirected weighted graph of user email
 * interactions within a specific window of time, meaning that user A sending to B and B sending to A are not considered
 * separately
 *
 * Abstraction function: uses an Adjacency list (Arraylist of Arraylists of integer arrays)
 * to represent an undirected weighted graph. Elements of the outermost arraylist (one that contains the other arraylist)
 * represent a vertex corresponding to a user ID. Each element of the innermost arraylist contains a 2-element integer array that corresponds to an adjacent vertex (receiver)
 * that the user has sent emails to. The first element of the array is the user ID of the receiver while the second element represents the weight. Since the graph is
 * undirected, weight represents the total back and forth transactions between the sender and the receiver.
 */

public class UDWInteractionGraph extends InteractionGraph {
    private final int vertices;
    private ArrayList<ArrayList<int[]>> adjacencyList;
    private final Map<Integer, Integer> IndexToUserID = new HashMap<>();
    private final Map<Integer, Integer> UserIDToIndex = new HashMap<>();
    private List<ArrayList<Integer>> interactions;

    /** Rep Invariants:
     * -AdjacencyList has the same number of elements in its outermost arraylist as vertices .
     * -IndexToUserID contains *vertices* entries each mapping to the corresponding UserID represented by the specific
     * vertex number.
     * -User ID is positive
     * -UserIDToIndex contains *vertices* entries each mapping to the corresponding vertex number that the UserID is
     * represented by in the interactions graph.
     *- Interactions contains all the valid email interactions that are represented in the AdjacencyList graph.
     */

    /* ------- Task 1 ------- */
    /* Building the Constructors */

    /**
     * Gets all the unique vertices that would be in a directed graph based on interaction data
     * @param allInteractions Contains the interactions between vertices, where
     *                        the first element represents the sender, the second represents the
     *                        receiver, and the third the timestamp of the interaction
     * @return A non-negative integer representing the number of unique vertices found in allInteractions
     */
    private static int getVertices(List<ArrayList<Integer>> allInteractions) {
        Set<Integer> uniqueVertices = new HashSet<>();
        for (ArrayList<Integer> interaction : allInteractions) {
            uniqueVertices.add(interaction.get(SRC));
            uniqueVertices.add(interaction.get(DST));
        }

        return uniqueVertices.size();
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public UDWInteractionGraph(String fileName) {
        List<ArrayList<Integer>> interactions = new ArrayList<>();
        this.interactions = readFile(fileName, interactions);

        this.vertices = getVertices(interactions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createUndirectedList(interactions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file
     * and considering a time window filter.
     * @param fileName the name of the file in the resources directory
     *                 containing email interactions
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the file
     *                   specified by fileName with sent time t in the
     *                   t0 <= t <= t1 range.
     */

    public UDWInteractionGraph(String fileName, int[] timeFilter) {
        List<ArrayList<Integer>> interactions = new ArrayList<>();
        List<ArrayList<Integer>> interactionsWithinTimeWindow = new ArrayList<>();
        int StartTimeInSeconds = timeFilter[0];
        int EndTimeInSeconds = timeFilter[1];
        interactions = readFile(fileName, interactions);

        for (ArrayList<Integer> interaction : interactions) {
            if (interaction.get(TIMESTAMP) >= StartTimeInSeconds && interaction.get(TIMESTAMP) <= EndTimeInSeconds) {
                interactionsWithinTimeWindow.add(interaction);
            }
        }

        this.interactions = interactionsWithinTimeWindow;
        this.vertices = getVertices(interactionsWithinTimeWindow);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createUndirectedList(this.interactions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the input
     *                   UDWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */

    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, int[] timeFilter) {
        int StartTimeInSeconds = timeFilter[0];
        int EndTimeInSeconds = timeFilter[1];
        List<ArrayList<Integer>> interactionsWithinTimeWindow = new ArrayList<>();

        for(int i = 0; i < inputUDWIG.interactions.size(); i++) {
            if(inputUDWIG.interactions.get(i).get(TIMESTAMP) >= StartTimeInSeconds && inputUDWIG.interactions.get(i).get(TIMESTAMP) <= EndTimeInSeconds) {
                interactionsWithinTimeWindow.add(inputUDWIG.interactions.get(i));
            }
        }

        this.interactions = new ArrayList<>(interactionsWithinTimeWindow);
        this.vertices = getVertices(interactionsWithinTimeWindow);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createUndirectedList(interactionsWithinTimeWindow, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param userFilter a List of User IDs. The created UDWInteractionGraph
     *                   should exclude those emails in the input
     *                   UDWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */

    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, List<Integer> userFilter) {
        Set<ArrayList<Integer>> filteredInteractionsSet = new HashSet<>();

        for(Integer userID : userFilter) {
            for(int i = 0; i < inputUDWIG.interactions.size(); i++) {
                if(Objects.equals(inputUDWIG.interactions.get(i).get(SRC), userID)) {
                    filteredInteractionsSet.add(inputUDWIG.interactions.get(i));
                }
                else if(Objects.equals(inputUDWIG.interactions.get(i).get(DST), userID)) {
                    filteredInteractionsSet.add(inputUDWIG.interactions.get(i));
                }
            }
        }

        List<ArrayList<Integer>> filteredInteractions = new ArrayList<>(filteredInteractionsSet);
        this.interactions = filteredInteractions;
        this.vertices = getVertices(filteredInteractions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createUndirectedList(filteredInteractions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG a DWInteractionGraph object
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {

        this.interactions = new ArrayList<>(inputDWIG.interactions);
        this.vertices = getVertices(interactions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createUndirectedList(interactions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this UDWInteractionGraph.
     */

    public Set<Integer> getUserIDs() {
        Set<Integer> userIDs = new HashSet<>();

        for (ArrayList<Integer> interaction : this.interactions) {
            userIDs.add(interaction.get(SRC));
            userIDs.add(interaction.get(DST));
        }

        return new HashSet<>(userIDs);
    }

    /**
     * @param user1 the User ID of the first user.
     * @param user2 the User ID of the second user.
     * @return the number of email interactions (send/receive) between user1 and user2. Returns
     * zero if there are no email interactions found between user1 and user2.
     */
    public int getEmailCount(int user1, int user2) {
        int emailCount = 0;

        if(!UserIDToIndex.containsKey(user1) || !UserIDToIndex.containsKey(user2)) {
            return emailCount;
        }

        for(int i = 0; i < this.adjacencyList.get(UserIDToIndex.get(user1)).size(); i++) {
            if (this.adjacencyList.get(UserIDToIndex.get(user1)).get(i)[RECEIVER] == user2) {
                emailCount = this.adjacencyList.get(UserIDToIndex.get(user1)).get(i)[WEIGHT];
            }
        }

        return emailCount;
    }

    /* ------- Task 2 ------- */

    /**
     * @param timeWindow is an int array of size 2 [t0, t1]
     *                   where t0<=t1 and t0 and t1 represent the start and end times in
     *                   seconds. Specifies the time interval within which to report
     *                   interaction activity.
     * @return an int array of length 2, with the following structure:
     * [NumberOfUsers, NumberOfEmailTransactions], where NumberOfUsers represents the number
     * of users that interacted within timeWindow and NumberOfEmailTransactions represents the number
     * of emails in total that were either sent or received within timeWindow.
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        int startTime = timeWindow[0];
        int endTime = timeWindow[1];
        List<ArrayList<Integer>> interactionsWithinTimeWindow = new ArrayList<>();
        Set<Integer> uniqueUsers = new HashSet<>();

        for (ArrayList<Integer> interaction : this.interactions) {
            if (interaction.get(TIMESTAMP) >= startTime && interaction.get(TIMESTAMP) <= endTime) {
                interactionsWithinTimeWindow.add(interaction);
            }
        }

        for(int i = 0; i < interactionsWithinTimeWindow.size(); i++) {
            for(int j = 0; j < interactionsWithinTimeWindow.get(i).size(); j++) {
                int sender = interactionsWithinTimeWindow.get(i).get(SRC);
                int receiver = interactionsWithinTimeWindow.get(i).get(DST);
                uniqueUsers.add(sender);
                uniqueUsers.add(receiver);
            }
        }

        return new int[]{uniqueUsers.size(), interactionsWithinTimeWindow.size()};
    }

    /**
     * @param userID the User ID of the user for which
     *               the report will be created
     * @return an int array of length 2 with the following structure:
     *  [NumberOfEmails, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of an UDWInteractionGraph,
     * returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        List<ArrayList<Integer>> interactionsWithUserID = new ArrayList<>();
        Set<Integer> uniqueUsers = new HashSet<>();

        for (ArrayList<Integer> interaction : this.interactions) {
            if(interaction.get(SRC) == userID || interaction.get(DST) == userID) {
                interactionsWithUserID.add(interaction);
            }
        }

        for (int i = 0; i < interactionsWithUserID.size(); i++) {
            int sender = interactionsWithUserID.get(i).get(SRC);
            int receiver = interactionsWithUserID.get(i).get(DST);

            if (sender == userID) {
                uniqueUsers.add(receiver);
            }
            else if (receiver == userID) {
                uniqueUsers.add(sender);
            }
        }

        return new int[]{interactionsWithUserID.size(), uniqueUsers.size()};
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active in terms of
     *          both sending and receiving emails in total.
     * @return the User ID for the Nth most active user, or -1 if the number of
     * vertices in the interaction graph is less than N.
     */

    public int NthMostActiveUser(int N) {
        if(adjacencyList.size() < N) {
            return -1;
        }

        Map<Integer, Integer> userIDtoWeight = new HashMap<>();

        for(int i = 0; i < adjacencyList.size(); i++) {
            int sentCount = 0;
            int userID = IndexToUserID.get(i);
            for(int j = 0; j < adjacencyList.get(i).size(); j++) {
                sentCount += adjacencyList.get(i).get(j)[WEIGHT];
            }
            userIDtoWeight.put(userID, sentCount);
        }

        List<Integer> userIDs = new ArrayList<>(userIDtoWeight.keySet());
        List<Integer> transactions = new ArrayList<>(userIDtoWeight.values());
        transactions.sort(Comparator.reverseOrder());
        List<Integer> userIDByTransaction = new ArrayList<>();
        List<Integer> unSorteduserIDByTransaction = new ArrayList<>();

        for(Integer transaction : transactions) {
            for(int i = 0; i < userIDtoWeight.size(); i++) {
                int userID = userIDs.get(i);
                if(Objects.equals(userIDtoWeight.get(userIDs.get(i)), transaction) && !userIDByTransaction.contains(userID)) {
                    unSorteduserIDByTransaction.add(userID);
                }
            }
            unSorteduserIDByTransaction.sort(Comparator.naturalOrder());
            for(Integer userID : unSorteduserIDByTransaction) {
                if(!userIDByTransaction.contains(userID)) {
                    userIDByTransaction.add(userID);
                }
            }
        }

        return userIDByTransaction.get(N - 1);
    }

    /* ------- Task 3 ------- */

    /**
     * @return A non-negative integer representing the number of completely disjoint graph
     *    components in the UDWInteractionGraph object, where disjoint means that there
     *    are no interactions between two components. One disjoint component's vertex
     *    cannot send/receive any emails to/from another disjoint component's vertex.
     */
    public int NumberOfComponents() {
        boolean[] isVisited = new boolean[vertices];
        Arrays.fill(isVisited, false);
        int components = 0;

        for(int i = 0; i < vertices; i++) {
            if(!isVisited[i]) {
                components++;
                visitAdjacentVertices(adjacencyList, i, isVisited, UserIDToIndex);
            }
        }
        return components;
    }

    /**
     * Modifies: Visits adjacent vertices of *vertex*, and sets the *vertex* element and all the vertex numbers of *vertex*'s
     * adjacent vertices in isVisited to true.
     * @param adjacencyList Adjacency list for an undirected weighted interaction graph. Must contain at least *vertex* elements
     *                      that each represent a unique user in the interaction graph.
     * @param vertex Vertex index number whose adjacent vertices will be visited
     * @param isVisited boolean array keeping track of all the visited vertices in *adjacencyList*. Has the same number
     *                  of entries as the number of vertex elements in *adjacencyList*
     * @param UserIDToIndex Maps a given user ID to its corresponding vertex number in *adjacencyList*
     */
    private static void visitAdjacentVertices(List<ArrayList<int[]>> adjacencyList, int vertex, boolean[] isVisited, Map<Integer, Integer> UserIDToIndex) {
        isVisited[vertex] = true;
        for(int[] receiverAndWeight : adjacencyList.get(vertex)) {
            int receiver = receiverAndWeight[RECEIVER];
            int receiverVertex = UserIDToIndex.get(receiver);
            if(!isVisited[receiverVertex]) {
                visitAdjacentVertices(adjacencyList, receiverVertex, isVisited, UserIDToIndex);
            }
        }
    }

    /**
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return Whether a path exists between the two users. Returns true if a path exists, or
     * false if a path does not exist between the two users, including if a user ID cannot be found in
     * the UDWInteractionGraph.
     */
    public boolean PathExists(int userID1, int userID2) {
        if(!UserIDToIndex.containsKey(userID1) || !UserIDToIndex.containsKey(userID2)) {
            return false;
        }

        boolean[] isVisited = new boolean[vertices];
        Arrays.fill(isVisited, false);
        int vertex1 = UserIDToIndex.get(userID1);
        int vertex2 = UserIDToIndex.get(userID2);
        Stack<Integer> vertexStack = new Stack<>();

        vertexStack.push(vertex1);
        isVisited[vertex1] = true;

        undirectedDFS(adjacencyList, vertexStack, isVisited, UserIDToIndex);

        if(!isVisited[vertex2]) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Modifies: Visits adjacent vertices of the vertices in *vertexStack*, and sets isVisited[i] to true if and only if
     * vertex i has been visited.
     * @param adjacencyList An adjacency list representing an undirected weighted interaction graph
     * @param vertexStack A stack containing vertices to visit next
     * @param isVisited An array where each element corresponds to a user in the adjacency list. The element at
     *                  position i represents the state of the ith vertex in adjacencyList. If vertex i has been visited,
     *                  isVisited[i] will be true. Otherwise, it will be false.
     * @param UserIDToIndex Map that takes in a user ID and returns its corresponding vertex index in adjacencyList.
     */
    private static void undirectedDFS(ArrayList<ArrayList<int[]>> adjacencyList, Stack<Integer> vertexStack, boolean[] isVisited, Map<Integer, Integer> UserIDToIndex) {
        while(!vertexStack.isEmpty()) {
            int nextVertex = vertexStack.pop();

            for(int[] receiverAndWeight : adjacencyList.get(nextVertex)) {
                int receiver = receiverAndWeight[RECEIVER];
                int receiverVertex = UserIDToIndex.get(receiver);

                if(!isVisited[receiverVertex]) {
                    vertexStack.push(receiverVertex);
                    isVisited[receiverVertex] = true;
                }
            }
        }
    }
}
