package cpen221.mp2;

import java.util.*;

/**
 * A DWInteractionGraph represents a directed weighted graph of user email
 * interactions within a specific window of time.
 *
 * Abstraction function: uses an Adjacency list (Arraylist of Arraylists of integer arrays)
 * to represent a directed weighted graph. Elements of the outermost arraylist (one that contains the other arraylist)
 * represent a vertex corresponding to a user ID. Each element of the innermost arraylist contains a 2-element integer array that corresponds to an adjacent vertex (receiver)
 * that the user has sent emails to. The first element of the array is the user ID of the receiver while the second element represents the weight (how many emails the user has sent to the adjacent user).
 */

public class DWInteractionGraph extends InteractionGraph {
    private final int vertices;
    private ArrayList<ArrayList<int[]>> adjacencyList;
    private final Map<Integer, Integer> IndexToUserID = new HashMap<>();
    private final Map<Integer, Integer> UserIDToIndex = new HashMap<>();
    public List<ArrayList<Integer>> interactions;

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
     * @param allInteractions List of email transaction information with each line representing one transaction
     *                        and containing the sender, receiver, and timestamp details
     * @return the number of unique senders and receivers found in allInteractions
     */
    private static int getVertices(List<ArrayList<Integer>> allInteractions) {
        Set<Integer> uniqueVertices = new HashSet<>();
        for (ArrayList<Integer> interaction : allInteractions) {
            uniqueVertices.add(interaction.get(SRC));
        }

        if (uniqueVertices.isEmpty()) {
            return 0;
        } else {
            return uniqueVertices.size();
        }
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     * Includes all email interactions within email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        List<ArrayList<Integer>> interactions = new ArrayList<>();
        interactions = readFile(fileName, interactions);

        this.interactions = interactions;
        this.vertices = getVertices(interactions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createDirectedList(interactions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file
     * and considering a time window filter.
     *
     * @param fileName   the name of the file in the resources directory
     *                   containing email interactions
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the file
     *                   specified by fileName with sent time t in the
     *                   t0 <= t <= t1 range.
     */

    public DWInteractionGraph(String fileName, int[] timeFilter) {
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
        this.vertices = getVertices(this.interactions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createDirectedList(interactionsWithinTimeWindow, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }


    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {
        int StartTimeInSeconds = timeFilter[0];
        int EndTimeInSeconds = timeFilter[1];
        List<ArrayList<Integer>> interactionsWithinTimeWindow = new ArrayList<>();

        for (int i = 0; i < inputDWIG.interactions.size(); i++) {
            if (inputDWIG.interactions.get(i).get(TIMESTAMP) >= StartTimeInSeconds && inputDWIG.interactions.get(i).get(TIMESTAMP) <= EndTimeInSeconds) {
                interactionsWithinTimeWindow.add(inputDWIG.interactions.get(i));
            }
        }

        this.interactions = interactionsWithinTimeWindow;
        this.vertices = getVertices(interactionsWithinTimeWindow);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createDirectedList(interactionsWithinTimeWindow, vertices, adjacencyList, UserIDToIndex, IndexToUserID);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        Set<ArrayList<Integer>> filteredInteractionsSet = new HashSet<>();
        for (Integer userID : userFilter) {
            for (int i = 0; i < inputDWIG.interactions.size(); i++) {
                if (Objects.equals(inputDWIG.interactions.get(i).get(SRC), userID)) {
                    filteredInteractionsSet.add(inputDWIG.interactions.get(i));
                } else if (Objects.equals(inputDWIG.interactions.get(i).get(DST), userID)) {
                    filteredInteractionsSet.add(inputDWIG.interactions.get(i));
                }
            }
        }

        List<ArrayList<Integer>> filteredInteractions = new ArrayList<>(filteredInteractionsSet);
        this.interactions = filteredInteractions;
        this.vertices = getVertices(filteredInteractions);
        this.adjacencyList = new ArrayList<>();
        this.adjacencyList = createDirectedList(filteredInteractions, vertices, adjacencyList, UserIDToIndex, IndexToUserID);

    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
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
     * @param sender   the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        int emailCount = 0;

        if (!UserIDToIndex.containsKey(sender)) {
            return emailCount;
        }

        for (int[] array : this.adjacencyList.get(UserIDToIndex.get(sender))) {
            if (array[RECEIVER] == receiver) {
                emailCount += array[WEIGHT];
            }
        }

        return emailCount;
    }

    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     * [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        int startTime = timeWindow[0];
        int endTime = timeWindow[1];
        List<ArrayList<Integer>> interactionsWithinTimeWindow = new ArrayList<>();
        Set<Integer> uniqueSenders = new HashSet<>();
        Set<Integer> uniqueReceivers = new HashSet<>();

        for (int i = 0; i < this.interactions.size(); i++) {
            if (this.interactions.get(i).get(TIMESTAMP) >= startTime && this.interactions.get(i).get(TIMESTAMP) <= endTime) {
                interactionsWithinTimeWindow.add(this.interactions.get(i));
            }
        }

        for (int i = 0; i < interactionsWithinTimeWindow.size(); i++) {
            uniqueSenders.add(interactionsWithinTimeWindow.get(i).get(SRC));
            uniqueReceivers.add(interactionsWithinTimeWindow.get(i).get(DST));
        }

        int numEmails = interactionsWithinTimeWindow.size();

        return new int[]{uniqueSenders.size(), uniqueReceivers.size(), numEmails};
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {
        int numSent = 0;
        int numReceived = 0;
        Set<Integer> uniqueUsers = new HashSet<>();

        if (UserIDToIndex.containsKey(userID)) {
            for (int i = 0; i < adjacencyList.get(UserIDToIndex.get(userID)).size(); i++) {
                numSent += adjacencyList.get(UserIDToIndex.get(userID)).get(i)[WEIGHT];
                uniqueUsers.add(adjacencyList.get(UserIDToIndex.get(userID)).get(i)[RECEIVER]);
            }
        }

        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                if (adjacencyList.get(i).get(j)[RECEIVER] == userID) {
                    numReceived += adjacencyList.get(i).get(j)[WEIGHT];
                    uniqueUsers.add(IndexToUserID.get(i));
                    break;
                }
            }
        }

        return new int[]{numSent, numReceived, uniqueUsers.size()};
    }

    /**
     * @param N               a positive number representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order.
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        if (adjacencyList.size() < N) {
            return -1;
        }

        List<Integer> maxUserIds;
        Map<Integer, Integer> userIdToNumEmails = new HashMap<>();

        if (interactionType == SendOrReceive.SEND) {
            for (int i = 0; i < adjacencyList.size(); i++) {
                int emailsSent = 0;
                for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                    emailsSent += adjacencyList.get(i).get(j)[WEIGHT];
                }
                userIdToNumEmails.put(IndexToUserID.get(i), emailsSent);
            }

            maxUserIds = getPotentialUserIds(N, userIdToNumEmails);

            if (maxUserIds.size() == 1) {
                return Collections.min(maxUserIds);
            } else if (maxUserIds.size() > N) {
                return maxUserIds.get(N - 1);
            } else {
                int newN = userIdToNumEmails.size() - N;
                return maxUserIds.get(maxUserIds.size() - newN - 1);
            }
        } else {
            for (int i = 0; i < adjacencyList.size(); i++) {
                for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                    int receiver = adjacencyList.get(i).get(j)[RECEIVER];
                    int weight = adjacencyList.get(i).get(j)[WEIGHT];

                    if (!userIdToNumEmails.containsKey(receiver)) {
                        userIdToNumEmails.put(receiver, weight);
                    } else {
                        int newWeight = weight + userIdToNumEmails.get(receiver);
                        userIdToNumEmails.put(receiver, newWeight);
                    }
                }
            }

            maxUserIds = getPotentialUserIds(N, userIdToNumEmails);

            if (maxUserIds.size() == 1) {
                return Collections.min(maxUserIds);
            } else if (maxUserIds.size() > N) {
                return maxUserIds.get(N - 1);
            } else {
                int newN = userIdToNumEmails.size() - N;
                return maxUserIds.get(maxUserIds.size() - newN - 1);
            }
        }
    }

    /**
     * @param N                 a positive number representing rank. N=1 means the most active.
     * @param userIdToNumEmails Map that maps an integer representing userID to
     *                          the total number of emails sent or received by that userID
     * @return all the userIDs that have the Nth most activity in terms of emails sent
     * or received
     */
    private static ArrayList<Integer> getPotentialUserIds(int N, Map<Integer, Integer> userIdToNumEmails) {
        List<Integer> numEmails = new ArrayList<>();
        Set<Integer> userIDs;
        Object[] tempNumEmails = userIdToNumEmails.values().toArray();

        for (Object o : tempNumEmails) {
            numEmails.add((Integer) o);
        }

        numEmails.sort(Comparator.naturalOrder());
        Set<Integer> sortedEmails = new TreeSet<>(numEmails);

        numEmails = new ArrayList<>(sortedEmails);
        int NthMostEmail;
        if (numEmails.size() - N < 0) {
            NthMostEmail = numEmails.get(0);
        } else {
            NthMostEmail = numEmails.get(numEmails.size() - N);
        }

        userIDs = userIdToNumEmails.keySet();

        Set<Integer> maxUserIdSet = new TreeSet<>();
        for (Integer userID : userIDs) {
            if (userIdToNumEmails.get(userID) == NthMostEmail) {
                maxUserIdSet.add(userID);
            }
        }

        return new ArrayList<>(maxUserIdSet);
    }

    /* ------- Task 3 ------- */

    /**
     * performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {

        ArrayList<Integer> present = new ArrayList<>();
        ArrayList<Integer> dummyPresent = new ArrayList<>();
        ArrayList<Integer> queue = new ArrayList<>();
        ArrayList<Integer> dummyQueue = new ArrayList<>();
        ArrayList<Integer> sortedUserHist = new ArrayList<>();
        ArrayList<Integer> history = new ArrayList<>();

        present.add(userID1);

        if (userID1 == userID2) {
            history.add(userID1);
            return history;
        }

        int[] userReportNone = ReportOnUser(userID1);
        if (userReportNone[0] == 0) {
            return null;
        }

        while (!present.contains(userID2)) {

            history.addAll(present);

            for (int i = 0; i < present.size(); i++) {
                int[] userReport1 = ReportOnUser(present.get(i));
                if (userReport1[0] != 0) {
                    dummyPresent.add(present.get(i));
                }
            }
            present.clear();
            present.addAll(dummyPresent);
            dummyPresent.clear();

            for (int j = 0; j < present.size(); j++) {

                for (int m = 0; m < adjacencyList.get(UserIDToIndex.get(present.get(j))).size(); m++) {

                    sortedUserHist.add(adjacencyList.get(UserIDToIndex.get(present.get(j))).get(m)[0]);

                }
                Collections.sort(sortedUserHist);
                queue.addAll(sortedUserHist);
                sortedUserHist.clear();
            }

            for (int i = 0; i < queue.size(); i++) {
                if (!history.contains(queue.get(i)) && !dummyQueue.contains(queue.get(i))) {
                    dummyQueue.add(queue.get(i));
                }
            }

            queue.clear();
            queue.addAll(dummyQueue);
            dummyQueue.clear();

            if (queue.isEmpty()) {
                return null;
            }

            present.clear();
            present.addAll(queue);
            queue.clear();
        }

        if (present.contains(userID2)) {
            for (int i = 0; i < present.size(); i++) {
                history.add(present.get(i));
                if (present.get(i) == userID2) {
                    return history;
                }
            }
        }

        return null;
    }

    /**
     * performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns a list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        //defensive copying of adjacencyList
        ArrayList<ArrayList<int[]>> adjacencyListCopy = new ArrayList<>(adjacencyList);

        for (int z = 0; z < vertices; z++) {
            for (int i = 0; i < adjacencyListCopy.get(z).size() - 1; i++) {
                for (int j = 0; j < adjacencyListCopy.get(z).size() - i - 1; j++) {
                    if (adjacencyListCopy.get(z).get(j)[RECEIVER] > adjacencyListCopy.get(z).get(j + 1)[RECEIVER]) {
                        int temp = adjacencyListCopy.get(z).get(j)[RECEIVER];
                        adjacencyListCopy.get(z).get(j)[RECEIVER] = adjacencyListCopy.get(z).get(j + 1)[RECEIVER];
                        adjacencyListCopy.get(z).get(j + 1)[RECEIVER] = temp;


                        int temp2 = adjacencyListCopy.get(z).get(j)[WEIGHT];
                        adjacencyListCopy.get(z).get(j)[WEIGHT] = adjacencyListCopy.get(z).get(j + 1)[WEIGHT];
                        adjacencyListCopy.get(z).get(j + 1)[WEIGHT] = temp2;
                    }
                }
            }
        }

        List<Integer> searchPath = new ArrayList<>();
        boolean found = recursiveFinderDFS(userID1, userID2, searchPath, adjacencyListCopy);

        if (found) {
            return searchPath;
        } else {
            return null;
        }
    }

    /**
     * Recursive helper method that determines if there is a path between 1 user and another
     *
     * @param userID1           the user ID for the first user
     * @param userID2           the user ID for the second user
     * @param searchPath        an empty array that will hold a list of user IDs
     *                          in the order encountered in the search.
     * @param adjacencyListCopy a copy of the adjacency list corresponding to the graph to be searched
     *                          Framecondition: searchPath is modified to contain the list of users visited in the order that they were encountered
     *                          during the search. If no path exists, searchPath is null.
     * @return true if a path exists, false otherwise.
     */

    private boolean recursiveFinderDFS(int userID1, int userID2, List<Integer> searchPath, ArrayList<ArrayList<int[]>> adjacencyListCopy) {
        searchPath.add(userID1);
        Integer n = UserIDToIndex.get(userID1);
        if (n == null && userID1 != userID2) {
            return false;
        }
        int adjacencySize = adjacencyListCopy.get(n).size();
        for (int i = 0; i < adjacencySize; i++) {
            int uID = adjacencyListCopy.get(UserIDToIndex.get(userID1)).get(i)[RECEIVER];
            if (uID == userID2) {
                searchPath.add(userID2);
                return true;
            } else if (!searchPath.contains(uID)) {
                if (recursiveFinderDFS(uID, userID2, searchPath, adjacencyListCopy)) {
                    return true;
                }
            }
        }
        return false;
    }


    /* ------- Task 4 ------- */

    /**
     * Read the MP README file carefully to understand
     * what is required from this method.
     *
     * @param hours
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {

        int timeWindowSize = hours * 3600;
        int maxInfections = 0;
        int maxInterInWin = 0;
        int startTime = 0;
        int endTime = 0;
        int maxStartIndex = 0;
        ArrayList<Integer> senders = new ArrayList<>();

        ArrayList<ArrayList<Integer>> sortedInteractions = new ArrayList<>(interactions);
        sortedInteractions.sort((l1, l2) -> l1.get(2).compareTo(l2.get(2)));

        if (this.interactions.get(interactions.size() - 1).get(TIMESTAMP) > timeWindowSize) {
            int maxStartTime = this.interactions.get(interactions.size() - 1).get(TIMESTAMP) - timeWindowSize;
            for (int i = 0; i < sortedInteractions.size(); i++) {
                if (sortedInteractions.get(i).get(2) > maxStartTime) {
                    maxStartIndex = i;
                }
            }
        } else {
            maxStartIndex = sortedInteractions.size() - 1;
        }

        for (int j = 0; j <= maxStartIndex; j++) {
            int[] timeWin = {sortedInteractions.get(j).get(2), sortedInteractions.get(j).get(2) + timeWindowSize};
            int[] reportWindow = ReportActivityInTimeWindow(timeWin);
            if (reportWindow[2] > maxInterInWin) {
                maxInterInWin = reportWindow[2];
                startTime = sortedInteractions.get(j).get(2);
                endTime = sortedInteractions.get(j).get(2) + timeWindowSize;
            }
        }
        if (endTime > sortedInteractions.get(sortedInteractions.size() - 1).get(2)) {
            endTime = sortedInteractions.get(sortedInteractions.size() - 1).get(2);
        }

        int[] timeWind = {startTime, endTime};
        DWInteractionGraph TFgraph = new DWInteractionGraph(this, timeWind);

        for (int k = 0; k < TFgraph.interactions.size(); k++) {
            int[] tempUserRep = ReportOnUser(TFgraph.interactions.get(k).get(0));
            if (tempUserRep[0] != 0 && !senders.contains(TFgraph.interactions.get(k).get(0))) {
                senders.add(TFgraph.interactions.get(k).get(0));
            }
        }

        for (int l = 0; l < senders.size(); l++) {
            //Find the sender affecting most people.
            for (Integer sender : senders) {
                int time = startTime;
                int user = sender;
                ArrayList<Integer> usersInfected = new ArrayList<>();
                usersInfected.add(user);
                while (time <= endTime) {

                    //Add all infections occurred at this time originating from user l.
                    for (int f = 0; f < TFgraph.interactions.size(); f++) {
                        if (TFgraph.interactions.get(f).get(2) == time) {
                            if (usersInfected.contains(TFgraph.interactions.get(f).get(0)) && !usersInfected.contains(TFgraph.interactions.get(f).get(1))) {
                                usersInfected.add(TFgraph.interactions.get(f).get(1));
                            }
                        }
                    }
                    time++;
                }
                if (usersInfected.size() > maxInfections) {
                    maxInfections = usersInfected.size();
                }
            }
        }
            return maxInfections;
    }
}
