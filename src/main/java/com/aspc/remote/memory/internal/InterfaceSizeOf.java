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
 *  Size of object
 *
 *  <br>
 *  <i>THREAD MODE: MULTI-THREADED memory management</i>
 *
 *  @author      Nigel Leck
 *  @since       14 November 2008
 */
public interface InterfaceSizeOf
{
    /**
     * calculate the estimated size in bytes
     * @return the estimated size
     */
    @CheckReturnValue
    public long sizeOf();

}
