import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;


public class MazeSolver {

	static Queue<MazeCell> queue=new LinkedList<MazeCell>();
	final static String teleporter="0123456789";
	static int[] sourceLocator;
	static int sourceCount=0;
	static int destinationCount=0;
	static boolean invalidFlag=false;
	static int rowLength=0;
	static int columnCharacterCount=0;
	static int columnLength=0;
	final static int maxLength=5000;
	static boolean teleporterFlag=false;

	public static void main(String[] args) throws IOException {

		int task =Integer.parseInt(args[0]);

		char c;
		int read;
		char last = 0;
		char[][]mazeArray=new char[maxLength][maxLength];

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				read= br.read();
				if(read==-1) break;

				c=(char)read;
				last=c;

				if (c=='\n')
				{
					if(columnCharacterCount==columnLength || columnLength==0)
					{
						columnLength=columnCharacterCount;
					}
					else{invalidFlag=true; break;}

					rowLength++;
					columnCharacterCount=0;
				}
				else
				{
					mazeArray[rowLength][columnCharacterCount] = c;
					columnCharacterCount++;
				}
			}
		}

		finally {
			br.close();
		}


		switch(task)
		{
		case 1 :
			if(last !='\n') invalidFlag=true;

			if(!invalidFlag)	
			{
				outerLoop:
					for (int i = 0; i<rowLength; i++){
						for (int j = 0; j<columnLength; j++){
							if(mazeArray[i][j] == 'S')sourceCount++;
							else if(mazeArray[i][j] =='D')destinationCount++;
							else if(mazeArray[i][j] !='#' && mazeArray[i][j] !='.') {invalidFlag = true; break outerLoop;}
						}
					}
			}

			if(destinationCount!=1 ||sourceCount!=1 ||invalidFlag){ System.out.println("NO");}
			else System.out.println("YES");
			break;

		case 2 :
			teleporterFlag=false;
			sourceLocator=getSource(mazeArray);
			queue.add(new MazeCell(sourceLocator[0],sourceLocator[1], null,null));
			MazeCell cell = doBFS(queue,mazeArray);
			if(cell!=null)
			{
				System.out.println("YES");
			}
			else
			{
				System.out.println("NO");
			}
			break;
		case 3 :
			teleporterFlag=false;
			sourceLocator=getSource(mazeArray);
			queue.add(new MazeCell(sourceLocator[0],sourceLocator[1], null,null));
			MazeCell mazeCell = doBFS(queue,mazeArray);
			if(mazeCell!=null)
			{
				printPath(mazeCell);
			}
			else
			{
				System.out.println("NO");
			}
			break;
		case 4 :
			teleporterFlag=true;
			sourceLocator=getSource(mazeArray);
			queue.add(new MazeCell(sourceLocator[0],sourceLocator[1], null,null));

			MazeCell mazeWithTeleporterCell =doBFS(queue,mazeArray);
			if(mazeWithTeleporterCell!=null)
			{
				printPath(mazeWithTeleporterCell);
			}
			else
			{
				System.out.println("NO");
			}
			break;

		default :
			break;
		}
	}


	/*
	 * This is the main function which executes the BFS logic for solving the maze
	 * 
	 * Queue data structure governs the basic workflow for this method.
	 * 
	 * Additional teleporterFlag is added to check if Teleporter logic is to be included for the search.
	 * 
	 */
	public static MazeCell doBFS(Queue<MazeCell> queue,char[][] mazeArray) {

		while(!queue.isEmpty()) {
			MazeCell mazecell = queue.remove();
			if (mazeArray[mazecell.x][mazecell.y] == 'D') {
				return mazecell;
			}
			if(teleporterFlag)
			{

				if(isValidTeleporter(mazecell.x,mazecell.y-1,mazeArray)) {
					mazeArray[mazecell.x][mazecell.y] = 'V';
					int teleporterArray[]=getTeleporterLocation(mazecell.x,mazecell.y-1,mazeArray);		
					queue.add(new MazeCell(teleporterArray[0],teleporterArray[1], mazecell,"L"));
				}
				if(isValidTeleporter(mazecell.x+1,mazecell.y,mazeArray)) {
					mazeArray[mazecell.x][mazecell.y] = 'V';
					int teleporterArray[]=getTeleporterLocation(mazecell.x+1,mazecell.y,mazeArray);	
					queue.add(new MazeCell(teleporterArray[0],teleporterArray[1], mazecell,"D"));
				}
				if(isValidTeleporter(mazecell.x-1,mazecell.y,mazeArray)) {
					mazeArray[mazecell.x][mazecell.y] = 'V';
					int teleporterArray[]=getTeleporterLocation(mazecell.x-1,mazecell.y,mazeArray);	
					queue.add(new MazeCell(teleporterArray[0],teleporterArray[1], mazecell,"U"));
				}
				if(isValidTeleporter(mazecell.x,mazecell.y+1,mazeArray)) {
					mazeArray[mazecell.x][mazecell.y] = 'V';
					int teleporterArray[]=getTeleporterLocation(mazecell.x,mazecell.y+1,mazeArray);	
					queue.add(new MazeCell(teleporterArray[0],teleporterArray[1], mazecell,"R"));
				}
			}
			if(isValidCell(mazecell.x+1,mazecell.y,mazeArray)) {
				mazeArray[mazecell.x][mazecell.y] = 'V';
				queue.add(new MazeCell(mazecell.x+1,mazecell.y, mazecell,"D"));
			}
			if(isValidCell(mazecell.x,mazecell.y+1,mazeArray)) {
				mazeArray[mazecell.x][mazecell.y] = 'V';
				queue.add(new MazeCell(mazecell.x,mazecell.y+1, mazecell,"R"));
			}
			if(isValidCell(mazecell.x,mazecell.y-1,mazeArray)) {
				mazeArray[mazecell.x][mazecell.y] = 'V';
				queue.add(new MazeCell(mazecell.x,mazecell.y-1, mazecell,"L"));
			}
			if(isValidCell(mazecell.x-1,mazecell.y,mazeArray)) {
				mazeArray[mazecell.x][mazecell.y] = 'V';
				queue.add(new MazeCell(mazecell.x-1,mazecell.y, mazecell,"U"));
			}
		}
		return null;
	}


	/*
	 * 
	 * Function to return the position of the Source (starting point) on the maze
	 * 
	 */
	public static int[] getSource(char[][] mazeArray)
	{
		int [] sourceLocator = new int[2];
		outerloop :
			for (int i = 0; i<rowLength; i++)
				for (int j = 0; j<columnLength; j++)
					if(mazeArray[i][j]== 'S') {
						sourceLocator[0]=i;
						sourceLocator[1]=j;
						break outerloop; }

		return sourceLocator;
	}


	/*
	 * 
	 * Function to check if the concerned mazeCell is valid and accessible
	 * 
	 */
	public static boolean isValidCell(int x, int y,char[][] mazeArray) {
		if((x >= 0 && x < rowLength) && (y >= 0 && y < columnLength) && (mazeArray[x][y] == '.' || mazeArray[x][y] == 'D')) {
			return true;
		}
		return false;
	}

	/*
	 * 
	 * Function to check if the concerned mazeCell is a valid Teleporter cell
	 * 
	 */
	public static boolean isValidTeleporter(int x, int y,char[][] mazeArray) {

		if((x >= 0 && x < rowLength) && (y >= 0 && y < columnLength) && (teleporter.contains(String.valueOf(mazeArray[x][y])))) {
			return true;
		}
		return false;
	}


	/*
	 * 
	 * Function which returns other cell location of the teleporter pair for a given teleporter cell.
	 * 
	 */

	public static int[] getTeleporterLocation(int x, int y,char[][] mazeArray) {

		int [] teleporterArray = new int[2];
		outerloop :
			for (int i = 0; i<rowLength; i++)
				for (int j = 0; j<columnLength; j++)
					if(mazeArray[i][j]== mazeArray[x][y] && (i!=x || j!=y)) {
						teleporterArray[0] = i;
						teleporterArray[1] = j;
						break outerloop;
					}
		return teleporterArray;
	}


	/*
	 * 
	 * Method to print the path of the BFS route by backtracking from the desitnation to the source.
	 * 
	 */

	public static void printPath(MazeCell cell)
	{
		StringBuffer path = new StringBuffer();

		while(cell.getParent() != null) {
			path.append(cell);
			cell = cell.getParent();
		}
		System.out.println(path.reverse().toString());

	}


	/*
	 * 
	 * 	Inner class MazeCell, which acts as a Node to be added to the BFS Queue.
	 * 
	 * Elements of MazeCell are :
	 *                           int x - row number of the maze
	 *                           int y - column number of the maze
	 *                           String direction - Direction taken to reach this Node
	 *                           MazeCell parent  - Parent Node to this Node
	 * 
	 */

	private static class MazeCell {
		int x;
		int y;
		MazeCell parent;
		String direction;

		public MazeCell(int x, int y, MazeCell parent,String direction) {
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.direction = direction;
		}

		public MazeCell getParent() {
			return this.parent;
		}
		public String toString() {
			return direction;
		}
	}
}

