package com.tamsiree.rxkit.demodata;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Collections2.transform;

/**
 * @author Binary Wang
 */
public class InsertSQLGenerator {
    private static final Joiner COMMA_JOINER = Joiner.on(", ");
    private Connection con;
    private String tableName;

    public InsertSQLGenerator(String url, String username, String password,
                              String tableName) {
        try {
            this.con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.tableName = tableName;
    }

    public String generateSQL() {
        List<String> columns = getColumns();

        return String.format("insert into %s(%s) values(%s)", this.tableName,
                COMMA_JOINER.join(columns),
                COMMA_JOINER.join(Collections.nCopies(columns.size(), "?")));
    }

    public String generateParams() {
        return COMMA_JOINER.join(transform(getColumns(), input -> "abc.get" + CaseFormat.LOWER_UNDERSCORE
                .to(CaseFormat.UPPER_CAMEL, input) + "()"));
    }

    private List<String> getColumns() {
        List<String> columns = Lists.newArrayList();
        try (PreparedStatement ps = this.con.prepareStatement("select * from " + this.tableName);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData rsm = rs.getMetaData();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                String columnName = rsm.getColumnName(i);
                System.out.print("Name: " + columnName);
                System.out.println(", Type : " + rsm.getColumnClassName(i));
                columns.add(columnName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return columns;
    }

    public void close() {
        try {
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
