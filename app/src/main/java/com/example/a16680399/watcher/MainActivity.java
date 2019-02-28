package com.example.a16680399.watcher;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {

    public static final Uri NOTES_URI = Uri.parse("content://com.example.vakulenko.notebook.notesprovider/notes");

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getContentResolver().query(NOTES_URI, null, null,
                null, null, null);
        System.out.println(cursor);

        textView = findViewById(R.id.textView);

        CustomTask customTask = new CustomTask();
        customTask.execute("test_");
        try {
            customTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //rxJava
//        Observable<Integer> observable = Observable.create(emitter -> {
//            try {
//                emitter.onNext(Integer.valueOf(10));
//                emitter.onNext(Integer.valueOf(10));
//                emitter.onNext(Integer.valueOf(10));
//            } catch (Exception e) {
//                emitter.onError(e);
//            }
//        });
//        observable.subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }

    public class CustomTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText(R.string.start);
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                    this.publishProgress(args[0] + String.valueOf(i + 1));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "закончили";
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            textView.setText(R.string.stop);
            textView.setText(res);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textView.setText(values[0]);
        }
    }
}
