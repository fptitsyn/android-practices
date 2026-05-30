package ru.mirea.ptitsyn.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employee")
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int salary;
}