import java.math.*;
import java.util.*;

public class Project1 {
    public static void main(String[] args) {
        System.out.println("Hello! Welcome to CS1699 Project 1");
        while(true) {
            System.out.println("What would you like to test?");
            System.out.println("A) Algorithm A");
            System.out.println("B) Algorithm B");
            Scanner s = new Scanner(System.in);
            String ans = s.nextLine();
            System.out.println("How many iterations of ModExp would you like?");
            int n = Integer.parseInt(s.nextLine());
            if(ans.equalsIgnoreCase("A")) {
                algorithmA(n);
            } else {
            
            }
            System.out.println("Press enter to continue or type -1 to quit");
            ans = s.nextLine();
            if(ans.equals("-1")) {
                System.out.println("Goodbye");
                s.close();
                break;
            }
        }
        
    }

    private static void algorithmA(int n) {
        System.out.println("Testing Algorithm A");
        System.out.println();
        // Random to be used in BigInt generation
        Random rand = new Random();
        // Testing 512 bit private key
        testAlgorithmA(new BigInteger(512, rand), new BigInteger(512, rand), new BigInteger(512, rand), BigInteger.ZERO, n, 512);
        System.out.println();
        //Testing 256 bit private key
        testAlgorithmA(new BigInteger(256, rand), new BigInteger(256, rand), new BigInteger(256, rand), BigInteger.ZERO, n, 256);
        System.out.println();
        //Testing 1024 bit private key
        testAlgorithmA(new BigInteger(1024, rand), new BigInteger(1024, rand), new BigInteger(1024, rand), BigInteger.ZERO, n, 1024);
        System.out.println();
        //Testing all 1s 512 bit private key
        byte[] allOnes = new byte[512];
        allOnes[0] = (byte)0;
        for(int i = 1; i < allOnes.length; i++) {
            allOnes[i] = (byte)1;
        } 
        System.out.println("Using ModExp on a BigInteger containing all 1s");
        testAlgorithmA(new BigInteger(512, rand), new BigInteger(512, rand), new BigInteger(allOnes), BigInteger.ZERO, n, 512);
        System.out.println();
        //Testing all 0s 512 private key
        byte[] allZeroes = new byte[512];
        for(int i = 0; i < allZeroes.length; i++) {
            allZeroes[i] = (byte)0;
        }
        System.out.println("Using ModExp on a BigInteger containing all 0s");
        testAlgorithmA(new BigInteger(512, rand), new BigInteger(512, rand), new BigInteger(allZeroes), BigInteger.ZERO, n, 512);
        System.out.println();
    }

    private static void testAlgorithmA(BigInteger c, BigInteger m, BigInteger priv, BigInteger blockRes, int numIterations, int numBits) {
        System.out.println("Using ModExp on a " + numBits +"-bit private key");
        long start, stop, total = 0;
        for(int i = 0; i < numIterations; i++) {
            start = System.nanoTime();
            blockRes = modularExp(c, priv, m);
            stop = System.nanoTime();
            total += (stop - start);
        }
        double average = (double)total/numIterations;
        average = average * Math.pow(10, -9);
        System.out.println("Average " + numBits + "-bit runtime: " + average + " seconds");
    }

    private static BigInteger modularExp(BigInteger x, BigInteger y, BigInteger n) {
        BigInteger zero = BigInteger.ZERO;
        BigInteger one = BigInteger.ONE;

        // Check if y equals -1 and return 
        // the modular inverse if so
        if (y.equals(one.negate())) { 
            return x.modInverse(n);
        }
        
        // Check if y is less than 0 and 
        // take the mod inverse of x and 
        // negate y if so
        if (y.compareTo(zero) == -1) { //moc inverse if < 0
            x = x.modInverse(n);
            y = y.negate();
        }

        // Initialize variable to store the result
        // of the modular exponentiation
        BigInteger result = BigInteger.ONE;
        while (!(y.compareTo(zero) == 0)) {
            if (!(y.mod(new BigInteger("2")) == zero)) { 
                result = gradeSchool(result, x).mod(n);
            }
            y = y.shiftRight(1); 
            x = x.multiply(x).mod(n);
        }
        return result;
    }

    // Grade School multiplcation method
    private static BigInteger gradeSchool(BigInteger op1, BigInteger op2) {
        BigInteger result = BigInteger.ZERO;
        BigInteger zero = BigInteger.ZERO;
        boolean negation = false; 

        // If either of the operands are negative 
        // then turn them positive to perform the math,
        // but keep a variable to indicate that the result
        // must be negated.
        if (checkNegative(op1.toByteArray())) {
            op1 = op1.negate();
            negation = true;
        }

        if (checkNegative(op2.toByteArray())) {
            op2 = op2.negate();
            negation = true;
        }

        while (!(op2.compareTo(zero) == 0)) { 
            if(!(op2.mod(new BigInteger("2")) == zero)) {
                result = result.add(new BigInteger(upSizeArray(op1.toByteArray())));
            }
            op1 = op1.shiftLeft(1);
            op2 = op2.shiftRight(1);
        }

        // Need to negate result since one of the numbers was negative
        if (negation) {
            result = result.negate();
        }
        return result;
    }

    private static boolean checkNegative(byte[] arr) {
        return (arr[0] < 0);
    }

    public static byte[] upSizeArray(byte[] arr) {
        byte[] newArray = new byte[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newArray[i+1] = arr[i];
        }
        return newArray;
    }
}