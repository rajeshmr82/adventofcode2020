import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        InputStream is = Solution.class.getClassLoader().getResourceAsStream("aoc1.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int t = 0;
        int[] input=new int[200];
        while (t<200) {
            input[t] =Integer.parseInt(br.readLine()) ;
        }
        AOC aoc= new AOC();
       System.out.println(aoc.sumOf2020(input));
    }
}

class AOC {
    int sumOf2020(int[] input){
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i <input.length; i++) {
            if(set.contains(2020-input[i])){
                return input[i]*(2020-input[i]);
            }
        }

        return -1;
    }

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


