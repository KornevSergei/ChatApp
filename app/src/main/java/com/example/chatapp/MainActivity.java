package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;

    //список для сообщений
    private FirebaseListAdapter<Message> adapter;


    //переменная для кнопки отправки сообщений
    private FloatingActionButton sendButton;


    //имплементируем для отследивания авторизации пользователя через интернет
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //проверяем если успешно зарегистрировался
        if (requestCode == SIGN_IN_CODE) {
            if (requestCode == RESULT_OK) {
                Snackbar.make(activity_main, "Успешно", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            } else {
                Snackbar.make(activity_main, "Не авторизованы", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //помещаем окно в котором будет появляться сообщение
        activity_main = findViewById(R.id.activity_main);

        sendButton = findViewById(R.id.buttonSend);

        //добавляем обраотчик события
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //получаем информацию от пользователя
                EditText textField = findViewById(R.id.messageField);
                //проверяем на пустое сообщение, если пустое код дальнейший не запускаем
                if (textField.getText().toString() == "")
                    return;

                //добавляем информацию в базу данных по нажатию, согласно конструктору сообщения
                FirebaseDatabase.getInstance().getReference().push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textField.getText().toString())
                );
                //елаем пустое поля по нажитию отправки
                textField.setText("");
            }
        });


        //проверяем на авторизацию, проверяем данные вошедшего пользователя, не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        } else {
            //выводим сообщение
            Snackbar.make(activity_main, "Успешно", Snackbar.LENGTH_SHORT).show();

            displayAllMessages();

        }
    }

    //получаем информацию из бызы данных и добавляем их в список сообщений
    private void displayAllMessages() {

        //связываем список с разметкой
        ListView listOfMessages = findViewById(R.id.listOfMessages);
        //добавляем данные в адаптер, указываем класс, разметку и делаем запрос к БД
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                //переменные для заполнения элементами разметку, связываем
                TextView messUser = v.findViewById(R.id.messageUser);
                TextView messTime = v.findViewById(R.id.messageTime);
                TextView messText = v.findViewById(R.id.messageText);

                //устанавливаем
                messUser.setText(model.getUserName());
                messText.setText(model.getTextMessage());
                messTime.setText(DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
            }
        };
        //устанавливаем в список адаптер
        listOfMessages.setAdapter(adapter);

    }
}