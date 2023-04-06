import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : test  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/23  18:10
 */

import java.util.Arrays;
import java.util.Comparator;
class test {
    public static void main(String[] args) {
        int[][] a={{0,1,5},{0, 2, 6}, {0, 3, 7}, {1, 1, 4}, {1, 2, 7}, {2, 1, 5}};
        MovieRentingSystem movieRentingSystem = new MovieRentingSystem(3,a);
        movieRentingSystem.search(1); // 返回 [1, 0, 2] ，商店 1，0 和 2 有未借出的 ID 为 1 的电影。商店 1 最便宜，商店 0 和 2 价格相同，所以按商店编号排序。
        movieRentingSystem.rent(0, 1); // 从商店 0 借出电影 1 。现在商店 0 未借出电影编号为 [2,3] 。
        movieRentingSystem.rent(1, 2); // 从商店 1 借出电影 2 。现在商店 1 未借出的电影编号为 [1] 。
        movieRentingSystem.report(); // 返回 [[0, 1], [1, 2]] 。商店 0 借出的电影 1 最便宜，然后是商店 1 借出的电影 2 。
        movieRentingSystem.drop(1, 2); // 在商店 1 返还电影 2 。现在商店 1 未借出的电影编号为 [1,2] 。
        movieRentingSystem.search(2); // 返回 [0, 1] 。商店 0 和 1 有未借出的 ID 为 2 的电影。商店 0 最便宜，然后是商店 1 。
    }


//leetcode submit region begin(Prohibit modification and deletion)
static class MovieRentingSystem {
    public int[][] entries,temps=new int[100][3];
    public int n=0;
    public MovieRentingSystem(int n, int[][] entries) {
        Arrays.sort(entries, Comparator.comparingInt(o -> o[0]));
        this.entries=entries;
    }

    public List<Integer> search(int movie) {
        List<Integer> result= new ArrayList<>();
        Arrays.sort(entries, Comparator.comparingInt(o -> o[2]));
        for (int[] entry : entries) {
            if (entry[1] == movie) {
                result.add(entry[0]);
            }
        }
        return result;
    }

    public void rent(int shop, int movie) {
        Arrays.sort(entries, Comparator.comparingInt(a -> a[2]));
        for (int[] entry : entries) {
            if (entry[0] == shop && entry[1] == movie) {
                temps[n][0] = entry[0];
                temps[n][1] = entry[1];
                temps[n][2] = entry[2];
                n++;
            }
        }
    }

    public void drop(int shop, int movie) {
        for (int i = 0; i < n; i++) {
            if (temps[i][0]==shop&& temps[i][1]==movie){
                temps[i][0]= temps[n--][0];
                temps[i][1]= temps[n--][1];
                temps[i][2]= temps[n--][2];
                n--;
            }
        }
    }

    public List<List<Integer>> report() {
        int[][] a=new int[n][3];
        for (int i = 0; i < n; i++) {
            a[i][0]= temps[i][0];
            a[i][1]= temps[i][1];
            a[i][2]= temps[i][2];
        }
        Arrays.sort(a, (b,c)-> b[2]==c[2]?b[0]-c[0]:b[2]-c[2]);
        List<List<Integer>> result =new ArrayList<>();
        for (int[] ints : a) {
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(ints[0]);
            integers.add(ints[1]);
            result.add(integers);
        }
        return result;
    }
}
}
