import java.util.*;
import java.io.*;


class Node {
	
	int heigth=8;
	int width=8;
	int board[][]=new int[heigth][width];
	int n_children=8;
	int value;
	Node children[]=new Node[width];
	Node() {
		value=5;
	}


	public void EmptyBoard(){
		
		for(int i=0;i<heigth;i++) {
			for(int j=0;j<width;j++) {
				board[i][j]=0;
				
			}
		}
	}
	

	void printBoard(int board[][]) {
		int p=1;
		for(int i=heigth; i>=0; i--){
			if(i==heigth){
				System.out.print("   ");
				for(int x=1; x<=width; x++){
					System.out.print(x+" ");
				}
				System.out.print("\n  -");
				for(int x=1; x<=width;x++)
				{
					System.out.print("--");
				}
				System.out.print("\n");
			}
			else{
				System.out.print(p+" |");
				p++;
				for(int j=0; j<width; j++){
					if(board[i][j]==0) System.out.print(" |");
					else if(board[i][j]==1){
						
						System.out.print("X");
						
						
						System.out.print("|");
					}
					else{
						
						System.out.print("O");
						
						System.out.print("|");
					}
				}
				System.out.print("\n  -");
				for(int x=1; x<=width;x++)
				{
					System.out.print("--");
					}
				System.out.print("\n");
				}
			}
		}
	
	public int ColumnBoardIsFull(int board[][],int pos){
		if(pos==0) {
			return 0;
		}
		else {
			if(board[heigth-1][pos-1]!=0 )
			return 1;
		else
			return 0;
		}
		
	}
	
	void copyBoard(int boardparent[][],int boardchild[][]){
	for(int  i=0;i<heigth;i++) {
		for(int j=0;j<width;j++){
			boardchild[i][j]=boardparent[i][j];
			}
		}
	}
	public int FullBoard(int board[][]){
	    int plena=0;
	    
	    for(int i=1;i<=width;i++){
	            if(board[heigth-1][width-i]!=0){
	            	 plena++;
	            }
	            else{
	            	plena--;
	            	}
	    }
	    if (plena==width)
	        return 1;
	    else
	        return 0;
	}
	
	int applyThrow(int board[][], int columna,int jugador) {
		int i=-1, aux;
		do{	
			i++;
			aux=board[i][columna-1];
			}while(aux!=0);
		board[i][columna-1]=jugador;
		return i;
	    	
	}
	
	public int insert(int board[][], int columna, int jugador,int fullmatrix){
	    int i=-1, aux;
	    if(columna==0 || fullmatrix==1) {
	    	return 0;
	    }
	    else {
	    	
	    	do
			{	
				i++;
				aux=board[i][columna-1];
			}while(aux!=0);

				board[i][columna-1]=jugador;

			return i;
	    }
	  
	}
	
	int numOfChildren(int[][] board) {
		int nchildren=0;
		for( int a=0;a<width;a++) {
			
			if(ColumnBoardIsFull(board,a+1)==0) {
					
				nchildren++;
				}
			}
	return nchildren;
}

	public int heuristic(Node childNode,int columna,int fila,int jugador){
		int valor;
		//si gana el ordenador muy alto
		//si gana el jugador muy bajo
		valor=childNode.comprova(childNode,columna,fila,jugador);
		if(valor==1) {
			Random r1=new Random();
			return -1*(1+r1.nextInt(width*heigth*10));
		}
		else {
			Random r1=new Random();
			return (1+r1.nextInt(width*heigth*10));
		}
		
	}
	
	public int TransformChild2Column(int board[][],int numChild){
		int nchildren=0;
			for( int a=0;a<width;a++) {
			
				if(ColumnBoardIsFull(board,a+1)==0) {
					if(nchildren==numChild) {
						return a;
						}
					nchildren++;
				
				}
			}
			return 0;
		
	}
Node createNode(Node parent,int numChild, int level,int jugador) {
	int fila;
	int columna=0;
	Node p=new Node();
	copyBoard(parent.board,p.board);
	columna=TransformChild2Column(p.board,numChild);
	fila=applyThrow(p.board,columna+1,jugador);   
	if (level<2){
	p.n_children=numOfChildren(p.board);  
	}
	else {
	p.n_children=0;
	p.value=heuristic(p,columna+1,fila,jugador);
	}
	return p;
	}

	void createChildren(Node parent,int level,int jugador){
	int i;
	for (i=0;i<parent.n_children;i++) {
		parent.children[i]=createNode(parent,i,level,jugador);
		}
	}

	//We consider root node already created and n_children already set.
		void createTree(Node root){
		int i;
		int jugador=2;
		root.n_children=numOfChildren(root.board);
		createChildren(root,1,jugador);   //1st generation
		for(i=0;i<root.n_children;i++) {       //creates the 2nd generation
			createChildren(root.children[i],2,1);
			}
		
		}
		int minimax(Node root){
			int i;
			int j;
			int minimo;
			int maximo;
			int columna=0;
			System.out.println("Ahora valores del segundo nivel");
			for(i=0;i<root.n_children;i++) {//creates the 2nd generation
				if(root.children[i].n_children!=0) {
					minimo=root.children[i].children[0].value;
					for(j=0;j<root.children[i].n_children;j++) {
						System.out.print("  "+root.children[i].children[j].value);
					if (minimo > root.children[i].children[j].value)
						minimo = root.children[i].children[j].value;
					}
					root.children[i].value=minimo;	
				}
				else {
					root.children[i].value=5;
				}
				
			}
			maximo=root.children[0].value;
			System.out.println("\nAhora valores del primer nivel");
			for(i=0;i<root.n_children;i++) {
				System.out.println("  "+root.children[i].value);
				if (maximo < root.children[i].value) {
					maximo = root.children[i].value;
					columna=i;
				}
			
			}
			columna=TransformChild2Column(root.board,columna);
			
			return columna;
		}
////////////////////////////////////////////////////////////
//3 functions to display the content of the tree, iteratively.
////////////////////////////////////////////////////////////
	void displayNode(Node n, int level) {
	int i;
	for(i=0;i<level;i++)
		System.out.printf("  ");
	System.out.printf("%f\n",n.value);
}

	void displayLevel(Node parent, int level) {
	int i;
	for (i=0;i<parent.n_children;i++) {
		displayNode(parent.children[i],level);
	}
}

	void displayTree(Node root) {
	int i;
	displayLevel(root,1);   //1st generation
	for(i=0;i<root.n_children;i++) {       //creates the 2nd generation
		displayLevel(root.children[i],2);
	}
}

////////////////////////////////////////////////////////////
//1 function to display the content of the tree recursively.
////////////////////////////////////////////////////////////
	void displayTreeRecursive(Node n,int level) {
	int i;
	//if (n.n_children==0)    //the way to know if we are now on a leaf node.
	
	displayNode(n, level);	//First, display the node. Then, display its children.
	
	if (n.n_children>0) {  //no need of this line
		for(i=0;i<n.n_children;i++){
			displayTreeRecursive(n.children[i],level+1);
		}
	}
}
	public int ComprovaHoritzontal(int board[][],int fila, int jugador){
        int comptador=0, i=0;
       
    	do{
    		if(board[fila][i]==jugador){
    			comptador++;
    		}
    		else{
    			comptador=0;
    		}
    		i++;
    	}while((i<width)&&(comptador<4));

    	return comptador;
    }

  public  int comprovaVertical(int board[][],int columna, int jugador){
	   int comptador=0 , i=0;
	   if(columna==0) {
		   
		   return comptador;
		   
	   }
	   else {
		   do{
			   if(board[i][columna-1]==jugador){
				   comptador++;
	    	}
	    	else{
	    	comptador=0;
	    	}
	    	i++;
	    	}while((i<heigth)&&(comptador<4));
	    	return comptador;
	   }
	 
    }
    public int comprovaDiagonal( int board[][],int columna, int fila, int jugador){
    	int comptador=0, x=columna-1, y=fila;
    	
    	do{
    		
    		comptador++;
    		x++;
    		y++;
    	}while(  (x<width) && (y<heigth)&&(board[y][x]==jugador) && (comptador<4));
    	
    	if (comptador<4){
    		x=columna-1;
    		y=fila;
    		comptador=0;
    		while(( (x>=0) && (y>=0) && board[y][x]==jugador) &&(comptador<4)) {
    			comptador++;
    				y--;
    				x--;	
    			}
    	}
    	if(comptador<4){
    		comptador=0;
    		x=columna-1;
    		y=fila;
    		do{
    			comptador++;
    			x++;
    			y--;
    		}while(  (x<width) && (y>=0)&&(board[y][x]==jugador) && (comptador<4));
    	}if (comptador<4)
    	{
    			comptador=0;
    			x=columna-1;
    			y=fila;
    			do{
    			comptador++;
    			x--;
    			y++;
    			}while(( (x>=0) && (y<heigth)&&board[y][x]==jugador) && (comptador<4));
    	} return comptador;
    }



public int comprova(Node root,int columna,int fila,int jugador){
    int comptador1=0;
	int comptador2=0;
	int comptador3=0;
	
    comptador1=ComprovaHoritzontal(root.board,fila, jugador);
    comptador2=comprovaVertical(root.board,	columna, jugador);
    comptador3=comprovaDiagonal(root.board, columna, fila, jugador);

    if((comptador1==4) ||(comptador2==4) || (comptador3==4)){
     return 1;
    }
 	else {
    return 0;
    }
    }
		
}

public class C4final {
		
	public static void main(String[] args) {
		char proseguir; 
		Node firstnode=new Node();
	
		
		int columna,fila,fullcolumn,fullboard;
		//Node.funcionestatica

		do{
			proseguir='x';
			int guanyat=0;
			fullboard=0;
			int jugador=1;
			//System.out.println("Todo ok");
			firstnode.EmptyBoard();
			do
			{
				do
				{
					do
					{
						System.out.println("\nConnecta 4!\n\n");
						firstnode.printBoard(firstnode.board);
						System.out.print(" Torn del jugador "+jugador+" \n\n");
						System.out.print(" Introdueix la columna:\n\n");
						System.out.print(" Si vols sortir, pulsa 0.\n\n");
						columna=0;
						
						if(jugador==1) {
						Scanner sc = new Scanner(System.in);
					    columna = sc.nextInt();
					    System.out.println("La columna es "+columna);
					    //sc.close();	
						}
						else {
							//Random r=new Random();
							
							//columna=1+r.nextInt(firstnode.width);
							firstnode.createTree(firstnode);
							columna=firstnode.minimax(firstnode)+1;
							System.out.println("\nEl valor de columna de la IA es "+(columna));
						}
						
					    
						if(columna<=0 || columna>firstnode.width){
			                System.out.println("\nLa columna introduida no és correcte.\n\n");
			               
			                try {
							      Thread.sleep(2000);
							} catch(Exception e){}
		                }
					}while((columna<0)||(columna>firstnode.width));
					fullcolumn=firstnode.ColumnBoardIsFull(firstnode.board,columna);
					fullboard=firstnode.FullBoard(firstnode.board);
					if(fullcolumn==1){
						System.out.println("Aquesta columna está plena!\nIntrodueix una altra columna.\n");
						try {
						      Thread.sleep(3000);
						} catch(Exception e){}
						
					}
					if(fullboard==1) {
						System.out.println("Full board");
					}

				}while((fullcolumn==1)&&(fullboard==0));
				fila=firstnode.insert(firstnode.board,columna,jugador,fullboard);
				guanyat=firstnode.comprova(firstnode,columna,fila,jugador);
				if(guanyat==1|| fullboard==1){
					do{
					
					if(guanyat==1 && fullboard==0) {
						System.out.println("Connecta 4!\n\n");
						firstnode.printBoard(firstnode.board);
						if(jugador==1) {
							System.out.println("\nEnhorabona Vicenç has guanyat a la IA \n");	
						}
						System.out.println("\nEnhorabona ! El jugador "+jugador+" es el guanyador !\n");
						System.out.println("\nVoleu tornar a jugar? S/N: ");
						Scanner s2 = new Scanner(System.in);
						proseguir = s2.nextLine().charAt(0);
						//s2.close();
						
					}
					else {
						System.out.println("Connecta 4!\n\n");
						firstnode.printBoard(firstnode.board);
						System.out.println("\nHeu empatat \n");
						System.out.println("\nVoleu tornar a jugar? S/N: ");
						Scanner s3 = new Scanner(System.in);
						proseguir = s3.nextLine().charAt(0);
						//s2.close();
					}
					     
		            }while((proseguir!='s') && (proseguir!='S') && (proseguir!='n') && (proseguir!='N'));
			    }
				if(jugador==1){
	                    jugador=2;}
				else{
					
				    jugador=1;}
			}while((columna!=0) && (guanyat==0)&&(fullboard==0));
		  }while((proseguir=='s')||(proseguir=='S'));
		
		System.out.println("Joc finalitzat");

	}

}
