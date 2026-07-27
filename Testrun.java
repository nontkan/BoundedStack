import java.util.NoSuchElementException;
import java.util.Arrays;

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
        testBoundedStack();

        System.out.println("\n--BoundedStack Test--");

        System.out.println("...Running Tasks...");
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
    // ทดสอบ constructor ของ BoundedStack ว่าทำงานถูกต้องหรือไม่

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
    // ทดสอบ push() ว่าทำงานถูกต้องหรือไม่
    // push() จะ throw exception ถ้า push ตอนเต็มแล้ว หรือ push ค่า null, blank, หรือ push ค่าซ้ำ

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
    // ทดสอบ pop() ว่าทำงานถูกต้องหรือไม่
    // pop() จะ throw exception ถ้า pop ตอนว่างแล้ว

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

    // ---------- Observer ----------
    // เอาไว้ทดสอบเมธอดที่ ดูข้อมูล แต่ไม่แก้ไขข้อมูล เช่น peek(), isEmpty(), isFull(), size()
    
    private static void testObserver() {
        BoundedStack s = new BoundedStack(3);
        check("stack ว่าง -> isEmpty เป็น true", s.isEmpty());
        check("stack ว่าง -> isFull เป็น false", !s.isFull());

        s.push("A");
        check("หลัง push -> isEmpty เป็น false", !s.isEmpty());
        check("หลัง push -> ขนาดเป็น 1", s.size() == 1);

        String peeked = s.peek();
        check("peek คืนค่าตัวบนสุด", peeked.equals("A"));
        check("peek ไม่ลบข้อมูล -> ขนาดเป็น 1", s.size() == 1);

        try {
            new BoundedStack(1).peek();
            check("peek ตอนว่าง -> ต้อง throw", false);
        } catch (NoSuchElementException e) {
            check("peek ตอนว่าง -> ต้อง throw", true);
        }
    }

    // ---------- Producer: copy() ----------
    // ทดสอบตัว copy() ว่าสร้าง object ใหม่ในหน่วยความจำ จริง ๆ ไม่ใช่แค่ส่งตัวชี้ (reference) ไปยัง object เดิมกลับมา

    private static void testProducer() {
        BoundedStack s = new BoundedStack(2);
        s.push("A");

        BoundedStack copy = s.copy();
        check("copy() -> ขนาดเท่ากับต้นฉบับ", copy.size() == s.size());
        check("copy() -> เป็นคนละ object กับต้นฉบับ", copy != s);

        // เคสนี้จะ FAIL ถ้า copy() ยังใช้ new BoundedStack() (capacity=100 ตายตัว)
        // แทนที่จะคง capacity เดิมไว้ (=2)
        copy.push("B");
        check("copy() -> คง capacity เดิมไว้", copy.isFull());

        copy.pop();
        copy.pop();
        check("copy() -> แก้ copy แล้วต้นฉบับจะไม่กระทบ", s.size() == 1);
    }

    // ---------- ลำดับแบบ LIFO ----------
    // ทดสอบพฤติกรรมของสแตกโดยตรง ไม่ใช่แค่ค่าที่ได้ทีละตัว เช่น push A, push B, push C แล้ว pop จะได้ C, B, A ตามลำดับ

    private static void testLifoOrder() {
        BoundedStack s = new BoundedStack(3);
        s.push("A");
        s.push("B");
        s.push("C");

        check("ลำดับ LIFO: pop ครั้งที่ 1", s.pop().equals("C"));
        check("ลำดับ LIFO: pop ครั้งที่ 2", s.pop().equals("B"));
        check("ลำดับ LIFO: pop ครั้งที่ 3", s.pop().equals("A"));
        check("ลำดับ LIFO: ว่างหลัง pop ครบ", s.isEmpty());
    }

    // ---------- Capacity invariant (ตรงกับ RI ที่ checkRep ควรตรวจ) ----------
    // เอาไว้ทดสอบ RI ของ BoundedStack ว่าขนาดไม่เกิน capacity เสมอ 
    private static void testCapacityInvariant() {
        BoundedStack s = new BoundedStack(1);
        s.push("A");
        check("size ไม่เกิน capacity เสมอ", s.size() <= 1);

        try {
            s.push("B");
            check("push เกิน capacity=1 -> ต้อง throw", false);
        } catch (IllegalStateException e) {
            check("push เกิน capacity=1 -> ต้อง throw", true);
        }
    }
}
// ค่อยมาแก้ต่อ ขก. 

    

