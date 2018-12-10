package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;
import com.ela.wallet.sdk.didlibrary.widget.GridRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordInputActivity extends BaseActivity {

    private RecyclerView rv_show;
    private GridRecyclerViewAdapter mWordShowAdapter;
    private List<WordModel> wordList;

    private RecyclerView rv_input;
    private GridRecyclerViewAdapter mWordInputAdapter;
    private List<WordModel> wordInputList;

    private String mnemonicWord;
    private String mInputWord;
    private StringBuffer sb;

    private TextView tv_word_tips;
    private Button btn_ok;
    private Button btn_clear;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_word_input;
    }

    @Override
    protected void initView() {
        rv_show = findViewById(R.id.rv_word_show);
        rv_input = findViewById(R.id.rv_word_input);

        tv_word_tips = findViewById(R.id.tv_word_input_tips);
        btn_ok = findViewById(R.id.btn_ok);
        btn_clear = findViewById(R.id.btn_clear);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilty.getPreference(Constants.SP_KEY_DID_MNEMONIC, "");
                String s1 = sb.toString();
                String s2 = mnemonicWord.trim().replaceAll(" ", "");
                if (s1.equals(s2)) {
                    showSuccessDialog();
                } else {
                    Toast.makeText(WordInputActivity.this, getString(R.string.word_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (WordModel word : wordList) {
                    word.setClicked(false);
                }
                mWordShowAdapter.notifyDataSetChanged();
                wordInputList.clear();
                mWordInputAdapter.notifyDataSetChanged();
                sb.delete(0, sb.length());

                tv_word_tips.setText(getString(R.string.word_click));
                rv_show.setVisibility(View.VISIBLE);
                btn_ok.setVisibility(View.GONE);
                btn_clear.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void initData() {
        wordList = new ArrayList<>(16);
        mnemonicWord = Utilty.getPreference(Constants.SP_KEY_DID_MNEMONIC, "");
        String[] words = mnemonicWord.trim().split(" ");
        for(String s : words) {
            WordModel wordModel = new WordModel();
            wordModel.setWord(s);
            wordList.add(wordModel);
        }
        Collections.shuffle(wordList);
        mWordShowAdapter = new GridRecyclerViewAdapter(this, wordList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rv_show.setLayoutManager(gridLayoutManager);
        rv_show.setAdapter(mWordShowAdapter);
        mWordShowAdapter.setOnItemClickListener(new GridRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, TextView textView) {
                textView.setTextColor(getResources().getColor(R.color.appColor));
                textView.setBackground(getResources().getDrawable(R.drawable.bg_roundcorner_trans));
                mInputWord = textView.getText().toString();
                WordModel wordModel = new WordModel();
                wordModel.setWord(mInputWord);
                wordInputList.add(wordModel);
                mWordInputAdapter.notifyDataSetChanged();
                textView.setClickable(false);
                sb.append(mInputWord);

                if(wordInputList.size() >= 12) {
                    tv_word_tips.setText(getString(R.string.word_iscorrect));
                    rv_show.setVisibility(View.GONE);
                    btn_ok.setVisibility(View.VISIBLE);
                    btn_clear.setVisibility(View.VISIBLE);
                }
            }
        });

        wordInputList = new ArrayList<>(16);
        mWordInputAdapter = new GridRecyclerViewAdapter(this, wordInputList);
        mWordInputAdapter.setType(GridRecyclerViewAdapter.TYPE_INPUT);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 4);
        rv_input.setLayoutManager(gridLayoutManager1);
        rv_input.setAdapter(mWordInputAdapter);

        sb = new StringBuffer();
    }

    public void onSureClick(View view) {
        sb.delete(0, sb.length());
        for (WordModel word : wordList) {
            sb.append(word.getWord());
        }
        Utilty.getPreference(Constants.SP_KEY_DID_MNEMONIC, "");
        if (sb.toString().equals(mnemonicWord.trim())) {
            showSuccessDialog();
        } else {
            Toast.makeText(this, getString(R.string.word_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCleanClick(View view) {
        for (WordModel word : wordList) {
            word.setClicked(false);
        }
        mWordShowAdapter.notifyDataSetChanged();
        wordInputList.clear();
        mWordInputAdapter.notifyDataSetChanged();

        tv_word_tips.setText(getString(R.string.word_click));
        rv_show.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.GONE);
        btn_clear.setVisibility(View.GONE);
    }

    private void showSuccessDialog() {
        new DidAlertDialog(this)
                .setTitle(getString(R.string.word_backdup))
                .setMessage(getString(R.string.word_tips))
                .setMessageGravity(Gravity.LEFT)
                .setRightButton(getString(R.string.btn_next), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPwdInputDialog();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showPwdInputDialog() {
        final DidAlertDialog pwdDialog = new DidAlertDialog(this);
        pwdDialog.setTitle(getString(R.string.word_newpwd))
                .setMessage(getString(R.string.word_pwdtips))
                .setMessageGravity(Gravity.LEFT)
                .setEditText(true)
//                .setLeftButton(getString(R.string.btn_last), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showSuccessDialog();
//                    }
//                })
                .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = pwdDialog.getEditTextView().getText().toString();
                        if (TextUtils.isEmpty(password) || password.length() < 8) {
                            Toast.makeText(WordInputActivity.this, getString(R.string.toast_pwd_notcorrect), Toast.LENGTH_SHORT).show();
                            showPwdInputDialog();
                            return;
                        }
                        Utilty.setPreference(Constants.SP_KEY_DID_PASSWORD, password);
                        Utilty.setPreference(Constants.SP_KEY_DID_ISBACKUP, "true");
                        Utilty.setPreference(Constants.SP_KEY_DID_MNEMONIC, "");
                        Toast.makeText(WordInputActivity.this, getString(R.string.word_backdup), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(WordInputActivity.this, DidLaunchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        WordInputActivity.this.startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();
    }
}
