import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


// นาย นนทการณ์ สุขสวัสดิ์ 6821651400
// นาย กฤษวัฒน์ ชูรัตน์ 6821651086


/**
 * BoundedStack — ADT รายการสิ่งที่ต้องทำ
 * ค่านามธรรม (A): ลำดับของรายการสิ่งที่ต้องทำ เช่น
 * "ทำการบ้าน", "อ่านหนังสือ", "ออกกำลังกาย"
 * 
 * 
 * 
 * ตัวอย่างการใช้งาน:
 *     BoundedStack todo = new BoundedStack();
 *     todo.push("ทำการบ้าน");
 *     todo.push("อ่านหนังสือ");
 *
 *     System.out.println(todo.size());   // 2
 */
public class BoundedStack{
    
    
    
    public static final int MAX_TASKS = 100;

     private final List<String> tasks;

     private final int capacity;

// Abstraction Function (AF)
// AF(tasks, capacity) = สแตกของรายการสิ่งที่ต้องทำ
// 
//
//
// Representation Invariant (RI)
// 1. tasks ต้องไม่เป็น null
// 2. capacity ต้องไม่เกิน MAX_TASKS
// 3. สมาชิกทุกตัวใน tasks ต้องไม่เป็น null
// 4. สมาชิกทุกตัวต้องไม่เป็นข้อความว่างหรือมีแต่ช่องว่าง
// 5. ไม่มีรายการซ้ำ
//
// Safety from Rep Exposure
// - tasks และ capacity เป็น private final



/**
 * ตรวจสอบว่า Representation Invariant (RI) ยังคงเป็นจริง
 * หลังจากสร้างออบเจ็กต์หรือหลังจากมีการแก้ไขข้อมูล
 * หากผิด จะเกิด AssertionError
 */
    private void checkRep() {
        assert tasks != null : "tasks ไม่เป็น null ได้";
        assert tasks.size() <= MAX_TASKS : "จำนวนรายการสิ่งที่ต้องทำเกินจำนวนสูงสุดที่กำหนด";
         Set<String> seen = new HashSet<>();
    for (String task : tasks) {
        assert task != null : "รายการต้องไม่เป็น null";
        assert task != "  " : "รายการต้องไม่เป็นข้อความว่าง";
        assert seen.add(task):"ห้ามมีรายการซ้ำ";
    }

}
    
    /** ====Creator====
     * สร้าง BoundedStack ว่าง
     */
    public BoundedStack() {
        this.capacity = MAX_TASKS;
        tasks = new ArrayList<>();
        checkRep();

    }

    public BoundedStack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity ต้องเป็นค่าบวก");
        this.capacity = capacity;
        tasks = new ArrayList<>();
        checkRep();
    }

/**    ====Creator====
 * สร้าง BoundedStack ว่างตามความจุที่กำหนด
 * @param capacity จำนวนรายการสูงสุดที่สแตกสามารถเก็บได้
 * @requires capacity ต้องเป็นค่าบวก
 * @effects สร้างสแตกใหม่ที่ไม่มีรายการ และมีความจุเท่ากับ capacity
 * @throws IllegalArgumentException ถ้า capacity น้อยกว่าหรือเท่ากับ 0
 */
    public BoundedStack(List<String> initial) {
    if (initial == null) {throw new IllegalArgumentException("รายการเริ่มต้นไม่สามารถเป็น null ได้");
    }
    if(initial.size() > MAX_TASKS)throw new IllegalArgumentException("จำนวนรายการเริ่มต้นเกินจำนวนสูงสุดที่กำหนด") ;
        Set<String> seen = new HashSet<>();
        for (String s : initial) {
            if(s == null)throw new IllegalArgumentException("รายการไม่สามารถเป็น null ได้");
            if(s == " ")throw new IllegalArgumentException("รายการไม่สามารถเป็นสตริงว่างได้");
            if(!seen.add(s))throw new IllegalArgumentException("รายการต้องไม่ซ้ำ");

        }this.tasks = new ArrayList<>(initial);
        this.capacity = MAX_TASKS;
        checkRep();
    }
    /**   ====Mutator====
     * @param Subject รายการสิ่งที่ต้องการเ
     * @requires Subject ต้องไม่เป็น null ไม่เป็นข้อความว่าง และต้องไม่ซ้ำกับรายการที่มีอยู่
     * @return ถ้า Subject มีอยู่แล้วให้โยน IllegalArgument
     * @throws IllegalArgumentException ถ้า Subject เป็น null หรือเป็นสตริงว่าง
     */
    public void push(String Subject){
        if(Subject == null)throw new IllegalArgumentException("Subject ต้องไม่เป็น null") ; 
        String cleaned = Subject.trim();
         if (cleaned.isEmpty())throw new IllegalArgumentException("Subject ต้องไม่เป็นสตริงว่าง") ;
        if(tasks.size() >= capacity)throw new IllegalStateException("Stack เต็มแล้ว") ;
        if(tasks.contains(cleaned))throw new IllegalArgumentException("Subject ต้องไม่ซ้ำ") ;
        tasks.add(cleaned);
        checkRep();

    }
    /**
     * นำรายการบนสุดออกจากสแตกและคืนค่ารายการนั้น
    * @effects ลบรายการบนสุดของสแตก
    * @return รายการที่ถูกนำออกจากบนสุดของสแตก
    * @throws IllegalStateException ถ้าสแตกว่าง
 */
    public String pop() {
        if (tasks.isEmpty()) throw new IllegalStateException("ไม่มีรายวิชาใน Stack");
        String removedTask = tasks.remove(tasks.size() - 1); // นำออกจากท้าย list = บนสุดของสแตก
        checkRep();
        return removedTask;
    }
    /**
     * ลบรายการทั้งหมดออกจากสแตก
     * @effects ทำให้สแตกว่างและมีขนาดเท่ากับ 0
     */ 
    public void clear() {
        tasks.clear();
        checkRep();
    }
/**     ====Producer====
 สร้างสำเนาของ BoundedStack ปัจจุบัน
 * @effects สร้าง BoundedStack ใหม่ที่มีข้อมูลเหมือนกับสแตกปัจจุบัน
 * @return BoundedStack ใหม่ที่มีข้อมูลเหมือนกับสแตกปัจจุบัน
 * @effects ไม่เปลี่ยนแปลงข้อมูลของสแตกเดิม
 */

public BoundedStack copy() {
   BoundedStack newStack = new BoundedStack(this.capacity);
    newStack.tasks.addAll(this.tasks);
    newStack.checkRep();
    return newStack;
}
    /**     ====Observer====
     คืนรายการที่อยู่บนสุดของสแตก โดยไม่ลบ
    * @return รายการบนสุดของสแตก
    * @effects ไม่เปลี่ยนแปลงข้อมูลในสแตก
    * @throws NoSuchElementException ถ้าสแตกว่าง
    */
  public String peek() {
    if (tasks.isEmpty()) {
        throw new NoSuchElementException("Stack ว่าง");
    }

    return tasks.get(tasks.size() - 1);
}
/**
 * คืนค่าจำนวนรายการในสแตก
 * @effects ไม่เปลี่ยนแปลงข้อมูลในสแตก
 * @return จำนวนรายการในสแตก
 */
public int size() {
    return tasks.size();
}
/**
 * ตรวจสอบว่าสแตกว่างหรือไม่
 * @effects ไม่เปลี่ยนแปลงข้อมูลในสแตก
 * @return true ถ้าสแตกว่าง, false ถ้าไม่ใช่
 */       
 
public boolean isEmpty() {
    return tasks.isEmpty();
}
/**
 * ตรวจสอบว่าสแตกเต็มหรือไม่
 * @effects ไม่เปลี่ยนแปลงข้อมูลในสแตก
 * @return true ถ้าสแตกเต็ม, false ถ้าไม่ใช่
 */
public boolean isFull() {
    return tasks.size() == capacity;
}
/**
 * คืนรายการทั้งหมดในสแตก
 * สมาชิกตำแหน่งแรกเป็นรายการล่างสุด
 * และสมาชิกตำแหน่งสุดท้ายเป็นรายการบนสุด
 * @effects ไม่เปลี่ยนแปลงข้อมูลในสแตก
 * @return List ใหม่ที่เป็นสำเนาของรายการภายในสแตก
 */
public List<String> tasks() {
    return new ArrayList<>(tasks);
}
}




