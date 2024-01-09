// Lab: Solve For X Program
// Eli Fisk
// This program takes input from the user as parameters to then make up to 10 random
// equations that are in the format of a 1st degree polynomial with a variable of "x". It
// will then ask the user to solve for the x value in the equations the program has made.
// After the user enters their answers, the program will output the user's results.
// Zach Estsus, Braden Wingfield

import java.util.Scanner;
import java.util.Random;

class solveX {

    // Create "main" that will ask for, and error check, user input.  Create arrays to hold values.
    // Format the equations to ask for x, then format and print results.
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int it = 0;
        int[] givens = new int[8];
        int y = 0;
        int o = 0;
        boolean bol = false;
        while (it < 3) {
            switch (o) {
                case 1:
                y = 1;
                break;
                case 2:
                y = 2;
                bol = true;
                break;
                case 4:
                y = 3;
            }
            System.out.print(questions[o]);
            if (sc.hasNextInt()) {
                givens[o] = sc.nextInt();
                if (!check(givens[o], y)) {
                    continue;
                }
                else {
                    ++o;
                    while (bol) {
                    System.out.print(questions[o]);
                        if (sc.hasNextInt()) {
                            givens[o] = sc.nextInt();
                            if (givens[o] >= givens[o-1] && check(givens[o], y)) {
                                ++it;
                                ++o;
                                break;
                            }
                            else{
                                if (!(givens[o] >= givens[o-1])) {
                                    --o;
                                    break;
                                }
                                else {
                                    continue;
                                }
                            }
                        }
                    sc.next();
                    }
                continue;
                }
            }
            sc.next();
        }
        int seed = givens[0];
        int prob = givens[1];
        int coeMin = givens[2];
        int coeMax = givens[3];
        int valMin = givens[4];
        int valMax = givens[5];
        int equMin = givens[6];
        int equMax = givens[7];
        r.setSeed(seed);
        int[] help = new int[prob];
        int[] coeff = generateRandomArray(coeMax, coeMin, prob);
        int[] value = generateRandomArray(valMax, valMin, prob);
        int[] signs = generateRandomArray(1, 0, prob);
        int[] equal = generateRandomArray(equMax, equMin, prob);
        double[] Answers = generateAnswers(coeff, value, signs, equal);
        double[] UserAnsw = new double[prob];
        StringBuilder equation = new StringBuilder();

        for (it = 0; it < prob; it++) {
            char signss;
            int use;
            int low;
            if (signs[it] == 1) {
                signss = 45;
            }
            else {
                signss = 43;
            }
            if (coeff[it] == 0) {
                
                if(valMin <= equMax && equMin <= valMax) {
                    if (valMax <= equMax) {
                        use = valMax;
                    }
                    else{
                        use = equMax;
                    }
                    if (valMin >= equMin) {
                        low = valMin;
                    }
                    else {
                        low = equMin;
                    }
                    equal[it] = r.nextInt(use - low + 1) + low;
                    value[it] = equal[it];
                    if ((signs[it] == 1)) {
                        value[it] *= -1;
                    }
                }
                else {
                    help[it] = 1;
                }
            }
            boolean bool;
            bool = true;
            while (bool) {
                if (help[it] == 1) {
                    System.out.println("Given parameters made x undefined.");
                    break;
                }
                System.out.printf("%dx %c %d = %d%n", coeff[it], signss, value[it], equal[it]);
                System.out.print("What is x? ");
                if (sc.hasNextDouble()) {
                    UserAnsw[it] = sc.nextDouble();
                    bool = false;
                }
                else{
                    sc.next();
                }
            }
        }
        sc.close();
        char sign;
        double numCor = 0.0;
        double numWro = 0.0;

        for(it = 0 ; it < prob ; ++it) {
            String lol;
            if (signs[it] == 1) {
                sign = 45;
            }
            else {
                sign = 43;
            }
            if (compareAnswer(Answers[it], UserAnsw[it])) {
                lol = "Correct";
                ++numCor;
            }
            else {
                lol = "Incorrect";
                ++numWro;
            }
            equation.append(coeff[it] + "x " + sign + " " + value[it] + " = " + equal[it]);
            String TrueEqu = equation.toString();
            equation.delete(0, equation.length());
            if(help[it] == 1) {
                System.out.println("Invalid parameters!  Points still given.");
                continue;
            }
            if (Answers[it] == 3.14159) {
                System.out.printf("%-20s x = %-10.10s %s%n", TrueEqu, "Any Num", lol);
            }
            else{
                System.out.printf("%-20s x = %-10.3f %s%n", TrueEqu, Answers[it], lol);
            }
        }
        System.out.printf("Num correct   = %.0f%n", numCor);
        System.out.printf("Num incorrect = %.0f%n", numWro);
        System.out.printf("Your score    = %.0f%%%n", (numCor/(numCor+numWro) * 100));
    }

    // Create initial questions to be called in main.
    static final String[] questions = {
        "Enter seed: ", "How many questions would you like? ",
        "Enter minimum coefficient: ", "Enter maximum coefficient: ",
        "Enter minimum value: ", "Enter maximum value: ",
        "Enter minimum equals: ", "Enter maximum equals: "
    };
    
    static Random r = new Random();

    // Create a random number generater to put random numbers into arrays.
    static int[] generateRandomArray(int max, int min, int Problem) {
        int[] coeff = new int[Problem];
        for (int i = 0 ; i < Problem; ++i) {
            coeff[i] = min + r.nextInt(max - min + 1);
        }
        return coeff;       
    }

    //Find the x value for each equation.
    static double[] generateAnswers(int[] coeffs, int[] values, int[] signs, int[] equals) {
        double[] Answers = new double[coeffs.length];
        for (int i = 0 ; i< coeffs.length ; i++) {
            Answers[i] = generateAnswer(coeffs[i], values[i], signs[i], equals[i]);
        }
        return Answers;
    }

    //Use the components of 1 equation to find the value of x.
    static double generateAnswer(int coeff, int value, int sign, int equal) {
        double answer;
        if(coeff == 0) {
            answer = 3.14159;
            return answer;
        }
        if (sign == 1) {
            answer = (equal + (double)value) / (double)coeff;
        }
        else {
            answer = (equal - (double)value) / (double)coeff;
        }
        return answer;
    }

    //Make the user's answer correct if it is within 0.01 away from the real answer.
    static boolean compareAnswer(double left, double right) {
        if (left == 3.14159) {
            return true;
        }
        double num = left - right;
        if (num < 0) {
            num *= -1;
        }
        if (num > 0.01) {
            return false;
        }
        else {
            return true;
        }
    }

    // Create a way to error check to make sure user input is within peramitters.
    static boolean check (int userNum, int t) {
        if (t == 3) {
            if (userNum >= -100000 && userNum <= 100000) {
                return true;
            }
            else {
                return false;
            }
        }
        if (t == 2) {
            if (userNum >= 0 && userNum <= 5) {
                return true;
            }
            else {
                return false;
            }
        }
        if (t == 1) {
            if (userNum >= 1 && userNum <= 10) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
}