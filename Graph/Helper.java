import java.util.Stack;

//Utility Class
public final class Helper {
    private Helper(){}
    
    //The following function was used from https://www.geeksforgeeks.org/python/python-program-for-common-divisors-of-two-numbers/ with minor changes
    public static Stack<Integer> commonDivisors(Integer p1, Integer p2) {
        Stack<Integer> divisors = new Stack<>();

        for (int i = 1; i < Math.min(p1, p2) + 1; i++) {
            if (p1 % i == 0 && p2 % i == 0) {
                divisors.push(i);
            }
        }

        return divisors;
    }

    //Euclidean algorithm
    //Created with the help of https://www.khanacademy.org/computing/computer-science/cryptography/modarithmetic/a/the-euclidean-algorithm
    public static Integer getGCD(Integer num1, Integer num2) {
        int remainder = num2 % num1;
        int MAX_TRIES = 10000;
        int currTry = 0;
        int newNum1 = num1;
        int newNum2 = num2;

        // ! TODO - Fix this (Division by Zero error)
        while (remainder > 0 && currTry < MAX_TRIES) {
            int timesDivided = (int) Math.floor(newNum2 / newNum1);
            System.out.println(String.format("NewNum2: %d = NewNum1: %d * Times: %d + Remainder: %d", newNum2, newNum1, timesDivided, remainder));
            remainder = newNum2 - (newNum1 * timesDivided);
            newNum2 = newNum1;
            newNum1 = remainder;
            currTry += 1;
        }

        if (currTry >= MAX_TRIES) {
            return -1;
        }

        //newNum1 will be the GCD of the two values
        return newNum1;
    }
}
