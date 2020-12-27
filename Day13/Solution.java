import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc13.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int earliestTime = -1;
        String schedule = "";
        while ((line = reader.readLine()) != null) {
            if (earliestTime == -1) {
                earliestTime = Integer.parseInt(line);
            } else {
                schedule = line;
            }
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("Part 1 - " + aoc.findEarliestBus(earliestTime, schedule));

        //Part 2
        System.out.println("Part 2 - " + aoc.findEarliestTime(schedule));
    }
}

class AOC {
    long findEarliestBus(int earliestTime, String schedule) {

        List<Integer> busTimes = Arrays.stream(schedule.split(","))
                .filter(b -> !b.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int min = Integer.MAX_VALUE, current = 0;
        for (int time : busTimes) {
            if (earliestTime % time == 0) {
                return 0;
            }
            int value = time * (earliestTime / time) + time;
            if ((value - earliestTime) < min) {
                min = value - earliestTime;
                current = time;
            }
        }
        return current * min;
    }

    //Part 2
    //Chinese Remainder Theorem (CRT)
    long findEarliestTime(String scheduleInput) {
        String[] schedule = scheduleInput.split(",");
        List<Long> busTimes = Arrays.stream(schedule)
                .filter(b -> !b.equals("x"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        long product = busTimes.stream()
                .reduce((curr, prev) -> curr * prev)
                .get();

        long[] reminders = IntStream.range(0, schedule.length)
                .filter(i -> !schedule[i].equals("x"))
                .mapToLong(i -> i % Long.parseLong(schedule[i]))
                .toArray();

        long sum = 0;
        for (int i = 0; i < busTimes.size(); i++) {
            long num = busTimes.get(i);
            long partialProduct = product / num;
            sum += partialProduct * BigInteger.valueOf(partialProduct).modInverse(BigInteger.valueOf(num)).longValue() * reminders[i];
        }

        return product - (sum % product);
    }
}
