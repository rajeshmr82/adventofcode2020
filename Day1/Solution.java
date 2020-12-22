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
    //Day 3: Part 1

    //Day 2: Part 2
    int countValidTobogganPassword(List<String> passwords){
        int count=0;
        for (String row:
                passwords) {
            String[] input = row.split(":");
            String policy = input[0].trim();
            String password = input[1].trim();
            if(isPasswordValidToboggan(policy,password)){
                count++;
            }
        }

        return count;
    }

    private boolean isPasswordValidToboggan(String policy, String password) {
        String counts = policy.split(" ")[0].trim();
        String character = policy.split(" ")[1].trim();
        int p1 = Integer.parseInt(counts.split("-")[0].trim())-1;
        int p2 = Integer.parseInt(counts.split("-")[1].trim())-1;
        if(String.valueOf(password.charAt(p1)).equals(character) ^ String.valueOf(password.charAt(p2)).equals(character)){
            return true;
        }

        return false;
    }

    //Day2 : Part 1
    int countValidPasswords(List<String> passwords){
        int count=0;
        for (String row:
             passwords) {
            String[] input = row.split(":");
            String policy = input[0].trim();
            String password = input[1].trim();
            if(isPasswordValid(policy,password)){
                count++;
            }
        }

        return count;
    }

    boolean isPasswordValid(String policy, String password) {
        String counts = policy.split(" ")[0].trim();
        String character = policy.split(" ")[1].trim();
        int min = Integer.parseInt(counts.split("-")[0].trim());
        int max = Integer.parseInt(counts.split("-")[1].trim());
        int count = 0;
        for (int i = 0; i < password.length(); i++) {
            if (password.substring(i, i + 1).equals(character)) {
                count++;
            }
        }

        if (count >= min && count <= max) {
            return true;
        }

        return false;
    }
}


