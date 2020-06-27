package com.example.pmsumail.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rule implements Parcelable {


    public enum Condition {
        TO,
        FROM,
        CC,
        SUBJECT
    }

    public enum Operation {
        MOVE,
        COPY,
        PASTE
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("condition")
    @Expose
    private Condition condition;
    @SerializedName("operation")
    @Expose
    private Operation operation;

    public Rule() {
    }

    public Rule(int id, Condition condition, Operation operation) {
        this.id = id;
        this.condition = condition;
        this.operation = operation;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.condition == null ? -1 : this.condition.ordinal());
        dest.writeInt(this.operation == null ? -1 : this.operation.ordinal());
    }

    protected Rule(Parcel in) {
        this.id = in.readInt();
        int tmpCondition = in.readInt();
        this.condition = tmpCondition == -1 ? null : Condition.values()[tmpCondition];
        int tmpOperation = in.readInt();
        this.operation = tmpOperation == -1 ? null : Operation.values()[tmpOperation];
    }

    public static final Parcelable.Creator<Rule> CREATOR = new Parcelable.Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel source) {
            return new Rule(source);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };
}
