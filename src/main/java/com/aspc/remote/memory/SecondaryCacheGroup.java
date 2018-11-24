/*
 *  Copyright (c) 1999-2004stSoftware pty ltd
 *
 *  www.stSoftware.com.au
 *
 *  All Rights Reserved.
 *
 *  This software is the proprietary information of
 * stSoftware pty ltd.
 *  Use is subject to license terms.
 */
package com.aspc.remote.memory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

/**
 *  The secondary cache group. The cache will be cleared at the group and child level. 
 *
 *  <br>
 *  <i>THREAD MODE: READONLY memory management</i>
 *
 * @author Nigel Leck
 * @since 12 April 2011
 */
public interface SecondaryCacheGroup
{
    /**
     * 
     * @return the group key
     */
    @CheckReturnValue @Nullable
    Object getSecondaryCacheGroupKey();
            
}
