import java.util.List;
import java.util.NoSuchElementException;
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
    private static void testBoundedStack(){
        System.out.println("\n--BoundedStack Test--");

        testCreator();
        testPush();
        testPop();
    
    }

    // ---------- Creator ----------

    private static void testCreator() {
        BoundedStack s = new BoundedStack(5);
        check("new(capacity) -> ขนาดเริ่มต้นเป็น 0", s.size() == 0);
        check("new(capacity) -> isEmpty = true", s.isEmpty());

        BoundedStack s1 = new BoundedStack(1);
        check("new(capacity=1) -> ขนาดเริ่มต้นเป็น 0", s1.size() == 0);

        try {
            new BoundedStack(0);
            check("new(capacity=0) -> throws", false);
        } catch (IllegalArgumentException e) {
            check("new(capacity=0) -> throws", true);
        }

        try {
            new BoundedStack(-1);
            check("new(capacity=-1) -> throws", false);
        } catch (IllegalArgumentException e) {
            check("new(capacity=-1) -> throws", true);
        }

        BoundedStack fromList = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("new(initial list) -> size = 3", fromList.size() == 3); 
        check("new(initial list) -> isFull false เมื่อยังไม่เต็ม", !fromList.isFull());
        // ตรงนี้จะ FAIL ถ้า capacity ยังถูก set = 0
    }

    // ---------- Mutator: push ----------

    private static void testPush() {
        BoundedStack s = new BoundedStack(2);
        s.push("A");
        check("push ครั้งเดียว -> size 1", s.size() == 1);

        s.push("B");
        check("push จนเต็มพอดี -> size == capacity", s.size() == 2);
        check("push จนเต็ม -> isFull = true", s.isFull());

        try {
            s.push("C");
            check("push ตอนเต็มแล้ว -> throws", false);
        } catch (IllegalStateException e) {
            check("push ตอนเต็มแล้ว -> throws", true);
        }

        try {
            s.push(null);
            check("push ค่า null -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push ค่า null -> throws", true);
        }

        // จุดนี้จะ FAIL ถ้าเช็ค blank ยังใช้ == แทน .isEmpty()
        try {
            new BoundedStack(3).push("   ");
            check("push ข้อความเว้นวรรค -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push ข้อความเว้นวรรค -> throws", true);
        }

        try {
            new BoundedStack(3).push("");
            check("push ข้อความว่างเปล่า -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push ข้อความว่างเปล่า -> throws", true);
        }

        BoundedStack dup = new BoundedStack(3);
        dup.push("A");
        try {
            dup.push("A");
            check("push ค่าซ้ำ -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push ค่าซ้ำ -> throws", true);
        }
    }

    // ---------- Mutator: pop ----------
    
    private static void testPop() {
        BoundedStack s = new BoundedStack(3);
        s.push("A");
        s.push("B");

        String top = s.pop();
        check("pop คืนค่าตัวที่ push ล่าสุด", top.equals("B"));
        check("pop -> ขนาดลดลง", s.size() == 1);

        s.pop();
        check("pop จนว่าง -> size 0", s.size() == 0);

        try {
            s.pop();
            check("pop ตอนว่าง -> throws", false);
        } catch (IllegalStateException e) {
            check("pop ตอนว่าง -> throws", true);
        }
    }
}

    

