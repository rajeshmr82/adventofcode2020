import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc8.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.findAccumulatorAfterTerminate(input));
    }
}

class AOC {
    //Part 1
    int findAccumulatorValueWhileInfiniteLoop(List<String> instructionSet) {
        int accumulator = 0;
        boolean[] visited = new boolean[instructionSet.size()];

        int line = 0;
        while (line < instructionSet.size()) {
            String currentInstruction = instructionSet.get(line);
            if (visited[line]) {
                return accumulator;
            }
            visited[line] = true;
            if (currentInstruction.contains("nop")) {
                line++;
                continue;
            } else if (currentInstruction.contains("acc")) {
                int value = Integer.parseInt(currentInstruction.split(" ")[1]);
                accumulator += value;
                line++;
            } else if (currentInstruction.contains("jmp")) {
                int value = Integer.parseInt(currentInstruction.split(" ")[1]);
                line += value;
            }
        }

        return accumulator;
    }

    //Part 2
    int findAccumulatorAfterTerminate(List<String> instructionSet) {
        int accumulator = 0;
        boolean[] wasExecuted = new boolean[instructionSet.size()];
        HashSet<Integer> swapped = new HashSet<>();
        boolean hasSwapped=false;

        int line = 0;
        while (line < instructionSet.size()) {

            if (line<0 || wasExecuted[line]) {
                hasSwapped = false;
                wasExecuted= new boolean[instructionSet.size()];
                line=0;
                accumulator=0;
            }
            String currentInstruction = instructionSet.get(line);

            wasExecuted[line] = true;
            if (currentInstruction.contains("nop")) {
                if(!hasSwapped && !swapped.contains(line) && currentInstruction.split(" ")[1]!="0"){
                    hasSwapped = true;
                    swapped.add(line);
                    line += Integer.parseInt(currentInstruction.split(" ")[1]);
                }else {
                    line++;
                    continue;
                }
            } else if (currentInstruction.contains("acc")) {
                int value = Integer.parseInt(currentInstruction.split(" ")[1]);
                accumulator += value;
                line++;
            } else if (currentInstruction.contains("jmp")) {
                if(!hasSwapped && !swapped.contains(line)){
                    hasSwapped = true;
                    swapped.add(line);
                    line++;
                    continue;
                }else{
                    line += Integer.parseInt(currentInstruction.split(" ")[1]);
                }
            }
        }

        return accumulator;
    }
}


