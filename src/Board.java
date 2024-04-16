//package ce326.hw3;
public class Board{
    
    public Board(){}

    //creates the board of the game
    public char[][] createBoard(int rows, int cols){
        char[][] board = new char[rows][cols];
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                board[i][j] = ' ';
            }
        }
        return board;
    }

    //sets players position in the board if it is allowed
    public int setPlayerPosition(int rows, int cols, int pos, char checker, char[][] board){
        int flag=0;
        int i;
        for(i=rows-1; i>=0; i--){
            for(int j=0; j<cols; j++){
                if(j==pos && board[i][j] == ' '){
                    board[i][j] = checker;
                    flag = 1;
                    break;
                }
            }
            if(flag==1){
                break;
            }
        }
        return i;
    }

    //prints the board of the game
    public void printBoard(int rows, int cols, char[][] board){
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                System.out.print("|");
                System.out.print(board[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
    }

    //checkes if the board is full so we have draw
    public int fullBoard(char[][] board){
        for(int i=0; i<7; i++){
            if(board[0][i] == ' '){
                return 0;
            }
        }
        return 1;
    }

    //calculates the points of a specific player in the board
    public int calcPoints(String player, int rows, int cols, char[][] board, Board newBoard){
        int numOfCheckers=0;
        int OK=0, profit=0;
        char opponent, myChecker;
        if(player.equals("AI")){
            myChecker = 'O';
            opponent = 'X';
        }
        else{
            myChecker = 'X';
            opponent = 'O';
        }

        //horizontally
        for(int i=rows-1; i>=0; i--){
            int start=0;
            int end=4;
            if(profit>=10000){
                break;
            }
            while(end<=cols){
                if(profit>=10000){
                    break;
                }
                for(int j=start; j<end; j++){
                    if(board[i][j] == myChecker){
                        OK=1;
                        numOfCheckers++;
                    }
                    else if(board[i][j] == opponent){
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    if(j==end-1){
                        if(OK==1){
                            if(numOfCheckers==1){
                                profit=profit+1;
                            }
                            else if(numOfCheckers==2){
                                profit=profit+4;
                            }
                            else if(numOfCheckers==3){
                                profit=profit+16;
                            }
                            else if(numOfCheckers==4){
                                profit=profit+10000;
                            }
                        }
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                }
            }
        }

        //vertically
        for(int j=0; j<cols; j++){
            int start=0;
            int end=4;
            if(profit>=10000){
                break;
            }
            while(end<=rows){
                if(profit>=10000){
                    break;
                }
                for(int i=start; i<end; i++){
                    if(board[i][j] == myChecker){
                        OK=1;
                        numOfCheckers++;
                    }
                    else if(board[i][j] == opponent){
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    if(i==end-1){
                        if(OK==1){
                            if(numOfCheckers==1){
                                profit=profit+1;
                            }
                            else if(numOfCheckers==2){
                                profit=profit+4;  
                            }
                            else if(numOfCheckers==3){
                                profit=profit+16;
                            }
                            else if(numOfCheckers==4){
                                profit=profit+10000;
                            }
                        }
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                }
            }
        }

        //upper diagonal
        numOfCheckers=0;
        for(int i=rows-1; i>=0; i--){
            int diag_i=i;
            int diag_j=0;
            if(profit>=10000){
                break;
            }
            while(diag_j<=3){
                if(profit>=10000){
                    break;
                }
                int start=diag_j;
                int end=start+4;
                diag_i=i;
                for(int j=start; j<end; j++){
                    if(board[diag_i][j] == myChecker){
                        OK=1;
                        numOfCheckers++;
                    }
                    else if(board[diag_i][j] == opponent){
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    if(diag_i==0 || j==end-1){
                        if(OK==1){
                            if(numOfCheckers==1){
                                profit=profit+1;
                            }
                            else if(numOfCheckers==2){
                                profit=profit+4;
                            }
                            else if(numOfCheckers==3){
                                profit=profit+16;
                            }
                            else if(numOfCheckers==4){
                                profit=profit+10000;
                            }
                        }
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    diag_i--;
                }
                diag_j++;
            }
        }

        //down diagonally
        numOfCheckers=0;
        for(int i=rows-1; i>=0; i--){
            int diag_i=i;
            int diag_j=3;
            if(profit>=10000){
                break;
            }
            while(diag_j<=6){
                if(profit>=10000){
                    break;
                }
                int start=diag_j;
                int end=start-3;
                diag_i=i;
                for(int j=start; j>=end; j--){
                    if(diag_i<0){
                        break;
                    }
                    if(board[diag_i][j] == myChecker){
                        OK=1;
                        numOfCheckers++;
                    }
                    else if(board[diag_i][j] == opponent){
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    if(j==end){
                        if(OK==1){
                            if(numOfCheckers==1){
                                profit=profit+1;
                            }
                            else if(numOfCheckers==2){ 
                                profit=profit+4;
                            }
                            else if(numOfCheckers==3){
                                profit=profit+16;
                            }
                            else if(numOfCheckers==4){
                                profit=profit+10000;
                            }
                        }
                        OK=0;
                        numOfCheckers=0;
                        start++;
                        end++;
                        break;
                    }
                    diag_i--;
                }
                diag_j++;
            }
        }
        return profit;
    }
}