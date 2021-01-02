import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc15.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int earliestTime = -1;
        String input="";
        while ((line = reader.readLine()) != null) {
            input=line;
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("test 1 - " + aoc.findNthNumber("0,3,6",2020));
        System.out.println("test 2 - " + aoc.findNthNumber("1,3,2",2020));
        System.out.println("test 3 - " + aoc.findNthNumber("2,1,3",2020));
        System.out.println("test 4 - " + aoc.findNthNumber("1,2,3",2020));
        System.out.println("test 5 - " + aoc.findNthNumber("2,3,1",2020));
        System.out.println("test 6 - " + aoc.findNthNumber("3,2,1",2020));
        System.out.println("test 7 - " + aoc.findNthNumber("3,1,2",2020));
        System.out.println("Part 1 - " + aoc.findNthNumber(input,2020));
        //Part 2
        System.out.println("test 8 - " + aoc.findNthNumber("0,3,6",30000000));
        System.out.println("test 9 - " + aoc.findNthNumber("1,3,2",30000000));
        System.out.println("test 10 - " + aoc.findNthNumber("2,1,3",30000000));
        System.out.println("test 11 - " + aoc.findNthNumber("1,2,3",30000000));
        System.out.println("test 12 - " + aoc.findNthNumber("2,3,1",30000000));
        System.out.println("test 13 - " + aoc.findNthNumber("3,2,1",30000000));
        System.out.println("test 14 - " + aoc.findNthNumber("3,1,2",30000000));
        System.out.println("Part 2 - " + aoc.findNthNumber(input,30000000));
    }
}

class AOC {
    int findNthNumber(String input, int n) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] numbers = Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int turn = 1;
        int lastNumber=0;
        for (int i = 0; i < numbers.length ; i++) {
            map.put(numbers[i], i + 1);
            turn++;
        }
        while (turn < n) {
            int last = map.getOrDefault(lastNumber, -1);
            map.put(lastNumber, turn);
            lastNumber = last > 0 ? turn - last : 0;
            turn++;
        }

        return lastNumber;
    }

}
