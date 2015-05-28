package com.github.miniwallet.actions.purchase;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.miniwallet.adapters.FilterableEntityListAdapter;
import com.github.miniwallet.filters.ProductPriceFilter;
import com.github.miniwallet.shopping.Product;

/**
 * Created by deviance on 28.05.15.
 */
public class PriceEditTextWatcher implements TextWatcher {
    private final EditText priceText;
    private final FilterableEntityListAdapter<Product> adapter;
    private final ProductPriceFilter productPriceFilter;

    public PriceEditTextWatcher(EditText priceText, FilterableEntityListAdapter<Product> adapter,
                                ProductPriceFilter productPriceFilter) {
        this.priceText = priceText;
        this.adapter = adapter;
        this.productPriceFilter = productPriceFilter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {
        priceText.removeTextChangedListener(this);

        String old = priceText.getText().toString();
        String s = old;

        if (itIsDeletionOfPointZero(start, before, count, s)) {
            s = s.substring(1);
        } else {
            s = s.replaceFirst("^0*", "");
            if (s.isEmpty() || s.charAt(0) == '.') s = "0" + s;
        }

        if (isProperNumber(priceText.getText().toString())) {
            setValue(Double.parseDouble(s));
            priceText.setText(s);
            setSelection(count, before, start, s, old);
        } else {
            priceText.setText("");
            setValue(null);
        }

        priceText.addTextChangedListener(this);
    }

    private boolean itIsDeletionOfPointZero(int start, int before, int count, String s) {
        return s.startsWith(".") && count == 0 && start == 0 && before == 1;
    }

    private void setSelection(int count, int before, int start, String s, String old) {
        if(("0" + s).equals(old)) priceText.setSelection(start);
        else if (count == 0) priceText.setSelection(start);
        else priceText.setSelection(start + 1 >= s.length() ? s.length() : start + 1);
    }

    private boolean isProperNumber(String text) {
        if (text.isEmpty()) return false;

        Double number;
        try {
            number = Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private void setValue(Double number) {
        productPriceFilter.setValue(number);
        adapter.filterAndNotify();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
