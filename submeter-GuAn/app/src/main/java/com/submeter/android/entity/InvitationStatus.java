package com.submeter.android.entity;


/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationStatus {
    /**
     * 编号
     */
    private Integer id;
    /**
     * 内容
     */
    private String name;
    /**
     * 是否选中
     */
    private boolean isChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
