
// 27846
import java.util.Arrays;
import java.util.List;

public class Lab1 {
    public static void main(String[] args) {
        int[] w = new int[11];
        for (int i = 5; i <= 25; i += 2) {
            w[(i - 5) / 2] = i;
        }

        double[] x = new double[14];
        for (int i = 0; i < 14; i++) {
            x[i] = Math.random() * 10 - 6;
        }
        Integer[] nums = { 11, 15, 17, 21, 25 };
        List<Integer> list = Arrays.asList(nums);
        double[][] w1 = new double[11][14];

        for (int i = 0; i < 11; i++) {
            if (w[i] == 5) {
                w1 = mas1(w1, i, x);
            } else if (list.contains(w[i])) {
                w1 = mas2(w1, i, x);
            } else {
                w1 = mas3(w1, i, x);
            }

        }


        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 14; j++) {
                System.out.printf("%.5f ", w1[i][j]);
            }
            System.out.println();
        }
    }

    public static double[][] mas1(double[][] arr, Integer i, double[] x) {
        for (int j = 0; j < 14; j++) {
            arr[i][j] = Math.tan(Math.cos(Math.pow(x[j], x[j])));
        }

        return arr;
    }

    public static double[][] mas2(double[][] arr, Integer i, double[] x) {
        for (int j = 0; j < 14; j++) {
            arr[i][j] = Math.sin((2 / 3) / (Math.atan((x[i] - 1) * Math.E + 1) - (1 / 3)));
        }

        return arr;
    }

    public static double[][] mas3(double[][] arr, Integer i, double[] x) {
        for (int j = 0; j < 14; j++) {
            double buf = Math.pow((Math.tan(x[i]) + 0.25), Math.tan(x[i]));
            double buf1 = Math.pow(buf, (0.5*Math.tan(x[i]/((2/3)+x[i]))));
            arr[i][j] = Math.pow((2 / buf1), 3);            
        }

        return arr;
    }
}
