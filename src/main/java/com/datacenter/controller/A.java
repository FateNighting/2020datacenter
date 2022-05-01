package com.datacenter.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class affair {
    //美团第三道：
    //System.out.println(Integer.MAX_VALUE);//20亿
    //第一行输入n，m1,m2,n是文件总数，m1/m2是游戏本/老爷机的文件改了多少次
    //文件编号1-n
    //m1:345678
    //m2:1235789
        /*10 2 3--n m1 m2
        3 5--m1修改中的左端点
        4 8--m1修改中的右端点
        1 5 7--m2修改中的左端点
        3 5 9--m2修改中的右端点
        */
    public static void main(String[] args) {

        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        int m1=sc.nextInt();
        int m2=sc.nextInt();

        int[]numsM1=new int[m1];
        int[]numsM1Sec=new int[m1];
        int[]numsM2=new int[m2];
        int[]numsM2Sec=new int[m2];
        //输入
        for (int i = 0; i < m1; i++) {
            numsM1[i]=sc.nextInt();
        }
        for (int i = 0; i < m1; i++) {
            numsM1Sec[i]=sc.nextInt();
        }
        for (int i = 0; i <m2 ; i++) {
            numsM2[i]=sc.nextInt();
        }
        for (int i = 0; i <m2 ; i++) {
            numsM2Sec[i]=sc.nextInt();
        }
        //获取集合
        Set<Integer> setM1=new HashSet<>();
        Set<Integer> setM2=new HashSet<>();
        for (int i = 0; i < m1; i++) {
            int left=numsM1[i],right=numsM1Sec[i];
            if(left>right){
                break;
            }

            while(left<=right)
            {
                setM1.add(left);
                left++;
            }
        }
        for (int i = 0; i < m2; i++) {
            int left=numsM2[i],right=numsM2Sec[i];
            if(left>right){
                break;
            }
            while(left<=right)
            {
                setM2.add(left);
                left++;
            }
        }
        //取交集
        int cnt=0;
        for(int num1:setM1)
        {
            for(int num2:setM2)
            {
                if(num1==num2){
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }
}