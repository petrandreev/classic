package cz.muni.fi.xharting.classic.test.transaction;

import static org.jboss.seam.annotations.TransactionPropagationType.MANDATORY;
import static org.jboss.seam.annotations.TransactionPropagationType.NEVER;
import static org.jboss.seam.annotations.TransactionPropagationType.SUPPORTS;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

/**
 * Seam2 component for transactional tests.
 *
 * @author pandreev
 *
 */
@Name("alpha")
public class Alpha implements Serializable {
    private static final long serialVersionUID = -2579343717564002446L;
    
    @Resource(lookup = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    /**
     * TX required by default.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional
    public int required() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX supported.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(SUPPORTS)
    public int supports() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX mandatory.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(MANDATORY)
    public int mandatory() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX never.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(NEVER)
    public int never() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }
}
