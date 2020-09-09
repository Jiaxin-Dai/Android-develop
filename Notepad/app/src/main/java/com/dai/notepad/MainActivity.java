package com.dai.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private String jsonNote = "";
    private StringBuilder sBuilder;
    private ArrayList<Note> notes;
    private Gson gson;
    public static final String INTENT_MTOE = "note";
    private long firstTime = 0;

    private RecyclerView recyclerView; // Layout's recyclerview

    private NoteListAdapter mAdapter; // Data to recyclerview adapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        notes = new ArrayList<Note>();
        sBuilder = new StringBuilder();
        initData();
        recyclerView = findViewById(R.id.notelists);
        mAdapter = new NoteListAdapter(notes, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getParent(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                showNormalDialog(position);
            }
        }));
        mAdapter.setOnitemClick(new onItemClick() {
            @Override
            public void setSelectedNum(int position) {
                Note edit_note = notes.get(position);
                if (edit_note != null) {

                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.EDIT_OBJ, edit_note);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                goAnotherAcitvity(AboutActivity.class);
                return true;
            case R.id.add:
                goAnotherAcitvity(EditActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void goAnotherAcitvity(Class activity) {
        Intent intent = new Intent(MainActivity.this, activity);
        startActivity(intent);
    }

    public void initData() {
        jsonNote = FileUtil.read(MainActivity.this, Constant.FILENAME);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        if (jsonNote != null) {
            if (!jsonNote.startsWith("[")) {
                notes = gson.fromJson("[" + jsonNote + "]", type);
            } else {
                notes = gson.fromJson(jsonNote, type);
            }
            //TODO 按时间排序
//            if (notes.size() > 1) {
//                SortClass sortClass = new SortClass();
//                Collections.sort(notes, sortClass);
//            }
        }
    }

    public void getJson() {
        if (this.getIntent() != null) {
            jsonNote = getIntent().getStringExtra(Constant.INTENT_ETOM);
            if (!jsonNote.startsWith("[")) {

                jsonNote = "[" + jsonNote + "]";
            }

        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "press twice to exit", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }

        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        //startActivityForResult(intent,B_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!notes.isEmpty()) {
            //noteList.remove(this);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!notes.isEmpty()) {
            //noteList.remove(this);
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void showNormalDialog(final int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("delete");
        normalDialog.setMessage(" delete it ?");
        normalDialog.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String id = notes.get(position).getId();
                        String json = FileUtil.read(MainActivity.this, Constant.FILENAME);
                        Type type = new TypeToken<ArrayList<Note>>() {
                        }.getType();
                        ArrayList<Note> noteArrayList = gson.fromJson("[" + json + "]", type);
                        for (int i = 0; i < noteArrayList.size(); i++) {
                            if (null != noteArrayList.get(i)) {
                                if (id.equals(noteArrayList.get(i).getId())) {
                                    noteArrayList.remove(noteArrayList.get(i));
                                    continue;
                                }
                            }

                        }
                        FileUtil.deleteFile(MainActivity.this, Constant.FILENAME);

                        for (int i = 0; i < noteArrayList.size(); i++) {

                            FileUtil.save(MainActivity.this, Constant.FILENAME, gson.toJson(noteArrayList.get(i), Note.class));
                        }
                        mAdapter.RemoveData(position);

                    }
                });
        normalDialog.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...TODO
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    getJson();
                    mAdapter.notifyDataSetChanged();
                }
                break;

            default:
        }

    }


}
