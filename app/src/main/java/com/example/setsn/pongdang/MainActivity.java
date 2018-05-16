package com.example.setsn.pongdang;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.pongdang);

        pongdang req = pongdang.retrofit.create(pongdang.class);
        Call<ResponseBody> call = req.test();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try{
                    String responseStr=response.body().string();
                    JSONObject json =new JSONObject(responseStr);
                    textView.setText(""+json.getString("time"));
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this).setSmallIcon(R.drawable.noti_icon)
                            .setContent("asdf")
                            .setContentText(""+json.getString("temp"));
                    NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                textView .setText("통신 실패");
            }
        });
    }

    public interface pongdang {
        @GET("/")
        Call<ResponseBody> test();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hangang.dkserver.wo.tc")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }
}
