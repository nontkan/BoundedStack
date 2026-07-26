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
        testObserver();
        testProducer();
        testLifoOrder();
        testCapacityInvariant();
    
    }

    // ---------- Creator ----------
    // partition: capacity ปกติ / capacity = 1 (boundary) / capacity = 0 / capacity ติดลบ
    // / constructor เปล่า / constructor จาก initial list
    private static void testCreator() {
        BoundedStack s = new BoundedStack(5);
        check("new(capacity) -> size 0", s.size() == 0);
        check("new(capacity) -> isEmpty true", s.isEmpty());

        BoundedStack s1 = new BoundedStack(1);
        check("new(capacity=1) -> size 0", s1.size() == 0);

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
        check("new(initial list) -> size 3", fromList.size() == 3);
        // จุดนี้จะ FAIL ถ้า capacity ยังถูก set = 0 ใน constructor —
        // ถือเป็นเทสที่จับบั๊ก RI (capacity < size) ได้ตรงจุด
        check("new(initial list) -> isFull false เมื่อยังไม่เต็ม", !fromList.isFull());
    }

    // ---------- Mutator: push ----------
    // partition: push ปกติ / push จนเต็มพอดี (boundary) / push ตอนเต็ม (error)
    // / push null / push blank string / push ค่าซ้ำ
    private static void testPush() {
        BoundedStack s = new BoundedStack(2);
        s.push("A");
        check("push once -> size 1", s.size() == 1);

        s.push("B");
        check("push to full (boundary) -> size == capacity", s.size() == 2);
        check("push to full -> isFull true", s.isFull());

        try {
            s.push("C");
            check("push when full -> throws", false);
        } catch (IllegalStateException e) {
            check("push when full -> throws", true);
        }

        try {
            s.push(null);
            check("push null -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push null -> throws", true);
        }

        // จุดนี้จะ FAIL ถ้าเช็ค blank ยังใช้ == แทน .isEmpty() หลัง trim
        try {
            new BoundedStack(3).push("   ");
            check("push blank(spaces) -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push blank(spaces) -> throws", true);
        }

        try {
            new BoundedStack(3).push("");
            check("push empty string -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push empty string -> throws", true);
        }

        BoundedStack dup = new BoundedStack(3);
        dup.push("A");
        try {
            dup.push("A");
            check("push duplicate -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push duplicate -> throws", true);
        }
    }

    // ---------- Mutator: pop ----------
    // partition: pop จากหลายตัว / pop จนเหลือ 0 (boundary) / pop ตอนว่าง (error)
    private static void testPop() {
        BoundedStack s = new BoundedStack(3);
        s.push("A");
        s.push("B");

        String top = s.pop();
        check("pop returns last pushed", top.equals("B"));
        check("pop -> size decreases", s.size() == 1);

        s.pop();
        check("pop to empty (boundary) -> size 0", s.size() == 0);

        try {
            s.pop();
            check("pop when empty -> throws", false);
        } catch (IllegalStateException e) {
            check("pop when empty -> throws", true);
        }
    }

    // ---------- Observer ----------
    // partition: size() หลาย state / isEmpty() true-false / isFull() true-false
    // / peek() ไม่เปลี่ยน state / peek() ตอนว่าง (error)
    private static void testObserver() {
        BoundedStack s = new BoundedStack(3);
        check("empty -> isEmpty true", s.isEmpty());
        check("empty -> isFull false", !s.isFull());

        s.push("A");
        check("after push -> isEmpty false", !s.isEmpty());
        check("after push -> size 1", s.size() == 1);

        String peeked = s.peek();
        check("peek returns top", peeked.equals("A"));
        check("peek does not remove -> size still 1", s.size() == 1);

        try {
            new BoundedStack(1).peek();
            check("peek on empty -> throws", false);
        } catch (NoSuchElementException e) {
            check("peek on empty -> throws", true);
        }
    }

    // ---------- Producer: copy() ----------
    // partition: copy คง size / copy คง capacity (จุดที่เคยพบบั๊ก) / copy เป็นคนละ object
    // / แก้ copy แล้วต้นฉบับไม่เปลี่ยน (independence)
    private static void testProducer() {
        BoundedStack s = new BoundedStack(2);
        s.push("A");

        BoundedStack copy = s.copy();
        check("copy() -> same size", copy.size() == s.size());
        check("copy() -> different object", copy != s);

        // จุดนี้จะ FAIL ถ้า copy() ยังใช้ new BoundedStack() (capacity=100 ตายตัว)
        // แทนที่จะคง capacity เดิม (=2)
        copy.push("B");
        check("copy() -> preserves original capacity", copy.isFull());

        copy.pop();
        copy.pop();
        check("copy() -> independent from original after mutation", s.size() == 1);
    }

    // ---------- LIFO order ----------
    private static void testLifoOrder() {
        BoundedStack s = new BoundedStack(3);
        s.push("A");
        s.push("B");
        s.push("C");

        check("LIFO order 1st pop", s.pop().equals("C"));
        check("LIFO order 2nd pop", s.pop().equals("B"));
        check("LIFO order 3rd pop", s.pop().equals("A"));
        check("LIFO order -> empty after all pops", s.isEmpty());
    }

    // ---------- Capacity invariant (ตรง ๆ กับ RI ที่ checkRep ควรตรวจ) ----------
    private static void testCapacityInvariant() {
        BoundedStack s = new BoundedStack(1);
        s.push("A");
        check("size never exceeds capacity", s.size() <= 1);

        try {
            s.push("B");
            check("push beyond capacity=1 -> throws", false);
        } catch (IllegalStateException e) {
            check("push beyond capacity=1 -> throws", true);
        }
    }
}    

