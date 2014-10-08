package cz.muni.fi.xharting.classic.test.transaction;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static javax.transaction.Status.STATUS_ACTIVE;
import static javax.transaction.Status.STATUS_NO_TRANSACTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionalException;

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
        return createSeamWebApp("test.war", Alpha.class, Bravo.class, Charlie.class);
    }

    @Inject
    private Alpha alpha;

    @Inject
    private Bravo bravo;

    @Inject
    private Charlie charlie;

    @Test
    public void requred() throws SystemException {
        int status = alpha.required();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test
    public void requiredTypeLevel() throws SystemException {
        int status = charlie.required();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test
    public void supportsNotTx() throws SystemException {
        int status = alpha.supports();
        assertEquals("the transaction must not be running!", STATUS_NO_TRANSACTION, status);
    }

    @Test
    @Transactional
    public void supportsWithTx() throws SystemException {
        int status = alpha.supports();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test(expected = TransactionalException.class)
    public void mandatoryNoTx() throws SystemException {
        alpha.mandatory();
        fail("an existing transaction must be enforced!");
    }

    @Test
    @Transactional
    public void mandatoryWithTx() throws SystemException, NotSupportedException {
        int status = alpha.mandatory();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test(expected = TransactionalException.class)
    public void mandatoryNoTxTypeLevel() throws SystemException {
        bravo.mandatory();
        fail("an existing transaction must be enforced!");
    }

    @Test
    @Transactional
    public void mandatoryWithTxTypeLevel() throws SystemException, NotSupportedException {
        int status = bravo.mandatory();
        assertEquals("the transaction must be active!", STATUS_ACTIVE, status);
    }

    @Test
    public void neverNoTx() throws SystemException {
        int status = alpha.never();
        assertEquals("the transaction must not be running!", STATUS_NO_TRANSACTION, status);
    }

    @Test(expected = TransactionalException.class)
    @Transactional
    public void neverWithTx() throws SystemException, NotSupportedException {
        alpha.never();
        fail("no running transaction must be accepted!");
    }
}
