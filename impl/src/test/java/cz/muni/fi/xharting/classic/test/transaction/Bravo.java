package cz.muni.fi.xharting.classic.test.transaction;

import static org.jboss.seam.annotations.TransactionPropagationType.MANDATORY;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

/**
 * Seam2 component for transactional tests which defines the transaction attributes at the type level.
 *
 * @author pandreev
 *
 */
@Name("bravo")
@Transactional(MANDATORY)
public class Bravo implements Serializable {
    private static final long serialVersionUID = -1311284241318892963L;

    @Resource(lookup = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    /**
     * TX mandatory.
     *
     * @return current transaction status
     * @throws SystemException
     */

    public int mandatory() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }
}
