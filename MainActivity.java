package com.example.expensetracker;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText expenseName;
    private EditText expenseAmount;
    private Button addExpenseButton;
    private TextView totalExpenseText;
    private ListView expenseListView;
    private ArrayList<String> expenses;
    private ExpenseAdapter adapter;
    private DatabaseHelper databaseHelper;
    private double totalExpense = 0.0;
    private Button resetExpenseButton;  // Reset button to clear expenses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseName = findViewById(R.id.expenseName);
        expenseAmount = findViewById(R.id.expenseAmount);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        resetExpenseButton = findViewById(R.id.resetExpenseButton); // Initialize reset button
        totalExpenseText = findViewById(R.id.totalExpenseText);
        expenseListView = findViewById(R.id.expenseListView);

        expenses = new ArrayList<>();
        adapter = new ExpenseAdapter(this, expenses);
        expenseListView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(this);

        loadExpenses(); // Load expenses from database

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense(); // Add new expense to the list and database
            }
        });

        resetExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetExpenses(); // Clear all expenses from the list and database
            }
        });
    }

    private void addExpense() {
        String name = expenseName.getText().toString();
        String amountText = expenseAmount.getText().toString();

        if (!name.isEmpty() && !amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText);

            // Insert the expense into the database
            boolean isInserted = databaseHelper.insertExpense(name, amount);
            if (isInserted) {
                totalExpense += amount;
                totalExpenseText.setText("Total Expense: " + totalExpense);

                expenses.add(name + " - $" + amount);
                adapter.notifyDataSetChanged();

                expenseName.setText("");
                expenseAmount.setText("");
            }
        }
    }

    private void loadExpenses() {
        Cursor cursor = databaseHelper.getAllExpenses();
        if (cursor.getCount() == 0) {
            return; // No data to load
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            double amount = cursor.getDouble(2);
            totalExpense += amount;
            expenses.add(name + " - $" + amount);
        }
        totalExpenseText.setText("Total Expense: " + totalExpense);
        adapter.notifyDataSetChanged();
    }

    private void resetExpenses() {
        databaseHelper.clearExpenses(); // Clear the database
        expenses.clear(); // Clear the ArrayList
        totalExpense = 0.0; // Reset the total expense
        totalExpenseText.setText("Total Expense: " + totalExpense);
        adapter.notifyDataSetChanged(); // Update the ListView
    }
}
