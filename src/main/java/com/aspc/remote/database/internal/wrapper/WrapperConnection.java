/**
 *  STS Remote library
 *
 *  Copyright (C) 2006  stSoftware Pty Ltd
 *
 *  stSoftware.com.au
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *
 *  info AT stsoftware.com.au
 *
 *  or by snail mail to:
 *
 *  stSoftware
 *  Suite 223, Lifestyle Working
 *  117 Old Pittwater Rd
 *  Brookvale NSW 2100
 *  Australia.
 */
package com.aspc.remote.database.internal.wrapper;

import com.aspc.developer.ThreadCop;
import com.aspc.remote.jdbc.SoapSQLException;
import com.aspc.remote.util.misc.CLogger;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import org.apache.commons.logging.Log;

/**
 * Remote Server database connection.
 * Implements a database connection through SOAP.
 *
 *  <i>THREAD MODE: SINGLE-THREADED</i>
 *
 * @author  Nigel Leck
 * @since 29 September 2006
 */
@SuppressWarnings("AssertWithSideEffects")
public class WrapperConnection implements Connection
{
    public final Connection connection;
    private final ArrayList<WrapperStatement> openStatementList=new ArrayList<>();

    private RuntimeException alreadyClosedException;
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.database.internal.wrapper.WrapperConnection");//#LOGGER-NOPMD
    private final ConnectionReference reference;
    private static final ReferenceQueue QUEUE=new ReferenceQueue();
    
    private static final boolean ASSERT;
    /**
     * JDBC via SOAP
     *
     * @param connection the connection
     * @throws Exception a serious problem
     */
    public WrapperConnection(final @Nonnull Connection connection) throws Exception
    {
        this.connection=connection;

        if( ASSERT)
        {
            reference=new ConnectionReference( this, QUEUE);
            
            while( true)
            {
                Reference ref = QUEUE.poll();
                
                if( ref == null) break;
                
                ConnectionReference checkReference=(ConnectionReference)ref;
                
                checkReference.check();
            }
        }
        else
        {
            reference=null;
        }
    }

    @Override @CheckReturnValue @Nonnull
    public String toString()
    {
        return "WrapperConnection{" + "connection=" + connection + ", openStatements=" + openStatementList.size() + '}';
    }

    private void checkValid()
    {
        if( alreadyClosedException != null)
        {
            LOGGER.warn( "connection was closed by", alreadyClosedException);
            throw new WrapperAssertError("connection already closed", alreadyClosedException);
        }

        assert ThreadCop.access(this);
    }

    /**
     *
     * @return list of open statements
     */
    public WrapperStatement[] listOpenStatements()
    {
        WrapperStatement[] a = new WrapperStatement[openStatementList.size()];
        return openStatementList.toArray(a);
    }

    /**
     *
     * @param ws the statement that was closed.
     */
    public void closeStatement( WrapperStatement ws)
    {
        if( openStatementList.remove(ws) == false)
        {
            throw new WrapperAssertError("Statmenet not in the open list");
        }
        
    }

    /** {@inheritDoc} */
    @Override
    public Statement createStatement() throws SQLException
    {
        checkValid();
        WrapperStatement ws= new WrapperStatement( this, connection.createStatement());

        openStatementList.add(ws);

        return ws;
    }

    /** {@inheritDoc} */
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException
    {
        checkValid();
        return connection.prepareStatement(sql);
    }

    /**
     * We don't care about result set type as ours is always scrollable.
     *
     * {@inheritDoc}
     */
    @Override
     public PreparedStatement prepareStatement(
        String sql,
        int resultSetType,
        int resultSetConcurrency
    ) throws SQLException
    {
        checkValid();
        return prepareStatement( sql);
    }

     /** {@inheritDoc} */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "prepareStatement(String sql, String[] columnNames) - Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "prepareStatement(String sql, int autoGeneratedKeys) - Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "prepareStatement(String sql, int[] columnIndexes) - Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) - Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException
    {
        checkValid();
        WrapperCallableStatement ws= new WrapperCallableStatement( this, connection.prepareCall(sql));

        openStatementList.add(ws);

        return ws;
    }

    /** {@inheritDoc} */
    @Override
    public String nativeSQL(String sql) throws SQLException
    {
        checkValid();
        return sql;
    }

    /** {@inheritDoc} */
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException
    {
        checkValid();
        connection.setAutoCommit(autoCommit);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getAutoCommit() throws SQLException
    {
        checkValid();
        return connection.getAutoCommit();
    }

    /** {@inheritDoc} */
    @Override
    public void commit() throws SQLException
    {
        checkValid();
        connection.commit();
    }

    /** {@inheritDoc} */
    @Override
    public void rollback() throws SQLException
    {
        checkValid();
        connection.rollback();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws SQLException
    {
        if( alreadyClosedException !=null) throw alreadyClosedException;
        
        alreadyClosedException=new RuntimeException( "closed by thread " + Thread.currentThread());
        
        if( reference !=null ) reference.close();
        
        connection.close();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isClosed() throws SQLException
    {
        if( alreadyClosedException != null) return true;
        return connection.isClosed();
    }

    /** {@inheritDoc} */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException
    {
        checkValid();
        return new WrapperDatabaseMetaData( connection.getMetaData());
    }

    /** {@inheritDoc} */
    @Override
    public void setReadOnly(boolean readOnly) throws SQLException
    {
        checkValid();
        connection.setReadOnly(readOnly);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadOnly() throws SQLException
    {
        checkValid();
        return connection.isReadOnly();
    }

    /** {@inheritDoc} */
    @Override
    public void setCatalog(String catalog) throws SQLException
    {
        checkValid();
        connection.setCatalog(catalog);
    }

    /** {@inheritDoc} */
    @Override
    public String getCatalog() throws SQLException
    {
        checkValid();
        return connection.getCatalog();
    }

    /** {@inheritDoc} */
    @Override
    public void setTransactionIsolation(int level) throws SQLException
    {
        checkValid();
        connection.setTransactionIsolation(level);
    }

    /** {@inheritDoc} */
    @Override
    public int getTransactionIsolation() throws SQLException
    {
        checkValid();
        return connection.getTransactionIsolation();
    }

    /** {@inheritDoc} */
    @Override
    public SQLWarning getWarnings() throws SQLException
    {
        checkValid();
        return connection.getWarnings();
    }

    /** {@inheritDoc} */
    @Override
    public void clearWarnings() throws SQLException
    {
        checkValid();
        connection.clearWarnings();
    }

    /** {@inheritDoc} */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException
    {
        checkValid();
        WrapperStatement ws= new WrapperStatement( this, connection.createStatement(resultSetType, resultSetConcurrency));

        openStatementList.add(ws);

        return ws;
    }

    /** {@inheritDoc} */
    @Override
    public CallableStatement prepareCall(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException
    {
        checkValid();
        WrapperCallableStatement ws= new WrapperCallableStatement( this, connection.prepareCall(sql, resultSetType, resultSetConcurrency));

        openStatementList.add(ws);

        return ws;
    }

    /** {@inheritDoc} */
    @Override
    public java.util.Map getTypeMap() throws SQLException
    {
        checkValid();
        return connection.getTypeMap();
    }

    /** {@inheritDoc} */
    @Override
    public void setTypeMap(java.util.Map map) throws SQLException
    {
        checkValid();
        connection.setTypeMap(map);
    }

    /** {@inheritDoc} */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkValid();
        return createStatement();
    }

    /** {@inheritDoc} */
    @Override
    public int getHoldability() throws SQLException
    {
        checkValid();
        return connection.getHoldability();
    }

    /** {@inheritDoc} */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkValid();
        WrapperCallableStatement ws= new WrapperCallableStatement( this, connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));

        openStatementList.add(ws);

        return ws;

    }

    /** {@inheritDoc} */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        checkValid();
        connection.releaseSavepoint(savepoint);
    }

    /** {@inheritDoc} */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException
    {
        checkValid();
        connection.rollback(savepoint);
    }

    /** {@inheritDoc} */
    @Override
    public void setHoldability(int holdability) throws SQLException
    {
        checkValid();
        connection.setHoldability(holdability);
    }

    /** {@inheritDoc} */
    @Override
    public Savepoint setSavepoint() throws SQLException
    {
        checkValid();
        return connection.setSavepoint();
    }

    /** {@inheritDoc} */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException
    {
        checkValid();
        return connection.setSavepoint(name);
    }

    /** {@inheritDoc} */
    @Override
    public Clob createClob() throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "createClob() - Not Supported");
    }


    /** {@inheritDoc} */
    @Override
    public Blob createBlob() throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "createBlob() - Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public NClob createNClob() throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public SQLXML createSQLXML() throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid(int timeout) throws SQLException
    {
        checkValid();
        return connection.isValid(timeout);
    }

    /** {@inheritDoc} */
    @Override
    public String getClientInfo(String name) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public Properties getClientInfo() throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc}
    public Object createQueryObject(Class ifc) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }*/

    /** {@inheritDoc} */
    @Override
    public Object unwrap(Class iface) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWrapperFor(Class iface) throws SQLException
    {
        checkValid();
        throw new SoapSQLException( "Not Supported");
    }

    /** {@inheritDoc} */
    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc } */
    @Override
    public void setSchema(String schema) throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc } */
    @Override
    public String getSchema() throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc } */
    @Override
    public void abort(java.util.concurrent.Executor executor) throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc } */
    @Override
    public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc } */
    @Override
    public int getNetworkTimeout() throws SQLException
    {
        checkValid();
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    class ConnectionReference extends PhantomReference<WrapperConnection>{

        private AssertionError error;
        public ConnectionReference(WrapperConnection referent, ReferenceQueue<? super WrapperConnection> q) {
            super(referent, q);
            
            error = new AssertionError(referent + " never closed");
        }

        public void close()
        {
            error=null;
        }
        public void check()
        {
            if( error !=null)
            {
                throw error;
            }
        }
    }
  
    static{
        boolean enabled=false;
        assert enabled=true;
        
        ASSERT=enabled;
    }
}
