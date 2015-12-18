/*
* Hieu Trung Nguyen
*/

import java.util.*;

/*
* This is a Huffman Tree class. It takes a string of message and encode it
* into binary of 0's and 1's. It is also able to decode back to the original
* text given the codes.
*/
public class CodingTree {
    public String bits;
    public Map<Character, String> codes;
    private Map<Character, Integer> frequencies;
    private TreeNode overallRoot;
    
    /*
    * Construct a Huffman Tree given the string message and execute
    * the Huffman algorithm to encode the message.
    */
    public CodingTree(String message) {
        codes = new HashMap<Character, String>();
        frequencies = new HashMap<Character, Integer>();
        countFrequency(message);
        overallRoot = buildTree();
        createCodeMap();
        bits = createStringBits(message);
    }
    
    /*
    * Count the frequency of each unique character
    * in the message
    */
    private void countFrequency(String message) {
        for (int i = 0; i < message.length(); i++) {
            char currentChar = message.charAt(i);
            if (frequencies.containsKey(currentChar)) {
                int frequency = frequencies.get(currentChar);
                frequency++;
                frequencies.put(currentChar, frequency);
            } else {
                frequencies.put(currentChar, 1);
            }
        }
    }
    
    /*
    * Build the Huffman Tree with the characters
    * and its frequency then return the new tree
    */
    private TreeNode buildTree() {
    	PriorityQueue<TreeNode> queue = new PriorityQueue<TreeNode>();
        for (char currentChar : frequencies.keySet()) {
            queue.offer(new TreeNode(currentChar, frequencies.get(currentChar)));
        }
        TreeNode combinedFrequency = null;
        while (!queue.isEmpty()) {
            TreeNode firstNode = queue.poll();
            if (queue.peek() != null) {
                TreeNode secondNode = queue.poll();
                combinedFrequency = new TreeNode(firstNode.frequency + secondNode.frequency,
                firstNode, secondNode);
                queue.offer(combinedFrequency);
            }
        }
        return combinedFrequency;
    }
    
    /*
    * Create a map of code of characters with code of 0's and 1's as its value
    */
    private void createCodeMap() {
        createCodeMap(overallRoot, "");
    }
    
    /*
    * Helper method to create the map of code
    */
    private void createCodeMap(TreeNode root, String code) {
        if (root.left == null && root.right == null) {
            codes.put(root.charValue, code);
        } else {
            createCodeMap(root.left, code + "0");
            createCodeMap(root.right, code + "1");
        }
    }
    
    /*
    * Create a string of 0's and 1's given the original
    * String message
    */
    private String createStringBits(String message) {
        StringBuilder myStringBuilder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            myStringBuilder.append(codes.get(message.charAt(i)));
        }
        return myStringBuilder.toString();
    }
    
    /*
    * Extra Credit
    * Given a string of 0's and 1's, and a map of codes, reconstruct
    * the tree and decode the code back into the original message then
    * return that message
    */
    public String decode(String bits, Map<Character, String> codes) {
        overallRoot = null;
        reconstructTree(codes);
        return createText(bits);
    }
    
    /*
    * Reconstruct the tree given a map of codes
    */
    private void reconstructTree(Map<Character, String >codes) {
        for (char currentChar : codes.keySet()) {
            String code = codes.get(currentChar);
            overallRoot = reconstructTree(currentChar, code, overallRoot);
        }
    }
    
    /*
    * Helper method to reconstruct the tree
    */
    private TreeNode reconstructTree(char currentChar, String code, TreeNode root) {
        if (root == null)
            // expand by creating a new node if the path for the code does not exist
        root = new TreeNode();
        if (code.length() == 0) {
            root.charValue = currentChar;
        } else if (code.charAt(0) == '0') {
            root.left = reconstructTree(currentChar, code.substring(1), root.left);
        } else {
            root.right = reconstructTree(currentChar, code.substring(1), root.right);
        }
        return root;
    }
    
    /*
    * Create the original String message from the bits of 0's and 1's
    * then return that message
    */
    private String createText(String bits) {
        StringBuilder myStringBuilder = new StringBuilder();
        int index = 0;
        TreeNode root = null;
        while (index < bits.length() && overallRoot != null) {
            root = overallRoot;
            while (index < bits.length() && (root.left != null && root.right != null)) {
                if (bits.charAt(index) == '0') {
                    root = root.left;
                } else if (bits.charAt(index) == '1') {
                    root = root.right;
                }
                index++;
            }
            myStringBuilder.append(root.charValue);
        }
        return myStringBuilder.toString();
    }
    
    /*
    * Getter method for main to test and debug calculating frequencies
    * of characters
    */
    public Map<Character, Integer> getMap() {
        return frequencies;
    }
    
    /*
     * Getter method for main to test and debug the layout of the current tree
     */
    public TreeNode getTree() {
    	return overallRoot;
    }
}
