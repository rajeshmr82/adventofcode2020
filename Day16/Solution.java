import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Array;
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
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc16.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int earliestTime = -1;
        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("Part 1 - " + aoc.findScanningErrorRate(input));
        //Part 2
        System.out.println("Part 2 - " + aoc.findDepartureFields());
    }
}

class AOC {

    class Range {
        private int maxValue;
        private int minValue;

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }
    }

    class Rule{
        private List<Range> ranges= new ArrayList<>();
        private String name;

        public Rule(String name, String rule) {
            this.name = name;
            String[] ruleStrings = rule.split("or");
            for (String ruleDesc:ruleStrings) {
                Range range = new Range();
                range.setMinValue(Integer.parseInt(ruleDesc.split("-")[0].trim()));
                range.setMaxValue(Integer.parseInt(ruleDesc.split("-")[1].trim()));
                this.ranges.add(range);
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isValidField(int value){
            for (Range range: this.ranges) {
                if(value>=range.getMinValue() && value<=range.getMaxValue()){
                    return true;
                }
            }
            return false;
        }
    }

    Set<String> validTickets = new HashSet<>();
    List<Rule> rules = new ArrayList<>();
    List<String> myTicket = new ArrayList();
    List<String> nearBy = new ArrayList<>();

    public long findScanningErrorRate(List<String> input) {
        long result = 0;

        readInput(input, rules, nearBy,myTicket);
        int[] myTicketFields = Arrays.stream(myTicket.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        nearBy.forEach(t -> validTickets.add(t));
        for (String ticket:nearBy) {
            int[] fields = Arrays.stream(ticket.split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray();
            for (int i = 0; i < fields.length; i++) {
                boolean isValid=false;
                int current = fields[i];
                for (int j = 0; j < rules.size(); j++) {
                    if(rules.get(j).isValidField(current)){
                        isValid = true;
                        break;
                    }
                }
                if(!isValid){
                    result+=current;
                    validTickets.remove(ticket);
                }
            }
        }
        
        return result;
    }

    public long findDepartureFields() {
        long result=1;

        boolean[][] candidates = new boolean[myTicket.get(0).split(",").length][rules.size()];

        List<String[]> tickets = validTickets
                                    .stream()
                                    .map(t -> t.split(","))
                                    .collect(Collectors.toList());

        for (int i = 0; i <candidates.length; i++) {
            Arrays.fill(candidates[i],true);
        }

        for (String[] ticket: tickets) {
            for (int f = 0; f < ticket.length; f++) {
                for (int r = 0; r < rules.size(); r++) {
                    candidates[f][r] = candidates[f][r] && rules.get(r).isValidField(Integer.parseInt(ticket[f]));
                }
            }
        }
        HashSet<Integer> considered = new HashSet<>();
        boolean hasChanged = true;
        while (hasChanged){
            hasChanged=false;
            for (int i = 0; i < candidates.length; i++) {
                int count=0;
                int indexOfSingle =-1;
                
                for (int j = 0; j < candidates.length; j++) {
                    if(candidates[i][j]){
                        count++;
                        indexOfSingle=j;
                    }
                }
                if(count==1 && !considered.contains(indexOfSingle)){
                    considered.add(indexOfSingle);
                    hasChanged=true;
                    for (int k = 0; k < candidates.length; k++) {
                        if(k!=i){
                            candidates[k][indexOfSingle]=false;
                        }
                    }
                }
            }
        }

        String[] ticketFields = myTicket.get(0).split(",");

        for (int r = 0; r < rules.size(); r++) {
            if(!rules.get(r).getName().startsWith("departure")){
                continue;
            }

            for (int f = 0; f < candidates.length; f++) {
                if(candidates[f][r]){
                    result *=Integer.parseInt(ticketFields[f]);
                }
            }
        }
        return result;
    }



    private void readInput(List<String> input, List<Rule> rules, List<String> nearBy,List<String> myTicket) {
        boolean loadRules = true;
        boolean loadMyTicket = false;
        boolean loadingNearBy = false;
        for (String line : input) {
            if (line.isEmpty()) {
                continue;
            }

            if (loadingNearBy) {
                if (!line.startsWith("nearby tickets")) {
                    nearBy.add(line);
                }
            }

            if (loadMyTicket) {
                myTicket.add(line);

                loadMyTicket = false;
                loadingNearBy = true;
            }
            if (loadRules) {
                if (line.startsWith("your ticket")) {
                    loadRules = false;
                    loadMyTicket = true;
                } else {
                    rules.add(new Rule(line.split(":")[0], line.split(":")[1]));
                }
            }
        }
    }
}
