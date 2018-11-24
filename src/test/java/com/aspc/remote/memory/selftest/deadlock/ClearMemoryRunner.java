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
package com.aspc.remote.memory.selftest.deadlock;

import com.aspc.remote.memory.MemoryHandler;
import com.aspc.remote.memory.MemoryManager;

/**
 *  Test the wrapper classes for Trans header, record and data
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED self test unit</i>
 *
 *  @author      Nigel Leck
 *  @since       27 October 2002
 */
public class ClearMemoryRunner implements Runnable
{
    /**
     * run
     */
    @Override
    public void run()
    {
        MemoryManager.clearMemory( MemoryHandler.Cost.LOW);
    }
}
