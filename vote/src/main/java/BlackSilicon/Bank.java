package BlackS;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bank {
        @Id
    	private long b_id;
    	private String bankName;
    	private int accountNumber;
    	private String accountName;
    	private int bvn;
    	@OneToOne
    	private Customer customer;

}
