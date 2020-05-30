package com.example.a521sohrtextavovnutrfail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {

    private EditText editLogin;
    private EditText editReg;
    private static final String FILE_NAME = "a521sohrtextavovnutrfail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        checkData();
        setOnClickButton();

        findViewById(R.id.fab_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File filesDir = getFilesDir();
                if (filesDir != null) {
                    sendFiles(filesDir);
                }
            }
        });
    }


    public void init() {
        editLogin = findViewById(R.id.editLogin);
        editReg = findViewById(R.id.editReg);

    }


    public void checkData() {
        if (editLogin.getText().toString().length() == 0 && editReg.getText().toString().length() == 0) {
            Toast.makeText(MainActivity.this, R.string.loginparol, Toast.LENGTH_LONG).show();
        } else if (!(editLogin.getText().toString().length() == 0) && editReg.getText().toString().length() == 0) {
            Toast.makeText(MainActivity.this, R.string.parol, Toast.LENGTH_LONG).show();
        } else if (editLogin.getText().toString().length() == 0 && !(editReg.getText().toString().length() == 0)) {
            Toast.makeText(MainActivity.this, R.string.login, Toast.LENGTH_LONG).show();

        }
    }

    private void setOnClickButton() {
        findViewById(R.id.btnReg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkData();

                try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                     BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                    bw.write(editLogin.getText().toString());
                    bw.write("\n");
                    bw.write(editReg.getText().toString());
                    Toast.makeText(MainActivity.this, R.string.saved, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkData();
                try (FileInputStream fileInputStream = openFileInput(FILE_NAME);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String login = reader.readLine();
                    String password = reader.readLine();

                    if (editLogin.getText().toString().equals(login) && editReg.getText().toString().equals(password)) {
                        Toast.makeText(MainActivity.this, R.string.voshli, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.nepr, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void sendFiles(@NonNull File root) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.here));
        intent.setType("*/*");
        ArrayList<Uri> files = new ArrayList<>();
        File[] listFiles = root.listFiles();
        if (listFiles == null) {
            Toast.makeText(this, R.string.nofiles, Toast.LENGTH_SHORT).show();
            return;
        }
        for (File file : listFiles) {
            Uri uri = FileProvider.getUriForFile(
                    MainActivity.this,
                    getString(R.string.file_provider_authority),
                    file);
            files.add(uri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(intent);
    }
}
