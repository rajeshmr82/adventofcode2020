import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc19_part2_simple.txt")).getFile());
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
        //System.out.println("Part 1 - " + aoc.findValidInputs(input));
        //Part 2
        //System.out.println("Part 2 - " + aoc.findValidInputsWithUpdates(input));
        aoc.parseRuleInput(input);
        aoc.rules.get("8").getRules().add(new ArrayList<>(Arrays.asList(
                "42",
                "8"
        )));
        aoc.rules.get("11").getRules().add(new ArrayList<>(Arrays.asList(
                "42",
                "11",
                "31"
        )));
        System.out.println(aoc.matchRule("babbbbaabbbbbabbbbbbaabaaabaaa", "0"));
    }
}

class AOC {
    class Rule {
        private String character;
        private List<List<String>> rules= new ArrayList<>();

        public Rule(String character) {
            this.character = character;
        }

        public String getCharacter() {
            return character;
        }

        public List<List<String>> getRules() {
            return rules;
        }

        public boolean isTerminal(){
            return this.rules.size()==0;
        }

        public boolean isMatch(String s){
            return this.character.equals(s);
        }

    }
    Map<String,Rule> rules = new HashMap<>();
    List<String> messages = new ArrayList<>();

    //Part 2
    public int findValidInputsWithUpdates(List<String> input) {
        messages.clear();
        rules.clear();
        parseRuleInput(input);
        this.rules.get("8").getRules().add(new ArrayList<>(Arrays.asList(
                "42",
                "8"
        )));
        this.rules.get("11").getRules().add(new ArrayList<>(Arrays.asList(
                "42",
                "11",
                "31"
        )));


        int matchCount = 0;
        for (String message :
                messages) {
            var result = matchRule(message,"0");
            for (Integer match:
                 result) {
                if(match== message.length()){
                    matchCount++;
                }
            }
        }
        return matchCount;
    }

    //Part 1
    public int findValidInputs(List<String> input) {
        messages.clear();
        rules.clear();
        parseRuleInput(input);

        int matchCount = 0;
        for (String message :
                messages) {
            var result = matchRule(message,"0");
            for (Integer match:
                    result) {
                if(match== message.length()){
                    matchCount++;
                }
            }
        }
        return matchCount;
        /*
        parseInput(input);

        int matchCount = 0;
        for (String message :
                messages) {

            boolean m = matchRule(message, "0");
            System.out.println(m + " - " + message);
            if (m) {
                matchCount++;
            }
        }
        return matchCount;*/
    }

    public List<Integer> matchRule(String message, String ruleNo) {

        if (message.length() <= 0) {
            return new ArrayList<>();
        }

        Rule rule = rules.get(ruleNo);
        if (rule.isTerminal()) {
            if (rule.isMatch(String.valueOf(message.charAt(0)))) {
                return new ArrayList<>(Arrays.asList(1));
            }
        }

        List<Integer> result = new ArrayList<>();

        for (List<String> childRules :
                rule.getRules()) {
            List<Integer> matchLengths = new ArrayList<>(Arrays.asList(0));
            for (String child :
                    childRules) {
                List<Integer> newCandidateLengths = new ArrayList<>();
                for (Integer m :
                        matchLengths) {
                    var matches = matchRule(message.substring(m), child);
                    if (matches.size() == 0) {
                        continue;
                    }

                    for (Integer matchCount :
                            matches) {
                        newCandidateLengths.add(matchCount + m);
                    }
                }
                matchLengths = newCandidateLengths;
            }
            result.addAll(matchLengths);
        }
        System.out.println(message + " - " + ruleNo+ " - " + result);
        return result;
    }

    public void parseRuleInput(List<String> input) {

        for (String line:
                input) {
            if(line.isEmpty()){
                continue;
            }
            Matcher ruleMatcher = Pattern.compile("(\\d+):\\s(.*)").matcher(line);

            while (ruleMatcher.find()) {
                String key = ruleMatcher.group(1);
                String value = ruleMatcher.group(2);
                if(value.startsWith("\"")){
                    rules.put(key, new Rule(value.substring(1,2)));
                }else{
                    String[] r = value.split("\\|");
                    rules.put(key,new Rule(""));
                    Rule current= rules.get(key);
                    Arrays.stream(r)
                            .map(c -> Arrays.stream(c.trim().split(" ")).collect(Collectors.toList()))
                            .collect(Collectors.toList())
                            .forEach(t -> current.getRules().add(t));
                }
                continue;
            }
            Matcher messageMatcher = Pattern.compile("^[^0-9][a-zA-Z]+$").matcher(line);

            while (messageMatcher.find()) {
                messages.add(line);
            }
        }
    }
}
