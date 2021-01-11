import java.io.*;
import java.util.*;

class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile=new File(Solution.class.getClassLoader().getResource("aoc2.txt").getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc= new AOC();
       System.out.println(aoc.countValidTobogganPassword(input));
    }
}

class AOC {
     //Day 1 : Part 1
    int sumOf2020(int[] input){
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i <input.length; i++) {
            if(set.contains(2020-input[i])){
                return input[i]*(2020-input[i]);
            }
        }

        return -1;
    }

    //Day 1: Part 2
    int tripletSumOf2020(int[] input){
        for (int i = 0; i <input.length-2; i++) {
            HashSet<Integer> set = new HashSet<>();
            int currSum = 2020 - input[i];
            for (int j = i+1; j < input.length; j++) {
                if(set.contains(currSum-input[j])){
                    return input[i]*input[j]*(currSum-input[j]);
                }
                set.add(input[j]);
            }
        }

        return -1;
    }
}
