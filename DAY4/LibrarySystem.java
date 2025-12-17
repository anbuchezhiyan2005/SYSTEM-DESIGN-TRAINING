import java.time.LocalDate;
import java.util.*;

// Book entity
class Book {
    String id;
    String name;
    String author;
    
    Book(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }
}

// Physical copy of book
class BookCopy {
    String cid;
    Book b;
    String s;
    
    BookCopy(String cid, Book b) {
        this.cid = cid;
        this.b = b;
        this.s = "AVAILABLE";
    }
}

// Member entity
class Member {
    String mid;
    String nm;
    List<BorrowRecord> hist;
    
    Member(String mid, String nm) {
        this.mid = mid;
        this.nm = nm;
        this.hist = new ArrayList<>();
    }
    
    int getActive() {
        int c = 0;
        for (BorrowRecord rec : hist) {
            if (rec.st.equals("ACTIVE")) {
                c++;
            }
        }
        return c;
    }
}

// Borrow transaction record
class BorrowRecord {
    String rid;
    Member m;
    BookCopy bc;
    LocalDate bd;
    LocalDate dd;
    LocalDate rd;
    String st;
    int fn;
    
    BorrowRecord(String rid, Member m, BookCopy bc) {
        this.rid = rid;
        this.m = m;
        this.bc = bc;
        this.bd = LocalDate.now();
        this.dd = LocalDate.now().plusDays(14);
        this.rd = null;
        this.st = "ACTIVE";
        this.fn = 0;
    }
}

class Library {
    Map<String, Book> bks;
    Map<String, BookCopy> cps;
    Map<String, Member> mems;
    int cpCtr = 0;
    int rcCtr = 0;
    
    Library() {
        bks = new HashMap<>();
        cps = new HashMap<>();
        mems = new HashMap<>();
    }
    
    void add(String bid, String nm, String auth) {
        Book bk = new Book(bid, nm, auth);
        bks.put(bid, bk);
    }
    
    void addCp(String bid) {
        if (!bks.containsKey(bid)) {
            return;
        }
        cpCtr++;
        String cpid = "CP" + cpCtr;
        Book bk = bks.get(bid);
        BookCopy cp = new BookCopy(cpid, bk);
        cps.put(cpid, cp);
    }
    
    int getFree(String bid) {
        int cnt = 0;
        for (BookCopy cp : cps.values()) {
            if (cp.b.id.equals(bid) && cp.s.equals("AVAILABLE")) {
                cnt++;
            }
        }
        return cnt;
    }
    
    BookCopy getCpAvail(String bid) {
        for (BookCopy cp : cps.values()) {
            if (cp.b.id.equals(bid) && cp.s.equals("AVAILABLE")) {
                return cp;
            }
        }
        return null;
    }
    
    void join(String mid, String nm) {
        Member mbr = new Member(mid, nm);
        mems.put(mid, mbr);
    }
    
    void borrow(String mid, String bid) {
        if (!mems.containsKey(mid)) {
            return;
        }
        
        Member mbr = mems.get(mid);
        
        if (mbr.getActive() >= 5) {
            return;
        }
        
        if (!bks.containsKey(bid)) {
            return;
        }
        
        BookCopy cp = getCpAvail(bid);
        if (cp == null) {
            return;
        }
        
        rcCtr++;
        String rcid = "RC" + rcCtr;
        BorrowRecord rc = new BorrowRecord(rcid, mbr, cp);
        
        cp.s = "BORROWED";
        mbr.hist.add(rc);
    }
    
    void ret(String rcid) {
        BorrowRecord trc = null;
        
        for (Member mbr : mems.values()) {
            for (BorrowRecord rc : mbr.hist) {
                if (rc.rid.equals(rcid)) {
                    trc = rc;
                    break;
                }
            }
            if (trc != null) break;
        }
        
        if (trc == null) {
            return;
        }
        
        if (trc.st.equals("RETURNED")) {
            return;
        }
        
        LocalDate retd = LocalDate.now();
        int fn = 0;
        if (retd.isAfter(trc.dd)) {
            long dlt = java.time.temporal.ChronoUnit.DAYS.between(trc.dd, retd);
            fn = (int) (dlt * 5);
        }
        
        trc.st = "RETURNED";
        trc.rd = retd;
        trc.fn = fn;
        
        trc.bc.s = "AVAILABLE";
    }
}
