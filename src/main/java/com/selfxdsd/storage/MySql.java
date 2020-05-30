/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.storage;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since
 */
public final class MySql implements Database {

    /**
     * JDBC Connection.
     */
    private final Connection connection;

    /**
     * Constructor to obtain an unconnected instance.
     */
    public MySql() {
        this(null);
    }

    /**
     * Constructor to obtain a connected instance.
     * @param connection JDBC Connection.
     */
    private MySql(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public MySql connect() {
        try {
            return new MySql(
                DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/self_xdsd?serverTimezone=CET",
                    "user",
                    "blabla"
                )
            );
        } catch (final SQLException exception) {
            throw new IllegalStateException(
                "Could not connect to the DB",
                exception
            );
        }
    }

    @Override
    public DSLContext jooq() {
        if(this.connection == null) {
            throw new IllegalStateException("You need to connect first!");
        }
        return DSL.using(connection, SQLDialect.MYSQL);
    }

    @Override
    public void close() {
        if(this.connection != null) {
            try {
                this.connection.close();
            } catch (final SQLException exception) {
                throw new IllegalStateException(
                    "Could not close the DB Connection",
                    exception
                );            }
        }
    }
}