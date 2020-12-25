import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc10.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Integer> input = new ArrayList<>();
        int max=0;
        while ((line = reader.readLine()) != null) {
            int value = Integer.parseInt(line);
            input.add(value);
            if(value>max){
                max=value;
            }
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("Part 1 - " + aoc.findJoltProduct(input,max));
        //Part 2
        System.out.println("Part 2  - " + aoc.findDistinctWays(input,max));
    }
}

class AOC {
    //Part 2
    long findDistinctWays(List<Integer> adapters,int max){
        HashMap<Integer,Long> memo = new HashMap<>();
        return countWays(adapters,0,max+3,memo);
    }

    long countWays(List<Integer> adapters, int currentJolt,int target, HashMap<Integer,Long> memo){
        if(currentJolt+3==target){
            return 1;
        }else if(memo.containsKey(currentJolt)){
            return memo.get(currentJolt);
        }

        Long ways = 0L;
        int[] indices = IntStream.rangeClosed(1, 3).toArray();

        for (int index : indices) {
            if(adapters.contains(currentJolt+index)){
                ways +=countWays(adapters,currentJolt+index,target,memo);
            }
        }
        memo.put(currentJolt,ways);
        return ways;
    }

    //Part 1
    long findJoltProduct(List<Integer> adapters,int max) {
        int oneJolts = 0, threeJolts = 0;
        int currentJolt = 0;
        while (currentJolt < max) {
            if (adapters.contains(currentJolt + 1)) {
                currentJolt += 1;
                oneJolts++;
            } else if (adapters.contains(currentJolt + 2)) {
                currentJolt += 2;
            } else if (adapters.contains(currentJolt + 3)) {
                currentJolt += 3;
                threeJolts++;
            }
        }

        return oneJolts * (threeJolts+1);
    }
}
