package cut;

public class LogicStructure {

    public int sequence(int a, int b) {
        return a + b;
    }

    public boolean soloIf(int a) {
        //System.out.println(123);
        return a>1;
    }

    public boolean ifElse(int op) {
        //while (op > 20){
        //    op-=2;
        // }

        if (op > 18)
            return true;
        else
            return false;
    }

    public String multipleIf(int op) {
        if (op % 15 == 0)
            return "FizzBuzz";
        else if (op % 5 == 0)
            return "Buzz";
        else if (op % 3 == 0)
            return "Fizz";
        else
            return Integer.toString(op);
    }

    public int soloFor(int op) {
        int tot = 0;
        for (int i = 0; i < op; ++i)
            tot += i;
        return tot;
    }

    public int multipleWhile(int op) {
        int x = 10;
        int ans = 0;
        while (x > 1) {
            while (op % x == 0) {
                op /= x;
                ans++;
            }
            x--;
        }
        return ans;
    }
}
