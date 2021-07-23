package com.mobvoi.ticwatch.framework.cache;

/**
 * Redis事务执行失败
 *
 * @author tandy
 */
public class TransactionExecFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public TransactionExecFailedException() {
  }

  public TransactionExecFailedException(Throwable cause) {
    super(cause);
  }

}
