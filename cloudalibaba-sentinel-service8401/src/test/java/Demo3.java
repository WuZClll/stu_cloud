/**
 * @author ZC_Wu 汐
 * @date 2024/9/11 20:22:32
 * @description 09_11 实验三 用if实现对三个数值从小到大排序
 */
public class Demo3 {
    public static void main(String[] args) {
        int num1 = 2;
        int num2 = 4;
        int num3 = 1;
        int min, mid, max;
        if (num1 <= num2 && num1 <= num3) {
            min = num1;
            if (num2 <= num3) {
                mid = num2;
                max = num3;
            } else {
                mid = num3;
                max = num2;
            }
        } else if (num2 <= num1 && num2 <= num3) {
            min = num2;
            if (num1 <= num3) {
                mid = num1;
                max = num3;
            } else {
                mid = num3;
                max = num1;
            }
        } else {
            min = num3;
            if (num1 <= num2) {
                mid = num1;
                max = num2;
            } else {
                mid = num2;
                max = num1;
            }
        }
        System.out.println("从小到大的排列为: " + min + ", " + mid + ", " + max);
    }
}
