import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc12.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        int max = 0;
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("Part 1 - " + aoc.findManhattanDistance(input));
        //Part 2
        System.out.println("Part 2 - " + aoc.findManhattanDistanceWithWaypoint(input));
    }
}

class AOC {
    final String EAST="E";
    final String WEST="W";
    final String NORTH="N";
    final String SOUTH="S";
    final String MOVE_RIGHT="R";
    final String MOVE_LEFT ="L";

    String orientation = "E";
    String wayPointOrientation = "E";

    //Part 2
    int findManhattanDistanceWithWaypoint(List<String> instructions) {
        int horizontal = 0, vertical = 0;
        int wayPointH = 10, wayPointV = 1;
        for (String direction : instructions) {
            int steps = Integer.parseInt(direction.substring(1));

            if (direction.startsWith(MOVE_RIGHT)) {
                int s = steps % 360;
                while (s > 0) {
                    int h=wayPointH;
                    wayPointH = (wayPointV < 0 ? -1 : 1) * Math.abs(wayPointV);
                    wayPointV = (h < 0 ? 1 : -1) * Math.abs(h);
                    s -= 90;
                }
            } else if(direction.startsWith(MOVE_LEFT)){
                int s = steps % 360;
                while (s > 0) {
                    int h=wayPointH;
                    wayPointH = (wayPointV > 0 ? -1 : 1) * Math.abs(wayPointV);
                    wayPointV = (h > 0 ? 1 : -1) * Math.abs(h);
                    s -= 90;
                }
            }
            else if (direction.startsWith(EAST)) {
                wayPointH += steps;
            } else if (direction.startsWith(WEST)) {
                wayPointH -= steps;
            } else if (direction.startsWith(NORTH)) {
                wayPointV += steps;
            } else if (direction.startsWith(SOUTH)) {
                wayPointV -= steps;
            } else if (direction.startsWith("F")) {
                horizontal += steps * wayPointH;
                vertical += steps * wayPointV;
            }
        }

        return Math.abs(horizontal) + Math.abs(vertical);
    }

    //Part 1
    int findManhattanDistance(List<String> instructions) {
        int horizontal = 0, vertical = 0;

        for (String direction : instructions) {
            int steps = Integer.parseInt(direction.substring(1));
            if ((direction.startsWith("F") && orientation == EAST) || (direction.startsWith(EAST))) {
                horizontal += steps;
            } else if ((direction.startsWith("F") && orientation == WEST || (direction.startsWith(WEST)))) {
                horizontal -= steps;
            } else if ((direction.startsWith("F") && orientation == NORTH || (direction.startsWith(NORTH)))) {
                vertical += steps;
            } else if ((direction.startsWith("F") && orientation == SOUTH || (direction.startsWith(SOUTH)))) {
                vertical -= steps;
            } else if (direction.startsWith(MOVE_RIGHT) || direction.startsWith(MOVE_LEFT)) {
                orientation = newOrientation(orientation, direction);
            }
        }

        return Math.abs(horizontal) + Math.abs(vertical);
    }

    private String newOrientation(String orientation,String direction) {
        int degree = Integer.parseInt(direction.substring(1));
        if (degree % 360 == 0) {
            return orientation;
        }

        if (degree % 180 == 0) {
            switch (orientation) {
                case EAST:
                    return WEST;
                case WEST:
                    return EAST;
                case NORTH:
                    return SOUTH;
                case SOUTH:
                    return NORTH;
                default:
                    return orientation;
            }
        }

        String dir = direction.substring(0, 1);

        if (degree == 90) {
            switch (orientation) {
                case EAST:
                    return dir.equals(MOVE_RIGHT) ? SOUTH : NORTH;
                case WEST:
                    return dir.equals(MOVE_RIGHT) ? NORTH : SOUTH;
                case NORTH:
                    return dir.equals(MOVE_RIGHT) ? EAST : WEST;
                case SOUTH:
                    return dir.equals(MOVE_RIGHT) ? WEST : EAST;
                default:
                    return orientation;
            }
        }

        if (degree == 270) {
            switch (orientation) {
                case EAST:
                    return dir.equals(MOVE_LEFT) ? SOUTH : NORTH;
                case WEST:
                    return dir.equals(MOVE_LEFT) ? NORTH : SOUTH;
                case NORTH:
                    return dir.equals(MOVE_LEFT) ? EAST : WEST;
                case SOUTH:
                    return dir.equals(MOVE_LEFT) ? WEST : EAST;
                default:
                    return orientation;
            }
        }

        return orientation;
    }
}


