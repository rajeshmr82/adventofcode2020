import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc24.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.countBlackTiles(input));
        //Part 2
        System.out.println(aoc.countBlackTilesAfter100Days(input));
    }


}

class AOC {
        static class Grid {
            private List<Pair<Integer, Integer>> blackTiles = new ArrayList<>();

            public List<Pair<Integer, Integer>> getBlackTiles() {
                return blackTiles;
            }

            public void flip(Pair<Integer, Integer> tile) {
                if (blackTiles.contains(tile)) {
                    blackTiles.remove(tile);
                } else {
                    blackTiles.add(tile);
                }
            }

            public int countAdjacentBlackTiles(Pair<Integer, Integer> tile) {
                return (int) Arrays.stream(getNeighbours(tile))
                        .filter(t -> blackTiles.contains(t))
                        .count();
            }

            public Pair<Integer, Integer>[] getNeighbours(Pair<Integer, Integer> tile) {
                return new Pair[]{
                        Pair.of(tile.getLeft() + 1, tile.getRight()),
                        Pair.of(tile.getLeft() - 1, tile.getRight()),
                        Pair.of(tile.getLeft(), tile.getRight() + 1),
                        Pair.of(tile.getLeft() - 1, tile.getRight() + 1),
                        Pair.of(tile.getLeft() + 1, tile.getRight() - 1),
                        Pair.of(tile.getLeft(), tile.getRight() - 1)
                };
            }

            public int getFlippedCount() {

                return blackTiles.size();
            }

            public void dailyFlip() {
                HashMap<Pair<Integer,Integer>,Boolean> allTiles = new HashMap<>();
                for (var tile:
                     blackTiles) {
                    allTiles.put(tile,true);
                    for (var neighbour:
                         getNeighbours(tile)) {
                        if(!blackTiles.contains(neighbour)){
                            allTiles.put(neighbour,false);
                        }
                    }
                }
                List<Pair<Integer, Integer>> newList = new ArrayList<>();
                for (var tileMap:
                     allTiles.entrySet()) {
                    var tile = tileMap.getKey();
                    var n = countAdjacentBlackTiles(tile);
                    if(tileMap.getValue()){
                        if(n==1 || n==2){
                            newList.add(tile);
                        }
                    }else{
                        if(n==2){
                            newList.add(tile);
                        }
                    }
                }
                blackTiles = newList;
            }
        }

    Grid grid = new Grid();
    final int TOTAL_MOVES = 100;
    final String NORTH_EAST = "ne";
    final String NORTH_WEST = "nw";
    final String SOUTH_EAST = "se";
    final String SOUTH_WEST = "sw";
    final String EAST = "e";
    final String WEST = "w";

    public int countBlackTiles(List<String> input) {
        setupInitial(input);

        return grid.getFlippedCount();
    }

    public int countBlackTilesAfter100Days(List<String> input) {
        setupInitial(input);
        for (int i = 0; i < TOTAL_MOVES; i++) {
            grid.dailyFlip();
            System.out.printf("Day %d: %d%n",i+1,grid.getFlippedCount());
        }
        return grid.getFlippedCount();
    }

    private void setupInitial(List<String> input) {
        for (var line:
                input) {
            Pair<Integer, Integer> tile = Pair.of(0, 0);
            while (line.length() > 0) {
                if (line.startsWith(EAST)) {
                    tile = Pair.of(tile.getLeft() + 1, tile.getRight());
                    line = line.substring(1);
                } else if (line.startsWith(WEST)) {
                    tile = Pair.of(tile.getLeft() - 1, tile.getRight());
                    line = line.substring(1);
                } else if (line.startsWith(NORTH_EAST)) {
                    tile = Pair.of(tile.getLeft(), tile.getRight() + 1);
                    line = line.substring(2);
                } else if (line.startsWith(NORTH_WEST)) {
                    tile = Pair.of(tile.getLeft() - 1, tile.getRight() + 1);
                    line = line.substring(2);
                } else if (line.startsWith(SOUTH_EAST)) {
                    tile = Pair.of(tile.getLeft() + 1, tile.getRight() - 1);
                    line = line.substring(2);
                } else if (line.startsWith(SOUTH_WEST)) {
                    tile = Pair.of(tile.getLeft(), tile.getRight() - 1);
                    line = line.substring(2);
                }
            }

            grid.flip(tile);
        }
    }
}
