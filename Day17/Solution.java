import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc17.txt")).getFile());
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
        System.out.println("Part 1 - " + aoc.findActive(input,6));
        //Part 2

    }
}

class AOC {
    final char ACTIVE = '#';
    HashMap<Coordinate,Boolean> grid = new HashMap<>();
    public int findActive(List<String> input, int n) {
        int activeCount = 0;
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (input.get(i).charAt(j) == ACTIVE) {
                    grid.put(new Coordinate(j, i, 0,0), true);
                }
            }
        }

        while (n-- > 0) {
            HashMap<Coordinate, Boolean> newGrid = new HashMap<>();
            for (Coordinate k : grid.keySet()) {
                newGrid.put(k, grid.get(k));
            }
            HashSet<Coordinate> neighbours = new HashSet<>();
            for (Coordinate point : grid.keySet()) {
                int activeNeighbours = getActiveNeighbours(point);
                if (newGrid.get(point)) {
                    if (activeNeighbours != 2 && activeNeighbours != 3) {
                        newGrid.remove(point);
                    }
                } else {
                    if (activeNeighbours == 3) {
                        newGrid.put(point, true);
                    }
                }

                for (Coordinate cube : getNeighbours(point)) {
                    if (!grid.containsKey(cube)) {
                        neighbours.add(cube);
                    }
                }
            }

            for (Coordinate point : neighbours) {
                if (getActiveNeighbours(point) == 3) {
                    newGrid.put(point, true);
                }
            }

            this.grid = newGrid;
        }

        return grid.size();
    }

    int getActiveNeighbours(Coordinate point){
        List<Coordinate> neighbours = getNeighbours(point);
        int activeCount=0;
        for (Coordinate n:neighbours) {
            if(grid.containsKey(n)){
                if(grid.get(n)){
                    activeCount++;
                }
            }
        }

        return activeCount;
    }

    List<Coordinate> getNeighbours(Coordinate point) {
        List<Coordinate> neighbours = new ArrayList<>();
        int activeCount = 0;
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    for (int wOffset = -1; wOffset <= 1; wOffset++) {
                        if (xOffset == 0 && yOffset == 0 && zOffset == 0 && wOffset == 0) {
                            continue;
                        }
                        neighbours.add(new Coordinate(point.getX() + xOffset,
                                        point.getY() + yOffset,
                                        point.getZ() + zOffset,
                                        point.getW() + wOffset));
                    }

                }
            }
        }
        return neighbours;
    }

    class Coordinate {
        private int x;
        private int y;
        private int z;
        private int w;

        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Coordinate(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y &&
                    z == that.z &&
                    w == that.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z,w);
        }
    }

}
