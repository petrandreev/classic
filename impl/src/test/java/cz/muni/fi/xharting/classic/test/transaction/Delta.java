package cz.muni.fi.xharting.classic.test.transaction;

import static javax.transaction.Transactional.TxType.MANDATORY;
import static javax.transaction.Transactional.TxType.NEVER;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

/**
 * CDI bean for transactional tests. Provides a target reference for TX test.
 *
 * @author pandreev
 *
 */
@Named("delta")
public class Delta implements Serializable {
    private static final long serialVersionUID = 298390158737283632L;

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
