/**
 * Map.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.lang.*;
import java.util.*;
import java.awt.*;

import util.Direction;

/**
 * @author giginet
 *
 */
public class Map{
  
  private HashMap<Point, Chip> map = null;
  private Point chara = null;
  private Set<Point> loads = null; 
  private int width = 0;
  private int height = 0;
  
  /**
   * コンストラクタ。大きさ0の空のマップを生成します
   */
  public Map(){
    map = new HashMap<Point, Chip>();
  }
  
  /**
   * コンストラクタ。渡されたHashMapを元に、マップを初期化します
   * @param map 座標をキー、 値をchipに持つHashMapを渡します
   * @param start キャラクターの初期位置を表す座標を渡します
   * @param loads 荷物の初期位置を表すSetを渡します
   * @exception 渡されたマップのサイズが不正なとき、IllegalArgumentExceptionを返します
   */
  public Map(HashMap<Point, Chip> map, Point start, Set<Point> loads) throws IllegalArgumentException{
    this.map = map;
    this.chara = start;
    this.loads = loads;
    int goalCount = 0;
    int max = 0;
    if(map.size() == 0) throw new IllegalArgumentException("渡されたマップが空です");
    // マップの横幅を算出する
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      if(p.x > max) max = p.x;
      Chip chip = map.get(p);
      if(chip.isGoal()) ++ goalCount;
    }
    // ゴールの数と荷物の数を比べる
    if(goalCount != loads.size()) throw new IllegalArgumentException("荷物の数とゴールの数が一致しません");
    width = max+1;
    // マップの高さを算出する
    if(map.size()%width != 0) throw new IllegalArgumentException("渡されたマップのサイズが不正です");
    height = map.size()/width;
  }
  
  /**
   * 指定された座標にあるマスを取り出します
   * @param p 取り出したい座標
   * @return その座標にあるマス
   */
  public Chip getChipAt(Point p){
    return this.map.get(p);
  }
  
  /**
   * このマップが探索終了状態に到達しているかどうかを判定します（全ての荷物がゴール地点上に乗っているかをチェックします）
   * @return このマップが探索終了状態かどうか
   */
  public boolean isGoal(){
    Iterator<Point> itr = loads.iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      if(!getChipAt(p).isGoal()) return false;
    }
    return true;
  }
  
  /**
   * ある荷物を指定した方向に動かせるかどうかを返します
   * @param load 動かす荷物のある座標
   * @param d 荷物を動かす方向
   * @return この荷物を動かせるかどうか
   */
  public boolean canMove(Point load, Direction d){
    if(!loads.contains(load)) return false;
    switch(d){
    case Up:
      return canThrough(new Point(load.x, load.y-1));
    case Right:
      return canThrough(new Point(load.x+1, load.y));
    case Down:
      return canThrough(new Point(load.x, load.y+1));
    case Left:
      return canThrough(new Point(load.x-1, load.y));
    }
    return false;
  }
  
  /**
   * ある荷物を指定した方向に動かしたときの状態を返します。動かせない場合は自分自身を返します
   * @param load 動かす荷物のある座標
   * @param d 荷物を動かす方向
   * @return
   */
  public Map moveLoad(Point load, Direction d){
    if(!canMove(load, d)) return this;
    return this;
  }
  
  /**
   * 2点間のマンハッタン距離を返します
   * @param p1 始点
   * @param p2 終点
   * @return マンハッタン距離
   */
  static public int manhattanDistance(Point p1, Point p2){
    return Math.abs(p2.x-p1.x) + Math.abs(p2.y-p1.y);
  }
  
  /**
   * 渡されたマップを表す文字列からマップを生成します
   * @param str マップを表す文字列を渡します。各記号は以下の意味を持ちます<br>
   * 各行は\nで区切られている必要があります<br>
   * <table>
   * <td><th>文字</th><th>生成されるもの</th></td>
   * <td><tr>.</tr><tr>床が生成されます</tr></td>
   * <td><tr>#</tr><tr>壁が生成されます</tr></td>
   * <td><tr>*</tr><tr>荷物が生成されます</tr></td>
   * <td><tr>@</tr><tr>キャラクターが生成されます</tr></td>
   * <td><tr>a</tr><tr>最終到達点とキャラクターが重なっている状態を表します</tr></td>
   * <td><tr>G</tr><tr>荷物の最終到達点が生成されます</tr></td>
   * <td><tr>g</tr><tr>最終到達点と荷物が重なっている状態を表します</tr></td>
   * </table>
   * @return 新しく生成されたマップ
   */
  static public Map parse(String str){
    return new Map();
  }
  
  /**
   * ある座標に荷物やキャラが進入可能かどうかを返します
   * @param p 調べる座標
   * @return 進入可能かどうか
   */
  private boolean canThrough(Point p){
    return 0 <= p.x && p.x < width && 0 <= p.y && p.y < height && getChipAt(p).canThrough();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException{
    HashMap<Point, Chip> newMap = new HashMap<Point, Chip>();
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point key = itr.next();
      newMap.put((Point)key.clone(), (Chip)map.get(key).clone());
    }
    return new Map(newMap, (Point)this.chara.clone(), this.loads);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    Map other = (Map)obj;
    return other.map == map && other.loads == loads && other.chara == chara;
  }
}
