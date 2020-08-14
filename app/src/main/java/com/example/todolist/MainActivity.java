package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText description;
    ArrayList<Task> task_list;
    FloatingActionButton floatingActionButton;
    AlertDialog alertDialog;
    DBHelper dbHelper;
    String string1, string2;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Vibrator vibrator;
   public static boolean check_night_mode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (check_night_mode==true)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(this);
        tv = findViewById(R.id.id_text_view);
        floatingActionButton = findViewById(R.id.id_floating_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerAdapter = new RecyclerAdapter(this, task_list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        task_list = dbHelper.ListAllTasks();

        if (task_list.size()!= 0) {

            recyclerAdapter = new RecyclerAdapter(this, task_list);
            recyclerView.setAdapter(recyclerAdapter);
        }
        else
            {
                tv.setText("No todo found yet!");
            }



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }
            @Override
            public void onLongClick(View view, int position) {

                vibrate();
                Dialog_show(position);
            }
        }));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_dialog, null);
                description = view.findViewById(R.id.id_description);
                builder.setView(view);
                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        string1 = getTime();
                        string2 = description.getText().toString();

                        if (TextUtils.isEmpty(string2)) {

                            Toast.makeText(MainActivity.this, "Empty Task!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Task task = new Task(string1, string2);
                            dbHelper.insert_todo(task);
                            dialog.cancel();
                            finish();
                            startActivity(getIntent());
                        }
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                 alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static String getTime() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YY hh:mm:ss");
            String current_time = simpleDateFormat.format(new Date());
            return current_time;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public void delete_Todo(int pos) {
        dbHelper.delete_todo(task_list.get(pos));
        task_list.remove(pos);
        recyclerAdapter.notifyItemRemoved(pos);
    }

    private void update_Todo(String note, int position) {

        Task t = task_list.get(position);
        t.setDescription(note);
        dbHelper.update_todo(t);
        task_list.set(position, t);
        recyclerAdapter.notifyItemChanged(position);
    }

    private void Dialog_show(final int position) {
        CharSequence options[] = new CharSequence[]{"Update", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, task_list.get(position), position);
                } else {
                    delete_Todo(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean is_update, final Task task, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.activity_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        final EditText inputTask = view.findViewById(R.id.id_description);
        TextView dialogTitle = view.findViewById(R.id.id_dialog_title);
        dialogTitle.setText(!is_update ? getString(R.string.new_task_title) : getString(R.string.update_task_title));

        if (is_update && task != null) {
            inputTask.setText(task.getDescription());
        }
        builder.setCancelable(false);
        builder.setPositiveButton(is_update ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputTask.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter update Task!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (is_update && task != null) {
                    update_Todo(inputTask.getText().toString(), position);
                } else
                    {
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper!=null)
        {
            dbHelper.close();
        }
    }
    public void vibrate()
    {
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(8000);
        vibrator.cancel();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_setting:
               Intent intent=new Intent(this,SettingActivity.class);
               startActivity(intent);
                break;
            case R.id.id_about:
                Intent n=new Intent(this,AboutActivity.class);
                startActivity(n);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
