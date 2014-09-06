package example;

public class Main {
    public static void main(String[] args) {
        // int n = Integer.parseInt(args[0]);

        Executor1 executor = new Executor1();
        long start = System.nanoTime();
        for(int n = 1; n <= 5; n++) {
            //long start = System.nanoTime();
            for(int i = 0; i < 3; i++) {
                executor.execute(n);
            }
            //System.out.println(n + "\t" + String.format("%.2f", (System.nanoTime() - start) / 3000000000.0f));
        }
        System.out.println(String.format("%.2f", (System.nanoTime() - start) / 3000000000.0f));
    }
}
