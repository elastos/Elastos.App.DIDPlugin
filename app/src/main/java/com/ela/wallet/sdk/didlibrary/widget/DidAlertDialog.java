package com.ela.wallet.sdk.didlibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.maple.msdialog.BaseDialog;


@SuppressWarnings("deprecation")
public class DidAlertDialog extends BaseDialog {
    private TextView txt_title;
    private TextView txt_msg;
    private Button leftButton;
    private Button rightButton;
    private ImageView img_line;
    private EditText et_input;

    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showRightBtn = false;
    private boolean showLeftBtn = false;
    private boolean bAutoDismiss = true;

    public DidAlertDialog(Context context) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_didalert, null);

        // get custom Dialog layout
        txt_title = rootView.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = rootView.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        leftButton = rootView.findViewById(R.id.bt_left);
        leftButton.setVisibility(View.GONE);
        rightButton = rootView.findViewById(R.id.bt_right);
        rightButton.setVisibility(View.GONE);
        img_line = rootView.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        et_input = rootView.findViewById(R.id.et_input);
        et_input.setVisibility(View.GONE);

        // set Dialog style
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(rootView);

        setScaleWidth(0.75D);
    }

    public DidAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public DidAlertDialog setScaleWidth(double scWidth) {
        return (DidAlertDialog) super.setScaleWidth(scWidth);
    }

    public DidAlertDialog setTitle(String title) {
        int color = mContext.getResources().getColor(R.color.textBlack);
        return setTitle(title, color, 18, true);
    }

    public DidAlertDialog setTitle(String title, int color, int size, boolean isBold) {
        showTitle = true;
        txt_title.setText(title);
        if (color != -1)
            txt_title.setTextColor(color);
        if (size > 0)
            txt_title.setTextSize(size);
        if (isBold)
            txt_title.setTypeface(txt_title.getTypeface(), Typeface.BOLD);
        return this;
    }

    public DidAlertDialog setMessage(CharSequence message) {
        int color = mContext.getResources().getColor(R.color.textBlack);
        return setMessage(message, color, 14, false);
    }

    @SuppressWarnings("deprecation")
    public DidAlertDialog setMessage(CharSequence message, int color, int size, boolean isBold) {
        showMsg = true;
        txt_msg.setText(message);
        if (color != -1)
            txt_msg.setTextColor(color);
        if (size > 0)
            txt_msg.setTextSize(size);
        if (isBold)
            txt_msg.setTypeface(txt_msg.getTypeface(), Typeface.BOLD);
        txt_msg.setMovementMethod(LinkMovementMethod.getInstance());
        txt_msg.setLinkTextColor(mContext.getResources().getColor(R.color.textBlack));
        return this;
    }

    public DidAlertDialog setMessageGravity(int gravity) {
        txt_msg.setGravity(gravity);
        return this;
    }

    public DidAlertDialog setEditText(boolean b) {
        et_input.setVisibility(b ? View.VISIBLE : View.GONE);
        return this;
    }

    public EditText getEditTextView() {
        return this.et_input;
    }

    public DidAlertDialog setRightButton(String text, final View.OnClickListener listener) {
        int color = mContext.getResources().getColor(R.color.appColor);
        return setRightButton(text, color, 18, true, listener);
    }

    public DidAlertDialog setRightButton(String text, int color, int size, boolean isBold, final View.OnClickListener listener) {
        showRightBtn = true;
        if (TextUtils.isEmpty(text)) {
            rightButton.setText("OK");
        } else {
            rightButton.setText(text);
        }
        if (color != -1)
            rightButton.setTextColor(color);
        if (size > 0)
            rightButton.setTextSize(size);
        if (isBold)
            rightButton.setTypeface(rightButton.getTypeface(), Typeface.BOLD);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                if (bAutoDismiss)
                    dialog.dismiss();
            }
        });
        return this;
    }

    public DidAlertDialog setLeftButton(String text, final View.OnClickListener listener) {
        int color = mContext.getResources().getColor(R.color.textBlack);
        return setLeftButton(text, color, 18, true, listener);
    }

    public DidAlertDialog setLeftButton(String text, int color, int size, boolean isBold, final View.OnClickListener listener) {
        showLeftBtn = true;
        if (TextUtils.isEmpty(text)) {
            leftButton.setText("Cancel");
        } else {
            leftButton.setText(text);
        }
        if (color != -1)
            leftButton.setTextColor(color);
        if (size > 0)
            leftButton.setTextSize(size);
        if (isBold)
            leftButton.setTypeface(leftButton.getTypeface(), Typeface.BOLD);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                if (bAutoDismiss)
                    dialog.dismiss();
            }
        });
        return this;
    }

    public DidAlertDialog setAutoDismiss(boolean autoDismiss) {
        this.bAutoDismiss = autoDismiss;
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("Alert");
            txt_title.setVisibility(View.VISIBLE);
        }
        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }
        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        // one button
        if (!showRightBtn && !showLeftBtn) {
            rightButton.setText("OK");
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setBackgroundResource(com.maple.msdialog.R.drawable.alertdialog_single_selector);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showRightBtn && !showLeftBtn) {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setBackgroundResource(com.maple.msdialog.R.drawable.alertdialog_single_selector);
        }

        if (!showRightBtn && showLeftBtn) {
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setBackgroundResource(com.maple.msdialog.R.drawable.alertdialog_single_selector);
        }
        // two button
        if (showRightBtn && showLeftBtn) {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setBackgroundResource(com.maple.msdialog.R.drawable.alertdialog_right_selector);
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setBackgroundResource(com.maple.msdialog.R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public boolean isShowing() {
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

