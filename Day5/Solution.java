import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Solution.class.getClassLoader().getResource("aoc5.txt").getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc= new AOC();
       System.out.println(aoc.findSeat(input));
    }
}

class AOC {
    //Day 5
    int findSeat(List<String> passes){
        int[] seats = new int[1024];

        for (String pass:
                passes) {
            ++seats[getSeatId(pass)];
        }
        for (int i = 1; i < 1023; i++) {
            if(seats[i]==0 && seats[i-1]==1 & seats[i+1]==1){
                return i;
            }
        }
        return -1;
    }

    int heighestSeatId(List<String> passes){
        int heighest=0;

        for (String pass:
             passes) {
            int seatId = getSeatId(pass);
            if(seatId>heighest){
                heighest = seatId;
            }
        }

        return heighest;
    }

    private int getSeatId(String pass) {
        int column = 0;
        for (int i = 9; i >= 7; i--) {
            if (pass.charAt(i) == 'R') {
                column += Math.pow(2, 9 - i);
            }
        }
        int row = 0;
        for (int i = 6; i >= 0; i--) {
            if (pass.charAt(i) == 'B') {
                row+=Math.pow(2,6-i);
            }
        }

        return row * 8 + column;
    }

}


