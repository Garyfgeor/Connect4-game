//package ce326.hw3;
import org.json.JSONObject;
import org.json.JSONArray;

public class MovesDone{
    String player;
    int row;
    int column;
    JSONArray JSONmoves = new JSONArray();
    String JSONstr;

    public MovesDone(){
    }

    public void setPlayer(String player){
        this.player = player;
    }

    public String getPlayer(){
        return this.player;
    }

    public void setMove(int row, int column){
        this.column = column;
        this.row = row;
    }

    public int getRow(){
        return this.row;
    }

    public int getColumn(){
        return this.column;
    }

    //adds the move that played from a player to a json object and then to the json array
    public JSONObject addMoves(MovesDone move){
        JSONObject Jmove = new JSONObject();
        Jmove.put("player", move.getPlayer());
        Jmove.put("row", move.getRow());
        Jmove.put("column", move.getColumn());
        JSONmoves.put(Jmove);
        return Jmove;
    }

    public String JSONtoString(){
        String JSONstr = JSONmoves.toString(2);
        return JSONstr;
    }

    public void clearJSON(String str){
        str=null;
    }
}