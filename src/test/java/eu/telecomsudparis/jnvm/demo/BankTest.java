package eu.telecomsudparis.jnvm.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankTest {

    static final private int N_ACCOUNTS = 1000;
    static final private long BALANCE = 100;

    private Bank bank;

    @Before
    public void setup() {
        this.bank = new Bank();
        this.bank.open();
        for (int i = 0; i < N_ACCOUNTS; i++) {
            bank.createAccount(Integer.toString(i), BALANCE);
        }
    }

    @After
    public void teardown() {
        this.bank.clear();
        this.bank.close();
        this.bank = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createExistingAccountTest() {
        String id = Integer.toString(1);
        bank.createAccount(id);
    }

    @Test
    public void getBalanceTest() {
        assertEquals(bank.getBalance("1"), BALANCE);
    }

    @Test
    public void creditTest() {
        bank.credit("1", 100);
        assertEquals(bank.getBalance("1"), BALANCE + 100);
    }

    @Test
    public void getTotalTest() {
        long expected = N_ACCOUNTS * BALANCE;
        assertEquals(bank.getTotal(), expected);
    }

    @Test
    public void performTransferTest() {
        bank.performTransfer("1", "2", 100);
        assertEquals(bank.getBalance("1") + bank.getBalance("2"), 2 * BALANCE);
    }

}
