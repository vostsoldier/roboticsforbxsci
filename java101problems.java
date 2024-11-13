public class java101problems {
    public static int countOccurrences(int[] arr, int n) {
        int count = 0;
        for (int num : arr) {
            if (num == n) {
                count++;
            }
        }
        return count;
    }

    public static int[] reverseArray(int[] arr) {
        int[] reversed = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }

    public static double sumGrid(double[][] grid) {
        double sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                sum += grid[i][j];
            }
        }
        return sum;
    }

    public static int fib(int n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 2, 2, 5, 2};
        int n = 2;
        System.out.println("Num of occurrences of " + n + ": " + countOccurrences(arr, n));

        int[] reversedArr = reverseArray(arr);
        System.out.print("Reversed array: ");
        for (int num : reversedArr) {
            System.out.print(num + " ");
        }
        System.out.println();

        double[][] grid = {
            {1.1, 2.2, 3.3},
            {4.4, 5.5, 6.6},
            {7.7, 8.8, 9.9}
        };
        System.out.println("Sum of grid elements: " + sumGrid(grid));

        int fibIndex = 10;
        System.out.println("Fibonacci number at index " + fibIndex + ": " + fib(fibIndex));
    }
}