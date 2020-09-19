package q6.Tournament;

import static java.lang.Math.ceil;
import static java.lang.Math.pow;

public class TournamentLock implements Lock {

    // Array representing a binary flag
    private volatile int[] flag;
    private volatile boolean[] turn;
    private volatile int numThreads; // total nodes in the flag so far?
    private volatile int treeHeight;
    private volatile int nextPowerOfTwo;

    public TournamentLock(int numThreads){

        // Num processes
        this.numThreads = numThreads;

        // Initialize treeHeight to the height of the tree
        this.treeHeight = getTreeHeight(this.numThreads);

        // Initialize turn to hold a value for each competition
        this.turn = new boolean[getNumberOfCompetitions(this.treeHeight)];

        // Compute number of leaf nodes in the full tree
        this.nextPowerOfTwo = (int)Math.pow(2, this.treeHeight);

        // Compute total number of nodes in the full tree
        int numNodesInFullTree = (int)Math.pow(2, this.treeHeight + 1) -1;
        this.flag = new int[numNodesInFullTree];

        // Initialize flag values to 0
        for (int i = 0; i < numNodesInFullTree; i++) {
            this.flag[i] = 0;
        }

        // Initialize turn values to false
        for (int i = 0; i < this.treeHeight; i++) {
            this.turn[i] = false;
        }
    }

    @Override
    public void lock(int pid)
    {
        // Get the leaf node # for the PID
        // Assume the root node is index 0
        int currNode = (int)Math.pow(2, this.treeHeight + 1) - 1 - this.nextPowerOfTwo + pid;

        // Determine if current leaf node is a left (false) or right (true) child
        boolean role = (currNode % 2) == 0;

        // Loop through k-1 levels in the tree
        for (int k = 1; k < this.treeHeight; k++)
        {
            // Identify parent node index
            var myParent = (int)Math.floor((currNode - 1) / 2.0);

            // Determine if my parent node is a left (false) or right (true) child
            var roleOfMyParent = (myParent % 2) == 0;

            // Identify competitors for this match
            // For the first competition, lowerBound and upperBound will encompass 2 nodes
            // For the second competition, lowerBound and upperBound will encompass 4 nodes
            // etc.
            var powerOfTwo = (int)Math.pow(2, k);
            var lowerBound = (pid / powerOfTwo) * powerOfTwo;
            var upperBound = lowerBound + powerOfTwo;

            this.flag[pid] = k; // wantCS = true
            this.turn[myParent] = role;  // turn = j

            for (int j = lowerBound; j < upperBound; j++) {        // for each of my potential competitors
                while (j != pid && this.flag[j] >= k && this.turn[myParent] == role) {  // j != i && gate[j] >= k && last[k] == i
                    // skip/no-op
                }
            }

            // The leaf node won the tournament, and may enter the critical section

            // Set up for next iteration
            currNode = myParent;
            role = roleOfMyParent;
        }
    }

    @Override
    public void unlock(int pid) {
        this.flag[pid] = 0;
    }

    /*
    Helper method to compute base 2 log
     */
    private static double log2(int x) {
        return (Math.log(x) / Math.log(2));
    }

    /*
    Helper method to get the tree height
     */
    private int getTreeHeight(int t) {
        return (int)ceil(log2(t));
    }

    /*
    Helper method to get the number of competitions, based on the tree height
     */
    private int getNumberOfCompetitions(int height) {
        return ((int)Math.pow(2, height)) - 1;
    }
}