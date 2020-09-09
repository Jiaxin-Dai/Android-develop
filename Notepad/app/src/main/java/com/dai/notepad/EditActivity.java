package com.dai.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private EditText titleEdi;
    private EditText textEdi;
    private String id;
    private String title;
    private String text;
    private String time;
    private Note note;
    boolean isSuccess = false;
    private Gson gson;
    private String jsonNote;
    private Note noteFromMain;
    private ArrayList<Note> notes;
    private SimpleDateFormat df;
    private String mainId;
    private Boolean isEdited = false;
    String newJson = "";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gson = new Gson();
        initView();


        mainId = getDataFromMain();
        if (mainId != null && !mainId.equals("")) {
            edit(mainId);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                onClickSave(item.getActionView());
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        titleEdi = findViewById(R.id.title_edit);
        textEdi = findViewById(R.id.note_edit);
        textEdi.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    /**
     * @param v 监听button
     */
    public void onClickSave(View v) {
        //        测试用来删数据的
//        FileUtil.deleteFile(this, Constant.FILENAME);
        save();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    /**
     * 保存
     */
    public void save() {
        boolean isSave = false;
        if (mainId != null) {
            isSave = saveAsJson(mainId);
        }
        if (isSave) {
            saveJson(jsonNote);
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            if (isSuccess) {
                intent.putExtra(Constant.INTENT_ETOM, newJson);
            }
            startActivityForResult(intent, 1);
        }
    }


    /**
     * 实体类保存成json
     */
    public boolean saveAsJson(String id) {
        boolean isSave = true;

        title = titleEdi.getText().toString();
        if (title.isEmpty() || title.trim().equals("")) {
            Toast.makeText(this, "input title,please", Toast.LENGTH_SHORT).show();
            isSave = false;
        }
        text = textEdi.getText().toString();
        time = df.format(new Date());

        // use currentTime as unique key for note
        if (id.isEmpty() || id.equals("")) {
            id = System.currentTimeMillis() + "";
        }
        note = new Note(id, title.trim(), text, time);
        jsonNote = gson.toJson(note, Note.class);

        return isSave;
    }

    /**
     * @param json 保存json
     */
    public void saveJson(String json) {

        boolean isFilePresent = FileUtil.isFilePresent(this, Constant.FILENAME);
        if (isFilePresent) {
            String jsonString = FileUtil.read(this, Constant.FILENAME);


            if (jsonString != null) {
                newJson = jsonString + "," + json;
            } else {
                newJson = json;
            }

            isSuccess = FileUtil.save(this, Constant.FILENAME, newJson);
        } else {
            FileUtil.save(this, Constant.FILENAME, json);
        }
//back to MainActivity


    }

    /**
     * @return get Data from MainActivity
     */
    public String getDataFromMain() {
        if (getIntent() != null) {
            noteFromMain = (Note) getIntent().getSerializableExtra(Constant.EDIT_OBJ);

            if (noteFromMain != null) {
                ////TODO
                String t = df.format(new Date());
                titleEdi.setText(noteFromMain.getTitle());
                textEdi.setText(noteFromMain.getText());
                String id = noteFromMain.getId();
                return id;
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        showNormalDialog();

    }

    private void showNormalDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(EditActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("save");
        normalDialog.setMessage(" save it ?");
        normalDialog.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                });
        normalDialog.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }


    /**
     * search from arrayList , then update
     */
    public void edit(String editId) {
        //TODO
        String json = FileUtil.read(this, Constant.FILENAME);

        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        ArrayList<Note> noteArrayList = gson.fromJson("[" + json + "]", type);

        for (int i = 0; i < noteArrayList.size(); i++) {
            if (null != noteArrayList.get(i)) {
                if (editId.equals(noteArrayList.get(i).getId())) {
                    noteArrayList.remove(noteArrayList.get(i));
                    continue;
                }
            }

        }
        FileUtil.deleteFile(this, Constant.FILENAME);

        for (int i = 0; i < noteArrayList.size(); i++) {

            saveJson(gson.toJson(noteArrayList.get(i), Note.class));
        }





    }

}
