package eu.telecomsudparis.jnvm.demo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    @Test
    public void testGetId() {
        Account a1 = new Account(1, 0);
        assertEquals(a1.getId(), 1);
    }

    @Test
    public void testGetBalance() {
        Account a1 = new Account(1, 10);
        assertEquals(a1.getBalance(), 10);
    }

    @Test
    public void testSetBalance() {
        Account a1 = new Account(1, 0);
        a1.setBalance(10);
        assertEquals(a1.getBalance(), 10);
    }

    @Test
    public void testIncBalance() {
        Account a1 = new Account(1, 0);
        a1.incBalance(10);
        assertEquals(a1.getBalance(), 10);
    }

    @Test
    public void testTransferTo() {
        Account a1 = new Account(1, 10);
        Account a2 = new Account(2, 30);

        a1.transferTo(a2, 10);
        assertEquals(a1.getBalance(), 0);
        assertEquals(a2.getBalance(), 40);
    }

}
