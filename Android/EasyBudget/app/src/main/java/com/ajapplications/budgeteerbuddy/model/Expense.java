/*
   Copyright (c) 2018 Jordan Judt and Alexis Layne.

   Original project "EasyBudget" Copyright (c) Benoit LETONDOR

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ajapplications.budgeteerbuddy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ajapplications.budgeteerbuddy.helper.DateHelper;

import java.util.Date;

/**
 * Object that represent an expense.
 *
 * @author Benoit LETONDOR
 */
public class Expense implements Parcelable {
    /**
     * DB id of this expense (can be null)
     */
    private Long id;
    /**
     * CategoryHelper of the expense
     */
    private Category category;
    /**
     * Title of the expense
     */
    private String title;
    /**
     * Amount of this expense (can be < 0).
     */
    private double amount;
    /**
     * Date of the expense
     */
    private Date date;
    /**
     * Associated recurring expense (can be null if not a recurring one)
     */
    @Nullable
    private RecurringExpense recurringExpense;

// --------------------------------->

    /**
     * @param category
     * @param title
     * @param amount
     * @param date
     */
    public Expense(@NonNull Category category, String title, double amount, @NonNull Date date) {
        this(null, category, title, amount, date, null);
    }

    /**
     * @param category
     * @param title
     * @param amount
     * @param date
     * @param recurringExpense
     */
    public Expense(@NonNull Category category, String title, double amount, @NonNull Date date, @Nullable RecurringExpense recurringExpense) {
        this(null, category, title, amount, date, recurringExpense);
    }

    /**
     * @param id
     * @param category
     * @param title
     * @param amount
     * @param date
     * @param recurringExpense
     */
    public Expense(Long id, @NonNull Category category, String title, double amount, @NonNull Date date, @Nullable RecurringExpense recurringExpense) {
        this.id = id;

        if (category == null)
            throw new IllegalArgumentException("CategoryHelper is null");

        if (amount == 0)
            throw new IllegalArgumentException("amount should be != 0");

        this.category = category;
        this.title = title;
        this.amount = amount;
        setDate(date);
        this.recurringExpense = recurringExpense;
    }

    /**
     * @param in
     */
    private Expense(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        category = Category.valueOf(in.readString());
        title = in.readString();
        amount = in.readDouble();
        date = new Date(in.readLong());
        recurringExpense = in.readParcelable(RecurringExpense.class.getClassLoader());
    }

// --------------------------------->

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public RecurringExpense getAssociatedRecurringExpense() {
        return recurringExpense;
    }

    public boolean isRecurring() {
        return recurringExpense != null;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    public void setCategory(@NonNull Category category) {
        this.category = category;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = DateHelper.cleanDate(date);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isRevenue() {
        return amount < 0;
    }

// --------------------------------->

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(category.name());
        dest.writeString(title);
        dest.writeDouble(amount);
        dest.writeLong(date.getTime());
        dest.writeParcelable(recurringExpense, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
