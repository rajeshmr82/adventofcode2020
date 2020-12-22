import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Solution.class.getClassLoader().getResource("aoc6.txt").getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc= new AOC();
       System.out.println(aoc.sumOfYes(input));
    }
}

class AOC {
    //Day 6
    int sumOfYes(List<String> forms){
        List<String> groupForms = new ArrayList<>();
        int sum=0;
        for (String form:
             forms) {
            if(form.isEmpty()){
                sum+= getGroupCountAllYes(groupForms);
                groupForms.clear();
            }else{
                groupForms.add(form);
            }
        }

        if(!groupForms.isEmpty()){
            sum+= getGroupCountAllYes(groupForms);
            groupForms.clear();
        }

        return sum;
    }

    // Part 1
    private int getGroupCountAnyYes(List<String> groupForms) {
        int count=0;
        int[] set = new int[27];
        for (String form: groupForms) {
            for (Character c: form.toCharArray()) {
                if (set[c-'a'] == 0) {
                    count++;
                    ++set[c- 'a'];
                }
            }
        }
        return count;
    }

    //Part 2
    private int getGroupCountAllYes(List<String> groupForms) {
        int groupSize = groupForms.size();
        int count=0;
        int[] set = new int[27];
        for (String form: groupForms) {
            for (Character c: form.toCharArray()) {
                ++set[c - 'a'];
                if (set[c - 'a'] == groupSize) {
                    count++;
                }
            }
        }
        return count;
    }

}


