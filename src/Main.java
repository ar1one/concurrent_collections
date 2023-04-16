import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Main {

    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static final int AMOUNT_TEXTS = 10_000;
    public static final int TEXT_LENGTH = 100_000;
    public static int maxA = 0;
    public static int maxB = 0;
    public static int maxC = 0;

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void countOfLetter(char a, int max, BlockingQueue<String> queue) {
        for (int i = 0; i < AMOUNT_TEXTS; i++) {
            int count = 0;
            try {
                String text = queue.take();
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == a) count++;
                }
                if (count > max) max = count;
            } catch (Exception e) {
                return;
            }
        }
        System.out.printf("Максимальное количество символов '%s' вышло: %d\n", a, max);
    }

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < AMOUNT_TEXTS; i++) {
                String text = generateText("abc", TEXT_LENGTH);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (Exception e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> countOfLetter('a', maxA, queue1)).start();
        new Thread(() -> countOfLetter('b', maxB, queue2)).start();
        new Thread(() -> countOfLetter('c', maxC, queue3)).start();
    }
}