package com.dezlum.www.productinventory;

import android.widget.EditText;

/**
 * Created by saurabh on 1/23/2017.
 */

public class ProductValidator {
    public boolean checkBlank(EditText words) {
        if (words.getText().toString().trim().length() > 0)
            return false;
        else
            return true;

    }

    public boolean checkIsFloat(EditText words) {
        try {
            Float.parseFloat(words.getText().toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean checkIsInteger(EditText words) {
        try {
            Integer.parseInt(words.getText().toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
