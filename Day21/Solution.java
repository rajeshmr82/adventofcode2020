import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc21.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.countNonAllergen(input));
        //Part 2
        System.out.println(aoc.getCanonicalIngredientList());
    }
}

class AOC {
    List<String> allergens = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    HashMap<Integer, List<String>> food = new HashMap<>();
    HashMap<Integer, List<String>> contains = new HashMap<>();
    HashMap<String, List<Integer>> allergenMap = new HashMap<>();
    HashMap<String, String> ingAllergenMap = new HashMap<>();
    HashMap<String, String> allergenIngMap = new HashMap<>();

    public int countNonAllergen(List<String> input) {
        parseInput(input);

        HashMap<String, ArrayList<String>> candidates = buildCandidates();

        identifyAllergenMap(candidates);

        int result=0;
        for (var f:
             food.entrySet()) {
            for (var ingredient:
                 f.getValue()) {
                if(!ingAllergenMap.containsKey(ingredient)){
                    result++;
                }
            }
        }
        return result;
    }

    private HashMap<String, ArrayList<String>> buildCandidates() {
        HashMap<String, ArrayList<String>> candidates = new HashMap<>();

        for (var a : allergens
        ) {
            HashMap<String, Integer> ingredientMap = new HashMap<>();
            for (var rec :
                    allergenMap.get(a)) {
                for (var ing :
                        food.get(rec)) {
                    ingredientMap.put(ing, ingredientMap.getOrDefault(ing, 0) + 1);
                }
            }

            for (var c :
                    ingredientMap.entrySet()) {
                if (c.getValue() == allergenMap.get(a).size()) {
                    candidates.computeIfAbsent(a, v -> new ArrayList<>()).add(c.getKey());
                }

            }
        }
        return candidates;
    }

    private void identifyAllergenMap(HashMap<String, ArrayList<String>> candidates) {
        boolean changed = true;

        Set<String> eliminatedCandidates = new HashSet<>();
        while (changed) {
            changed = false;
            for (var currCandidate :
                    candidates.entrySet()) {
                if (eliminatedCandidates.contains(currCandidate.getKey())) {
                    continue;
                }
                for (var ingredient :
                        ingAllergenMap.entrySet()) {
                    currCandidate.getValue().remove(ingredient.getKey());
                }
                if (currCandidate.getValue().size() == 1) {
                    changed = true;
                    for (var ingredient :
                            currCandidate.getValue()) {
                        ingAllergenMap.put(ingredient, currCandidate.getKey());
                        allergenIngMap.put(currCandidate.getKey(),ingredient);
                    }
                    eliminatedCandidates.add(currCandidate.getKey());
                }
            }
        }
    }

    public void parseInput(List<String> input) {
        int f = 0;
        for (String line :
                input) {
            Matcher inputMatch = Pattern.compile("(.*)(\\(contains (.*)\\))").matcher(line);
            while (inputMatch.find()) {
                for (String ing :
                        inputMatch.group(1).split(" ")) {
                    if (!ingredients.contains(ing.trim())) {
                        ingredients.add(ing.trim());
                    }

                    food.computeIfAbsent(f, i -> new ArrayList<>()).add(ing.trim());
                }
                for (String alr :
                        inputMatch.group(3).split(", ")) {
                    if (!allergens.contains(alr.trim())) {
                        allergens.add(alr.trim());
                    }
                    contains.computeIfAbsent(f, i -> new ArrayList<>()).add(alr.trim());
                    allergenMap.computeIfAbsent(alr.trim(), a -> new ArrayList<>()).add(f);
                }
            }
            f++;
        }

    }

    public String getCanonicalIngredientList() {
        List<String> list = new ArrayList<>();
        allergenIngMap.keySet().stream().sorted().forEach(a -> list.add(allergenIngMap.get(a)));
        return String.join(",",list);
    }
}
