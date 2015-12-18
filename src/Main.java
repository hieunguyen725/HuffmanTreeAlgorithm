/*
* Hieu Trung Nguyen
*/

import java.util.*;
import java.io.*;

/*
* Main simulator of CodingTree.java
*/
public class Main {
    public static void main(String[] args) throws IOException {
        long beginTime = System.currentTimeMillis();
        
        String fileName = "WarAndPeace.txt"; // Change the file name to test different files
        
        // Read file to create a String message then build the Huffman tree
        // Thanks to the Java API Library for an overview of FileReader
        // and BufferedReader, BufferedWriter, BufferedOutputStream
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        StringBuilder myStringBuilder = new StringBuilder();
        int originalSize = 0;
        int charValue;
        while ((charValue = input.read()) != -1) {
            myStringBuilder.append((char) charValue);
            originalSize += 8;
        }
        String message = myStringBuilder.toString();
        CodingTree myTree = new CodingTree(message);
        myStringBuilder.setLength(0);
        input.close();
        
        // Output the map of codes to a file
        // Thanks to the JAVA API Library for an overview of FileWriter
        BufferedWriter codesOutput = new BufferedWriter(new FileWriter("codes.txt"));
        codesOutput.write(myTree.codes.toString());
        codesOutput.close();
        
        // Convert String bits to bytes and output it to a binary file
        // Thanks to the Java API library for a review of FileOutputStream
        BufferedOutputStream binaryOutput = new BufferedOutputStream(new FileOutputStream("compressed.txt"));
        int remainingBits = myTree.bits.length() % 8;
        int length = myTree.bits.length() - remainingBits;
        int compressedSize = 0;
        for (int i = 0; i < length; i += 8) {
            for (int j = 0; j < 8; j++) {
                myStringBuilder.append(myTree.bits.charAt(i + j));
            }
            compressedSize += 8;
            binaryOutput.write(Integer.parseInt(myStringBuilder.toString()));
            myStringBuilder.setLength(0);
        }
        binaryOutput.write(Integer.parseInt(myTree.bits.substring(length)));
        compressedSize += 8;
        binaryOutput.close();
        
        // decode the output codes back to the original text and output it to a file
        BufferedWriter decodedOutput = new BufferedWriter(new FileWriter("decoded.txt"));
        decodedOutput.write(myTree.decode(myTree.bits, myTree.codes));
        decodedOutput.close();
        
        // Display statistics
        double ratio = (double) compressedSize / originalSize * 100;
        System.out.println("Original file size in bits   : " + originalSize + " bits  (" + originalSize / 8 + " bytes)");
        System.out.println("Compressed file size in bits : " + compressedSize + " bits  (" + compressedSize / 8 + " bytes)");
        System.out.println("Compressed ratio             : " + String.format("%.2f", ratio) + " %");
        long endTime = System.currentTimeMillis();
        System.out.println("Total Time                   : " + (endTime - beginTime) + " milliseconds");
        
        // Test or debug methods used, please comment out for use
//        testCountingFrequency(myTree);
//        testTreeLayout(myTree);
    }
    
    public static void testCountingFrequency(CodingTree myTree) {
        System.out.println("\n\n\nTesting counting frequency\n");
        Map<Character, Integer> map = myTree.getMap();
        for (char currentChar : map.keySet()) {
            // I display it in ASCII decimal value so I can check the ASCII
            // table for char of new line, carriage return or space
            System.out.println("Letter = " + (int) currentChar + "   Frequency = " + map.get(currentChar));
        }
    }
    
    public static void testTreeLayout(CodingTree myTree) {
    	// view the character and their current depth in their tree during debug
    	// to see if they makes logical sense according to Huffman's algorithm
    	System.out.println("\n\n\nCharacters in Tree and their current depth\n");
    	TreeNode overallRoot = myTree.getTree();
    	viewTree(overallRoot, 0);
    }
    
    private static void viewTree(TreeNode root, int depth) {
    	if (root != null) {
    		viewTree(root.left, depth++);
    		if (root.left == null && root.right == null) {
    			System.out.print(" (" + root.charValue + " " + depth + ") ");
    		}
    		viewTree(root.right, depth++);
    	}
    }
}

// http://www.asciitable.com/
// http://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html
// http://docs.oracle.com/javase/7/docs/api/java/io/FileReader.html
// http://docs.oracle.com/javase/7/docs/api/java/io/FileOutputStream.html
// http://courses.cs.washington.edu/courses/cse142/13au/lectures/21-ch07-3-arrayparameters.pdf