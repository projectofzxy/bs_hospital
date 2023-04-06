package com.oss.test; /**
 * @ClassName : lqb  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/3/8  11:26
 */

import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        Integer n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i]= sc.nextInt();
        }
        Arrays.sort(a);
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }

    }
}
