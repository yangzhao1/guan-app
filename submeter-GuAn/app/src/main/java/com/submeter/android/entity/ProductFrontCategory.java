package com.submeter.android.entity;

/**
 * @author thm
 * @date 2018/12/13
 */
public class ProductFrontCategory {

    private int id;
    private String name;
    /**
     * 当前是否被选中
     */
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
