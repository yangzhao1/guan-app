package com.submeter.android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.tools.Utils;

/**
 * Created by zhao.bo on 2015/12/16.
 */
public class MyAlertDialog extends Dialog {

    public MyAlertDialog(Context context) {
        super(context);
    }

    public MyAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private boolean cancelable = true;
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public MyAlertDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            // instantiate the dialog with the custom Theme
            final MyAlertDialog dialog = new MyAlertDialog(context, R.style.DialogScale);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(false);

            View layout = null;
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
                layout = Utils.inflateView(context, inflater, R.layout.alert_dialog_simple, null);
                if (!TextUtils.isEmpty(title)) {
                    message = title;
                    title = null;
                }
            } else {
                layout = Utils.inflateView(context, inflater, R.layout.alert_dialog, null);
            }

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView titleView = (TextView) layout.findViewById(R.id.title);
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
            } else if (null != titleView) {
                titleView.setVisibility(View.GONE);
            }

            // set the content message
            TextView messageView = (TextView) layout.findViewById(R.id.message);
            if (!TextUtils.isEmpty(message)) {
                messageView.setText(message);
            } else if (null != messageView) {
                messageView.setVisibility(View.GONE);
            }

            // set the confirm button
            Button positiveButton = (Button) layout.findViewById(R.id.ok_btn);
            if (positiveButtonText != null) {
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                positiveButton.setVisibility(View.GONE);
            }
            // set the cancel button
            Button negativeButton = (Button) layout.findViewById(R.id.cancel_btn);
            if (negativeButtonText != null) {
                negativeButton.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                negativeButton.setVisibility(View.GONE);
            }

            dialog.setContentView(layout);
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            lp.y = 0;
            lp.width = Utils.dip2px(context, 277);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            w.setAttributes(lp);

            return dialog;
        }
    }
}


