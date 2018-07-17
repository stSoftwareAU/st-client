/*
 *  Copyright (c) 2002-2004 ASP Converters pty ltd
 *
 *  www.stSoftware.com.au
 *
 *  All Rights Reserved.
 *
 *  This software is the proprietary information of
 *  ASP Converters Pty Ltd.
 *  Use is subject to license terms.
 */
package com.aspc.remote.memory.internal;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 *  Cache entry
 *
 *  <br>
 *  <i>THREAD MODE: MULTI-THREADED memory management</i>
 *
 *  @author      Nigel Leck
 *  @since       17 December 2007
 */
public interface InterfaceEntryObject extends InterfaceEntry
{
    /**
     * the key
     * @return the key
     */
    @CheckReturnValue @Nonnull
    public Object key();
}
