import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc20.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        aoc.parseInput(input);
        //Part 1
        System.out.println(aoc.findCornerSum());
        //Part 2
        System.out.println(aoc.countSeaBits());
    }
}

class AOC {
    final int EDGE_TOP = 0;
    final int EDGE_RIGHT = 1;
    final int EDGE_BOTTOM = 2;
    final int EDGE_LEFT = 3;


    static class Tile {

        private String tileId;
        List<List<Boolean>> bits = new ArrayList<>();

        public List<List<Boolean>> getBits() {
            return bits;
        }

        public void setBits(List<List<Boolean>> bits) {
            this.bits = bits;
        }

        public String getTileId() {
            return tileId;
        }

        public void setTileId(String tileId) {
            this.tileId = tileId;
        }

        public Edge[] getEdges() {
            int top = 0, bottom = 0, left = 0, right = 0;
            for (int i = 0; i < bits.size(); i++) {
                if (bits.get(0).get(i)) {
                    top |= 1 << i;
                }
                if (bits.get(bits.size() - 1).get(i)) {
                    bottom |= 1 << i;
                }

                if (bits.get(i).get(0)) {
                    left |= 1 << i;
                }
                if (bits.get(i).get(bits.size() - 1)) {
                    right |= 1 << i;
                }
            }
            return new Edge[]{new Edge(top), new Edge(right), new Edge(bottom), new Edge(left)};
        }

        public Tile crop() {
            Tile croppedTile= new Tile();
            List<List<Boolean>> cropped = new ArrayList<>();

            int n = bits.size();
            for (int i = 1; i < n-1; i++) {
                var line =new ArrayList<>(bits.get(i));
                line.remove(n - 1);
                line.remove(0);
                cropped.add(line);
            }

            croppedTile.setBits(cropped);
            croppedTile.setTileId(this.tileId);
            return croppedTile;
        }

        public Tile rotateClockWise(){
            Tile rotated = new Tile();
            List<List<Boolean>> rotatedBits = new ArrayList<>();
            int n=bits.size()-1;
            for (int r = 0; r < bits.size(); r++) {
                var line= new ArrayList<>(bits.get(r));
                for (int c = 0; c < bits.size() ; c++) {
                    line.set(c,bits.get(n-c).get(r));
                }
                rotatedBits.add(line);
            }
            rotated.setTileId(tileId);
            rotated.setBits(rotatedBits);

            return rotated;
        }

        public Tile flipHorizontal(){
            Tile flipped = new Tile();
            List<List<Boolean>> flippedBits = new ArrayList<>(bits);
            int n=bits.size()-1;
            for (int r = 0; r < bits.size(); r++) {
                flippedBits.set(n-r,new ArrayList<>(bits.get(r)));
            }
            flipped.setTileId(tileId);
            flipped.setBits(flippedBits);

            return flipped;
        }

        public void print(){
            System.out.printf("Tile : %s%n",tileId);
            for (var l:
                    bits) {
                System.out.println(Arrays.toString(l.stream().map(x -> x?"#":".").toArray()) );
            }
        }

        public int countBits(){
            int result=0;
            for (var r:
                 this.bits) {
                result+=r.stream().mapToInt(b -> b?1:0).sum();
            }
            return result;
        }

        @Override
        public String toString() {
            return "{" +
                    "tileId='" + tileId + '\'' +
                    '}';
        }
    }

    static class Edge {
        private int checkSum;

        public Edge(int checkSum) {
            this.checkSum = checkSum;
        }

        public int getCheckSum() {
            return checkSum;
        }

        public void setCheckSum(int checkSum) {
            this.checkSum = checkSum;
        }

        @Override
        public String toString() {
            return String.valueOf(checkSum);
        }

        public Edge flip() {
            return new Edge(reverseIntBitwise(checkSum));
        }

        public int reverseIntBitwise(int value) {
            int i = 0, rev = 0, bit;
            while (i++ < 10) {
                bit = (value & 1);
                value = value >> 1;
                rev = rev ^ bit;
                if (i < 10)
                    rev = rev << 1;
            }
            return rev;
        }
    }

    class Image {
        private final Tile[][] mosaic;
        private String[][] imageInPlace;
        private final ArrayList<String> seaMonster = new ArrayList<>(
                Arrays.asList("                  # ",
                        "#    ##    ##    ###",
                        " #  #  #  #  #  #   "
                )
        );
        private final int CROP_SIZE= 8;

        public Image() {
            int s = (int) Math.sqrt(AOC.this.tiles.size());
            this.mosaic = new Tile[s][s];
            this.imageInPlace = new String[s*CROP_SIZE][s*CROP_SIZE];
        }

        public void setTile(Tile topLeftCornerTile, int r, int c) {
            this.mosaic[r][c] = topLeftCornerTile;
        }

        public void print() {
            for (Tile[] value : mosaic) {
                System.out.println(Arrays.toString(value));
            }
        }

        public boolean checkForMatch(Tile trial, int row, int col) {
            return checkEdge(trial, row, col, EDGE_TOP)
                    && checkEdge(trial, row, col, EDGE_RIGHT)
                    && checkEdge(trial, row, col, EDGE_BOTTOM)
                    && checkEdge(trial, row, col, EDGE_LEFT);
        }

        public boolean checkTilesSet() {
            for (Tile[] tile : mosaic) {
                if (tile == null) {
                    return false;
                }

                for (Tile t :
                        tile) {
                    if (t == null) {
                        return false;
                    }
                }
            }
            return true;
        }

        public void findMatchingTiles(List<Tile> corners, HashMap<Integer, List<String>> allEdges) {
            Tile topLeftCornerTile = getTopLeftCornerTile(corners, allEdges);
            setTile(topLeftCornerTile, 0, 0);
            HashSet<String> usedTiles = new HashSet<>();
            assert topLeftCornerTile != null;
            usedTiles.add(topLeftCornerTile.tileId);

            int row = 0, col = 1;
            while (true) {
                if (col >= mosaic.length) {
                    row++;
                    col = 0;
                }

                if (row >= mosaic.length) {
                    break;
                }

                boolean tilePlaced = false;
                for (var t :
                        tiles.entrySet()) {
                    if (usedTiles.contains(t.getKey())) {
                        continue;
                    }
                    Tile currentTile = t.getValue();
                    ArrayList<Tile> combinations = getAllCombinations(currentTile);
                    for (Tile trial :
                            combinations) {
                        if (checkForMatch(trial, row, col)) {
                            setTile(trial, row, col);
                            usedTiles.add(trial.tileId);
                            tilePlaced = true;
                            break;
                        }
                    }
                    if (tilePlaced) {
                        break;
                    }
                }

                col++;
            }
        }

        public int countSeaBits(){
            cropTiles();
            int m = countAllMonsters();
            int total = getTotalSeaBits();
            return total - m * 15;
        }

        private int getTotalSeaBits() {
            int total=0;
            for (String[] strings : imageInPlace) {
                for (int c = 0; c < imageInPlace.length; c++) {
                    if (strings[c].equals("#")) {
                        total++;
                    }
                }
            }

            return total;
        }

        private int countAllMonsters() {
            for (int f = 0; f < 2; f++) {
                for (int r = 0; r < 4; r++) {
                    int m = areThereMonsters();
                    if(m>0){
                        return m;
                    }
                    rotateClockWise();
                }
                flipHorizontally();
            }

            return 0;
        }

        private void rotateClockWise(){
           var newImage = new String[imageInPlace.length][imageInPlace.length];
           int n = imageInPlace.length;
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    newImage[r][c] = imageInPlace[n-c-1][r];
                }
            }

            imageInPlace = newImage;
        }

        public void flipHorizontally(){
            var newImage = new String[imageInPlace.length][imageInPlace.length];
            int n=imageInPlace.length;
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    newImage[r][c] = imageInPlace[r][n-c-1];
                }
            }
            imageInPlace = newImage;
        }

        private int areThereMonsters() {
            int count =0;
            for (int r = 0; r + seaMonster.size() < imageInPlace.length; r++) {
                for (int c = 0; c + seaMonster.get(0).length() < imageInPlace.length; c++) {
                    if(checkForSeaMonster(r, c)){
                        count++;
                    }
                }
            }
            return count;
        }

        private boolean checkForSeaMonster(int r, int c) {
            for (int ro = 0; ro < seaMonster.size(); ro++) {
                for (int co = 0; co < seaMonster.get(0).length(); co++) {
                    if(seaMonster.get(ro).charAt(co)!='#'){
                        continue;
                    }
                    if(!imageInPlace[r +ro][c +co].equals("#")){
                       return false;
                    }
                }
            }

            return true;
        }

        private void cropTiles() {
            for (int r = 0; r < mosaic.length; r++) {
                for (int c = 0; c < mosaic.length; c++) {
                    var b = mosaic[r][c].crop().getBits();
                    for (int ro = 0; ro < b.size(); ro++) {
                        for (int co = 0; co < b.size(); co++) {
                            imageInPlace[r*CROP_SIZE + ro][c*CROP_SIZE + co] = b.get(ro).get(co) ? "#" : ".";
                        }
                    }
                }
            }
        }

        private boolean checkEdge(Tile trial, int row, int col, int side) {
            int r = row, c = col;
            switch (side) {
                case EDGE_TOP:
                    if (row - 1 < 0) {
                        return true;
                    }
                    r = row - 1;
                    break;
                case EDGE_RIGHT:
                    if (col + 1 >= mosaic.length) {
                        return true;
                    }
                    c = col + 1;
                    break;
                case EDGE_BOTTOM:
                    if (row + 1 >= mosaic.length) {
                        return true;
                    }
                    r = row + 1;
                    break;
                case EDGE_LEFT:
                    if (col - 1 < 0) {
                        return true;
                    }
                    c = col - 1;
                    break;
                default:
                    break;
            }

            Tile neighbour = mosaic[r][c];
            if (neighbour == null) {
                return true;
            }
            return neighbour.getEdges()[(side + 2) % 4].getCheckSum() == trial.getEdges()[side].getCheckSum();
        }
    }

    HashMap<String, Tile> tiles = new HashMap<>();

    public void parseInput(List<String> input) {
        String id = "";
        boolean readTile = false;
        Tile tile = new Tile();
        for (String line :
                input) {
            if (!readTile) {
                Matcher m = Pattern.compile("Tile (\\d+):").matcher(line);
                while (m.find()) {
                    id = m.group(1);
                }
                readTile = true;
                tile = new Tile();
                continue;
            }

            if (line.isEmpty()) {
                readTile = false;
                tiles.put(id, tile);
                continue;
            }
            tile.tileId = id;
            List<Boolean> bits = IntStream.range(0, line.length())
                    .mapToObj(b -> line.charAt(b) == '#')
                    .collect(Collectors.toList());
            tile.getBits().add(bits);
        }

        tiles.put(id, tile);

    }

    public long findCornerSum() {
        HashMap<String, Integer> uniqueEdges = findUniqueEdges();

        long result = 1;
        for (var k :
                uniqueEdges.entrySet()) {
            if (k.getValue() > 2 && k.getValue() <= 4) {
                result *= Integer.parseInt(k.getKey());
            }
        }

        return result;
    }

    public HashMap<String, Integer> findUniqueEdges() {
        HashMap<Integer, List<String>> allEdges = getAllEdges();

        HashMap<String, Integer> uniqueEdges = new HashMap<>();

        for (var e :
                allEdges.entrySet()) {
            if (e.getValue().size() == 1) {
                String key = e.getValue().get(0);
                uniqueEdges.put(key, uniqueEdges.getOrDefault(key, 0) + 1);
            }
        }
        return uniqueEdges;
    }

    private HashMap<Integer, List<String>> getAllEdges() {
        HashMap<Integer, List<String>> allEdges = new HashMap<>();
        for (Map.Entry<String, Tile> t :
                tiles.entrySet()) {
            for (Edge edge :
                    t.getValue().getEdges()) {
                allEdges.computeIfAbsent(edge.getCheckSum(), e -> new ArrayList<>()).add(t.getKey());
                allEdges.computeIfAbsent(edge.flip().getCheckSum(), e -> new ArrayList<>()).add(t.getKey());
            }
        }
        return allEdges;
    }

    //Part 2
    public long countSeaBits() {
        Image image = putTilesInPlace();
        return image.countSeaBits();
    }

    private Image putTilesInPlace() {

        HashMap<String, Integer> uniqueEdges = findUniqueEdges();
        List<Tile> corners = new ArrayList<>();

        for (var edge :
                uniqueEdges.entrySet()) {
            if (edge.getValue() > 2 && edge.getValue() <= 4) {
                corners.add(tiles.get(edge.getKey()));
            }
        }

        Image image = new Image();

        HashMap<Integer, List<String>> allEdges = getAllEdges();
        image.findMatchingTiles(corners, allEdges);
        return image;

    }


    private ArrayList<Tile> getAllCombinations(Tile tile) {
        ArrayList<Tile> combinations = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int r = 0; r < 4; r++) {
                tile = tile.rotateClockWise();
                combinations.add(tile);

            }
            tile = tile.flipHorizontal();
        }

        return combinations;
    }

    private Tile getTopLeftCornerTile(List<Tile> corners,HashMap<Integer, List<String>> allEdges){
        Tile trial;
        for (Tile c :
                corners) {
            trial = c;
            for (int r = 0; r < 4; r++) {
                Edge[] edges = c.getEdges();
                if(matchRandB(allEdges,edges)){
                    return trial;
                }else{
                    var flipped = trial.flipHorizontal();
                    edges = flipped.getEdges();
                    if(matchRandB(allEdges,edges)){
                        return flipped;
                    }
                }

                trial=trial.rotateClockWise();
            }
        }

        return null;
    }

    private boolean matchRandB(HashMap<Integer, List<String>> allEdges, Edge[] edges){
        List<String> rEdges = allEdges.get(edges[EDGE_RIGHT].getCheckSum());
        List<String> bEdges = allEdges.get(edges[EDGE_BOTTOM].getCheckSum());

        return rEdges != null && bEdges != null & rEdges.size() >= 2 && bEdges.size() >= 2;
    }
}
