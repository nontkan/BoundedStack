import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * Testrun
 * ตัวรัน test สำหรับ BoundedStack — เขียนขึ้นเองทั้งหมดตาม C4
 * พิมพ์ผล PASS/FAIL ของแต่ละเคส และสรุปจำนวนรวมเมื่อจบการทำงาน
 * แบ่งฟังก์ชันทดสอบตามบทบาท
 * invariant สำคัญของ ADT เพื่อให้เห็นเหตุผลของแต่ละกลุ่มเคสชัดเจน
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
 
    // ---------- main ----------
    // จุดเริ่มต้นการทำงานของโปรแกรม
    public static void main(String[] args) {
        // ตรวจสอบว่า assertion ถูกเปิดใช้งานหรือไม่
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea Testrun\n");
        }
        testBoundedStack();

        System.out.println("\n---BoundedStack Test---");
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

    // เรียกฟังก์ชันทดสอบย่อยทั้งหมด
    private static void testBoundedStack(){
        System.out.println("\n---BoundedStack Test---");

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
    // ทดสอบว่า object ที่สร้างเสร็จมี state เริ่มต้นถูกต้องตาม RI ทุกกรณี

    private static void testCreator() {
        BoundedStack s = new BoundedStack(5);
        check("new(capacity) -> initial size is 0", s.size() == 0);
        check("new(capacity) -> isEmpty = true", s.isEmpty());

        BoundedStack s1 = new BoundedStack(1);
        check("new(capacity=1) -> initial size is 0", s1.size() == 0);

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

        // ตรงนี้จะ FAIL ถ้า capacity ยังถูก set = 0
        BoundedStack fromList = new BoundedStack(Arrays.asList("ทำการบ้าน", "อ่านหนังสือ", "ออกกำลังกาย"));
        check("new(initial list) -> size = 3", fromList.size() == 3); 
        check("new(initial list) -> isFull is false when not full", !fromList.isFull());
    }

    // ---------- Mutator: push ----------
    // ทดสอบ push() ว่าทำงานถูกต้องหรือไม่
    // push() จะ throw exception ถ้า push ตอนเต็มแล้ว หรือ push ค่า null, blank, หรือ push ค่าซ้ำ

    private static void testPush() {
        BoundedStack s = new BoundedStack(2);
        s.push("ทำการบ้าน");
        check("push once -> size is 1", s.size() == 1);

        // push จนเต็มพอดีตาม capacity
        s.push("อ่านหนังสือ");
        check("push to full -> size == capacity", s.size() == 2);
        check("push to full -> isFull = true", s.isFull());
        
        // push ตอนเต็มแล้วต้อง throw exception
        try {
            s.push("ออกกำลังกาย");
            check("push when full -> throws", false);
        } catch (IllegalStateException e) {
            check("push when full -> throws", true);
        }
        
        // push null ต้อง throw exception
        try {
            s.push(null);
            check("push null -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push null -> throws", true);
        }

        // จุดนี้จะ FAIL ถ้าเช็ค blank ยังใช้ == แทน .isEmpty()
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
        dup.push("ทำการบ้าน");
        try {
            dup.push("ทำการบ้าน");
            check("push duplicate -> throws", false);
        } catch (IllegalArgumentException e) {
            check("push duplicate -> throws", true);
        }
    }

    // ---------- Mutator: pop ----------
    // ทดสอบ pop() ว่าทำงานถูกต้องหรือไม่
    // pop() จะ throw exception ถ้า pop ตอนว่างแล้ว

    private static void testPop() {
        BoundedStack s = new BoundedStack(3);
        s.push("ทำการบ้าน");
        s.push("อ่านหนังสือ");

        String top = s.pop();
        check("pop returns last pushed", top.equals("อ่านหนังสือ"));
        check("pop -> size decreases", s.size() == 1);
        
        // pop จนเหลือ 0 พอดี
        s.pop();
        check("pop to empty -> size is 0", s.size() == 0);
        
        // pop ตอนว่างแล้วต้อง throw exception
        try {
            s.pop();
            check("pop when empty -> throws", false);
        } catch (IllegalStateException e) {
            check("pop when empty -> throws", true);
        }
    }

    // ---------- Observer ----------
    // เอาไว้ทดสอบเมธอดที่ดูข้อมูล เช่น peek(), isEmpty(), isFull(), size()
    
    private static void testObserver() {
        BoundedStack s = new BoundedStack(3);
        check("empty stack -> isEmpty is true", s.isEmpty());
        check("empty stack -> isFull is false", !s.isFull());

        s.push("ทำการบ้าน");
        check("after push -> isEmpty is false", !s.isEmpty());
        check("after push -> size is 1", s.size() == 1);
        
        // peek ต้องคืนค่าตัวบนสุดโดยไม่ลบออก
        String peeked = s.peek();
        check("peek returns top element", peeked.equals("ทำการบ้าน"));
        check("peek does not mutate -> size still 1", s.size() == 1);
        
        // peek ตอน stack ว่าง
        try {
            new BoundedStack(1).peek();
            check("peek on empty -> throws", false);
        } catch (NoSuchElementException e) {
            check("peek on empty -> throws", true);
        }
    }

    // ---------- Producer: copy() ----------
    // ทดสอบตัว copy() ว่าสร้าง object ใหม่ในหน่วยความจำ จริง ๆ ไม่ใช่แค่ส่งตัวชี้ (reference) ไปยัง object เดิมกลับมา

    private static void testProducer() {
        BoundedStack s = new BoundedStack(2);
        s.push("ทำการบ้าน");

        BoundedStack copy = s.copy();
        check("copy() -> same size as original", copy.size() == s.size());

        // ต้องเป็นคนละ object
        check("copy() -> different object identity", copy != s);

        // จุดนี้จะ FAIL ถ้า copy() ยังใช้ new BoundedStack() (capacity=100 ตายตัว)
        // แทนที่จะคง capacity เดิมไว้ (=2)
        copy.push("B");
        check("copy() -> preserves original capacity", copy.isFull());

        // แก้ copy แล้วต้นฉบับต้องไม่กระทบ 
        copy.pop();
        copy.pop();
        check("copy() -> mutating copy does not affect original", s.size() == 1);
    }

    // ---------- ลำดับแบบ LIFO ----------
    // ทดสอบพฤติกรรมของสแตกโดยตรง ไม่ใช่แค่ค่าที่ได้ทีละตัว เช่น push A, push B, push C แล้ว pop จะได้ C, B, A ตามลำดับ
    // เป็นเทสระดับพฤติกรรมรวม 

    private static void testLifoOrder() {
        BoundedStack s = new BoundedStack(3);
        s.push("ทำการบ้าน");
        s.push("อ่านหนังสือ");
        s.push("ออกกำลังกาย");

        check("LIFO order: 1st pop", s.pop().equals("ออกกำลังกาย"));
        check("LIFO order: 2nd pop", s.pop().equals("อ่านหนังสือ"));
        check("LIFO order: 3rd pop", s.pop().equals("ทำการบ้าน"));
        check("LIFO order: empty after popping all", s.isEmpty());
    }

    // ---------- Capacity invariant (ตรงกับ RI ที่ checkRep ควรตรวจ) ----------
    // เอาไว้ทดสอบ RI ของ BoundedStack ว่าขนาดไม่เกิน capacity เสมอ 
    private static void testCapacityInvariant() {
        BoundedStack s = new BoundedStack(1);
        s.push("ทำการบ้าน");
        check("size never exceeds capacity", s.size() <= 1);

        try {
            s.push("อ่านหนังสือ");
            check("push beyond capacity=1 -> throws", false);
        } catch (IllegalStateException e) {
            check("push beyond capacity=1 -> throws", true);
        }
    }
}


    

