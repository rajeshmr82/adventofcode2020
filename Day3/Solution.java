import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile=new File(Solution.class.getClassLoader().getResource("aoc3.txt").getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc= new AOC();
       System.out.println(aoc.getResult(input));
    }
}

class AOC {
    //Day 3: Part 2
    int getResult(List<String> map) {
        int rows = map.size();
        int width = map.get(0).length();

        return getTreeCount(map, rows, width, 1, 1)
                * getTreeCount(map, rows, width, 3, 1)
                * getTreeCount(map, rows, width, 5, 1)
                * getTreeCount(map, rows, width, 7, 1)
                * getTreeCount(map, rows, width, 1, 2);
    }

    //Day 3: Part 1
    int countTrees(List<String> map){
        int rows = map.size();
        int width = map.get(0).length();
        int result = getTreeCount(map, rows, width, 3, 1);

        return result;
    }

    private int getTreeCount(List<String> map, int rows, int width, int xStep, int yStep) {
        int result=0;
        int x=0,y=0;
        while(y< rows){
            x = x % width;
            Character currentItem = map.get(y).charAt(x);
            if(map.get(y).charAt(x)=='#'){
                result++;
            }

            x+= xStep;
            y+= yStep;
        }
        return result;
    }

}


