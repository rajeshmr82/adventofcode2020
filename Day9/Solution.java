import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc9.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Long> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(Long.parseLong(line));
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.firstNonConfirmingValue(input));
        //Part 2
        System.out.println(aoc.findEncryptionWeakness(input));
    }
}

class AOC {
    final int PREAMBLE=25;
    ArrayList<Long> queue = new ArrayList<>();
    //Part 2
    long findEncryptionWeakness(List<Long> numbers) {
        queue.clear();
        for (int i = 0; i < numbers.size(); i++) {
            long current = numbers.get(i);
            if (i > PREAMBLE) {
                queue.remove(0);
            }

            if (i >= PREAMBLE) {
                if (!findMatch(current)) {
                    int s=0,e=s+1,n=numbers.size();
                    long sum=numbers.get(s)+numbers.get(e);
                    while(s<n && e<n && s<e){
                        if(sum<current){
                            e++;
                            if(e<n){
                                sum+=numbers.get(e);
                            }
                        }else if(sum>current){
                            if(s<n && s<e){
                                sum-=numbers.get(s);
                            }
                            s++;
                            if(s==e){
                                e++;
                            }
                        }else{
                            break;
                        }
                    }
                    long min=Long.MAX_VALUE;
                    long max=Long.MIN_VALUE;
                    for (int j = s; j <= e; j++) {
                        long value = numbers.get(j);
                        if(value>max){
                            max = value;
                        }
                        if(value<min){
                            min = value;
                        }
                    }

                    return max+min;
                }
            }

            queue.add(current);
        }

        return -1;
    }
    //Part 1
    long firstNonConfirmingValue(List<Long> numbers) {

        for (int i = 0; i < numbers.size(); i++) {
            long current = numbers.get(i);
            if (i > PREAMBLE) {
                queue.remove(0);
            }

            if (i >= PREAMBLE) {
                if (!findMatch(current)) {
                    return current;
                }
            }

            queue.add(current);
        }

        return -1;
    }

    private boolean findMatch(long  current) {
        for (int j = 0; j < PREAMBLE; j++) {
            if(queue.contains(current - queue.get(j))) {
                return true;
            }
        }

        return false;
    }
}
