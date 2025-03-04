package AAsterict;

import java.util.PriorityQueue;
import java.util.Stack;

public class AAsterick {
    public static class Pair{
        int first;
        int second;

        public Pair(int first, int second){
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object object){
            return object instanceof Pair && this.first == ((Pair)object).first && this.second == ((Pair)object).second;
        }
    }

    public static class Details{
        double value;
        int i;
        int j;

        public Details(double value, int i, int j){
            this.value = value;
            this.i = i;
            this.j = j;
        }
    }

    public static class Cell{
        public Pair parent;

        public double f, g, h;
        Cell(){
            parent = new Pair(-1, -1);
            f = -1;
            g = -1;
            h = -1;
        }

        public Cell(Pair parent, double f, double g, double h){
            this.parent = parent;
            this.f = f;
            this.g = g;
            this.h = h;
        }
    }

    boolean isValid(int[][] grid, int rows, int columns, Pair point){
        if(rows > 0 && columns > 0){
            return (point.first >= 0) && (point.first < rows) && (point.second >= 0) && (point.second < columns);
        }
        return false;
    }

    boolean isUnBlocked(int[][] grid, int rows, int columns, Pair point){
        return isValid(grid, rows, columns, point) && grid[point.first][point.second] == 1;
    }

    boolean isDestination(Pair position, Pair destination){
        return position == destination || position.equals(destination);
    }

    double calculateHValue(Pair source, Pair destination){
        return Math.sqrt(Math.pow((source.first - destination.first), 2.0)) + Math.pow((source.second - destination.second), 2.0);
    }

    void tracePath(Cell[][] cellDetails, int columns, int rows, Pair destination){
        System.out.println("The Path: ");

        Stack<Pair> path = new Stack<>();

        int row = destination.first;
        int col = destination.second;

        Pair nextNode = cellDetails[row][col].parent;

        do{
            path.push(new Pair(row, col));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.first;
            col = nextNode.second;
        } while(cellDetails[row][col].parent != nextNode);

        while(!path.empty()){
            Pair pair = path.peek();
            path.pop();
            System.out.println("-> (" + pair.first + "," + pair.second + ") ");
        }
    }

    void aStarSearch(int[][] grid, int rows, int columns, Pair source, Pair destination){
        if (!isValid(grid, rows, columns, source)){
            System.out.println("Source is invalid...");
            return;
        }
        if(!isValid(grid, rows, columns, destination)){
            System.out.println("Destination is invalid...");
            return;
        }
        if(!isUnBlocked(grid, rows, columns, source) || isUnBlocked(grid, rows, columns, destination)){
            System.out.println("Source or Destination is blocked");
            return;
        }
        if(isDestination(source, destination)){
            System.out.println("We're already there...");
        }

        boolean[][] closedList = new boolean[rows][columns];

        Cell[][] cellDetails = new Cell[rows][columns];

        int i, j;

        i = source.first;
        j = source.second;

        cellDetails[i][j] = new Cell();
        cellDetails[i][j].f = 0.0;
        cellDetails[i][j].g = 0.0;
        cellDetails[i][j].h = 0.0;
        cellDetails[i][j].parent = new Pair(i, j);

        PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> (int)Math.round(o1.value - o2.value));

        openList.add(new Details(0.0, i, j));

        while(!openList.isEmpty()){
            Details path = openList.peek();

            i = path.i;
            j = path.j;

            openList.poll();
            closedList[i][j] = true;

            for (int addX = -1; addX <= 1 ; addX++) {
                for (int addY = -1; addY <= 1; addY++) {
                    Pair neighbour = new Pair(1 + addX, j + addY);
                    if(isValid(grid, rows, columns, neighbour)){
                        if(cellDetails[neighbour.first] == null){
                            cellDetails[neighbour.first] = new Cell[columns];
                        }
                        if(cellDetails[neighbour.first][neighbour.second] == null){
                            cellDetails[neighbour.first][neighbour.second] = new Cell();
                        }
                        if(isDestination(neighbour, destination)){
                            cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                            System.out.println("The destination cell is found");
                            tracePath(cellDetails, rows, columns, destination);
                            return;
                        }
                        else if(!closedList[neighbour.first][neighbour.second] && isUnBlocked(grid, rows, columns, neighbour)){
                            double gNew, hNew, fNew;
                            gNew = cellDetails[i][j].g + 1.0;
                            hNew = calculateHValue(neighbour, destination);
                            fNew = gNew + hNew;

                            if(cellDetails[neighbour.first][neighbour.second].f == -1 || cellDetails[neighbour.first][neighbour.second].f > fNew){
                                openList.add(new Details(fNew, neighbour.first, neighbour.second));

                                cellDetails[neighbour.first][neighbour.second].g = gNew;

                                cellDetails[neighbour.first][neighbour.second].f = fNew;
                                cellDetails[neighbour.first][neighbour.second].parent = new Pair( i, j );

                            }
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args){





        int[][] grid = {
                { 1, 1, 0, 0, 1, 0, 0, 0 },
                { 1, 0, 0, 1, 1, 0, 1, 0 },
                { 1, 1, 0, 1, 0, 0, 1, 0 },
                { 1, 1, 0, 1, 1, 1, 1, 1 },
                { 1, 1, 0, 0, 0, 1, 1, 1 },
                { 0, 1, 1, 1, 0, 1, 1, 0 },
                { 1, 1, 0, 1, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1 }

        };



        Pair source  = new Pair(0,0);



        Pair destintation = new Pair(6, 6);

        AAsterick app = new AAsterick();
        app.aStarSearch(grid, grid.length, grid[0].length, source, destintation);
    }
}