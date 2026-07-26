package A;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    // 1.Abstraction Function:
    //   AF(tasks) = ลำดับของรายการสิ่งที่ต้องทำที่เก็บอยู่ใน tasks
    //   เช่น tasks(รายการ) = "ทำการบ้าน", "อ่านหนังสือ" 

    // 2.Representation Invariant:
    //  tasks ไม่เป็น null
    //  tasks.size() <= MAX_TASKS
    //  ไม่มีสมาชิกเป็น null
    //  ไม่มีข้อความว่างหรือมีแต่ช่องว่าง

    // 3.Safety from rep exposure:
    // tasks เป็น private final
    // 
     

    /**
     * ตรวจสอบความถูกต้องของสถานะภายในของออบเจกต์
     * แปลง RI ทุกข้อเป็น assert หนึ่งบรรทัด พร้อมข้อความอธิบาย
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
    
    /**
     * สร้าง BoundedStack ว่าง
     */
    public BoundedStack() {
        tasks = new ArrayList<>();
        checkRep();

    }

    public BoundedStack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity ต้องเป็นค่าบวก");
        this.capacity = capacity;
        tasks = new ArrayList<>();
        checkRep();
    }

/**
     * สร้าง BoundedStack ด้วยรายการเริ่มต้น
     *
     * @param initial รายการสิ่งที่ต้องทำเริ่มต้น
     * @throws IllegalArgumentException ถ้ารายการผิดเงื่อนไข
     */
    public BoundedStack(List<String> initial) {
    if (initial == null) {
        throw new IllegalArgumentException();
    }
    if(initial.size() > MAX_TASKS)throw new IllegalArgumentException() ;
        Set<String> seen = new HashSet<>();
        for (String s : initial) {
            if(s == null)throw new IllegalArgumentException();
            if(s == " ")throw new IllegalArgumentException() ;
            if(!seen.add(s))throw new IllegalArgumentException() ;

        }this.tasks = new ArrayList<>(initial);
        checkRep();
    }
    /* เพิ่มวิชาไว้บนสุดของสแตก
     * 
     * @param Subject ต้องไม่เป็น null และไม่เป็นสตริงว่าง
     * @return ถ้า Subject มีอยู่แล้วให้โยน IllegalArgument
     * @throws IllegalArgumentException ถ้า Subject เป็น null หรือเป็นสตริงว่าง
     */
    public void push(String Subject){
        if(Subject == null)throw new IllegalArgumentException("Subject ต้องไม่เป็น null") ; 
        if(Subject == " ")throw new IllegalArgumentException("Subject ต้องไม่เป็นสตริงว่าง") ;
        if(tasks.size() >= MAX_TASKS)throw new IllegalStateException("Stack เต็มแล้ว") ;
        if(tasks.contains(Subject))throw new IllegalArgumentException("Subject ต้องไม่ซ้ำ") ;
        tasks.add(0, Subject);
        checkRep();

    }

    public String pop(){
        if(tasks.isEmpty())throw new IllegalStateException("ไม่มีรายวิชาใน Stack") ;
        int topindex = tasks.size() - 1;
        String removedSubject = tasks.remove(topindex);
        checkRep();
        return removedSubject;
    }



}