package ru.mirea.ptitsyn.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "EmployeeDB";
    private TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput = findViewById(R.id.tvOutput);

        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        Employee hero1 = new Employee();
        hero1.name = "Супермен";
        hero1.salary = 50000;
        employeeDao.insert(hero1);

        Employee hero2 = new Employee();
        hero2.name = "Бэтмен";
        hero2.salary = 45000;
        employeeDao.insert(hero2);

        List<Employee> heroes = employeeDao.getAll();
        StringBuilder sb = new StringBuilder("Список героев:\n");
        for (Employee e : heroes) {
            sb.append(e.id).append(". ").append(e.name)
                    .append(" - зарплата: ").append(e.salary).append("\n");
            Log.d(TAG, e.id + ": " + e.name + " - " + e.salary);
        }
        tvOutput.setText(sb.toString());

        if (!heroes.isEmpty()) {
            Employee first = heroes.get(0);
            first.salary = 75000;
            employeeDao.update(first);
            Log.d(TAG, "Обновлён: " + first.name + " новая зарплата " + first.salary);
            tvOutput.append("\nОбновлён: " + first.name + " -> " + first.salary);
        }

        if (heroes.size() >= 2) {
            Employee second = heroes.get(1);
            employeeDao.delete(second);
            Log.d(TAG, "Удалён: " + second.name);
            tvOutput.append("\nУдалён: " + second.name);
        }

        List<Employee> updatedList = employeeDao.getAll();
        tvOutput.append("\n\nПосле изменений:\n");
        for (Employee e : updatedList) {
            tvOutput.append(e.name + " - " + e.salary + "\n");
        }
    }
}