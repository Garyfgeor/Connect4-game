//package ce326.hw3;
public class MinMaxPruning{

    public MinMaxPruning(){}

    public int[] calcScore(int maxDepth, String player, char[][] board, int rows, int cols, int pos, Board newBoard){
        int temp=0;
        int row, YOUpoints, AIpoints, AI_i=-1, AI_j=-1;

        if(pos >= 0){
            char checker;
            if(player.equals("AI")){
                checker = 'O';
            }
            else{
                checker = 'X';
            }
            row = newBoard.setPlayerPosition(rows, cols, pos, checker, board);
            if(row >= 0){
                YOUpoints = newBoard.calcPoints("YOU", rows, cols, board, newBoard);
                AIpoints = newBoard.calcPoints("AI", rows, cols, board, newBoard);
                YOUpoints = YOUpoints*(-1);

                if(player.equals("AI")){//AI plays
                    if(AIpoints >= 10000){
                        int res[] = {10000, row, pos};
                        return res;
                    }
                    else{
                        temp = AIpoints+YOUpoints;
                        AI_i = row;
                        AI_j = pos;
                    }
                }
                else{//YOU plays
                    if(YOUpoints <= -10000){
                        int res[] = {-10000, row, pos};
                        return res;
                    }
                    else{
                        temp = AIpoints+YOUpoints;
                        AI_i = row;
                        AI_j = pos;
                    }
                } 
            }
        }
        int [] res = {temp, AI_i, AI_j};
        return res;
    }

    public int[] MinMaxPruning_help(int res[], int depth, int maxDepth, String player,  int alpha, int beta, char[][] board, int rows, int cols, Board newBoard){
        int min = -Integer.MAX_VALUE;
        int max = Integer.MAX_VALUE;

        for(int j=0; j<cols; j++){
            int[] newres = calcScore(maxDepth, player, board, rows, cols, j, newBoard);
            
            if(newres[1] != -1){
                board[newres[1]][newres[2]] = ' ';
            }
            if(newres[0] == 10000 || newres[0] == -10000){
                return newres;
            }
        }

        if(depth == maxDepth){//reached the requested depth so return
            return res;
        }
        
        if(player.equals("AI") == true){//AI plays
            int[] bestValue = {min, 0, 0};
            for(int j=0; j<cols; j++){
                res = calcScore(maxDepth, "AI", board, rows, cols, j, newBoard);
                
                if(res[1] != -1){
                    int[] valuesYOU = MinMaxPruning_help(res, depth+1, maxDepth, "YOU", alpha, beta, board, rows, cols, newBoard);
                    
                    board[res[1]][res[2]] = ' ';
                    if(valuesYOU[1] != -1){
                        board[valuesYOU[1]][valuesYOU[2]] = ' ';
                    }
                    
                    if(valuesYOU[0] > bestValue[0]){
                        bestValue[0] = valuesYOU[0];
                        bestValue[1] = res[1];
                        bestValue[2] = j;
                    }
                    alpha = Math.max(alpha, bestValue[0]);
                    if (alpha >= beta){
                        break;
                    }
                    
                }
            }
            return bestValue;
        }
        else{//YOU plays
            int[] bestValue = {max, 0, 0};
            for(int j=0; j<cols; j++){
                res = calcScore(maxDepth, "YOU", board, rows, cols, j, newBoard);
                
                if(res[1]!= -1){
                    int[] valuesAI = MinMaxPruning_help(res, depth+1, maxDepth, "AI", alpha, beta, board, rows, cols, newBoard);
                    
                    board[res[1]][res[2]] = ' ';
                    if(valuesAI[1] != -1){
                        board[valuesAI[1]][valuesAI[2]] = ' ';
                    }
    
                    if(valuesAI[0] < bestValue[0]){
                        bestValue[0] = valuesAI[0];
                        bestValue[1] = res[1];
                        bestValue[2] = j;
                     }
    
                    beta = Math.min(beta, bestValue[0]);
                    if (alpha >= beta){
                        break;
                    }
                }
            }
            return bestValue;
        }

    }
}
