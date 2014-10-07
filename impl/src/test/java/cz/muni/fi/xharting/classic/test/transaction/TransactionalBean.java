package cz.muni.fi.xharting.classic.test.transaction;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

/**
 * A test component for transactional tests.
 *
 * @author pandreev
 *
 */
@Name("transactionalBean")
public class TransactionalBean implements Serializable {

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
        new Exception().printStackTrace();
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX supported.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(TransactionPropagationType.SUPPORTS)
    public int supports() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX mandatory.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(TransactionPropagationType.MANDATORY)
    public int mandatory() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }

    /**
     * TX never.
     * 
     * @return current transaction status
     * @throws SystemException
     */
    @Transactional(TransactionPropagationType.NEVER)
    public int never() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }
}
