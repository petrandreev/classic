package cz.muni.fi.xharting.classic.test.transaction;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static javax.transaction.Status.STATUS_ACTIVE;
import static javax.transaction.Status.STATUS_NO_TRANSACTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the transactional behavior of migrated Seam 2 beans.
 *
 * @author pan
 *
 */
@RunWith(Arquillian.class)
public class TransactionalTest {

    @Deployment
    public static WebArchive getDeployment() {
        return createSeamWebApp("test.war", true, false, TransactionalBean.class).addAsWebInfResource("cz/muni/fi/xharting/classic/test/transaction/beans.xml", "beans.xml");
    }

    @Inject
    private TransactionalBean transactionalBean;

    @Test
    public void requred() throws SystemException {
        int status = transactionalBean.required();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test
    public void supportsNotTx() throws SystemException {
        int status = transactionalBean.supports();
        assertEquals("the transaction must not be running!", STATUS_NO_TRANSACTION, status);
    }

    @Test
    @Transactional
    public void supportsWithTx() throws SystemException {
        int status = transactionalBean.supports();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test(expected = IllegalStateException.class)
    public void mandatoryNoTx() throws SystemException {
        transactionalBean.mandatory();
        fail("an existing transaction must be enforced!");
    }

    @Test
    @Transactional
    public void mandatoryWithTx() throws SystemException, NotSupportedException {
        int status = transactionalBean.mandatory();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test
    public void neverNoTx() throws SystemException {
        int status = transactionalBean.never();
        assertEquals("the transaction must not be running!", STATUS_NO_TRANSACTION, status);
    }

    @Test(expected = IllegalStateException.class)
    @Transactional
    public void neverWithTx() throws SystemException, NotSupportedException {
        transactionalBean.never();
        fail("no running transaction must be accepted!");
    }
}
