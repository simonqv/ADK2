/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;
import java.util.Arrays;

public class ClosestWords {
  LinkedList<String> closestWords = null;
  private int[][] dynMatrix; // Store "previous" matrix
  private String prevW2 = "";

  int closestDistance = -1;

  /**
  * Fills matrix with editing distances.
  * @param w1: misspelled word
  * @param w2: correct word
  * @param w1len: length of w1
  * @param w2len: length of w2
  * @return matrix
  */
  int[][] partDist(String w1, String w2, int w1len, int w2len) {

    int startRow = compareWord(w2, prevW2);

    /*
    [
    [0      1       2       ...       w2len ]
    [1                                      ]
    [2                                      ]
    [.                                      ]
    [.                                      ]
    [.                                      ]
    [w1len                                  ]
    ]
    First row and column are filled 0 to w1len (including) and 0 to w2len (including).
    */

    for (int i = startRow; i <= w2len; i++) {
      for (int j = 0; j <= w1len; j++){
        if (i == 0){
          dynMatrix[i][j] = j;
        }
        if (j == 0){
          dynMatrix[i][j] = i;
        }
        if (i != 0 && j != 0) {
          dynMatrix[i][j] = Math.min(switchCheck(dynMatrix, i, j, w1, w2), Math.min(dynMatrix[i][j-1] + 1, dynMatrix[i-1][j] + 1));
        }
      }
    }
    prevW2 = w2;
    return dynMatrix;
  }

  /**
  * Checks if switch is necessary by comparing characters
  * @return editing distance (+1 if if chars not same)
  */
  int switchCheck(int[][] matrix, int i, int j, String w1, String w2){
    int res = matrix[i-1][j-1];
    if (w1.charAt(j-1) != w2.charAt(i-1)){
      res++;
    }
    return res;
  }

  /**
  * @return editing distance
  */
  int distance(String w1, String w2) {
    // Controlls if it is worth trying for smaller editing distance
    if (closestDistance != -1 && closestDistance - Math.abs(w1.length() - w2.length()) < 0) {
      return closestDistance + 1;
    }
    return partDist(w1, w2, w1.length(), w2.length())[w2.length()][w1.length()];
  }

  /**
  * Checks how much two words overlap
  * @param word1
  * @param word2
  * @return number of overlapping characters
  */
  int compareWord(String word1, String word2) {
    int overlappingChars = 0;
    int len = Math.min(word1.length(), word2.length());
    for(int i = 0; i < len; i++) {
      if (word1.charAt(i) != word2.charAt(i)){
        return overlappingChars;
      }
      overlappingChars++;
    }
    return overlappingChars;
  }

  public ClosestWords(String w, List<String> wordList) {
    // Initialize matrix that will contain editing distances later
    dynMatrix = new int[50][w.length() + 1];
    for (String s : wordList) {
      int dist = distance(w, s);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}
