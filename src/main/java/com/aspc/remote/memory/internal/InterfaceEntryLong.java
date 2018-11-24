/*
 *  Copyright (c) 2002-2004stSoftware pty ltd
 *
 *  www.stSoftware.com.au
 *
 *  All Rights Reserved.
 *
 *  This software is the proprietary information of
 * stSoftware pty ltd.
 *  Use is subject to license terms.
 */
package com.aspc.remote.memory.internal;

import javax.annotation.CheckReturnValue;

/**
 *  Cache entry
 *
 *  <br>
 *  <i>THREAD MODE: MULTI-THREADED memory management</i>
 *
 *  @author      Nigel Leck
 *  @since       17 December 2007
 */
public interface InterfaceEntryLong extends InterfaceEntry
{
    /**
     * 
     * @return the key
     */
    @CheckReturnValue
    public long key();
}
