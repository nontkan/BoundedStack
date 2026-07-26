import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * Testrun
 */
public class Testrun {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea Testrun\n");
        }
        System.out.println("--BoundedStack Test--");

        System.out.println("\n...Running Tasks...");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));
        System.out.println(failed == 0 ? "All Tasks passed!" : "Some Tasks failed.");

        if (failed > 0){
            System.exit(1);
        } else {
            System.exit(0);
        }

        





        

    }
}
