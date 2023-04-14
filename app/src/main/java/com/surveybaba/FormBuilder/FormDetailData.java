package com.surveybaba.FormBuilder;

import com.google.gson.annotations.SerializedName;
import com.surveybaba.Utilities.Utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FormDetailData {

    @SerializedName("column_name")
    private String column_name;

    @SerializedName("type")
    private String type;

    @SerializedName("required")
    private boolean required = false;

    @SerializedName("label")
    private String label;

    @SerializedName("name")
    private String name;

    @SerializedName("multiple")
    private String multiple;

    @SerializedName("selvalues")
    //private String selvalues;
    private ArrayList<FormOptions> selvalues;


    @SerializedName("subtype")
    private String subtype;

    @SerializedName("fill_value")
    private String fill_value;

    @SerializedName("value")
    private String value = "";

    @SerializedName("input_id")
    private String input_id;

    @SerializedName("is_history")
    private String is_history;

    @SerializedName("options")
    private List<FormDetailOption> optionsList;


    //private List<FormOptions> optionsl;


//    private String formbg_color;
//    private String form_logo;
//    private String form_sno;
//
//    public String getFormbg_color() {
//        return formbg_color;
//    }
//
//    public void setFormbg_color(String formbg_color) {
//        this.formbg_color = formbg_color;
//    }
//
//    public String getForm_logo() {
//        return form_logo;
//    }
//
//    public void setForm_logo(String form_logo) {
//        this.form_logo = form_logo;
//    }
//
//    public String getForm_sno() {
//        return form_sno;
//    }
//
//    public void setForm_sno(String form_sno) {
//        this.form_sno = form_sno;
//    }

    public String getFill_value() {
        return fill_value;
    }

    public void setFill_value(String fill_value) {
        this.fill_value = fill_value;
    }

    public List<FormOptions> getSelvalues() {
        return selvalues;
    }

    public void setSelvalues(ArrayList<FormOptions> selvalues) {
        this.selvalues = selvalues;
    }

    /*@SerializedName("groupLabel")
    private String group_label;

    @SerializedName("group_label_id")
    private String group_label_id;*/

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setIs_history(String is_history) {
        this.is_history = is_history;
    }

    public void setOptionsList(List<FormDetailOption> optionsList) {
        this.optionsList = optionsList;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getInput_id() {
        return input_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInput_id(String input_id) {
        this.input_id = input_id;
    }

    /*public String getGroup_label() {
        return group_label;
    }

    public String getGroup_label_id() {
        return group_label_id;
    }*/

    public boolean isRequired() {
        return required;
    }

    public List<FormDetailOption> getOptionsList() {
        return optionsList;
    }

    public String getIs_history() {
        return is_history;
    }



    public class FormDetailOption {
        @SerializedName("label")
        private String label;

        @SerializedName("value")
        private String value = "";

        private boolean isSelected;

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }


    public static class FormOptions{

        @SerializedName("label")
        private String label;
        @SerializedName("value")
        private String value;
        @SerializedName("selected")
        private boolean selected;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
