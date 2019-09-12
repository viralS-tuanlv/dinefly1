/*
 * Copyright 2017 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbgapps.simplenumberpicker.decimal;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.sbgapps.simplenumberpicker.R;
import com.sbgapps.simplenumberpicker.utils.ThemeUtil;

import java.text.DecimalFormatSymbols;

public class DecimalPickerDialog extends DialogFragment
{

    private static final String ARG_REFERENCE = "ARG_REFERENCE";
    private static final String ARG_RELATIVE = "ARG_RELATIVE";
    private static final String ARG_NATURAL = "ARG_NATURAL";
    private static final String ARG_THEME = "ARG_THEME";
    private static final String ARG_SAVED_VALUE = "ARG_SAVED_VALUE";

    private static final int NB_KEYS = 10;
    private static final int DEFAULT_REFERENCE = 0;

    private AlertDialog dialog;
    private TextView numberTextView;
    private ImageButton backspaceButton;

    private int reference = DEFAULT_REFERENCE;
    private boolean relative = true;
    private boolean natural = false;
    private String decimalSeparator;
    private int theme = R.style.SimpleNumberPickerTheme;
    private DecimalPickerHandler callback;

    private static DecimalPickerDialog newInstance(int reference, boolean relative, boolean natural, int theme, final DecimalPickerHandler callback)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_REFERENCE, reference);
        args.putBoolean(ARG_RELATIVE, relative);
        args.putBoolean(ARG_NATURAL, natural);
        args.putInt(ARG_THEME, theme);
        DecimalPickerDialog fragment = new DecimalPickerDialog();
        fragment.setArguments(args);
        fragment.callback = callback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            assignArguments(savedInstanceState);
        } else if (getArguments() != null)
        {
            assignArguments(getArguments());
        }

        setStyle(STYLE_NO_TITLE, theme);
        setCancelable(false);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        TypedArray attributes = getActivity().obtainStyledAttributes(theme, R.styleable.SimpleNumberPicker);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.snp_dialog_decimal_picker, null);

        // Init number
        int color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor, ContextCompat.getColor(getActivity(), android.R.color.secondary_text_light));
        numberTextView = (TextView) view.findViewById(R.id.tv_hex_number);
        numberTextView.setTextColor(color);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_SAVED_VALUE))
        {
            numberTextView.setText(savedInstanceState.getString(ARG_SAVED_VALUE));
        }

        // Init backspace
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpBackspaceColor, ContextCompat.getColor(getActivity(), android.R.color.secondary_text_light));
        backspaceButton = (ImageButton) view.findViewById(R.id.key_backspace);
        backspaceButton.setImageDrawable(ThemeUtil.makeSelector(getActivity(), R.drawable.snp_ic_backspace_black_24dp, color));
        backspaceButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View v)
            {
                CharSequence number = numberTextView.getText().subSequence(0, numberTextView.getText().length() - 1);
                if (1 == number.length() && '-' == number.charAt(0))
                {
                    number = "";
                }

                numberTextView.setText(number);
                DecimalPickerDialog.this.onNumberChanged();
            }
        });
        backspaceButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(final View v)
            {
                numberTextView.setText("");
                DecimalPickerDialog.this.onNumberChanged();
                return true;
            }
        });

        // Create dialog
        dialog = new AlertDialog.Builder(getActivity(), theme)
                         .setView(view)
                         .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                         {
                             public void onClick(final DialogInterface dialog, final int which)
                             {
                                 String result = numberTextView.getText().toString();

                                 if (result.isEmpty())
                                 {
                                     result = "0";
                                 }

                                 result = result.replace(',', '.');

                                 if (result.equals("."))
                                 {
                                     result = "0";
                                 }

                                 final float number = Float.parseFloat(result);
                                 final Activity activity = DecimalPickerDialog.this.getActivity();
                                 final Fragment fragment = DecimalPickerDialog.this.getParentFragment();

                                 if (activity instanceof DecimalPickerHandler)
                                 {
                                     final DecimalPickerHandler handler = (DecimalPickerHandler) activity;
                                     handler.onDecimalNumberPicked(reference, number);
                                 } else if (fragment instanceof DecimalPickerHandler)
                                 {
                                     final DecimalPickerHandler handler = (DecimalPickerHandler) fragment;
                                     handler.onDecimalNumberPicked(reference, number);
                                 } else if (callback != null)
                                 {
                                     callback.onDecimalNumberPicked(reference, number);
                                 }

                                 DecimalPickerDialog.this.dismiss();
                             }
                         }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        DecimalPickerDialog.this.dismiss();
                    }
                }).create();

        // Init dialog
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpDialogBackground, ContextCompat.getColor(getActivity(), android.R.color.white));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(color));

        // Init keys
        View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(final View v)
            {
                int key = (int) v.getTag();
                String id = numberTextView.getText() + Integer.toString(key);
                numberTextView.setText(id);
                DecimalPickerDialog.this.onNumberChanged();
            }
        };

        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor, ThemeUtil.getThemeAccentColor(getActivity()));
        TypedArray ids = getResources().obtainTypedArray(R.array.snp_key_ids);

        for (int i = 0; i < NB_KEYS; i++)
        {
            TextView key = (TextView) view.findViewById(ids.getResourceId(i, -1));
            key.setTag(i);
            key.setOnClickListener(listener);
            key.setTextColor(color);
        }

        // Init sign
        TextView sign = (TextView) view.findViewById(R.id.key_sign);
        if (relative)
        {
            sign.setTextColor(color);
            sign.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(final View v)
                {
                    String number = numberTextView.getText().toString();
                    if (number.startsWith("-"))
                    {
                        numberTextView.setText(number.substring(1));
                    } else
                    {
                        numberTextView.setText("-" + number);
                    }
                    DecimalPickerDialog.this.onNumberChanged();
                }
            });
        } else
        {
            sign.setVisibility(View.INVISIBLE);
        }

        // Init decimal separator
        initDecimalSeparator();
        TextView separator = (TextView) view.findViewById(R.id.key_point);
        if (natural)
        {
            separator.setVisibility(View.INVISIBLE);
        } else
        {
            separator.setText(decimalSeparator);
            separator.setTextColor(color);
            separator.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(final View v)
                {
                    if (numberTextView.getText().toString().contains(decimalSeparator))
                    {
                        return;
                    }
                    String number = numberTextView.getText().toString();
                    numberTextView.setText(number + decimalSeparator);
                    DecimalPickerDialog.this.onNumberChanged();
                }
            });
        }

        ids.recycle();
        attributes.recycle();

        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        onNumberChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_REFERENCE, reference);
        outState.putBoolean(ARG_RELATIVE, relative);
        outState.putBoolean(ARG_NATURAL, natural);
        outState.putInt(ARG_THEME, theme);
        outState.putString(ARG_SAVED_VALUE, numberTextView.getText().toString());
    }

    private void onNumberChanged()
    {
        backspaceButton.setEnabled(0 != numberTextView.length());
        if (0 == numberTextView.getText().length())
        {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else if ((1 == numberTextView.getText().length())
                           && ('-' == numberTextView.getText().charAt(0)))
        {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else
        {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void initDecimalSeparator()
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        decimalSeparator = "" + formatSymbols.getDecimalSeparator();
    }

    private void assignArguments(Bundle args)
    {
        if (args.containsKey(ARG_REFERENCE))
        {
            reference = args.getInt(ARG_REFERENCE);
        }
        if (args.containsKey(ARG_RELATIVE))
        {
            relative = args.getBoolean(ARG_RELATIVE);
        }
        if (args.containsKey(ARG_NATURAL))
        {
            natural = args.getBoolean(ARG_NATURAL);
        }
        if (args.containsKey(ARG_THEME))
        {
            theme = args.getInt(ARG_THEME);
        }
    }

    public static class Builder
    {

        private int reference = DEFAULT_REFERENCE;
        private boolean relative = false;
        private boolean natural = false;
        private int theme = R.style.SimpleNumberPickerTheme;
        private DecimalPickerHandler callback;

        public Builder reference(int reference)
        {
            this.reference = reference;
            return this;
        }

        public Builder relative()
        {
            this.relative = true;
            return this;
        }

        public Builder natural()
        {
            this.natural = true;
            return this;
        }

        public Builder theme(int theme)
        {
            this.theme = theme;
            return this;
        }

        public Builder callback(DecimalPickerHandler callback)
        {
            this.callback = callback;
            return this;
        }

        public DecimalPickerDialog build()
        {
            return newInstance(reference, relative, natural, theme, callback);
        }

        public void show(FragmentManager manager, String tag)
        {
            build().show(manager, tag);
        }
    }
}
