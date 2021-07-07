package evm;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ballot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime date;
    @ManyToOne
    private Contestants contestant;

    public Ballot() {
    }

    public Ballot(int cid) {
        this.date = LocalDateTime.now();
        this.contestant = new Contestants(cid,"",0,0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Contestants getContestant() {
        return contestant;
    }

    public void setContestant(Contestants contestant) {
        this.contestant = contestant;
    }
}
